package ru.pyply.games.points.client;

public class Request {
    protected long id;
    protected long timestamp;
    protected String data;

    Request(String data) {}

    public Response send() {
        return new Response(0, "{}", 202);
    }
}
