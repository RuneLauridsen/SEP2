package booking.client.model;

import booking.shared.objects.User;

public interface ClientModelLogin
{
     User login(int viaid, String password) throws ClientModelException;
}
