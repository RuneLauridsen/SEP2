package test;

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
            Connection connection = DatabaseHandler.openConnection();
            Statement statement = connection.createStatement();

            URL sqlSetupUlr = TestDatabaseUtil.class.getResource("setup.sql");
            String sqlSetup = Files.readString(Path.of(sqlSetupUlr.toURI()));

            statement.execute(sqlSetup);
            statement.close();
            connection.close();

            DatabaseHandler database = new DatabaseHandler();
            database.open();
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
