package booking.client.model;

import booking.shared.objects.User;

public interface ClientModelLogin
{
    public User login(int viaid, String password) throws ClientModelException;
}
