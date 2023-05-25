package booking.client.model;

import booking.shared.objects.RoomType;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.UserGroup;

import java.util.List;

public interface ClientModelCoordinatorBooking extends ClientModelUserBooking
{
    public List<TimeSlot> getTimeSlots() throws ClientModelException;
    public List<UserGroup> getUserGroups() throws ClientModelException;
}
