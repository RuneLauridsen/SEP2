package booking.client.model;

import booking.shared.objects.UserType;

import java.util.List;

public interface ClientModelRegister
{
     void register(String username, String password, String initials, int viaid, UserType userType) throws ClientModelException;
     List<UserType> getUserTypes() throws ClientModelException;
}
