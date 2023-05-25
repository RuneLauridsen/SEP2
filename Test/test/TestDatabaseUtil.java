package test;

import booking.server.persistene.DatabaseConnectionPool;
import booking.server.persistene.DatabaseHandler;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

public class TestDatabaseUtil
{
    public static DatabaseHandler setup()
    {
        try
        {
            DatabaseConnectionPool tempConnectionPool = new DatabaseConnectionPool();
            tempConnectionPool.addConnections(1);

            URL sqlSetupUlr = TestDatabaseUtil.class.getResource("setup.sql");
            String sqlSetup = Files.readString(Path.of(sqlSetupUlr.toURI()));

            Connection tempConnection = tempConnectionPool.acquireConnection();
            Statement statement = tempConnection.createStatement();

            statement.execute(sqlSetup);
            statement.close();
            tempConnectionPool.releaseConnection(tempConnection);
            tempConnectionPool.close();

            DatabaseHandler database = new DatabaseHandler();
            database.open(1);
            return database;
        }
        catch (Exception e)
        {
            // NOTE(rune): Database setup fejlede -> kan ikke køre test.
            throw new RuntimeException(e);
        }
    }

    public static void setdown(DatabaseHandler database)
    {
        database.close();
    }
}
