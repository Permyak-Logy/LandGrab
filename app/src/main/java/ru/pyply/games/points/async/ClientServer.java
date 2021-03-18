package ru.pyply.games.points.async;

public class ClientServer {
    private long id;
    private String address;

    ClientServer(long id, String address) {
    }

    public boolean sendRequest(Request req) {
        return false;
    }

    public Response getResponse() {
        return new Response(0, "{}", 202);
    }
}
