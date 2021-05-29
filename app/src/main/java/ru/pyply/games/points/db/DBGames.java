package ru.pyply.games.points.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

@SuppressWarnings("unused")
@SuppressLint("Recycle")
public class DBGames {

    private static final String DATABASE_NAME = "points.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GAMES = "games";
    private static final String GAMES_COLUMN_ID = "id";
    private static final String GAMES_COLUMN_COUNT_PLAYERS = "count_players";
    private static final String GAMES_COLUMN_TARGET_POINTS = "target_points";
    private static final String GAMES_COLUMN_LIMIT_TIME = "limit_time";
    private static final String GAMES_COLUMN_DURATION = "duration";
    private static final int GAMES_NUM_COLUMN_ID = 0;
    private static final int GAMES_NUM_COLUMN_COUNT_PLAYERS = 1;
    private static final int GAMES_NUM_COLUMN_TARGET_POINTS = 2;
    private static final int GAMES_NUM_COLUMN_LIMIT_TIME = 3;
    private static final int GAMES_NUM_COLUMN_DURATION = 4;

    private static final String TABLE_PlAYERS = "players";
    private static final String PLAYERS_COLUMN_ID = "id";
    private static final String PLAYERS_COLUMN_NAME = "name";
    private static final int PLAYERS_NUM_COLUMN_ID = 0;
    private static final int PLAYERS_NUM_COLUMN_NAME = 1;

    private static final String TABLE_RESULTS = "results_game";
    private static final String RESULTS_COLUMN_ID = "id";
    private static final String RESULTS_COLUMN_PLAYER_ID = "player_id";
    private static final String RESULTS_COLUMN_GAME_ID = "game_id";
    private static final String RESULTS_COLUMN_RESULT = "result";
    private static final String RESULTS_COLUMN_M_POINTS = "m_points";
    private static final String RESULTS_COLUMN_CAPTURED_POINTS = "captured_points";
    private static final String RESULTS_COLUMN_LOST_POINTS = "lost_points";
    private static final String RESULTS_COLUMN_COUNT_MOVES = "count_moves";
    private static final String RESULTS_COLUMN_TOTAL_TIME_MOVES = "total_time_moves";
    private static final int RESULTS_NUM_COLUMN_ID = 0;
    private static final int RESULTS_NUM_COLUMN_PLAYER_ID = 1;
    private static final int RESULTS_NUM_COLUMN_GAME_ID = 2;
    private static final int RESULTS_NUM_COLUMN_RESULT = 3;
    private static final int RESULTS_NUM_COLUMN_M_POINTS = 4;
    private static final int RESULTS_NUM_COLUMN_CAPTURED_POINTS = 5;
    private static final int RESULTS_NUM_COLUMN_LOST_POINTS = 6;
    private static final int RESULTS_NUM_COLUMN_COUNT_MOVES = 7;
    private static final int RESULTS_NUM_COLUMN_TOTAL_TIME_MOVES = 8;

    private final SQLiteDatabase database;

    public DBGames(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        database = mOpenHelper.getWritableDatabase();
    }

    // Взаимодействие с Results
    public long insertResult(String id, int player_id, int game_id, int result, int m_points, int captured_points, int lost_points, int count_moves, long total_time_moves) {
        ContentValues cv = new ContentValues();
        cv.put(RESULTS_COLUMN_ID, id);
        cv.put(RESULTS_COLUMN_PLAYER_ID, player_id);
        cv.put(RESULTS_COLUMN_GAME_ID, game_id);
        cv.put(RESULTS_COLUMN_RESULT, result);
        cv.put(RESULTS_COLUMN_M_POINTS, m_points);
        cv.put(RESULTS_COLUMN_CAPTURED_POINTS, captured_points);
        cv.put(RESULTS_COLUMN_LOST_POINTS, lost_points);
        cv.put(RESULTS_COLUMN_COUNT_MOVES, count_moves);
        cv.put(RESULTS_COLUMN_TOTAL_TIME_MOVES, total_time_moves);

        return database.insert(TABLE_RESULTS, null, cv);
    }

    public long insertResult(Result result) {
        return insertResult(result.id, result.player_id, result.game_id, result.result, result.m_points, result.captured_points, result.lost_points, result.count_moves, result.total_time_moves);
    }

    public int updateResult(Result result) {
        ContentValues cv = new ContentValues();
        cv.put(RESULTS_COLUMN_ID, result.id);
        cv.put(RESULTS_COLUMN_PLAYER_ID, result.player_id);
        cv.put(RESULTS_COLUMN_GAME_ID, result.game_id);
        cv.put(RESULTS_COLUMN_RESULT, result.result);
        cv.put(RESULTS_COLUMN_M_POINTS, result.m_points);
        cv.put(RESULTS_COLUMN_CAPTURED_POINTS, result.captured_points);
        cv.put(RESULTS_COLUMN_LOST_POINTS, result.lost_points);
        cv.put(RESULTS_COLUMN_COUNT_MOVES, result.count_moves);
        cv.put(RESULTS_COLUMN_TOTAL_TIME_MOVES, result.total_time_moves);
        return database.update(TABLE_RESULTS, cv,
                RESULTS_COLUMN_PLAYER_ID + " = ? AND " + RESULTS_COLUMN_GAME_ID + " = ?",
                new String[]{String.valueOf(result.player_id), String.valueOf(result.game_id)});
    }

    public void deleteAllResults() {
        database.delete(TABLE_RESULTS, null, null);
    }

    public void deleteResult(int player_id, int game_id) {
        database.delete(TABLE_RESULTS,
                RESULTS_COLUMN_PLAYER_ID + " = ? AND " + RESULTS_COLUMN_GAME_ID + " = ?",
                new String[]{String.valueOf(player_id), String.valueOf(game_id)});
    }

    public void deletePlayer(Result result) {
        deleteResult(result.player_id, result.game_id);
    }

    public Result selectResult(int player_id, int game_id) {
        Cursor mCursor = database.query(TABLE_RESULTS, null, RESULTS_COLUMN_PLAYER_ID + " = ? AND " + RESULTS_COLUMN_GAME_ID + " = ?",
                new String[]{String.valueOf(player_id), String.valueOf(game_id)}, null, null, null);
        int result = mCursor.getInt(RESULTS_NUM_COLUMN_RESULT);
        int m_points = mCursor.getInt(RESULTS_NUM_COLUMN_M_POINTS);
        int captured_points = mCursor.getInt(RESULTS_NUM_COLUMN_CAPTURED_POINTS);
        int lost_points = mCursor.getInt(RESULTS_NUM_COLUMN_LOST_POINTS);
        int count_moves = mCursor.getInt(RESULTS_NUM_COLUMN_COUNT_MOVES);
        long total_time_moves = mCursor.getLong(RESULTS_NUM_COLUMN_TOTAL_TIME_MOVES);
        return new Result(player_id, game_id, result, m_points, captured_points, lost_points, count_moves, total_time_moves);
    }

    public ArrayList<Result> selectAllResults() {
        Cursor mCursor = database.query(TABLE_RESULTS, null, null, null, null, null, null);

        ArrayList<Result> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                int player_id = mCursor.getInt(RESULTS_NUM_COLUMN_PLAYER_ID);
                int game_id = mCursor.getInt(RESULTS_NUM_COLUMN_GAME_ID);
                int result = mCursor.getInt(RESULTS_NUM_COLUMN_RESULT);
                int m_points = mCursor.getInt(RESULTS_NUM_COLUMN_M_POINTS);
                int captured_points = mCursor.getInt(RESULTS_NUM_COLUMN_CAPTURED_POINTS);
                int lost_points = mCursor.getInt(RESULTS_NUM_COLUMN_LOST_POINTS);
                int count_moves = mCursor.getInt(RESULTS_NUM_COLUMN_COUNT_MOVES);
                long total_time_moves = mCursor.getLong(RESULTS_NUM_COLUMN_TOTAL_TIME_MOVES);
                arr.add(new Result(player_id, game_id, result, m_points, captured_points, lost_points, count_moves, total_time_moves));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    // Взаимодействие с Players
    public long insertPlayer(String name) {
        ContentValues cv = new ContentValues();
        cv.put(PLAYERS_COLUMN_NAME, name);

        return database.insert(TABLE_PlAYERS, null, cv);
    }

    public long insertPlayer(Player player) {
        return insertPlayer(player.name);
    }

    public int updatePlayer(Player player) {
        ContentValues cv = new ContentValues();
        cv.put(PLAYERS_COLUMN_NAME, player.name);

        return database.update(TABLE_PlAYERS, cv, PLAYERS_COLUMN_ID + " = ?", new String[]{String.valueOf(player.id)});
    }

    public void deleteAllPlayers() {
        database.delete(TABLE_PlAYERS, null, null);
    }

    public void deletePlayer(int id) {
        database.delete(TABLE_PlAYERS, PLAYERS_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deletePlayer(Player player) {
        deletePlayer(player.id);
    }

    public Player selectPlayer(int id) {
        Cursor mCursor = database.query(TABLE_PlAYERS, null, PLAYERS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        String name = mCursor.getString(PLAYERS_NUM_COLUMN_NAME);
        return new Player(id, name);
    }

    public ArrayList<Player> selectAllPlayers() {
        Cursor mCursor = database.query(TABLE_PlAYERS, null, null, null, null, null, null);

        ArrayList<Player> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                int id = mCursor.getInt(PLAYERS_NUM_COLUMN_ID);
                String name = mCursor.getString(PLAYERS_NUM_COLUMN_NAME);
                arr.add(new Player(id, name));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    // Взаимодействие c Games
    public long insertGame(int count_players, int target_points, int limit_time, long duration) {
        ContentValues cv = new ContentValues();
        cv.put(GAMES_COLUMN_COUNT_PLAYERS, count_players);
        cv.put(GAMES_COLUMN_TARGET_POINTS, target_points);
        cv.put(GAMES_COLUMN_LIMIT_TIME, limit_time);
        cv.put(GAMES_COLUMN_DURATION, duration);
        return database.insert(TABLE_GAMES, null, cv);
    }

    public long insertGame(Game game) {
        return insertGame(game.count_players, game.target_points, game.limit_time, game.duration);
    }

    public int updateGame(Game game) {
        ContentValues cv = new ContentValues();
        cv.put(GAMES_COLUMN_COUNT_PLAYERS, game.count_players);
        cv.put(GAMES_COLUMN_TARGET_POINTS, game.target_points);
        cv.put(GAMES_COLUMN_LIMIT_TIME, game.limit_time);
        cv.put(GAMES_COLUMN_DURATION, game.duration);
        return database.update(TABLE_GAMES, cv, GAMES_COLUMN_ID + " = ?", new String[]{String.valueOf(game.id)});
    }

    public void deleteAllGames() {
        database.delete(TABLE_GAMES, null, null);
    }

    public void deleteGame(int id) {
        database.delete(TABLE_GAMES, GAMES_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteGame(Game game) {
        deleteGame(game.id);
    }

    public Game selectGame(int id) {
        Cursor mCursor = database.query(TABLE_GAMES, null, GAMES_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        int count_players = mCursor.getInt(GAMES_NUM_COLUMN_COUNT_PLAYERS);
        int target_points = mCursor.getInt(GAMES_NUM_COLUMN_TARGET_POINTS);
        int limit_time = mCursor.getInt(GAMES_NUM_COLUMN_LIMIT_TIME);
        long duration = mCursor.getLong(GAMES_NUM_COLUMN_DURATION);
        return new Game(id, count_players, target_points, limit_time, duration);
    }

    public ArrayList<Game> selectAllGames() {
        Cursor mCursor = database.query(TABLE_GAMES, null, null, null, null, null, null);
        ArrayList<Game> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                int id = mCursor.getInt(GAMES_NUM_COLUMN_ID);
                int count_players = mCursor.getInt(GAMES_NUM_COLUMN_COUNT_PLAYERS);
                int target_points = mCursor.getInt(GAMES_NUM_COLUMN_TARGET_POINTS);
                int limit_time = mCursor.getInt(GAMES_NUM_COLUMN_LIMIT_TIME);
                long duration = mCursor.getLong(GAMES_NUM_COLUMN_DURATION);
                arr.add(new Game(id, count_players, target_points, limit_time, duration));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @SuppressLint("SQLiteString")
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_PlAYERS + " (" +
                    PLAYERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    PLAYERS_COLUMN_NAME + " STRING UNIQUE NOT NULL" +
                    ");"
            );

            db.execSQL("CREATE TABLE " + TABLE_GAMES + " (" +
                    GAMES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    GAMES_COLUMN_COUNT_PLAYERS + " INT," +
                    GAMES_COLUMN_TARGET_POINTS + " INT," +
                    GAMES_COLUMN_LIMIT_TIME + " INT," +
                    GAMES_COLUMN_DURATION + " LONG" +
                    ");"
            );

            db.execSQL("CREATE TABLE " + TABLE_RESULTS + " (" +
                    RESULTS_COLUMN_ID + " STRING PRIMARY KEY," +
                    RESULTS_COLUMN_PLAYER_ID + " INTEGER REFERENCES " + TABLE_PlAYERS + " (" + PLAYERS_COLUMN_ID + "), " +
                    RESULTS_COLUMN_GAME_ID + " LONG REFERENCES " + TABLE_GAMES + " (" + GAMES_COLUMN_ID + ")," +
                    RESULTS_COLUMN_RESULT + " INT NOT NULL," +
                    RESULTS_COLUMN_M_POINTS + " INT NOT NULL," +
                    RESULTS_COLUMN_CAPTURED_POINTS + " INT NOT NULL," +
                    RESULTS_COLUMN_LOST_POINTS + " INT NOT NULL," +
                    RESULTS_COLUMN_COUNT_MOVES + " INT NOT NULL," +
                    RESULTS_COLUMN_TOTAL_TIME_MOVES + " LONG NOT NULL" +
                    ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PlAYERS);
            onCreate(db);
        }
    }
}
