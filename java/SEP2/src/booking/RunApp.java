package booking;

import booking.database.DatabaseHandler;
import booking.server.Server;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetworkSocket;
import javafx.application.Application;

import java.sql.SQLException;

public class RunApp
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread serverThread = new Thread(() -> {
            try
            {
                DatabaseHandler db = new DatabaseHandler();
                db.open();

                ServerModel serverModel = new ServerModelImpl(db);
                ServerNetworkSocket socketServer = new ServerNetworkSocket(serverModel);
                socketServer.run();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        });

        serverThread.start();
        Thread.sleep(2000); // NOTE(rune): Wait for server to start

        Application.launch(App.class, args);
    }
}
