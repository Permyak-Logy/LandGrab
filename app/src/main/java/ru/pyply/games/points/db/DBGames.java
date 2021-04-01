package ru.pyply.games.points.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBGames {

    private static final String DATABASE_NAME = "statistic.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "statistic";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RESULT = "result";
    private static final String COLUMN_COUNT_PLAYERS = "count_players";
    private static final String COLUMN_M_POINTS = "m_points";
    private static final String COLUMN_M_SQUARE = "m_square";
    private static final String COLUMN_TRAVELS = "travels";
    private static final String COLUMN_AVERAGE_TRAVEL_TIME = "average_travel_time";
    private static final String COLUMN_TOTAL_GAME_TIME = "total_game_time";
    private static final String COLUMN_CAPTURED_POINTS = "captured_points";
    private static final String COLUMN_LOST_POINTS = "lost_points";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_RESULT = 1;
    private static final int NUM_COLUMN_COUNT_PLAYERS = 2;
    private static final int NUM_COLUMN_M_POINTS = 3;
    private static final int NUM_COLUMN_M_SQUARE = 4;
    private static final int NUM_COLUMN_TRAVELS = 5;
    private static final int NUM_COLUMN_AVERAGE_TRAVEL_TIME = 6;
    private static final int NUM_COLUMN_TOTAL_GAME_TIME = 7;
    private static final int NUM_COLUMN_CAPTURED_POINTS = 8;
    private static final int NUM_COLUMN_LOST_POINTS = 9;

    private final SQLiteDatabase database;

    public DBGames(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        database = mOpenHelper.getWritableDatabase();
    }

    public long insert(int result, byte countPlayers, int mPoints, double mSquare, int travels, double averageTravelTime, double totalGameTime, int capturedPoints, int lostPoints) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RESULT, result);
        cv.put(COLUMN_COUNT_PLAYERS, countPlayers);
        cv.put(COLUMN_M_POINTS, mPoints);
        cv.put(COLUMN_M_SQUARE, mSquare);
        cv.put(COLUMN_TRAVELS, travels);
        cv.put(COLUMN_AVERAGE_TRAVEL_TIME, averageTravelTime);
        cv.put(COLUMN_TOTAL_GAME_TIME, totalGameTime);
        cv.put(COLUMN_CAPTURED_POINTS, capturedPoints);
        cv.put(COLUMN_LOST_POINTS, lostPoints);
        return database.insert(TABLE_NAME, null, cv);
    }

    public int update(Statistics sts) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RESULT, sts.getResult());
        cv.put(COLUMN_COUNT_PLAYERS, sts.getCountPlayers());
        cv.put(COLUMN_M_POINTS, sts.getMPoints());
        cv.put(COLUMN_M_SQUARE, sts.getMSquare());
        cv.put(COLUMN_TRAVELS, sts.getTravels());
        cv.put(COLUMN_AVERAGE_TRAVEL_TIME, sts.getAverageTravelTime());
        cv.put(COLUMN_TOTAL_GAME_TIME, sts.getTotalGameTime());
        cv.put(COLUMN_CAPTURED_POINTS, sts.getCapturedPoints());
        cv.put(COLUMN_LOST_POINTS, sts.getLostPoints());
        return database.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(sts.getId())});
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Statistics select(long id) {
        @SuppressLint("Recycle") Cursor mCursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        int victory = mCursor.getInt(NUM_COLUMN_RESULT);
        int countPlayers = mCursor.getInt(NUM_COLUMN_COUNT_PLAYERS);
        int mPoints = mCursor.getInt(NUM_COLUMN_M_POINTS);
        double mSquare = mCursor.getDouble(NUM_COLUMN_M_SQUARE);
        int travels = mCursor.getInt(NUM_COLUMN_TRAVELS);
        double averageTravelTime = mCursor.getDouble(NUM_COLUMN_AVERAGE_TRAVEL_TIME);
        double totalGameTime = mCursor.getDouble(NUM_COLUMN_TOTAL_GAME_TIME);
        int capturedPoints = mCursor.getInt(NUM_COLUMN_CAPTURED_POINTS);
        int lostPoints = mCursor.getInt(NUM_COLUMN_LOST_POINTS);
        return new Statistics(id, victory, countPlayers, mPoints, mSquare, travels, averageTravelTime, totalGameTime, capturedPoints, lostPoints);
    }

    public ArrayList<Statistics> selectAll() {
        @SuppressLint("Recycle") Cursor mCursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Statistics> arr = new ArrayList<Statistics>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                int victory = mCursor.getInt(NUM_COLUMN_RESULT);
                int countPlayers = mCursor.getInt(NUM_COLUMN_COUNT_PLAYERS);
                int mPoints = mCursor.getInt(NUM_COLUMN_M_POINTS);
                double mSquare = mCursor.getDouble(NUM_COLUMN_M_SQUARE);
                int travels = mCursor.getInt(NUM_COLUMN_TRAVELS);
                double averageTravelTime = mCursor.getDouble(NUM_COLUMN_AVERAGE_TRAVEL_TIME);
                double totalGameTime = mCursor.getDouble(NUM_COLUMN_TOTAL_GAME_TIME);
                int capturedPoints = mCursor.getInt(NUM_COLUMN_CAPTURED_POINTS);
                int lostPoints = mCursor.getInt(NUM_COLUMN_LOST_POINTS);
                arr.add(new Statistics(id, victory, countPlayers, mPoints, mSquare, travels, averageTravelTime, totalGameTime, capturedPoints, lostPoints));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RESULT + " INT, " +
                    COLUMN_COUNT_PLAYERS + " INT, " +
                    COLUMN_M_POINTS + " INT," +
                    COLUMN_M_SQUARE + " DOUBLE, " +
                    COLUMN_TRAVELS + " INT, " +
                    COLUMN_AVERAGE_TRAVEL_TIME + " DOUBLE, " +
                    COLUMN_TOTAL_GAME_TIME + " DOUBLE, " +
                    COLUMN_CAPTURED_POINTS + " INT, " +
                    COLUMN_LOST_POINTS + " INT); ";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
