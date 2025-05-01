package com.alcanl.app.chat.client.webcam;

import com.alcanl.app.chat.client.Client;
import com.alcanl.app.chat.client.ClientBuilder;
import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.modules.webcam.IWebcamSenderIBuilder;
import com.github.sarxos.webcam.Webcam;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientWebcamSender extends Client {
    private static Webcam webcam;
    private DataOutputStream dataOutputStream;
    private ClientWebcamSender(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    protected void connect()
    {
        connectionHandler.clientImageSender(clientSocket, webcam, dataOutputStream);
    }
    @Override
    public void run()
    {
        connect();
    }
    public static class Builder extends ClientBuilder implements IWebcamSenderIBuilder {
        public Builder(Socket clientSocket)
        {
            client = new ClientWebcamSender(clientSocket);
        }
        @Override
        public Builder setDataOutputStream(Socket clientSocket) throws IOException
        {
            ((ClientWebcamSender)client).dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            return this;
        }
        @Override
        public Builder setConnector(String connector)
        {
            ((ClientWebcamSender)client).connector = connector;
            return this;
        }
        @Override
        public Builder setWebcam(Webcam webcam)
        {
            ClientWebcamSender.webcam = webcam;
            return this;
        }
        @Override
        public ClientWebcamSender create() {
            return ((ClientWebcamSender)client);
        }
    }
}
