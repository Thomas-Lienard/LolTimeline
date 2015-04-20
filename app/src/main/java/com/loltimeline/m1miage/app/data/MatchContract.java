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

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Defines table and column names for the weather database.
 */
public class MatchContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.thomaslienard.loltimeline.app";
   // public static final String CONTENT_AUTHORITY = "euw.api.pvp.net";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("Content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MATCH = "match";
    public static final String PATH_SUMMONER = "summoner";

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT.
     */
    public static String getDbDateString(Date date){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /* Inner class that defines the table contents of the location table */
    public static final class SummonerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUMMONER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_SUMMONER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_SUMMONER;

        // Table name
        public static final String TABLE_NAME = "summoner";
        public static final String COLUMN_SUMMONER_ID = "summoner_id";
        public static final String COLUMN_SUMMONER_NAME = "summoner_name";
        public static final String COLUMN_SUMMONER_LEVEL = "summoner_level";
        public static final String COLUMN_SUMMONER_ICON = "summoner_icon";

        public static Uri buildSummonerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class MatchEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MATCH).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;

        public static final String TABLE_NAME = "match";

        // Column with the foreign key into the match table.
        public static final String COLUMN_MATCH_ID = "match_id";
        public static final String COLUMN_SUMMONER_ID = "summoner_id";
        public static final String COLUMN_CHAMPION_ID = "champion_id";
        public static final String COLUMN_WINNER = "winner";
        public static final String COLUMN_MAP_ID = "map_id";
        public static final String COLUMN_QUEUE_TYPE = "queue_type";
        public static final String COLUMN_KILLS = "kills";
        public static final String COLUMN_DEATHS = "deaths";
        public static final String COLUMN_ASSISTS = "assists";
        public static final String COLUMN_MATCH_CREATION = "match_creation";
        public static final String COLUMN_MATCH_DURATION = "match_duration";
        public static final String COLUMN_CHAMP_LEVEL = "champ_level";
        public static final String COLUMN_SPELL1_ID = "spell1_id";
        public static final String COLUMN_SPELL2_ID = "spell2_id";
        public static final String COLUMN_GOLD_EARNED = "gold_earned";
        public static final String COLUMN_MINIONS_KILLED = "minions_killed";
        public static final String COLUMN_WARDS_PLACED = "wards_placed";
        public static final String COLUMN_ITEM0_ID = "item0_id";
        public static final String COLUMN_ITEM1_ID = "item1_id";
        public static final String COLUMN_ITEM2_ID = "item2_id";
        public static final String COLUMN_ITEM3_ID = "item3_id";
        public static final String COLUMN_ITEM4_ID = "item4_id";
        public static final String COLUMN_ITEM5_ID = "item5_id";
        public static final String COLUMN_ITEM6_ID = "item6_id";
        public static final String COLUMN_TOTAL_DAMAGE_DEALT = "total_damage_dealt";
        public static final String COLUMN_TOTAL_DAMAGE_TAKEN = "total_damage_taken";



        public static Uri buildMatchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
}
