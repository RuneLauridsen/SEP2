package booking.core;

public class User
{
    private int viaId;
    private UserType type;

    public User(int viaID, UserType type)
    {
        this.viaId = viaID;
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
