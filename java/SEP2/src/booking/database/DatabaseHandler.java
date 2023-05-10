package booking.database;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.RoomType;
import booking.core.User;
import booking.core.UserType;

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

    public List<Booking> getActiveBookings(User user, LocalDate startDate, LocalDate endDate)
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
                + "WHERE u.user_id = ?;";

            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
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

    public List<Room> getAvailableRooms(User user, BookingInterval interval)
    {
        Objects.requireNonNull(user);
        Objects.requireNonNull(interval);

        Map<Integer, RoomType> roomTypes = getRoomTypes();

        String query = "SELECT r.room_id, r.room_name, r.room_size, r.room_comfort_capacity, r.room_fire_capacity, r.room_comment, r.room_type_id "
            + "FROM sep2.room r "
            + "INNER JOIN sep2.\"user\" u ON u.user_id = ? "
            + "INNER JOIN sep2.user_type_allowed_room_type utart ON r.room_type_id = utart.room_type_id AND u.user_type_id = utart.user_type_id "
            + "INNER JOIN sep2.booking b ON r.room_id = b.room_id "
            + "WHERE (b.booking_date <> ?) OR NOT (b.booking_end_time > ? AND b.booking_start_time < ?) "
            + "GROUP BY r.room_id, r.room_name, r.room_size, r.room_comfort_capacity, r.room_fire_capacity, r.room_comment, r.room_type_id "
            + "ORDER BY r.room_name ";

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setDate(2, Date.valueOf(interval.getDate()));
            statement.setTime(3, Time.valueOf(interval.getStart()));
            statement.setTime(4, Time.valueOf(interval.getEnd()));
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
        // TODO(rune): Forbinde til rigtig database, ikke bare localhost. Måske er måde at konfigurere
        // hvilken connection string der skal bruges, f.eks. debug database eller prod database.
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "asdasd");
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
}
