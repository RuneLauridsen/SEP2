package booking.server.persistene;

import booking.server.logger.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseConnectionPool
{
    private final ArrayList<Connection> freeConnections;
    private final ArrayList<Connection> busyConnections;

    public DatabaseConnectionPool()
    {
        freeConnections = new ArrayList<>();
        busyConnections = new ArrayList<>();
    }

    public synchronized void addConnections(int connectionCount) throws SQLException
    {
        for (int i = 0; i < connectionCount; i++)
        {
            freeConnections.add(newConnection());
        }
    }

    public synchronized Connection acquireConnection()
    {
        while (freeConnections.size() == 0)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                Logger.log(e);
            }
        }

        Connection connection = freeConnections.remove(0);
        busyConnections.add(connection);

        return connection;
    }

    public synchronized void releaseConnection(Connection connection)
    {
        busyConnections.remove(connection);
        freeConnections.add(connection);
        notifyAll();
    }

    public synchronized void close()
    {
        while (busyConnections.size() > 0)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                Logger.log(e);
            }
        }

        for (Connection connection : freeConnections)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                Logger.log(e);
            }
        }

        freeConnections.clear();
    }

    private static Connection newConnection() throws SQLException
    {
        String connectionString = getConnectionString();

        return DriverManager.getConnection(connectionString);

        /*
        switch (System.getProperty("user.name"))
        {
            case "runel":
                // TODO(rune): Forbinde til rigtig database, ikke bare localhost. Måske er måde at konfigurere
                // hvilken connection string der skal bruges, f.eks. debug database eller prod database.
                return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=asdasd");
            case "jbram":
                // TODO(rune): Forbinde til rigtig database, ikke bare localhost. Måske er måde at konfigurere
                // hvilken connection string der skal bruges, f.eks. debug database eller prod database.
                return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=1234");

            default:
                throw new RuntimeException("Unknown user");
        }

         */
    }

    private static String getConnectionString() throws SQLException
    {
        try
        {
            URL connectionStringURL = DatabaseConnectionPool.class.getResource("DatabaseConnectionString.txt");
            String connectionString = Files.readString(Path.of(connectionStringURL.toURI()));
            return connectionString;
        }
        catch (IOException | URISyntaxException e)
        {
            Logger.log(e);
            throw new SQLException("Could not read connection string from resource file.", e);
        }
    }
}
