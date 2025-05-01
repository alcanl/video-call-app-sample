package com.alcanl.app.chat.client;

public abstract class ClientBuilder {
    protected Client client;
    protected Client create()
    {
        return client;
    }
}
