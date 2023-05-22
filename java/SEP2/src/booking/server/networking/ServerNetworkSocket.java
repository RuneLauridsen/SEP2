package booking.server.networking;

import booking.server.model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkSocket implements ServerNetwork
{
    private final ServerModel model;

    private boolean keepRunning;
    private ServerSocket serverSocket;

    public ServerNetworkSocket(ServerModel model)
    {
        this.model = model;
    }

    public void run()
    {
        keepRunning = true;

        try
        {
            serverSocket = new ServerSocket(2910);

            while (keepRunning)
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
        finally
        {
            close();
        }
    }

    public void close()
    {
        keepRunning = false;

        try
        {
            serverSocket.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
