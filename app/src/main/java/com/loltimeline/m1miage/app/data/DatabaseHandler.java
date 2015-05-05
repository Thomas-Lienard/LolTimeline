package com.loltimeline.m1miage.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 01/04/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LOLDataBase";

    // Summoners table name
    private static final String TABLE_SUMMONERS = "SUMMONERS";

    // Matches table name
    private static final String TABLE_MATCHES = "matches";

    // Summoners Table Columns names
    private static final String KEY_SUMMONERID = "summonerId";
    private static final String KEY_NAME = "name";
    private static final String KEY_LVL = "lvl";
    private static final String KEY_ICON = "icon";

    // Matches Table Colums names
    private static final String KEY_MATCH_ID = "match_id";
    private static final String KEY_MAP_ID = "map_id";
    private static final String KEY_CHAMPION_ID = "champion_id";
    private static final String KEY_WINNER = "winner";
    private static final String KEY_QUEUE_TYPE = "queue_type";
    private static final String KEY_MATCH_CREATION = "match_creation";
    private static final String KEY_MATCH_DURATION = "match_duration";
    private static final String KEY_KILLS = "kills";
    private static final String KEY_DEATHS = "deaths";
    private static final String KEY_ASSISTS = "assists";
    private static final String KEY_SPELL1_ID = "spell1_id";
    private static final String KEY_SPELL2_ID = "spell2_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SUMMONERS = "CREATE TABLE " + TABLE_SUMMONERS + "("
                + KEY_SUMMONERID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_LVL + " TEXT," + KEY_ICON + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SUMMONERS);
        final String CREATE_TABLE_MATCH = "CREATE TABLE " + TABLE_MATCHES + " (" +
                KEY_MATCH_ID + " INTEGER PRIMARY KEY," +
                KEY_MAP_ID + " INTEGER," +
                KEY_CHAMPION_ID + " INTEGER NOT NULL," +
                KEY_WINNER + " TEXT NOT NULL," +
                KEY_QUEUE_TYPE + " TEXT NOT NULL," +
                KEY_MATCH_CREATION + " TEXT NOT NULL," +
                KEY_MATCH_DURATION+ " TEXT NOT NULL," +
                KEY_KILLS + " TEXT NOT NULL," +
                KEY_DEATHS + " TEXT NOT NULL," +
                KEY_ASSISTS + " TEXT NOT NULL," +
                KEY_SPELL1_ID + " INTEGER NOT NULL," +
                KEY_SPELL2_ID + " INTEGER NOT NULL" +
               /* " FOREIGN KEY (" + KEY_SUMMONER_ID + ") REFERENCES " +
                TABLE_SUMMONERS + " (" + KEY_SUMMONERID + "), " +*/
                " );";
        db.execSQL(CREATE_TABLE_MATCH);

    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUMMONERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);

        // Create tables again
        onCreate(db);
    }

    // Adding new summoner
    public void addSummoner(Summoner summoner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUMMONERID, summoner.getSummonerId());
        values.put(KEY_NAME, summoner.getName());
        values.put(KEY_LVL, summoner.getLvl());
        values.put(KEY_ICON, summoner.getIcon());


        // Inserting Row
        db.insert(TABLE_SUMMONERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single summoner
    public Summoner getSummonerById(long summonerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUMMONERS, new String[]{KEY_SUMMONERID,
                        KEY_NAME}, KEY_SUMMONERID + "=?",
                new String[]{String.valueOf(summonerId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Summoner summoner = new Summoner(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3));

        return summoner;
    }

    // Getting All summoners
    public List<Summoner> getAllSummoners() {
        List<Summoner> summonerList = new ArrayList<Summoner>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUMMONERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Summoner summoner = new Summoner();
                summoner.setSummonerId(Long.parseLong(cursor.getString(0)));
                summoner.setName(cursor.getString(1));
                summoner.setLvl(cursor.getLong(2));
                summoner.setIcon(cursor.getString(3));
                // Adding Summoner to list
                summonerList.add(summoner);
            } while (cursor.moveToNext());
        }
        // return Summoner list
        return summonerList;
    }

    // Updating single summoner
    public int updateSummoner(Summoner summoner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUMMONERID, summoner.getSummonerId());
        values.put(KEY_NAME, summoner.getName());
        values.put(KEY_LVL, summoner.getLvl());
        values.put(KEY_ICON, summoner.getIcon());

        // updating row
        return db.update(TABLE_SUMMONERS, values, KEY_SUMMONERID + " = ?",
                new String[]{String.valueOf(summoner.getSummonerId())});
    }

    // Deleting single Summoner
    public void deleteSummoner(Summoner summoner) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUMMONERS, KEY_SUMMONERID + " = ?",
                new String[]{String.valueOf(summoner.getSummonerId())});
        db.close();
    }

    // Deleting all summoners
    public void deleteAllSummoners() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SUMMONERS);
        db.close();
    }


    // Getting Summoners Count
    public int getSummonersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SUMMONERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Adding new match
    public void addMatch(Match match) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATCH_ID, match.getMatchId());
        values.put(KEY_CHAMPION_ID, match.getChampion_id());
        values.put(KEY_WINNER, match.getWinner());
        values.put(KEY_MAP_ID, match.getMap_id());
        values.put(KEY_QUEUE_TYPE, match.getQueue_type());
        values.put(KEY_KILLS, match.getKILLS());
        values.put(KEY_DEATHS, match.getDEATHS());
        values.put(KEY_ASSISTS, match.getASSISTS());
        values.put(KEY_MATCH_CREATION, match.getMatch_creation());
        values.put(KEY_MATCH_DURATION, match.getMatch_duration());
        values.put(KEY_SPELL1_ID, match.getSpelle1_id());
        values.put(KEY_SPELL2_ID, match.getSpell2_id());

        // Inserting Row
        db.insert(TABLE_MATCHES, null, values);
        db.close(); // Closing database connection
    }

   /* // Getting single match
    public Match getMatchById(long matchId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MATCHES, new String[]{KEY_MATCHID,
                        KEY_NAME}, KEY_MATCHID + "=?",
                new String[]{String.valueOf(matchId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Match match = new Match(cursor.getLong(0), cursor.getLong(1));

        return match;
    }*/

    // Getting All matchs
    public List<Match> getAllMatchs() {
        List<Match> matchesList = new ArrayList<Match>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MATCHES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Match match = new Match();
                match.setMatchId(Long.parseLong(cursor.getString(0)));
                match.setMap_id(Integer.parseInt(cursor.getString(1)));
                match.setChampion_id(Integer.parseInt(cursor.getString(2)));
                match.setWinner(cursor.getString(3));
                match.setQueue_type(cursor.getString(4));
                match.setMatch_creation(Long.parseLong(cursor.getString(5)));
                match.setMatch_duration(Long.parseLong(cursor.getString(6)));
                match.setKILLS(Long.parseLong(cursor.getString(7)));
                match.setDEATHS(Long.parseLong(cursor.getString(8)));
                match.setASSISTS(Long.parseLong(cursor.getString(9)));
                match.setSpelle1_id(Integer.parseInt(cursor.getString(10)));
                match.setSpell2_id(Integer.parseInt(cursor.getString(11)));
                // Adding Match to list
                matchesList.add(match);
            } while (cursor.moveToNext());
        }
        // return Match list
        return matchesList;
    }

    // Deleting single match
    public void deleteMatch(Match match) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MATCHES, KEY_MATCH_ID + " = ?",
                new String[]{String.valueOf(match.getMatchId())});
        db.close();
    }

    // Deleting all match
    public void deleteAllMatch() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MATCHES);
        db.close();
    }

    // Deleting all match for a summoner
    public void deleteAllMatchSummoner(Summoner summoner) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MATCHES + " WHERE " + KEY_SUMMONERID + " = " + summoner.getSummonerId());
        db.close();
    }


    // Getting matchs Count
    public int getMatchsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MATCHES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}