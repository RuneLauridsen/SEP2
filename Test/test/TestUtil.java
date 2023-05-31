package test;

import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.model.FileIO;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkException;
import booking.client.networking.ClientNetworkSocket;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetwork;
import booking.server.networking.ServerNetworkSocket;
import booking.server.persistene.DatabaseConnectionPool;
import booking.server.persistene.DatabaseHandler;
import booking.server.persistene.Persistence;
import booking.server.persistene.PersistenceCacheProxy;
import booking.server.persistene.PersistenceException;
import booking.shared.NowProvider;
import booking.shared.objects.Room;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil
{
    public static DatabaseHandler setupDatabase()
    {
        try
        {
            //
            // Run SQL setup script
            //
            {
                DatabaseConnectionPool tempConnectionPool = new DatabaseConnectionPool();
                tempConnectionPool.addConnections(1);

                URL sqlSetupUlr = TestUtil.class.getResource("setup.sql");
                String sqlSetup = Files.readString(Path.of(sqlSetupUlr.toURI()));

                Connection tempConnection = tempConnectionPool.acquireConnection();
                Statement statement = tempConnection.createStatement();

                statement.execute(sqlSetup);
                statement.close();
                tempConnectionPool.releaseConnection(tempConnection);
                tempConnectionPool.close();
            }

            //
            // Create test database instance
            //
            {
                DatabaseHandler database = new DatabaseHandler();
                database.open(1);
                return database;
            }
        }
        catch (Exception e)
        {
            // NOTE(rune): Database setup fejlede, så vi kan ikke køre test.
            throw new RuntimeException(e);
        }
    }

    public static void setdownDatabase(DatabaseHandler database)
    {
        database.close();
    }

    record ServerInstance(Thread serverThread, ServerNetwork server, DatabaseHandler database)
    {
    }

    public static ServerInstance setupServer()
    {
        DatabaseHandler database = setupDatabase();
        NowProvider nowProvider = new FakeNowProvider();
        Persistence persistence = new PersistenceCacheProxy(database, 10_000, new FakeNowProvider());
        ServerModel serverModel = new ServerModelImpl(persistence, nowProvider);
        ServerNetwork serverNetwork = new ServerNetworkSocket(serverModel);

        Thread serverThread = new Thread(serverNetwork);
        serverThread.setDaemon(true);
        serverThread.start();

        try
        {
            Thread.sleep(100); // HACK(rune): Vent på at server starter.
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return new ServerInstance(serverThread, serverNetwork, database);
    }

    public static void setdownServer(ServerInstance instance)
    {
        setdownDatabase(instance.database());
        instance.serverThread().interrupt();
        instance.server().close();
    }

    public static ClientModel setupClient()
    {
        try
        {
            FileIO fileIO = new FakeFileIO();

            NowProvider nowProvider = new FakeNowProvider();
            ClientNetwork clientNetwork = new ClientNetworkSocket();
            ClientModel clientModel = new ClientModelImpl(clientNetwork, nowProvider, fileIO);
            clientNetwork.connect();

            return clientModel;
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void assertRooms(List<Room> rooms, String... roomNames)
    {
        if (rooms.size() != roomNames.length)
        {
            assertTrue(false);
        }

        Set<String> roomNamesSet = Set.of(roomNames);
        for (int i = 0; i < rooms.size(); i++)
        {
            if (!rooms.get(i).getName().equals(roomNames[i]))
            {
                assertTrue(false);
            }
        }
    }
}
