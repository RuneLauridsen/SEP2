package booking.server.networking;

import booking.server.model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
