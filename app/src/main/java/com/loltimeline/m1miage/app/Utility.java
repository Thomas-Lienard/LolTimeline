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
package com.loltimeline.m1miage.app;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loltimeline.m1miage.app.data.DatabaseHandler;
import com.loltimeline.m1miage.app.data.Match;
import com.loltimeline.m1miage.app.data.MatchContract;
import com.loltimeline.m1miage.app.data.Summoner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Utility {
    public static String MatchHistoryError(int codeError) {
        switch (codeError) {
            case 400:
                return "Bad request";
            case 401:
                return "Unauthorized";
            case 404:
                return "Game not found";
            case 422:
                return "Summoner has an entry, but hasn't played since the start of 2013";
            case 429:
                return "Rate limit exceeded";
            case 500:
                return "Internal server error";
            case 503:
                return "Service unavailable";
            default:
                return "Unknown codeError";
        }
    }


    public static String getSummonerUrlByName(String summonerName) {
        String api_key = "0610f47d-dba7-46ff-84c7-fc9eeee8b788";

        final String BASE_URL =
                "https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/";
        final String API_KEY_PARAM = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(summonerName)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();
        return builtUri.toString();
    }

    public static String getHistoryUrlBySummonerId(long id) {
        URL url = null;
        String api_key = "0610f47d-dba7-46ff-84c7-fc9eeee8b788";
        int beginIndex = 0;
        int endIndex = 10;

        final String FORECAST_BASE_URL =
                "https://euw.api.pvp.net/api/lol/euw/v2.2/matchhistory/";
        final String API_KEY_PARAM = "api_key";
        final String BEGIN_INDEX_PARAM = "beginIndex";
        final String END_INDEX = "endIndex";

        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendQueryParameter(BEGIN_INDEX_PARAM, Integer.toString(beginIndex))
                .appendQueryParameter(END_INDEX, Integer.toString(endIndex))
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    public static String getChampionImageUriById(int id) {


        return "http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/Aatrox.png";


    }

    public static String getSummonerIconUrlbyId(String iconId) {
        String URL = "http://ddragon.leagueoflegends.com/cdn/4.11.3/img/profileicon/";
        return URL.concat(String.valueOf(iconId).concat(".png"));
    }

    public static String getMapNameById(int mapId) {
        String mapName = null;
        switch (mapId) {
            case 1:
                mapName = "Summoner's Rift";
                break;
            case 2:
                mapName = "Summoner's Rift";
                break;
            case 3:
                mapName = "The Proving Grounds";
                break;
            case 4:
                mapName = "Twisted Treeline";
                break;
            case 8:
                mapName = "The Crystal Scar";
                break;
            case 10:
                mapName = "Twisted Treeline";
                break;
            case 11:
                mapName = "Summoner's Rift";
                break;
            case 12:
                mapName = "Howling Abyss";
                break;
            default:
        }
        return mapName;


    }

    public static Summoner parseJsonSummoner(JSONObject response, String value) {
        Summoner summoner = new Summoner();

        final String SUMMONER_ID = "id";
        final String ICON = "profileIconId";
        final String LEVEL = "summonerLevel";
        long id, lvl;
        String icone;

        JsonElement root = new JsonParser().parse(String.valueOf(response));
        id = root.getAsJsonObject().get(value).getAsJsonObject().get(SUMMONER_ID).getAsLong();
        lvl = root.getAsJsonObject().get(value).getAsJsonObject().get(LEVEL).getAsLong();
        icone = root.getAsJsonObject().get(value).getAsJsonObject().get(ICON).getAsString();


        summoner.setLvl(lvl);
        summoner.setName(value);
        summoner.setSummonerId(id);
        summoner.setIcon(icone);

        return summoner;
    }

    public static List<Match> parseJsonHistory(JSONObject response) {
        List<Match> matchList = new ArrayList<Match>();
        // Dans Match Summary
        final String MATCH_ID = "matchId";
        final String WINNER = "winner";
        final String MAP_ID = "mapId";
        final String QUEUE_TYPE = "queueType";
        final String MATCH_CREATION = "matchCreation";
        final String MATCH_DURATION = "matchDuration";

        // Dans Participant
        final String CHAMPION_ID = "championId";
        final String SPELL1_ID = "spell1Id";
        final String SPELL2_ID = "spell2Id";

        // Dans ParticipanStats
        final String KILLS = "kills";
        final String DEATHS = "deaths";
        final String ASSISTS = "assists";
        final String CHAMP_LEVEL = "champLevel";
        final String GOLD_EARNED = "goldEarned";
        final String MINIONS_KILLED = "minionsKilled";
        final String WARDS_PLACED = "wardsPlaced";
        final String ITEM0_ID = "item0";
        final String ITEM1_ID = "item1";
        final String ITEM2_ID = "item2";
        final String ITEM3_ID = "item3";
        final String ITEM4_ID = "item4";
        final String ITEM5_ID = "item5";
        final String ITEM6_ID = "item6";
        final String TOTAL_DAMAGE_DEALT = "totalDamageDealt";
        final String TOTAL_DAMAGE_TAKEN = "totalDamageTaken";

        try {
            JSONArray matchesArray = response.getJSONArray("matches");


            for (int i = 0; i < matchesArray.length(); i++) {
                Match match = new Match();
                JSONObject matchJson = matchesArray.getJSONObject(i);

                long matchId = matchJson.getLong(MATCH_ID);
                match.setMatchId(matchId);
                int mapId = matchJson.getInt(MAP_ID);
                match.setMap_id(mapId);
                long matchCreation = matchJson.getLong(MATCH_CREATION);
                match.setMatch_creation(matchCreation);
                long matchDuration = matchJson.getLong(MATCH_DURATION);
                match.setMatch_duration(matchDuration);
                String queueType = matchJson.getString(QUEUE_TYPE);
                match.setQueue_type(queueType);

                JSONArray participantArray = matchJson.getJSONArray("participants");

                for (int j = 0; j < participantArray.length(); j++) {
                    JSONObject participantJson = participantArray.getJSONObject(j);

                    int championId = participantJson.getInt(CHAMPION_ID);
                    match.setChampion_id(championId);

                    int spell1Id = participantJson.getInt(SPELL1_ID);
                    match.setSpelle1_id(spell1Id);

                    int spell2Id = participantJson.getInt(SPELL2_ID);
                    match.setSpell2_id(spell2Id);

                    JSONObject statsJson = participantJson.getJSONObject("stats");

                    long kills = statsJson.getLong(KILLS);
                    match.setKILLS(kills);

                    long deaths = statsJson.getLong(DEATHS);
                    match.setDEATHS(deaths);

                    long assists = statsJson.getLong(ASSISTS);
                    match.setASSISTS(assists);


                    String winner = statsJson.getString(WINNER);
                    match.setWinner(winner);




                }

                JSONArray entitiesArray = matchJson.getJSONArray("participantIdentities");
                for (int j = 0; j < entitiesArray.length(); j++) {
                    JSONObject entitiesJson = entitiesArray.getJSONObject(j);
                    JSONObject playerJson = entitiesJson.getJSONObject("player");
                    Long summoner_id = playerJson.getLong("summonerId");
                    match.setSummoner_id(summoner_id);
                    String summoner_name = playerJson.getString("summonerName");
                    match.setSummoner_name(summoner_name);

                }

                matchList.add(match);
                Log.d("UTILITY", match.toString());


            }

        } catch (JSONException e) {
            Log.e("parsingHistory", e.getMessage(), e);
            e.printStackTrace();
        }
        return matchList;
    }
}