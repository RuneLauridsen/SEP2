package booking.client.model;

import booking.shared.objects.User;

public interface ClientModelActiveUser
{
    User getUser(); // NOTE(rune): Returns null if user is not logged in.
}
