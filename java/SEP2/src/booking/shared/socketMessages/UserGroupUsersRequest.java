package booking.shared.socketMessages;

import booking.shared.objects.UserGroup;

// NOTE(rune): Henter alle Users i en UserGroup, f.eks. alle studerende i en klasse
public class UserGroupUsersRequest extends Request
{
    private final UserGroup userGroup;

    public UserGroupUsersRequest(UserGroup userGroup)
    {
        this.userGroup = userGroup;
    }

    public UserGroup getUserGroup()
    {
        return userGroup;
    }
}
