package booking.client.model;

import booking.shared.objects.TimeSlot;
import booking.shared.objects.UserGroup;

import java.util.List;

public interface ClientModelCoordinatorBooking extends ClientModelUserBooking
{
     List<TimeSlot> getTimeSlots() throws ClientModelException;
     List<UserGroup> getUserGroups() throws ClientModelException;
}
