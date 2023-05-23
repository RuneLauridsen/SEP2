package booking.shared.socketMessages;

import booking.shared.objects.User;

import java.util.Objects;

public class LoginResponse extends Response
{
    private final User user;

    public LoginResponse(User user)
    {
        this.user = Objects.requireNonNull(user);
    }

    public User getUser()
    {
        return user;
    }
}
