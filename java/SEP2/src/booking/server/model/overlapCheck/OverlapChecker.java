package booking.server.model.overlapCheck;

import booking.server.persistene.Persistence;
import booking.server.persistene.PersistenceException;
import booking.shared.CreateBookingParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.Overlap;
import booking.shared.objects.Room;
import booking.shared.objects.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OverlapChecker
{
    public static List<Overlap> getOverlaps(
        CreateBookingParameters parameters,
        User activeUser,
        Persistence persistence
    )
        throws PersistenceException
    {
        Booking newBooking = new Booking(
            0,
            parameters.getInterval(),
            parameters.getRoom(),
            activeUser,
            parameters.getUserGroup()
        );

        List<Booking> relevantBookings = OverlapChecker.getRelevantBookings(
            List.of(newBooking),
            persistence
        );

        List<Overlap> overlaps = OverlapChecker.getOverlaps(
            newBooking,
            relevantBookings,
            persistence
        );

        return overlaps;
    }

    public static List<Overlap> getOverlaps(
        Booking newBooking,
        List<Booking> checkAgainstBookings,
        Persistence persistence
    )
        throws PersistenceException
    {
        List<User> newBookingUsers = getRelevantUsers(newBooking, persistence);

        List<Overlap> foundOverlaps = new ArrayList<>();

        for (Booking checkAgainst : checkAgainstBookings)
        {
            if (!checkAgainst.equals(newBooking))
            {
                if (checkAgainst.getInterval().isOverlapWith(newBooking.getInterval()))
                {
                    boolean isOverlap = false;
                    List<User> overlapUsers = new ArrayList<>();

                    //
                    // Check om lokale overlapper
                    //
                    if (checkAgainst.getRoom().equals(newBooking.getRoom()))
                    {
                        isOverlap = true;
                    }

                    //
                    // Check om brugere overlapper
                    //
                    if (checkAgainst.getUserGroup() == null)
                    {
                        User checkAgainstUser = checkAgainst.getUser();

                        if (newBookingUsers.contains(checkAgainstUser))
                        {
                            isOverlap = true;
                            overlapUsers.add(checkAgainstUser);
                        }
                    }
                    else
                    {
                        for (User checkAgainstUser : persistence.getUserGroupUsers(checkAgainst.getUserGroup()))
                        {
                            if (newBookingUsers.contains(checkAgainstUser))
                            {
                                isOverlap = true;
                                overlapUsers.add(checkAgainstUser);
                            }
                        }
                    }

                    //
                    // Tilføj til return value list
                    //
                    if (isOverlap)
                    {
                        foundOverlaps.add(
                            new Overlap(
                                checkAgainst,
                                overlapUsers
                            )
                        );
                    }
                }
            }
        }

        return foundOverlaps;
    }

    // NOTE(rune): Antager at alle bookinger i bookingsToCheck har samme dato!
    public static List<Booking> getRelevantBookings(
        List<Booking> bookingsToCheck,
        //Map<UserGroup, List<User>> userGroupUsersCache,
        Persistence persistence
    )
        throws PersistenceException
    {
        LocalDate thisDate = bookingsToCheck.get(0).getInterval().getDate();

        Set<User> relevantUsers = new HashSet<>();
        Set<Room> relevantRooms = new HashSet<>();
        Set<Booking> relevantBookings = new HashSet<>();

        //
        // Find relevante lokaler + relevant brugere ud fra klasse/hold
        //
        for (Booking bookingToCheck : bookingsToCheck)
        {
            Room room = bookingToCheck.getRoom();

            relevantRooms.add(room);

            relevantBookings.add(bookingToCheck);
            relevantUsers.addAll(getRelevantUsers(bookingToCheck, persistence));
        }

        //
        // Find alle bookinger for relevante lokaler
        //
        for (Room relevantRoom : relevantRooms)
        {
            relevantBookings.addAll(persistence.getBookingsForRoom(relevantRoom, thisDate, thisDate, null));
        }

        //
        // Find alle bookinger for relevante brugere ud fra hold/klasser
        //
        for (User relevantUser : relevantUsers)
        {
            relevantBookings.addAll(persistence.getBookingsForUser(relevantUser, thisDate, thisDate, null));
            relevantBookings.addAll(persistence.getBookingsForUserGroupUser(relevantUser, thisDate, thisDate, null));
        }

        //
        // Find alle bookinger for relevante brugere ud fra booking uden hold/klasse
        //
        for (Booking booking : new ArrayList<>(relevantBookings))
        {
            if (booking.getUserGroup() == null)
            {
                if (!relevantUsers.contains(booking.getUser()))
                {
                    relevantUsers.add(booking.getUser());
                    relevantBookings.addAll(persistence.getBookingsForUser(booking.getUser(), thisDate, thisDate, null));
                    relevantBookings.addAll(persistence.getBookingsForUserGroupUser(booking.getUser(), thisDate, thisDate, null));
                }
            }
        }

        return new ArrayList<>(relevantBookings);
    }

    private static List<User> getRelevantUsers(Booking booking, Persistence persistence) throws PersistenceException
    {
        if (booking.getUserGroup() == null)
        {
            return List.of(booking.getUser());
        }
        else
        {
            return persistence.getUserGroupUsers(booking.getUserGroup());
        }
    }
}
