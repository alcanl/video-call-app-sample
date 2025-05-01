package com.alcanl.app.chat.client;

import com.alcanl.app.chat.connection.ConnectionHandler;

import java.net.Socket;

public abstract class Client extends Thread {

    protected Socket clientSocket;
    protected String connector;
    protected ConnectionHandler connectionHandler;
    protected abstract void connect();
    @Override
    public void run()
    {
        super.run();
    }
}
