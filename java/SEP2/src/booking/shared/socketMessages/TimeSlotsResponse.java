package booking.shared.socketMessages;

import booking.shared.objects.TimeSlot;

import java.util.List;

public class TimeSlotsResponse extends Response
{
    private final List<TimeSlot> timeSlots;

    public TimeSlotsResponse(List<TimeSlot> timeSlots)
    {
        this.timeSlots = timeSlots;
    }

    public List<TimeSlot> getTimeSlots()
    {
        return timeSlots;
    }
}
