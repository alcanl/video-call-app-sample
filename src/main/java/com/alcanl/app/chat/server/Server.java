package com.alcanl.app.chat.server;

import com.alcanl.app.chat.connection.ConnectionHandler;

import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server extends Thread {
    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    protected String connector;
    protected ConnectionHandler connectionHandler;
    public static final String IP_ADDRESS = "localhost";
    protected abstract void connect();
    @Override
    public void run()
    {
        super.run();
    }
}
