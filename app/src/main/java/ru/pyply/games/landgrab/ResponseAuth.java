package ru.pyply.games.landgrab;

public class ResponseAuth extends Response {

    ResponseAuth(long id, String content, int code) {
        super(id, content, code);
    }

    public boolean success() {
        return false;
    }
}
