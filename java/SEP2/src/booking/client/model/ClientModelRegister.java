package booking.client.model;

import booking.shared.objects.UserType;

import java.util.List;

public interface ClientModelRegister
{
    public void register(String username, String password, String initials, int viaid, UserType userType) throws ClientModelException;
    public List<UserType> getUserTypes() throws ClientModelException;
}
