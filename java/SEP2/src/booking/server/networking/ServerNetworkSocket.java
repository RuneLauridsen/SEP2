package booking.server.networking;

import booking.core.User;
import booking.server.model.ServerModel;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.SocketHandler;

public class ServerNetworkSocket implements Runnable
{
    private final ServerModel model;

    public ServerNetworkSocket(ServerModel model)
    {
        this.model = model;
    }

    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(2910);

            while (true)
            {
                Socket socket = serverSocket.accept();
                ServerNetworkSocketHandler socketHandler = new ServerNetworkSocketHandler(socket, model);

                Thread thread = new Thread(socketHandler);
                thread.setDaemon(true);
                thread.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
