package ru.pyply.games.points.client;

public class ResponseAuth extends Response {

    ResponseAuth(long id, String content, int code) {
        super(id, content, code);
    }

    public boolean success() {
        return false;
    }
}
