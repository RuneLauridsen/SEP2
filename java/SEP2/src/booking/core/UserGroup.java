package booking.core;

import java.io.Serializable;
import java.util.List;

public class UserGroup implements Serializable
{
    private final int id;
    private final String name;
    private final Course course;
    private final List<User> users;

    public UserGroup(int id, String name, Course course, List<User> users)
    {
        this.id = id;
        this.name = name;
        this.course = course;
        this.users = users;
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

    public List<User> getUsers()
    {
        return users;
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
}
