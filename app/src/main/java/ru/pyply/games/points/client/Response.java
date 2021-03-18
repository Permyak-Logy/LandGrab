package ru.pyply.games.points.client;

public class Response {
    protected long id;
    public String content;
    public int code;

    Response(long id, String content, int code) {}

    // TODO: Сделать возвращаемый тип Dict
    public void encodeToJSON() {
    }
}
