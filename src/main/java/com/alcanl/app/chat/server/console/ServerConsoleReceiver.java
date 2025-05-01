package com.alcanl.app.chat.server.console;

import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.chat.server.Server;
import com.alcanl.app.chat.server.ServerBuilder;
import com.alcanl.app.modules.IBuilder;
import com.alcanl.app.modules.console.IConsoleReceiverBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerConsoleReceiver extends Server {
    private BufferedReader bufferedReader;

    private ServerConsoleReceiver(ServerSocket serverSocket, Socket clientSocket)
    {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    protected void connect()
    {
        connectionHandler.serverMessageReceiver(serverSocket, clientSocket, bufferedReader);
    }
    @Override
    public void run()
    {
        connect();
    }

    public static class ConsoleReceiverBuilder extends ServerBuilder implements IConsoleReceiverBuilder {
        public ConsoleReceiverBuilder(ServerSocket serverSocket, Socket clientSocket)
        {
            server = new ServerConsoleReceiver(serverSocket, clientSocket);
        }
        @Override
        public ConsoleReceiverBuilder setBufferedReader(Socket clientSocket) throws IOException
        {
            ((ServerConsoleReceiver)server).bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            return this;
        }
        @Override
        public ServerConsoleReceiver create()
        {
            return ((ServerConsoleReceiver)server);
        }

        @Override
        public IBuilder setConnector(String connector)
        {
            ((ServerConsoleReceiver)server).connector = connector;
            return this;
        }
    }
}
