package ru.pyply.games.landgrab;

import java.util.Dictionary;

public class Response {
    protected long id;
    public String content;
    public int code;

    Response(long id, String content, int code) {}

    // TODO: Сделать возвращаемый тип Dict
    public void encodeToJSON() {
    }
}
