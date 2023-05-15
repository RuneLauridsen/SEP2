package booking.database;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.RoomType;
import booking.core.User;
import booking.core.UserType;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// TODO(rune): Måske noget connection pooling?
// TODO(rune): Er der en bedre måde at skrive lange SQL queries ind i java? Det kan
// godt være lidt svært at finde rundt i, når de bare står som String literals.
// TODO(rune): Dette er en stor klasse, skal den deles op i mindre dele?
public class DatabaseHandler implements Persistence
{
    private Connection connection;

    public DatabaseHandler()
    {
    }

    public void open() throws SQLException
    {
        connection = openConnection();
    }

    public void close()
    {
        closeConnection(connection);
    }

    public User getUser(String username)
    {
        Objects.requireNonNull(username);

        Map<Integer, UserType> userTypes = getUserTypes();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT u.user_id, u.user_type_id, u.user_name, u.user_initials, u.user_viaid FROM sep2.\"user\" u WHERE u.user_name = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);
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
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public boolean isAvailable(String roomName)
    {

        Map<Integer, RoomType> roomTypes = getRoomTypes();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT  "
                + " b.booking_date, b.booking_start_time, b.booking_end_time "
                + "FROM sep2.booking b "
                + "INNER JOIN sep2.room r ON b.room_id = r.room_id "
                + "WHERE r.room_name = ?;";

            statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                BookingInterval bookingInterval = new BookingInterval(resultSet.getDate("booking_date").toLocalDate(), resultSet.getTime("booking_start_time").toLocalTime(), resultSet.getTime("booking_end_time").toLocalTime());
                if (bookingInterval.getDate().equals(LocalDate.now()) && bookingInterval.isOverlapWith(LocalTime.now()))
                    return false;
            }
            return true;

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public Room getRoom(String roomName)
    {
        Objects.requireNonNull(roomName);

        Map<Integer, RoomType> roomTypes = getRoomTypes();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT room_id, room_name, room_size, room_comfort_capacity, room_fire_capacity, room_comment, room_type_id FROM sep2.room WHERE room_name = ?;";

            statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
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
                    roomTypes.get(resultSet.getInt("room_type_id"))
                );
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public Map<Integer, RoomType> getRoomTypes()
    {
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
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public List<Room> getRooms()
    {
        Map<Integer, RoomType> roomTypes = getRoomTypes();

        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT room_id, room_name, room_size, room_comfort_capacity, room_fire_capacity, room_comment, room_type_id FROM sep2.room;";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

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
                    roomTypes.get(resultSet.getInt("room_type_id"))
                ));
            }

            return rooms;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public Map<Integer, UserType> getUserTypes()
    {
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            Map<Integer, RoomType> roomTypes = getRoomTypes();

            String query = "SELECT "
                + "    ut.user_type_id, "
                + "    ut.user_type_name, "
                + "    ut.can_edit_rooms, "
                + "    ut.can_edit_users, "
                + "    ut.can_edit_bookings, "
                + "    ut.max_booking_count, "
                + "    rt.room_type_id, "
                + "    rt.room_type_name "
                + "FROM sep2.user_type ut "
                + "INNER JOIN sep2.user_type_allowed_room_type utart "
                + "    ON ut.user_type_id = utart.user_type_id "
                + "INNER JOIN sep2.room_type rt "
                + "    ON utart.room_type_id = rt.room_type_id;";

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
                        resultSet.getBoolean("can_edit_rooms"),
                        resultSet.getBoolean("can_edit_users"),
                        resultSet.getBoolean("can_edit_bookings"),
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
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    //TODO måske optimere på dette
    @Override public List<BookingInterval> getBookingsFromRoomName(String roomName)
    {
        Objects.requireNonNull(roomName);

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT  "
                + " b.booking_id, b.booking_date, b.booking_start_time, b.booking_end_time "
                + "FROM sep2.booking b "
                + "INNER JOIN sep2.room r ON b.room_id = r.room_id "
                + "WHERE r.room_name = ? "
                + "ORDER BY  b.booking_date, b.booking_start_time;";

            statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
            resultSet = statement.executeQuery();

            List<BookingInterval> bookings = new ArrayList<>();
            while (resultSet.next())
            {
                // Map resultSet til Booking objekt
                bookings.add(
                    new BookingInterval(
                        resultSet.getDate("booking_date").toLocalDate(),
                        resultSet.getTime("booking_start_time").toLocalTime(),
                        resultSet.getTime("booking_end_time").toLocalTime()
                    )
                );
            }

            return bookings;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public List<Booking> getBookingsForUser(User user, LocalDate startDate, LocalDate endDate)
    {
        Objects.requireNonNull(user);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        Map<Integer, RoomType> roomTypes = getRoomTypes();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT  "
                + " b.booking_id, b.booking_date, b.booking_start_time, b.booking_end_time, "
                + " r.room_id, r.room_name, r.room_size, r.room_comfort_capacity, r.room_fire_capacity, r.room_comment, r.room_type_id "
                + "FROM sep2.booking b "
                + "INNER JOIN sep2.\"user\" u ON b.user_id = u.user_id "
                + "INNER JOIN sep2.room r ON b.room_id = r.room_id "
                + "WHERE u.user_id = ? "
                + "AND b.booking_date BETWEEN ? AND ? "
                + "ORDER BY  b.booking_date, b.booking_start_time;";

            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setDate(2, truncateToSqlDate(startDate));
            statement.setDate(3, truncateToSqlDate(endDate));
            resultSet = statement.executeQuery();

            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next())
            {
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
                            roomTypes.get(resultSet.getInt("room_type_id"))
                        ),
                        user
                    )
                );
            }

            return bookings;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    @Override public List<Booking> getBookingsForRoom(Room room, LocalDate startDate, LocalDate endDate)
    {
        Objects.requireNonNull(room);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        Map<Integer, UserType> userTypes = getUserTypes();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            String query = "SELECT  "
                + " b.booking_id, b.booking_date, b.booking_start_time, b.booking_end_time, "
                + " u.user_id, u.user_type_id, u.user_name, u.user_initials, u.user_viaid "
                + "FROM sep2.booking b "
                + "INNER JOIN sep2.\"user\" u ON b.user_id = u.user_id "
                + "INNER JOIN sep2.room r ON b.room_id = r.room_id "
                + "WHERE r.room_id = ? "
                + "AND b.booking_date BETWEEN ? AND ? "
                + "ORDER BY  b.booking_date, b.booking_start_time;";

            statement = connection.prepareStatement(query);
            statement.setInt(1, room.getId());
            statement.setDate(2, truncateToSqlDate(startDate));
            statement.setDate(3, truncateToSqlDate(endDate));
            resultSet = statement.executeQuery();

            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next())
            {
                // Map resultSet til Booking objekt
                bookings.add(
                    new Booking(
                        resultSet.getInt("booking_id"),
                        new BookingInterval(
                            resultSet.getDate("booking_date").toLocalDate(),
                            resultSet.getTime("booking_start_time").toLocalTime(),
                            resultSet.getTime("booking_end_time").toLocalTime()
                        ),
                        room,
                        new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_initials"),
                            resultSet.getInt("user_viaid"),
                            userTypes.get(resultSet.getInt("user_type_id"))
                        )
                    )
                );
            }

            return bookings;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public void createBooking(User user, Room room, BookingInterval interval)
    {
        Objects.requireNonNull(user);
        Objects.requireNonNull(room);
        Objects.requireNonNull(interval);

        String query = "INSERT INTO sep2.booking "
            + " (booking_date, booking_start_time, booking_end_time, room_id, user_id) "
            + "VALUES "
            + " (?, ?, ?, ?, ?);";

        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(interval.getDate()));
            statement.setTime(2, Time.valueOf(interval.getStart()));
            statement.setTime(3, Time.valueOf(interval.getEnd()));
            statement.setInt(4, room.getId());
            statement.setInt(5, user.getId());
            statement.execute();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);  // TODO(rune): Bedre error handling
        }
        finally
        {
            closeStatement(statement);
        }
    }

    @Override public void deleteBooking(Booking booking)
    {

    }

    public List<Room> getAvailableRooms(User user, BookingInterval interval, Integer minCapacity, Integer maxCapacity, Character building, Integer floor)
    {
        Objects.requireNonNull(user);
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

        String query = "SELECT r.room_id, r.room_name, r.room_size, r.room_comfort_capacity, r.room_fire_capacity, r.room_comment, r.room_type_id "
            + "FROM sep2.room r "
            + "INNER JOIN sep2.\"user\" u ON u.user_id = ? "
            + "INNER JOIN sep2.user_type_allowed_room_type utart ON r.room_type_id = utart.room_type_id AND u.user_type_id = utart.user_type_id "
            + "LEFT OUTER JOIN sep2.booking b ON r.room_id = b.room_id "
            + "WHERE ((b.booking_id IS NULL) OR (b.booking_date <> ?) OR NOT (b.booking_end_time > ? AND b.booking_start_time < ?)) "
            + "AND ((r.room_comfort_capacity >= ?))  "
            + "AND ((r.room_comfort_capacity <= ?))  "
            + "AND ((substr(r.room_name, 1, 1) = ?) OR (? IS NULL)) "
            + "AND ((substr(r.room_name, 3, 1) = ?) OR (? IS NULL)) "
            + "GROUP BY r.room_id, r.room_name, r.room_size, r.room_comfort_capacity, r.room_fire_capacity, r.room_comment, r.room_type_id "
            + "ORDER BY r.room_name;";

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setDate(2, Date.valueOf(interval.getDate()));
            statement.setTime(3, Time.valueOf(interval.getStart()));
            statement.setTime(4, Time.valueOf(interval.getEnd()));
            statement.setInt(5, minCapacity);
            statement.setInt(6, maxCapacity);
            statement.setString(7, buildingString);
            statement.setString(8, buildingString);
            statement.setString(9, floorString);
            statement.setString(10, floorString);
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
                        roomTypes.get(resultSet.getInt("room_type_id"))
                    )
                );
            }

            return rooms;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public boolean createUser(String name, String initials, Integer viaid, String passwordHash, UserType type)
    {
        Objects.requireNonNull(name);
        Objects.requireNonNull(passwordHash);
        Objects.requireNonNull(type);

        User userWithSameName = getUser(name);
        if (userWithSameName != null)
        {
            // Brugernavn er optaget.
            return false;
        }

        PreparedStatement statement = null;

        try
        {
            String query = "INSERT INTO sep2.\"user\" (user_name, user_initials, user_viaid, user_password_hash, user_type_id) VALUES (?, ?, ?, ?, ?);";
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, initials);

            // TODO(rune): Brug setObject i stedet?
            if (viaid == null)
            {
                statement.setNull(3, Types.INTEGER);
            }
            else
            {
                statement.setInt(3, viaid);
            }

            statement.setString(4, passwordHash);
            statement.setInt(5, type.getId());

            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            closeStatement(statement);
        }
    }

    private static Connection openConnection() throws SQLException
    {
        switch (System.getProperty("user.name"))
        {
            case "runel":
                // TODO(rune): Forbinde til rigtig database, ikke bare localhost. Måske er måde at konfigurere
                // hvilken connection string der skal bruges, f.eks. debug database eller prod database.
                return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "asdasd");
            case "jbram":
                // TODO(rune): Forbinde til rigtig database, ikke bare localhost. Måske er måde at konfigurere
                // hvilken connection string der skal bruges, f.eks. debug database eller prod database.
                return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1346");

            default:
                throw new RuntimeException("Unknown user");
        }
    }

    private static void closeConnection(Connection connection)
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                // TODO(rune): Hvad gør vi hvis connection ikke kan lukkes?
                throw new RuntimeException(e);
            }
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

    private static void statementSetInteger(int index, Integer value, PreparedStatement statement) throws SQLException
    {
        if (value == null)
        {
            statement.setNull(index, Types.INTEGER);
        }
        else
        {
            statement.setInt(index, value);
        }
    }

    private static Date truncateToSqlDate(LocalDate localDate)
    {
        // NOTE(rune): Postgres' dato type har ikke ligeså stor range som Java's LocalDate.
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
