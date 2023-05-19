package booking.shared.socketMessages;

import java.util.List;
import booking.shared.objects.UserType;

public class UserTypesResponse extends Response
{
    private final List<UserType> userTypes;

    public UserTypesResponse(List<UserType> userTypes)
    {
        this.userTypes = userTypes;
    }

    public List<UserType> getUserTypes()
    {
        return userTypes;
    }
}
