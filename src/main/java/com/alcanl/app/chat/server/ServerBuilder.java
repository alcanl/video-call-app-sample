package com.alcanl.app.chat.server;

public abstract class ServerBuilder {
    protected Server server;
    protected Server create()
    {
        return server;
    }
}
