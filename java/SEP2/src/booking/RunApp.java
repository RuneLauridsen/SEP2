package booking;

import booking.database.DatabaseHandler;
import booking.shared.NowProvider;
import booking.shared.ReadTimeNowProvider;
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
    
                NowProvider nowProvider = new ReadTimeNowProvider();
                ServerModel serverModel = new ServerModelImpl(db, nowProvider);
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
