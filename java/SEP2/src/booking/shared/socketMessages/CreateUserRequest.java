package booking.shared.socketMessages;

import booking.shared.objects.UserType;

import java.util.Objects;

public class CreateUserRequest extends Request
{
    private final String username;
    private final String password;
    private final String initials; // NOTE(rune): Null hvis ikke medarbejder
    private final int viaid;
    private final UserType userType;

    public CreateUserRequest(String username, String password, String initials, int viaid, UserType userType)
    {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.initials = initials;
        this.viaid = viaid;
        this.userType = Objects.requireNonNull(userType);
    }

    public String getUsername()
    {
      return username;
    }

    public String getPassword()
    {
      return password;
    }

    public String getInitials()
    {
        return initials;
    }

    public int getViaid()
    {
        return viaid;
    }

    public UserType getUserType()
    {
        return userType;
    }
}
