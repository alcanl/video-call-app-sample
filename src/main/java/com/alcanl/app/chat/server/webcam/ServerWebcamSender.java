package com.alcanl.app.chat.server.webcam;

import com.alcanl.app.chat.connection.ConnectionHandler;
import com.alcanl.app.chat.server.Server;
import com.alcanl.app.chat.server.ServerBuilder;
import com.alcanl.app.modules.webcam.IWebcamSenderIBuilder;
import com.github.sarxos.webcam.Webcam;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWebcamSender extends Server {
    public static final int PORT = 19430;
    private DataOutputStream dataOutputStream;
    private static Webcam webcam;

    private ServerWebcamSender(ServerSocket serverSocket, Socket clientSocket)
    {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        connectionHandler = new ConnectionHandler();
    }
    @Override
    protected void connect()
    {
        connectionHandler.serverImageSender(serverSocket, clientSocket, webcam, dataOutputStream);
    }

    @Override
    public void run()
    {
        connect();
    }
    public static class Builder extends ServerBuilder implements IWebcamSenderIBuilder {

        public Builder(ServerSocket serverSocket, Socket clientSocket) {
            server = new ServerWebcamSender(serverSocket, clientSocket);
        }
        @Override
        public Builder setDataOutputStream(Socket clientSocket) throws IOException {
            ((ServerWebcamSender)server).dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            return this;
        }
        @Override
        public Builder setConnector(String connector) {
            ((ServerWebcamSender)server).connector = connector;
            return this;
        }
        @Override
        public Builder setWebcam(Webcam webcam) {
            ServerWebcamSender.webcam = webcam;
            return this;
        }
        @Override
        public ServerWebcamSender create() {
            return ((ServerWebcamSender)server);
        }
    }
}
