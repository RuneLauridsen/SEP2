import booking.database.DatabaseHandler;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import booking.shared.objects.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class TestDatabase
{
    private DatabaseHandler database;

    @BeforeEach void setup()
    {
        try
        {
            Connection connection = DatabaseHandler.openConnection();
            Statement statement = connection.createStatement();

            URL sqlSetupUlr = getClass().getResource("setup.sql");
            String sqlSetup = Files.readString(Path.of(sqlSetupUlr.toURI()));

            statement.execute(sqlSetup);
            statement.close();
            connection.close();

            database = new DatabaseHandler();
            database.open();
        }
        catch (Exception e)
        {
            // NOTE(rune): Database setup fejlede -> kan ikke køre test.
            throw new RuntimeException(e);
        }
    }

    @AfterEach void cleanUp()
    {
        database.close();
    }

    @Test void testGetUser()
    {
        User user = database.getUser("Gitte");

        assertEquals(user.getId(), 5);
        assertEquals(user.getName(), "Gitte");
        assertEquals(user.getInitials(), "GITT");
        assertEquals(user.getViaId(), 555555); // TODO(rune): Har medarbejdere viaid ligesom studerende?
        assertEquals(user.getType().getId(), 1);
    }

    @Test void testGetUserNotFound()
    {
        User user = database.getUser("AAAAA");
        assertEquals(user, null);
    }

    @Test void testGetRoom()
    {
        User user = database.getUser("Rune");
        Room room = database.getRoom("C06.01", user);

        assertEquals(room.getId(), 20);
        assertEquals(room.getName(), "C06.01");
        assertEquals(room.getSize(), 1);
        assertEquals(room.getComfortCapacity(), 11);
        assertEquals(room.getFireCapacity(), 111);
        assertEquals(room.getComment(), "");
        assertEquals(room.getType().getId(), 3);
        assertEquals(room.getUserComment(), "");
        assertEquals(room.getUserColor(), -1);
    }

    @Test void testGetRoomNotFound()
    {
        User user = database.getUser("Rune");
        Room room = database.getRoom("AAAAA", user);

        assertEquals(room, null);
    }

    @Test void testUpdateUserRoomData()
    {
        User user = database.getUser("Gitte");
        Room roomBefore = database.getRoom("A02.01", user);
        database.updateUserRoomData(user, roomBefore, "jeg hedder gitte 2", 0x00ff00ff);
        Room roomAfter = database.getRoom("A02.01", user);

        assertEquals(roomBefore.getUserComment(), "jeg hedder gitte");
        assertEquals(roomBefore.getUserColor(), 99);
        assertEquals(roomAfter.getUserComment(), "jeg hedder gitte 2");
        assertEquals(roomAfter.getUserColor(), 0x00ff00ff);
    }

    @Test void testInsertUserRoomData()
    {
        // NOTE(rune): A02.03 har ikke bruger-specifik data i forvejen, så her
        // test vi også at indsætte en ny user_room_data række.

        User user = database.getUser("Gitte");
        Room roomBefore = database.getRoom("A02.03", user);
        database.updateUserRoomData(user, roomBefore, "test comment", 0xff00ff00);
        Room roomAfter = database.getRoom("A02.03", user);

        assertEquals(roomBefore.getUserComment(), "");
        assertEquals(roomBefore.getUserColor(), -1);
        assertEquals(roomAfter.getUserComment(), "test comment");
        assertEquals(roomAfter.getUserColor(), 0xff00ff00);
    }

    @Test void testUpdateRoom()
    {
        User user = database.getUser("Gitte");
        Room roomToUpdate = database.getRoom("A02.03", user);

        assertEquals(roomToUpdate.getName(), "A02.03");
        assertEquals(roomToUpdate.getSize(), 3);
        assertEquals(roomToUpdate.getComfortCapacity(), 33);
        assertEquals(roomToUpdate.getFireCapacity(), 333);
        assertEquals(roomToUpdate.getComment(), "");
        assertEquals(roomToUpdate.getType().getId(), 1);


        roomToUpdate.setName("A02.99");
        roomToUpdate.setSize(90);
        roomToUpdate.setComfortCapacity(91);
        roomToUpdate.setComment("new global comment");

        database.updateRoom(roomToUpdate);


        assertEquals(roomToUpdate.getName(), "A02.99");
        assertEquals(roomToUpdate.getSize(), 90);
        assertEquals(roomToUpdate.getComfortCapacity(), 91);
        assertEquals(roomToUpdate.getFireCapacity(), 333);
        assertEquals(roomToUpdate.getComment(), "new global comment");
        assertEquals(roomToUpdate.getType().getId(), 1);
    }

    @Test void testGetRoomTypes()
    {
        Map<Integer, RoomType> roomTypes = database.getRoomTypes();
        assertEquals(roomTypes.size(), 7);

        assertEquals(roomTypes.get(4).getId(), 4);
        assertEquals(roomTypes.get(4).getName(), "Klasselokale");
    }
}

