package ru.pyply.games.landgrab;

public class RequestAuth extends Request {
    public String login;
    public String password;

    RequestAuth(String login, String password) {
        super("");
    }

    public ResponseAuth send() {
        return new ResponseAuth(0, "{}", 202);
    }
}