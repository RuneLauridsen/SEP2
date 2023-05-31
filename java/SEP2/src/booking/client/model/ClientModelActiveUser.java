package booking.client.model;

import booking.shared.objects.User;

public interface ClientModelActiveUser
{
    User getUser(); // NOTE: Returns null if user is not logged in.
}
