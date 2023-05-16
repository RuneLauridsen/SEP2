package booking.shared.socketMessages;

import booking.shared.objects.User;

import java.util.List;

public class UserGroupUsersResponse extends Response
{
    private final List<User> users;

    public UserGroupUsersResponse(List<User> users)
    {
        this.users = users;
    }

    public List<User> getUsers()
    {
        return users;
    }
}
