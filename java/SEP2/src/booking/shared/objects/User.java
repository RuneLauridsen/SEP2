package booking.shared.objects;

import java.io.Serializable;

public class User implements Serializable
{
    private final int id;
    private final String name;
    private final String initials;    // NOTE(rune): Null hvis ikke medarbejder
    private final Integer viaId;      // NOTE(rune): Null hvis ikke studerende
    private final UserType type;

    public User(int id, String name, String initials, Integer viaId, UserType type)
    {
        this.id = id;
        this.name = name;
        this.initials = initials;
        this.viaId = viaId;
        this.type = type;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getInitials()
    {
        return initials;
    }

    public Integer getViaId()
    {
        return viaId;
    }

    public UserType getType()
    {
        return type;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof User other)
        {
            return this.id == other.id;
        }
        else
        {
            return false;
        }
    }

    @Override public String toString()
    {
        return name;
    }
}
