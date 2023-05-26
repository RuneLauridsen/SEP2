package booking.server.networking;

import booking.server.model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            System.out.println(e.getMessage());
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
