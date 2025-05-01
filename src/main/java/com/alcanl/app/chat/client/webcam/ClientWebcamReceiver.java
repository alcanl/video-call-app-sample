package com.alcanl.app.chat.client.webcam;

import com.alcanl.app.chat.client.Client;
import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.global.ImageDisplayPanel;
import com.alcanl.app.global.Resources;
import com.alcanl.app.chat.client.ClientBuilder;
import com.alcanl.app.modules.webcam.IWebcamReceiverIBuilder;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientWebcamReceiver extends Client {
    private DataInputStream dataInputStream;
    private  ImageDisplayPanel imageDisplayPanel;
    private ClientWebcamReceiver(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    protected void connect()
    {
        connectionHandler.clientImageReceiver(clientSocket, imageDisplayPanel, dataInputStream);
    }
    @Override
    public void run()
    {
        connect();
    }
    public static class Builder extends ClientBuilder implements IWebcamReceiverIBuilder {
        public Builder(Socket clientSocket)
        {
            this.client = new ClientWebcamReceiver(clientSocket);
        }
        @Override
        public Builder setDataInputStream(Socket clientSocket)
        {
            try {
                ((ClientWebcamReceiver)client).dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            }
            catch (IOException ex)
            {
                Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
            }
            return this;
        }
        @Override
        public Builder setConnector(String connector)
        {
            ((ClientWebcamReceiver)client).connector = connector;
            return this;
        }
        @Override
        public Builder setImageDisplayPanel(ImageDisplayPanel imageDisplayPanel)
        {
            ((ClientWebcamReceiver)client).imageDisplayPanel = imageDisplayPanel;
            Resources.setJFrame(((ClientWebcamReceiver)client).imageDisplayPanel);

            return this;
        }
        @Override
        public ClientWebcamReceiver create()
        {
            return (ClientWebcamReceiver)client;
        }
    }
}
