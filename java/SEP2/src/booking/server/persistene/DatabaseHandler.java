package booking.server.persistene;

import booking.shared.objects.*;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseHandler implements Persistence
{
    private final DatabaseConnectionPool connectionPool;

    public DatabaseHandler()
    {
        this.connectionPool = new DatabaseConnectionPool();
    }

    public void open(int concurrentConnectionCount) throws SQLException
    {
        connectionPool.addConnections(concurrentConnectionCount);
    }

    public void close()
    {
        connectionPool.close();
    }

    public User getUser(int viaid) throws PersistenceException
    {
        return getUser(viaid, null);
    }

    public User getUser(int viaid, String passwordHash) throws PersistenceException
    {
        Map<Integer, UserType> userTypes = getUserTypes();

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = """
                SELECT
                    u.user_id, 
                    u.user_type_id, 
                    u.user_name, 
                    u.user_initials, 
                    u.user_viaid 
                FROM 
                    sep2."user" u 
                WHERE 
                    u.user_viaid = ?
                AND
                    u.user_password_hash = COALESCE(?, u.user_password_hash)
                """;

            statement = connection.prepareStatement(query);
            statement.setInt(1, viaid);
            statement.setString(2, passwordHash);
            resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                // Map resultSet til User objekt
                return new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("user_initials"),
                    resultSet.getInt("user_viaid"), // NOTE(rune): Hvis user_viaid er NULL, returnere resulSet.getInt 0.
                    userTypes.get(resultSet.getInt("user_type_id"))
                );
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    public Room getRoom(String roomName, User activeUser) throws PersistenceException
    {
        Objects.requireNonNull(roomName);

        int userId = activeUser == null ? 0 : activeUser.getId();

        Map<Integer, RoomType> roomTypes = getRoomTypes();

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = """
                SELECT
                    r.room_id,
                    r.room_name, 
                    room_size, 
                    room_comfort_capacity, 
                    room_fire_capacity, 
                    room_comment, 
                    room_type_id,
                    COALESCE(urd.comment, '') AS user_data_comment,
                    COALESCE(urd.color, CAST(x'ffffffff' AS int)) as user_data_color
                FROM 
                    sep2.room r
                LEFT OUTER JOIN 
                    sep2.user_room_data urd ON urd.room_id = r.room_id AND urd.user_id = ?
                WHERE 
                    r.room_name = ?
                """;

            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, roomName);
            resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                // Map resultSet til User objekt
                return new Room(
                    resultSet.getInt("room_id"),
                    resultSet.getString("room_name"),
                    resultSet.getInt("room_size"),
                    resultSet.getInt("room_comfort_capacity"),
                    resultSet.getInt("room_fire_capacity"),
                    resultSet.getString("room_comment"),
                    roomTypes.get(resultSet.getInt("room_type_id")),
                    resultSet.getString("user_data_comment"),
                    resultSet.getInt("user_data_color")
                );
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    public Map<Integer, RoomType> getRoomTypes() throws PersistenceException
    {
        Connection connection = connectionPool.acquireConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT room_type_id, room_type_name FROM sep2.room_type";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            Map<Integer, RoomType> roomTypes = new HashMap<>();
            while (resultSet.next())
            {
                roomTypes.put(
                    resultSet.getInt("room_type_id"),
                    new RoomType(
                        resultSet.getInt("room_type_id"),
                        resultSet.getString("room_type_name")
                    )
                );
            }

            return roomTypes;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    public List<Room> getRooms(User activeUser) throws PersistenceException
    {
        int userId = activeUser == null ? 0 : activeUser.getId();

        Map<Integer, RoomType> roomTypes = getRoomTypes();

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = """
                SELECT
                    r.room_id, 
                    r.room_name, 
                    r.room_size, 
                    r.room_comfort_capacity, 
                    r.room_fire_capacity, 
                    r.room_comment, 
                    r.room_type_id,
                    COALESCE(urd.comment, '') AS user_data_comment,
                    COALESCE(urd.color, CAST(x'ffffffff' AS int)) AS user_data_color
                FROM 
                    sep2.room r
                LEFT OUTER JOIN 
                    sep2.user_room_data urd ON urd.room_id = r.room_id AND urd.user_id = ? 
                ORDER BY
                    r.room_name
                """;

            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            resultSet = statement.executeQuery();

            List<Room> rooms = new ArrayList<>();
            while (resultSet.next())
            {
                rooms.add(new Room(
                    resultSet.getInt("room_id"),
                    resultSet.getString("room_name"),
                    resultSet.getInt("room_size"),
                    resultSet.getInt("room_comfort_capacity"),
                    resultSet.getInt("room_fire_capacity"),
                    resultSet.getString("room_comment"),
                    roomTypes.get(resultSet.getInt("room_type_id")),
                    resultSet.getString("user_data_comment"),
                    resultSet.getInt("user_data_color"))
                );
            }

            return rooms;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    public Map<Integer, UserType> getUserTypes() throws PersistenceException
    {
        Map<Integer, RoomType> roomTypes = getRoomTypes();

        Connection connection = connectionPool.acquireConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {

            String query = """
                SELECT
                    ut.user_type_id,
                    ut.user_type_name,
                    ut.can_edit_rooms,  
                    ut.can_edit_users, 
                    ut.can_edit_bookings, 
                    ut.can_overlap_bookings, 
                    ut.max_booking_count, 
                    rt.room_type_id, 
                    rt.room_type_name 
                FROM 
                    sep2.user_type ut 
                INNER JOIN 
                    sep2.user_type_allowed_room_type utart ON ut.user_type_id = utart.user_type_id 
                INNER JOIN 
                    sep2.room_type rt ON utart.room_type_id = rt.room_type_id;
                 """;

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Map til UserType objekter. Der er en række pr. user_type + allowed_room,
            // så samme user_type vil stå i flere rækker.
            Map<Integer, UserType> userTypes = new HashMap<>();
            while (resultSet.next())
            {
                UserType userType = userTypes.getOrDefault(resultSet.getInt("user_type_id"), null);
                if (userType == null)
                {
                    userType = new UserType(
                        resultSet.getInt("user_type_id"),
                        resultSet.getString("user_type_name"),
                        resultSet.getBoolean("can_edit_users"),
                        resultSet.getBoolean("can_edit_rooms"),
                        resultSet.getBoolean("can_edit_bookings"),
                        resultSet.getBoolean("can_overlap_bookings"),
                        resultSet.getInt("max_booking_count"),
                        new ArrayList<>()
                    );

                    userTypes.put(resultSet.getInt("user_type_id"), userType);
                }

                userType.getAllowedRoomTypes().add(roomTypes.get(resultSet.getInt("room_type_id")));
            }

            return userTypes;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    private List<Booking> getBookings(User user, Room room, User userGroupUser, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        Map<Integer, UserType> userTypes = getUserTypes();
        Map<Integer, RoomType> roomTypes = getRoomTypes();

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        int userId = user == null ? 0 : user.getId();
        int roomId = room == null ? 0 : room.getId();
        int userGroupUserId = userGroupUser == null ? 0 : userGroupUser.getId();
        int activeUserId = activeUser == null ? 0 : activeUser.getId();

        try
        {
            String query = """
                SELECT
                    b.booking_id,
                    b.booking_date,
                    b.booking_start_time, 
                    b.booking_end_time, 
                    r.room_id,
                    r.room_name, 
                    r.room_size, 
                    r.room_comfort_capacity, 
                    r.room_fire_capacity, 
                    r.room_comment, 
                    r.room_type_id, 
                    COALESCE(urd.comment, '') AS user_data_comment, 
                    COALESCE(urd.color, CAST(x'ffffffff' AS int)) AS user_data_color,
                    u.user_id, 
                    u.user_type_id, 
                    u.user_name, 
                    u.user_initials, 
                    u.user_viaid, 
                    ug.user_group_id,
                    ug.user_group_name,
                    c.course_id,
                    c.course_name
                FROM 
                    sep2.booking b 
                INNER JOIN 
                    sep2."user" u ON b.user_id = u.user_id 
                INNER JOIN 
                    sep2.room r ON b.room_id = r.room_id 
                LEFT OUTER JOIN
                    sep2.user_group ug ON ug.user_group_id = b.user_group_id\s
                LEFT OUTER JOIN
                    sep2.course c ON c.course_id = ug.course_id
                LEFT OUTER JOIN
                    sep2.user_room_data urd ON urd.room_id = r.room_id AND urd.user_id = ?
                WHERE
                    (? = 0 OR ? = r.room_id) -- hent pr. lokale
                AND
                    (? = 0 OR ? = u.user_id) -- hent pr. booked by user
                AND 
                    (? = 0 OR ug.user_group_id IN -- hent pr. medlem af klasse/hold
                        (
                            SELECT 
                                user_group_id
                            FROM 
                                sep2.user_group_user
                            WHERE
                                user_id = ?
                        ) 
                    )
                AND 
                    b.booking_date BETWEEN ? AND ? 
                ORDER BY  
                    b.booking_date, 
                    b.booking_start_time;
                """;

            statement = connection.prepareStatement(query);
            statement.setInt(1, activeUserId);
            statement.setInt(2, roomId);
            statement.setInt(3, roomId);
            statement.setInt(4, userId);
            statement.setInt(5, userId);
            statement.setInt(6, userGroupUserId);
            statement.setInt(7, userGroupUserId);

            statement.setDate(8, truncateToSqlDate(startDate));
            statement.setDate(9, truncateToSqlDate(endDate));
            resultSet = statement.executeQuery();

            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next())
            {
                UserGroup userGroup = null;
                if (resultSet.getInt("user_group_id") != 0)
                {
                    userGroup = new UserGroup(
                        resultSet.getInt("user_group_id"),
                        resultSet.getString("user_group_name"),
                        new Course(
                            resultSet.getInt("course_id"),
                            resultSet.getString("course_name")
                        )
                    );
                }

                // Map resultSet til Booking objekt
                bookings.add(
                    new Booking(
                        resultSet.getInt("booking_id"),
                        new BookingInterval(
                            resultSet.getDate("booking_date").toLocalDate(),
                            resultSet.getTime("booking_start_time").toLocalTime(),
                            resultSet.getTime("booking_end_time").toLocalTime()
                        ),
                        new Room(
                            resultSet.getInt("room_id"),
                            resultSet.getString("room_name"),
                            resultSet.getInt("room_size"),
                            resultSet.getInt("room_comfort_capacity"),
                            resultSet.getInt("room_fire_capacity"),
                            resultSet.getString("room_comment"),
                            roomTypes.get(resultSet.getInt("room_type_id")),
                            resultSet.getString("user_data_comment"),
                            resultSet.getInt("user_data_color")
                        ),
                        new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_initials"),
                            resultSet.getInt("user_viaid"),
                            userTypes.get(resultSet.getInt("user_type_id"))
                        ),
                        userGroup
                    )
                );
            }

            return bookings;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public List<Booking> getBookingsForUser(User user, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        return getBookings(user, null, null, startDate, endDate, activeUser);
    }

    @Override public List<Booking> getBookingsForRoom(Room room, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        return getBookings(null, room, null, startDate, endDate, activeUser);
    }

    @Override public List<Booking> getBookingsForUserGroupUser(User user, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        return getBookings(null, null, user, startDate, endDate, activeUser);
    }

    @Override public void createBooking(User activeUser, Room room, BookingInterval bookingInterval, UserGroup userGroup)
        throws PersistenceException
    {
        Objects.requireNonNull(activeUser);
        Objects.requireNonNull(room);
        Objects.requireNonNull(bookingInterval);

        String query = """
            INSERT INTO sep2.booking (booking_date, booking_start_time, booking_end_time, room_id, user_id, user_group_id) 
                VALUES (?, ?, ?, ?, ?, ?);""";

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        try
        {
            statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(bookingInterval.getDate()));
            statement.setTime(2, Time.valueOf(bookingInterval.getStart()));
            statement.setTime(3, Time.valueOf(bookingInterval.getEnd()));
            statement.setInt(4, room.getId());
            statement.setInt(5, activeUser.getId());

            if (userGroup == null)
                statement.setNull(6, Types.INTEGER);
            else
                statement.setInt(6, userGroup.getId());
            statement.execute();
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public void deleteBooking(Booking booking) throws PersistenceException
    {
        Objects.requireNonNull(booking);

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;

        try
        {
            String sql = """
                DELETE FROM sep2.booking WHERE booking_id = ?
                """;

            statement = connection.prepareStatement(sql);
            statement.setInt(1, booking.getId());
            statement.execute();
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public List<Room> getAvailableRooms(User activeUser, BookingInterval interval, Integer minCapacity, Integer maxCapacity, Character building, Integer floor) throws PersistenceException
    {
        Objects.requireNonNull(activeUser);
        Objects.requireNonNull(interval);

        String floorString = null;
        if (floor != null)
        {
            floorString = floor.toString();
        }

        String buildingString = null;
        if (building != null)
        {
            buildingString = building.toString();
        }

        if (minCapacity == null)
        {
            minCapacity = Integer.MIN_VALUE;
        }

        if (maxCapacity == null)
        {
            maxCapacity = Integer.MAX_VALUE;
        }

        Map<Integer, RoomType> roomTypes = getRoomTypes();

        String query = """
            SELECT DISTINCT
                r.room_id,
                r.room_name,
                r.room_size,
                r.room_comfort_capacity,
                r.room_fire_capacity,
                r.room_comment,
                r.room_type_id,
                COALESCE(urd.comment, '') AS user_data_comment,
                COALESCE(urd.color, CAST(x'ffffffff' AS int)) AS user_data_color
            FROM
                sep2.room r
            INNER JOIN
                sep2."user" u ON u.user_id = ?
            INNER JOIN
                sep2.user_type_allowed_room_type utart ON r.room_type_id = utart.room_type_id AND u.user_type_id = utart.user_type_id
            LEFT OUTER JOIN
                sep2.user_room_data urd ON urd.room_id = r.room_id AND urd.user_id = u.user_id
            WHERE
                ((r.room_comfort_capacity >= ?))
            AND
                ((r.room_comfort_capacity <= ?))
            AND
                ((substr(r.room_name, 1, 1) = ?) OR (? IS NULL))
            AND
                ((substr(r.room_name, 3, 1) = ?) OR (? IS NULL))
            AND
                r.room_id NOT IN
                (
                     -- find alle optagede lokaler
                    SELECT
                        r.room_id
                    FROM
                        sep2.room r
                    INNER JOIN
                        sep2.booking b ON r.room_id = b.room_id
                    WHERE
                        b.booking_date = ?
                    AND
                        (b.booking_start_time < ? OR b.booking_start_time = ?)
                    AND
                        (b.booking_end_time > ? OR b.booking_end_time = ?)
                )
            ORDER BY
                r.room_name;
            """;

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            statement = connection.prepareStatement(query);
            statement.setInt(1, activeUser.getId());
            statement.setInt(2, minCapacity);
            statement.setInt(3, maxCapacity);
            statement.setString(4, buildingString);
            statement.setString(5, buildingString);
            statement.setString(6, floorString);
            statement.setString(7, floorString);

            statement.setDate(8, Date.valueOf(interval.getDate()));
            statement.setTime(9, Time.valueOf(interval.getEnd()));
            statement.setTime(10, Time.valueOf(interval.getStart()));
            statement.setTime(11, Time.valueOf(interval.getStart()));
            statement.setTime(12, Time.valueOf(interval.getEnd()));

            resultSet = statement.executeQuery();

            List<Room> rooms = new ArrayList<>();
            while (resultSet.next())
            {
                rooms.add(
                    new Room(
                        resultSet.getInt("room_id"),
                        resultSet.getString("room_name"),
                        resultSet.getInt("room_size"),
                        resultSet.getInt("room_comfort_capacity"),
                        resultSet.getInt("room_fire_capacity"),
                        resultSet.getString("room_comment"),
                        roomTypes.get(resultSet.getInt("room_type_id")),
                        resultSet.getString("user_data_comment"),
                        resultSet.getInt("user_data_color")
                    )
                );
            }

            return rooms;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);

            connectionPool.releaseConnection(connection);
        }
    }

    public boolean createUser(String name, String initials, int viaid, String passwordHash, UserType type) throws PersistenceException
    {
        Objects.requireNonNull(name);
        Objects.requireNonNull(passwordHash);
        Objects.requireNonNull(type);

        User userWithViaid = getUser(viaid, passwordHash);
        if (userWithViaid != null)
        {
            // Viaid er optaget.
            return false;
        }

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;

        try
        {
            String query = """
                INSERT INTO sep2."user" 
                    (user_name, user_initials, user_viaid, user_password_hash, user_type_id)
                VALUES 
                    (?, ?, ?, ?, ?);
                """;

            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, initials);
            statement.setInt(3, viaid);

            statement.setString(4, passwordHash);
            statement.setInt(5, type.getId());

            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public List<UserGroup> getUserGroups() throws PersistenceException
    {
        Connection connection = connectionPool.acquireConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = """
                SELECT
                    ug.user_group_id, 
                    ug.user_group_name, 
                    c.course_id, 
                    c.course_name
                FROM 
                    sep2.course c
                INNER JOIN 
                    sep2.user_group ug ON c.course_id = ug.course_id
                ORDER BY
                    ug.user_group_name 
                """;

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            List<UserGroup> userGroups = new ArrayList<>();
            while (resultSet.next())
            {
                userGroups.add(
                    new UserGroup(
                        resultSet.getInt("user_group_id"),
                        resultSet.getString("user_group_name"),
                        new Course(
                            resultSet.getInt("course_id"),
                            resultSet.getString("course_name")
                        )
                    )
                );
            }

            return userGroups;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup) throws PersistenceException
    {
        Objects.requireNonNull(userGroup);

        Map<Integer, UserType> userTypes = getUserTypes();

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = """
                SELECT 
                    u.user_id, 
                    u.user_type_id, 
                    u.user_name, 
                    u.user_initials, 
                    u.user_viaid
                FROM 
                    sep2.user_group_user ugu
                INNER JOIN 
                    sep2."user" u ON u.user_id = ugu.user_id
                WHERE 
                    ugu.user_group_id = ? 
                ORDER BY
                    u.user_name
                """;

            statement = connection.prepareStatement(query);
            statement.setInt(1, userGroup.getId());
            resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next())
            {
                // Map resultSet til User objekt
                users.add(
                    new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("user_initials"),
                        resultSet.getInt("user_viaid"),
                        userTypes.get(resultSet.getInt("user_type_id"))
                    )
                );
            }

            return users;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public void updateRoom(Room room) throws PersistenceException
    {
        Objects.requireNonNull(room);

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;

        try
        {
            String sql = """
                UPDATE
                    sep2.room
                SET
                    room_name = ?,
                    room_size = ?,
                    room_comfort_capacity = ?,
                    room_fire_capacity = ?,
                    room_comment = ?,
                    room_type_id = ?
                WHERE
                    room_id = ?
                """;

            statement = connection.prepareStatement(sql);
            statement.setString(1, room.getName());
            statement.setInt(2, room.getSize());
            statement.setInt(3, room.getComfortCapacity());
            statement.setInt(4, room.getFireCapacity());
            statement.setString(5, room.getComment());
            statement.setInt(6, room.getType().getId());
            statement.setInt(7, room.getId());
            statement.execute();
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public void updateUserRoomData(User user, Room room, String comment, int color) throws PersistenceException
    {
        Objects.requireNonNull(user);
        Objects.requireNonNull(room);
        Objects.requireNonNull(comment);

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;

        try
        {
            // TODO(rune): Er der en bedre måde at lave "INSERT el. UPDATE hvis række findes i forvejen" på?
            // NOTE(rune): https://stackoverflow.com/a/11135767
            String query = " "
                + "UPDATE sep2.user_room_data SET comment = ?, color = ? WHERE user_id = ? AND room_id = ?; "
                + "INSERT INTO sep2.user_room_data (user_id, room_id, comment, color) "
                + "SELECT ?, ?, ?, ? WHERE NOT EXISTS(SELECT 1 FROM sep2.user_room_data WHERE user_id = ? AND room_id = ?);";

            statement = connection.prepareStatement(query);

            // UPDATE
            statement.setString(1, comment);
            statement.setInt(2, color);
            statement.setInt(3, user.getId());
            statement.setInt(4, room.getId());

            // INSERT+SELECT
            statement.setInt(5, user.getId());
            statement.setInt(6, room.getId());
            statement.setString(7, comment);
            statement.setInt(8, color);
            statement.setInt(9, user.getId());
            statement.setInt(10, room.getId());

            statement.execute();
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public List<TimeSlot> getTimeSlots() throws PersistenceException
    {
        Connection connection = connectionPool.acquireConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT time_slot_id, time_slot_start, time_slot_end FROM sep2.time_slot ORDER BY time_slot_start; ";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            List<TimeSlot> timeSlots = new ArrayList<>();
            while (resultSet.next())
            {
                timeSlots.add(new TimeSlot(resultSet.getInt("time_slot_id"),
                                           resultSet.getTime("time_slot_start").toLocalTime(),
                                           resultSet.getTime("time_slot_end").toLocalTime()));
            }

            return timeSlots;
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws PersistenceException
    {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        Objects.requireNonNull(comment);
        Objects.requireNonNull(doubleName);

        String query = "INSERT INTO sep2.room "
            + " (room_name, room_size, room_comfort_capacity, room_fire_capacity, room_comment, room_type_id) "
            + "VALUES "
            + " (?, ?, ?, ?, ?,?);";

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, size);
            statement.setInt(3, maxComf);
            statement.setInt(4, maxSafety);
            statement.setString(5, comment);
            statement.setInt(6, type.getId());
            statement.execute();
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override public void deleteRoom(Room room) throws PersistenceException
    {
        Objects.requireNonNull(room);

        Connection connection = connectionPool.acquireConnection();
        PreparedStatement statement = null;

        try
        {
            String sql = """
                BEGIN TRANSACTION;
                DELETE FROM sep2.booking WHERE room_id = ?;
                DELETE FROM sep2.user_room_data WHERE room_id = ?;
                DELETE FROM sep2.room WHERE room_id = ?;
                COMMIT TRANSACTION;
                """;

            statement = connection.prepareStatement(sql);
            statement.setInt(1, room.getId());
            statement.setInt(2, room.getId());
            statement.setInt(3, room.getId());
            statement.execute();
        }
        catch (SQLException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            closeStatement(statement);
            connectionPool.releaseConnection(connection);
        }
    }

    private static void closeStatement(Statement statement)
    {
        if (statement != null)
        {
            try
            {
                statement.close();
            }
            catch (SQLException e)
            {
                // TODO(rune): Hvad gør vi hvis statement ikke kan lukkes?
                throw new RuntimeException(e);
            }
        }
    }

    private static void closeResultSet(ResultSet resultSet)
    {
        if (resultSet != null)
        {
            try
            {
                resultSet.close();
            }
            catch (SQLException e)
            {
                // TODO(rune): Hvad gør vi hvis resultSet ikke kan lukkes?
                throw new RuntimeException(e);
            }
        }
    }

    private static Date truncateToSqlDate(LocalDate localDate)
    {
        // NOTE(rune): Postgres' dato type har ikke lige så stor range som Java's LocalDate.
        // Vi bruger LocalDate.MIN og LocalDate.MAX når man f.eks. vil finde ALLE bookinger,
        // med getBookingsForRoom, så denne funktion sørger for, at de ikke overskrider
        // Postgres' dato types grænser. Håndterer ikke BCE.
        // https://www.postgresql.org/docs/current/datatype-datetime.html#DATATYPE-DATETIME-TABLE

        if (localDate.getYear() <= 0)
        {
            return Date.valueOf(LocalDate.of(0, 1, 1));
        }
        else if (localDate.getYear() > 5874897)
        {
            return Date.valueOf(LocalDate.of(5874897, 1, 1));
        }
        else
        {
            return Date.valueOf(localDate);
        }
    }
}
