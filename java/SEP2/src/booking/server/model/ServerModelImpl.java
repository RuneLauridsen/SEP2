package booking.server.model;

import booking.client.model.HashingEncrypter;
import booking.shared.CreateBookingParameters;
import booking.shared.NowProvider;
import booking.shared.objects.*;
import booking.database.Persistence;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.ErrorResponseReason;

import static booking.shared.socketMessages.ErrorResponseReason.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServerModelImpl implements ServerModel
{
    private final Persistence persistence;
    private final NowProvider nowProvider;

    // NOTE(rune): Når man checker efter overlap mellem bookinger, er koden
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

    @Override public User getUser(int viaid)
    {
        return persistence.getUser(viaid);
    }

    @Override public User login(int viaid, String password)
    {
        try
        {
            return persistence.getUser(viaid, HashingEncrypter.encrypt(password));
        }
        catch (NoSuchAlgorithmException e)
        {
            return null; // TODO(rune): Måske en måde er rapportere fejl
        }
    }

    @Override public Room getRoom(String roomName, User activeUser)
    {
        return persistence.getRoom(roomName, activeUser);
    }

    @Override public List<Room> getRooms(User activeUser)
    {
        return persistence.getRooms(activeUser);
    }

    @Override public List<RoomType> getRoomTypes()
    {
        return new ArrayList<>(persistence.getRoomTypes().values());
    }

    @Override public List<UserType> getUserTypes()
    {
        return new ArrayList<>(persistence.getUserTypes().values());
    }

    @Override public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters)
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

    @Override public ErrorResponseReason createBooking(User user, CreateBookingParameters parameters, List<Overlap> overlaps)
    {
        // NOTE(rune): Vi stoler på at klienten sender Room med korrekt RoomType værdi. Dette er ikke en sikker måde
        // a styre adgang til lokaler på, da klienten nemt kunne sende en Room instans med modificeret RoomType,
        // for at få adgang til at booke lokaler, som klienten normalt ikke ville have adgang til.
        // Dog holder SocketHandler'en selv styr på User objektet, så der kan klienten ikke snyde.

        boolean isAllowedToBookRoom = user.getType().getAllowedRoomTypes().contains(parameters.getRoom().getType());
        if (isAllowedToBookRoom)
        {
            List<Booking> activeBookings = persistence.getBookingsForUser(
                user,
                LocalDate.now(), // TODO(rune): Lav om så det kan unit testes
                LocalDate.MAX,
                user
            );

            if (activeBookings.size() < user.getType().getMaxBookingCount())
            {
                // TODO(rune): Overvej om der kan ske deadlock her
                synchronized (createBookingLock)
                {
                    overlaps.addAll(getOverlaps(parameters));

                    // NOTE(rune): Selvom klient sender isOverlapAllowed kan det godt være
                    // at klientens brugertype ikke har tilladelse til at overlappe bookinger.
                    boolean isOverlapAllowed = false;
                    if (parameters.isOverlapAllowed() && user.getType().canOverlapBookings())
                    {
                        isOverlapAllowed = true;
                    }

                    // NOTE(rune): Overlap kan enten betyde at lokalet er optaget i booking interval,
                    // eller at en af medlemmerne i en bookings user group er optaget.
                    // Planlæggere skal have mulighed for at overlappe bookinger alligvel, så vi tjekker
                    // ikke hvis klient eksplicit siger at den nye booking godt må overlappe.
                    if (overlaps.size() == 0 || isOverlapAllowed)
                    {
                        persistence.createBooking(user, parameters);
                    }

                    return ERROR_RESPONSE_REASON_NONE;
                }
            }
            else
            {
                return ERROR_RESPONSE_REASON_TOO_MANY_ACTIVE_BOOKINGS;
            }
        }
        else
        {
            return ERROR_RESPONSE_REASON_ROOM_TYPE_NOT_ALLOWED;
        }
    }

    @Override public ErrorResponseReason createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
    {
        //TODO(julie) errorhandle stuff

        persistence.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
        return ERROR_RESPONSE_REASON_NONE;
    }

    @Override public ErrorResponseReason deleteBooking(User activeUser, Booking booking)
    {
        if (activeUser.getType().canEditBookings() || activeUser.equals(booking.getUser()))
        {
            persistence.deleteBooking(booking);
            return ERROR_RESPONSE_REASON_NONE;
        }
        else
        {
            return ERROR_RESPONSE_REASON_ACCESS_DENIED;
        }
    }

    @Override public List<Booking> getBookingsForUser(User user, LocalDate from, LocalDate to, User activeUser)
    {
        // TODO(rune): Almindelige brugere må ikke se andre brugeres bookinger?

        return persistence.getBookingsForUser(user, from, to, activeUser);
    }

    @Override public List<Booking> getBookingsForRoom(String roomName, LocalDate from, LocalDate to, User activeUser)
    {
        Room room = persistence.getRoom(roomName, activeUser);
        if (room != null)
        {
            return persistence.getBookingsForRoom(room, from, to);
        }
        else
        {
            return List.of();
        }
    }

    @Override public List<UserGroup> getUserGroups()
    {
        List<UserGroup> userGroups = persistence.getUserGroups();
        return userGroups;
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup)
    {
        List<User> users = persistence.getUserGroupUsers(userGroup);
        return users;
    }

    @Override public ErrorResponseReason updateRoom(Room room, User activeUser)
    {
        if (activeUser.getType().canEditRooms())
        {
            persistence.updateRoom(room);
            return ERROR_RESPONSE_REASON_NONE;
        }
        else
        {
            return ERROR_RESPONSE_REASON_ACCESS_DENIED;
        }
    }

    @Override public void updateUserRoomData(User user, Room room, String comment, int color)
    {
        persistence.updateUserRoomData(user, room, comment, color);
    }

    @Override public List<TimeSlot> getTimeSlots()
    {
        return persistence.getTimeSlots();
    }

    @Override public ErrorResponseReason deleteRoom(Room room, User activeUser)
    {
        if (activeUser.getType().canEditRooms()){
            persistence.deleteRoom(room);
            return ERROR_RESPONSE_REASON_NONE;
        }
        else
            return ERROR_RESPONSE_REASON_ACCESS_DENIED;

    }

    public ErrorResponseReason createUser(String username, String password, String initials, int viaid, UserType userType)
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

            if (createUserResult)
            {
                return ERROR_RESPONSE_REASON_NONE;
            }
            else
            {
                return ERROR_RESPONSE_REASON_USERNAME_TAKEN;
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            return ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR;
        }
    }

    private List<Overlap> getOverlaps(CreateBookingParameters parameters)
    {
        List<Overlap> overlaps = new ArrayList<>();

        List<User> newBookingUsers = List.of();
        if (parameters.getUserGroup() != null)
        {
            newBookingUsers = persistence.getUserGroupUsers(parameters.getUserGroup());
        }

        List<Booking> oldBookings = persistence.getBookingsForRoom(
            parameters.getRoom(),
            parameters.getInterval().getDate(),
            parameters.getInterval().getDate()
        );

        for (Booking oldBooking : oldBookings)
        {
            if (parameters.getInterval().isOverlapWith(oldBooking.getInterval()))
            {
                List<User> overlapUsers = new ArrayList<>();

                // NOTE(rune): Hvis booking er til bestemt klasse/hold, så skal
                // vi tjekke om den nye booking ill overlappe med en eller flere
                // users fra andre klasers/holds eksisterende bookinger.
                if (oldBooking.getUserGroup() != null)
                {
                    List<User> oldBookingUsers = persistence.getUserGroupUsers(oldBooking.getUserGroup());

                    for (User newBookingUser : newBookingUsers)
                    {
                        if (oldBookingUsers.contains(newBookingUser))
                        {
                            overlapUsers.add(newBookingUser);
                        }
                    }
                }

                overlaps.add(new Overlap(
                    oldBooking,
                    overlapUsers
                ));
            }
        }

        return overlaps;
    }
}
