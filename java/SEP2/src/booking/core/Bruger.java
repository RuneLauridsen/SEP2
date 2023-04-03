package booking.core;

public class Bruger
{
    private int viaID;
    private BrugerType type;

    public Bruger(int viaID, BrugerType type)
    {
        this.viaID = viaID;
        this.type = type;
    }

    public int getMaxNumberOfActiveBookings()
    {
        switch (type)
        {
            case USER_TYPE_STUDENT:
                return 2;

            case USER_TYPE_TEACHER:
                return Integer.MAX_VALUE;

            case USER_TYPE_ADMIN:
                return Integer.MAX_VALUE;

            default:
                throw new IllegalArgumentException();
        }
    }
}
