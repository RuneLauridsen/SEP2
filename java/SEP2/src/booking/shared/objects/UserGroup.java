package booking.shared.objects;

import java.io.Serializable;

public class UserGroup implements Serializable
{
    private final int id;
    private final String name;
    private final Course course;

    // NOTE: I databasen linker UserGroup til en liste af Users, men i Java-land har
    // UserGroup objektet ikke selv en liste af Users, da man skal bruge UserGroupUsersRequest
    // i stedet.

    public UserGroup(int id, String name, Course course)
    {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Course getCourse()
    {
        return course;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof UserGroup other)
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
