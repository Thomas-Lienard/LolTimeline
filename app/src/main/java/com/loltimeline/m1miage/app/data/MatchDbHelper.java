/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loltimeline.m1miage.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.loltimeline.m1miage.app.data.MatchContract.MatchEntry;
import com.loltimeline.m1miage.app.data.MatchContract.SummonerEntry;

/**
 * Manages a local database for weather data.
 */
public class MatchDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "loltimeline.db";

    public MatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_SUMMONER_TABLE = "CREATE TABLE " + SummonerEntry.TABLE_NAME + " (" +
                SummonerEntry._ID + " TEXT PRIMARY KEY," +
                SummonerEntry.COLUMN_SUMMONER_NAME + " TEXT NOT NULL, " +
                SummonerEntry.COLUMN_SUMMONER_LEVEL + " INTEGER NOT NULL, " +
                SummonerEntry.COLUMN_SUMMONER_ICON + " INTEGER NOT NULL" +
                " );";

        final String SQL_CREATE_MATCH_TABLE = "CREATE TABLE " + MatchEntry.TABLE_NAME + " (" +
                MatchEntry._ID + " TEXT PRIMARY KEY" +
                MatchEntry.COLUMN_SUMMONER_ID + " TEXT PRIMARY KEY" +
                MatchEntry.COLUMN_MAP_ID + " INTEGER PRIMARY KEY" +
                MatchEntry.COLUMN_CHAMPION_ID + " INTEGER NOT NULL, " +
                MatchEntry.COLUMN_CHAMP_LEVEL + " INTEGER NOT NULL, " +
                MatchEntry.COLUMN_WINNER + " NUMERIC NOT NULL, " +
                MatchEntry.COLUMN_QUEUE_TYPE + " TEXT NOT NULL," +
                MatchEntry.COLUMN_MATCH_CREATION + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_MATCH_DURATION+ " TEXT NOT NULL, " +
                MatchEntry.COLUMN_KILLS + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_DEATHS + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ASSISTS + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_SPELL1_ID + " INTEGER NOT NULL, " +
                MatchEntry.COLUMN_SPELL2_ID + " INTEGER NOT NULL, " +
                MatchEntry.COLUMN_WARDS_PLACED + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_GOLD_EARNED + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_MINIONS_KILLED + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_TOTAL_DAMAGE_DEALT + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_TOTAL_DAMAGE_TAKEN + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM0_ID + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM1_ID + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM2_ID + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM3_ID + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM4_ID + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM5_ID + " TEXT NOT NULL, " +
                MatchEntry.COLUMN_ITEM6_ID + " TEXT NOT NULL" +
                " FOREIGN KEY (" + MatchEntry.COLUMN_SUMMONER_ID + ") REFERENCES " +
                SummonerEntry.TABLE_NAME + " (" + SummonerEntry._ID + "), " +
                " );";

               /* // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + MatchEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                SummonerEntry.TABLE_NAME + " (" + SummonerEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MatchEntry.COLUMN_DATETEXT + ", " +
                MatchEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";*/

        sqLiteDatabase.execSQL(SQL_CREATE_SUMMONER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SummonerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MatchEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
