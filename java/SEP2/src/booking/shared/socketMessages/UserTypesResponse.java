package booking.shared.socketMessages;

import booking.shared.objects.UserType;

import java.util.List;

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
