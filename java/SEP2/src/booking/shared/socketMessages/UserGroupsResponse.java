package booking.shared.socketMessages;

import booking.shared.objects.UserGroup;

import java.util.List;

public class UserGroupsResponse extends Response
{
    private final List<UserGroup> userGroups;

    public UserGroupsResponse(List<UserGroup> userGroups)
    {
        this.userGroups = userGroups;
    }

    public List<UserGroup> getUserGroups()
    {
        return userGroups;
    }
}
