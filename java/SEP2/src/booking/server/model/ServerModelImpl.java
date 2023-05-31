package booking.server.model;

import booking.server.logger.Logger;
import booking.server.model.importFile.ImportFile;
import booking.server.model.importFile.ImportFileResult;
import booking.server.model.overlapCheck.OverlapChecker;
import booking.server.persistene.Persistence;
import booking.server.persistene.PersistenceException;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.NowProvider;
import booking.shared.objects.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static booking.shared.socketMessages.ErrorResponseReason.*;

public class ServerModelImpl implements ServerModel
{
    private final Persistence persistence;
    private final NowProvider nowProvider;

    // NOTE: Når man checker efter overlap mellem bookinger, er koden
    // synchronized med dette objekt for at undgå en race condition ved
    // booking overlap check, når flere brugere vil lave en ny booking på
    // samme tid.
    private final Object createBookingLock;

    public ServerModelImpl(Persistence persistence, NowProvider nowProvider)
    {
        this.persistence = persistence;
        this.nowProvider = nowProvider;
        this.createBookingLock = new Object();
    }

    @Override public User getUser(int viaid) throws ServerModelException
    {
        try
        {
            return persistence.getUser(viaid);
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public User login(int viaid, String password) throws ServerModelException
    {
        try
        {
            User user = persistence.getUser(viaid, HashingEncrypter.encrypt(password));
            if (user == null)
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS);
            }
            else
            {
                return user;
            }
        }
        catch (PersistenceException | NoSuchAlgorithmException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
        }
    }

    @Override public Room getRoom(String roomName, User activeUser) throws ServerModelException
    {
        // NOTE: Checker ikke om activeUser er logget ind, da vi bare returnere
        // lokale uden bruger specifik data, hvis activeUser ikke er logged ind.

        try
        {
            return persistence.getRoom(roomName, activeUser);
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<Room> getRooms(User activeUser) throws ServerModelException
    {
        // NOTE: Checker ikke om activeUser er logget ind, da vi bare returnere
        // lokale uden bruger specifik data, hvis activeUser ikke er logged ind.

        try
        {
            return persistence.getRooms(activeUser);
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<RoomType> getRoomTypes() throws ServerModelException
    {
        try
        {
            return new ArrayList<>(persistence.getRoomTypes().values());
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<UserType> getUserTypes() throws ServerModelException
    {
        try
        {
            return new ArrayList<>(persistence.getUserTypes().values());
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                return persistence.getAvailableRooms(
                    activeUser,
                    parameters.getInterval(),
                    parameters.getMinCapacity(),
                    parameters.getMaxCapacity(),
                    parameters.getBuilding(),
                    parameters.getFloor()
                );
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<Overlap> createBooking(User activeUser, CreateBookingParameters parameters) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                synchronized (createBookingLock)
                {
                    // NOTE: Vi stoler på at klienten sender Room med korrekt RoomType værdi. Dette er ikke en sikker måde
                    // a styre adgang til lokaler på, da klienten nemt kunne sende en Room instans med modificeret RoomType,
                    // for at få adgang til at booke lokaler, som klienten normalt ikke ville have adgang til.
                    // Dog holder SocketHandler'en selv styr på User objektet, så der kan klienten ikke snyde.

                    boolean isAllowedToBookRoom = activeUser.getType().getAllowedRoomTypes().contains(parameters.getRoom().getType());
                    if (!isAllowedToBookRoom)
                    {
                        throw new ServerModelException(ERROR_RESPONSE_REASON_ROOM_TYPE_NOT_ALLOWED);
                    }

                    List<Booking> activeBookings = persistence.getBookingsForUser(
                        activeUser,
                        nowProvider.nowDate(),
                        LocalDate.MAX,
                        activeUser
                    );

                    if (activeBookings.size() >= activeUser.getType().getMaxBookingCount())
                    {
                        throw new ServerModelException(ERROR_RESPONSE_REASON_TOO_MANY_ACTIVE_BOOKINGS);
                    }

                    // NOTE: Selvom klient sender isOverlapAllowed kan det godt være
                    // at klientens brugertype ikke har tilladelse til at overlappe bookinger.
                    boolean isOverlapAllowed = parameters.isOverlapAllowed() && activeUser.getType().canOverlapBookings();

                    List<Overlap> overlaps = OverlapChecker.getOverlaps(parameters, activeUser, persistence);

                    // NOTE: Overlap kan enten betyde at lokalet er optaget i booking interval,
                    // eller at en af medlemmerne i en bookings user group er optaget. Planlæggere skal
                    // have mulighed for at overlappe bookinger alligevel, så vi ignorerer overlap hvis
                    // klient eksplicit siger at den nye booking godt må overlappe.
                    if (overlaps.size() == 0 || isOverlapAllowed)
                    {
                        persistence.createBooking(
                            activeUser,
                            parameters.getRoom(),
                            parameters.getInterval(),
                            parameters.getUserGroup()
                        );
                        return List.of();
                    }
                    else
                        return overlaps;
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public void createRoom(User activeUser, String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                if (activeUser.getType().canEditRooms())
                {
                    persistence.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
                }
                else
                {
                    throw new ServerModelException(ERROR_RESPONSE_REASON_ACCESS_DENIED);
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public void deleteBooking(User activeUser, Booking booking) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                if (activeUser.getType().canEditBookings() || activeUser.equals(booking.getUser()))
                {
                    persistence.deleteBooking(booking);
                }
                else
                {
                    throw new ServerModelException(ERROR_RESPONSE_REASON_ACCESS_DENIED);
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<Booking> getBookingsForUser(User activeUser, User user, LocalDate from, LocalDate to) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                return persistence.getBookingsForUser(user, from, to, activeUser);
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<Booking> getBookingsForRoom(User activeUser, String roomName, LocalDate from, LocalDate to) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                Room room = persistence.getRoom(roomName, activeUser);
                if (room != null)
                {
                    return persistence.getBookingsForRoom(room, from, to, activeUser);
                }
                else
                {
                    return List.of();
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<UserGroup> getUserGroups() throws ServerModelException
    {
        try
        {
            return persistence.getUserGroups();
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup) throws ServerModelException
    {
        try
        {
            return persistence.getUserGroupUsers(userGroup);
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public void updateRoom(User activeUser, Room room) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                if (activeUser.getType().canEditRooms())
                {
                    persistence.updateRoom(room);
                }
                else
                {
                    throw new ServerModelException(ERROR_RESPONSE_REASON_ACCESS_DENIED);
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public void updateUserRoomData(User activeUser, Room room, String comment, int color) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                persistence.updateUserRoomData(activeUser, room, comment, color);
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new RuntimeException(e);
        }
    }

    @Override public void deleteRoom(User activeUser, Room room) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                if (activeUser.getType().canEditRooms())
                {
                    persistence.deleteRoom(room);
                }
                else
                {
                    throw new ServerModelException(ERROR_RESPONSE_REASON_ACCESS_DENIED);
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new RuntimeException(e);
        }
    }

    public void createUser(String username, String password, String initials, int viaid, UserType userType) throws ServerModelException
    {
        try
        {
            String passwordHash = HashingEncrypter.encrypt(password);

            boolean createUserResult = persistence.createUser(
                username,
                initials,
                viaid,
                passwordHash,
                userType
            );

            if (!createUserResult)
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_USERNAME_TAKEN);
            }
        }
        catch (NoSuchAlgorithmException | PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override public ImportFileResult importFile(User activeUser, String fileContent) throws ServerModelException
    {
        try
        {
            if (activeUser != null)
            {
                synchronized (createBookingLock)
                {
                    return ImportFile.importFile(fileContent, activeUser, persistence);
                }
            }
            else
            {
                throw new ServerModelException(ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
            }
        }
        catch (PersistenceException e)
        {
            Logger.log(e);
            throw new ServerModelException(ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR, e);
        }
    }
}
