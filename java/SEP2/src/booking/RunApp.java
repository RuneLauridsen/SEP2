package booking;

import booking.server.persistene.DatabaseHandler;
import booking.server.persistene.Persistence;
import booking.server.persistene.PersistenceCacheProxy;
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
                db.open(10);

                NowProvider nowProvider = new ReadTimeNowProvider();
                Persistence persistence = new PersistenceCacheProxy(db, 10_000, nowProvider);
                ServerModel serverModel = new ServerModelImpl(persistence, nowProvider);
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
