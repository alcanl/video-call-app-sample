package com.alcanl.app.chat.server.webcam;

import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.chat.server.Server;
import com.alcanl.app.chat.server.ServerBuilder;
import com.alcanl.app.global.ImageDisplayPanel;
import com.alcanl.app.global.Resources;
import com.alcanl.app.modules.webcam.IWebcamReceiverIBuilder;
import com.karandev.io.util.console.Console;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWebcamReceiver extends Server {
    private DataInputStream dataInputStream;
    private ImageDisplayPanel imageDisplayPanel;
    private ServerWebcamReceiver(ServerSocket serverSocket, Socket clientSocket)
    {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    protected void connect()
    {
         connectionHandler.serverImageReceiver(serverSocket, clientSocket, imageDisplayPanel, dataInputStream);
    }
    @Override
    public void run()
    {
        connect();
    }
    public static class Builder extends ServerBuilder implements IWebcamReceiverIBuilder {
        public Builder(ServerSocket serverSocket, Socket clientSocket)
        {
            server = new ServerWebcamReceiver(serverSocket, clientSocket);
        }
        @Override
        public Builder setDataInputStream(Socket clientSocket)
        {
            try {
                ((ServerWebcamReceiver)server).dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            }
            catch (IOException ex)
            {
                Console.writeLine(ex.getMessage());
            }
            return this;
        }
        @Override
        public Builder setConnector(String connector)
        {
            ((ServerWebcamReceiver)server).connector = connector;
            return this;
        }
        @Override
        public Builder setImageDisplayPanel(ImageDisplayPanel imageDisplayPanel)
        {
            ((ServerWebcamReceiver)server).imageDisplayPanel = imageDisplayPanel;
            Resources.setJFrame(((ServerWebcamReceiver)server).imageDisplayPanel);

            return this;
        }
        @Override
        public ServerWebcamReceiver create()
        {
            return ((ServerWebcamReceiver)server);
        }
    }
}
