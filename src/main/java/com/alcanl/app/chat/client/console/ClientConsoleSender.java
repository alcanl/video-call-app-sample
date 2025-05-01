package com.alcanl.app.chat.client.console;

import com.alcanl.app.chat.client.Client;
import com.alcanl.app.chat.client.ClientBuilder;
import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.modules.console.IConsoleSenderBuilder;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientConsoleSender extends Client {
    private PrintWriter printWriter;
    private ClientConsoleSender(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    public void connect()
    {
        connectionHandler.clientMessageSender(clientSocket, printWriter, connector);
    }
    @Override
    public void run()
    {
        connect();
    }
    public static class Builder extends ClientBuilder implements IConsoleSenderBuilder {
        public Builder(Socket clientSocket)
        {
            client = new ClientConsoleSender( clientSocket);
        }
        @Override
        public Builder setPrintWriter(Socket clientSocket) throws IOException
        {
            ((ClientConsoleSender)client).printWriter = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
            return this;
        }
        @Override
        public Builder setConnector(String connector)
        {
            ((ClientConsoleSender)client).connector = connector;
            return this;
        }
        @Override
        public ClientConsoleSender create() {
            return ((ClientConsoleSender)client);
        }
    }
}


