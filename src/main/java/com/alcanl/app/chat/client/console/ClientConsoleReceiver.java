package com.alcanl.app.chat.client.console;

import com.alcanl.app.chat.client.Client;
import com.alcanl.app.chat.client.ClientBuilder;
import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.modules.console.IConsoleReceiverBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientConsoleReceiver extends Client {
    private BufferedReader bufferedReader;
    private ClientConsoleReceiver(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    protected void connect()
    {
        connectionHandler.clientMessageReceiver(clientSocket, bufferedReader);
    }
    @Override
    public void run()
    {
        connect();
    }
    public static class Builder extends ClientBuilder implements IConsoleReceiverBuilder {
        public Builder(Socket clientSocket)
        {
            client = new ClientConsoleReceiver(clientSocket);
        }
        @Override
        public Builder setBufferedReader(Socket clientSocket) throws IOException
        {
            ((ClientConsoleReceiver)client).bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            return this;
        }
        @Override
        public Builder setConnector(String connector)
        {
            ((ClientConsoleReceiver)client).connector = connector;
            return this;
        }
         @Override
        public ClientConsoleReceiver create() {
            return ((ClientConsoleReceiver)client);
        }
    }
}
