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

import android.net.Uri;

public class Utility {
    public static String MatchHistoryError(int codeError) {
        switch(codeError) {
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

    public static String getChampionImageUriById(int id) {


        return "http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/Aatrox.png";


    }

    public static String getSummonerIconUrlbyId(int iconId) {
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


}

