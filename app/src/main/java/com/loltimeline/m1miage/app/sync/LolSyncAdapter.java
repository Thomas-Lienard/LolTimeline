package com.loltimeline.m1miage.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.data.MatchContract.MatchEntry;

public class LolSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = LolSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;


   /* private static final String[] NOTIFY_MATCH_PROJECTION = new String[] {
            MatchEntry.COLUMN_WEATHER_ID,
            MatchEntry.COLUMN_MAX_TEMP,
            MatchEntry.COLUMN_MIN_TEMP,
            MatchEntry.COLUMN_SHORT_DESC
    };

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;
    private static final int INDEX_SHORT_DESC = 3;*/

    public LolSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        // Getting the zipcode to send to the API


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String matchListJsonStr = null;

        String api_key = "0610f47d-dba7-46ff-84c7-fc9eeee8b788";
        int beginIndex = 0;
        int endIndex = 10;
        String summoner_id = "20862198";

        //http://api.openweathermap.org/data/2.5/weather?q=London&mode=json&units=metric&cnt=14
        //https://euw.api.pvp.net/api/lol/euw/v2.2/matchhistory/20862198?api_key=0610f47d-dba7-46ff-84c7-fc9eeee8b788
        //https://euw.api.pvp.net/api/lol/euw/v2.2/matchhistory/20862198%3F?beginIndex=0&endIndex=10&api_key=0610f47d-dba7-46ff-84c7-fc9eeee8b788
        //https://euw.api.pvp.net/api/lol/euw/v2.2/matchhistory/20862198?beginIndex=0&endIndex=10&api_key=0610f47d-dba7-46ff-84c7-fc9eeee8b788

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL =
                    "https://euw.api.pvp.net/api/lol/euw/v2.2/matchhistory/";
            final String API_KEY_PARAM = "api_key";
            final String BEGIN_INDEX_PARAM = "beginIndex";
            final String END_INDEX = "endIndex";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendPath(summoner_id)
                    .appendQueryParameter(BEGIN_INDEX_PARAM, Integer.toString(beginIndex))
                    .appendQueryParameter(END_INDEX, Integer.toString(endIndex))
                    .appendQueryParameter(API_KEY_PARAM, api_key)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d("LOL", url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            matchListJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

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
            JSONObject matchListJson = new JSONObject(matchListJsonStr);
            JSONArray matchesArray = matchListJson.getJSONArray("matches");

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(matchesArray.length());

            for (int i = 0; i < matchesArray.length(); i++) {
                ContentValues matchValues = new ContentValues();
                JSONObject matchJson = matchesArray.getJSONObject(i);

                long matchId = matchJson.getLong(MATCH_ID);
                int mapId = matchJson.getInt(MAP_ID);
                long matchCreation = matchJson.getLong(MATCH_CREATION);
                long matchDuration = matchJson.getLong(MATCH_DURATION);
                String queueType = matchJson.getString(QUEUE_TYPE);

                JSONArray participantArray = matchJson.getJSONArray("participants");

                for (int j = 0; j < participantArray.length(); j++) {
                    JSONObject participantJson = participantArray.getJSONObject(j);

                    int championId = participantJson.getInt(CHAMPION_ID);
                    matchValues.put(MatchEntry.COLUMN_CHAMPION_ID, championId);
                    int spell1Id = participantJson.getInt(SPELL1_ID);
                    matchValues.put(MatchEntry.COLUMN_SPELL1_ID, spell1Id);
                    int spell2Id = participantJson.getInt(SPELL2_ID);
                    matchValues.put(MatchEntry.COLUMN_SPELL2_ID, spell2Id);

                    JSONObject statsJson = participantJson.getJSONObject("stats");

                    long kills = statsJson.getLong(KILLS);
                    matchValues.put(MatchEntry.COLUMN_KILLS, kills);

                    long deaths = statsJson.getLong(DEATHS);
                    matchValues.put(MatchEntry.COLUMN_DEATHS, deaths);

                    long assists = statsJson.getLong(ASSISTS);
                    matchValues.put(MatchEntry.COLUMN_ASSISTS, assists);

                    long champLevel = statsJson.getLong(CHAMP_LEVEL);
                    matchValues.put(MatchEntry.COLUMN_CHAMP_LEVEL, champLevel);

                    long goldEarned = statsJson.getLong(GOLD_EARNED);
                    matchValues.put(MatchEntry.COLUMN_GOLD_EARNED, goldEarned);

                    long minionsKilled = statsJson.getLong(MINIONS_KILLED);
                    matchValues.put(MatchEntry.COLUMN_MINIONS_KILLED, minionsKilled);

                    long wardsPlaced = statsJson.getLong(WARDS_PLACED);
                    matchValues.put(MatchEntry.COLUMN_WARDS_PLACED, wardsPlaced);

                    long damageDealt = statsJson.getLong(TOTAL_DAMAGE_DEALT);
                    matchValues.put(MatchEntry.COLUMN_TOTAL_DAMAGE_DEALT, damageDealt);

                    long damageTaken = statsJson.getLong(TOTAL_DAMAGE_TAKEN);
                    matchValues.put(MatchEntry.COLUMN_TOTAL_DAMAGE_TAKEN, damageTaken);

                    boolean winner = statsJson.getBoolean(WINNER);
                    matchValues.put(MatchEntry.COLUMN_WINNER, winner);

                    long item0 = statsJson.getLong(ITEM0_ID);
                    long item1 = statsJson.getLong(ITEM1_ID);
                    long item2 = statsJson.getLong(ITEM2_ID);
                    long item3 = statsJson.getLong(ITEM3_ID);
                    long item4 = statsJson.getLong(ITEM4_ID);
                    long item5 = statsJson.getLong(ITEM5_ID);
                    long item6 = statsJson.getLong(ITEM6_ID);
                    matchValues.put(MatchEntry.COLUMN_ITEM0_ID, item0);
                    matchValues.put(MatchEntry.COLUMN_ITEM1_ID, item0);
                    matchValues.put(MatchEntry.COLUMN_ITEM2_ID, item1);
                    matchValues.put(MatchEntry.COLUMN_ITEM3_ID, item2);
                    matchValues.put(MatchEntry.COLUMN_ITEM4_ID, item3);
                    matchValues.put(MatchEntry.COLUMN_ITEM5_ID, item4);
                    matchValues.put(MatchEntry.COLUMN_ITEM6_ID, item5);

                }
                matchValues.put(MatchEntry.COLUMN_MATCH_ID, matchId);
                matchValues.put(MatchEntry.COLUMN_MAP_ID, mapId);
                matchValues.put(MatchEntry.COLUMN_MATCH_CREATION, matchCreation);
                matchValues.put(MatchEntry.COLUMN_MATCH_DURATION, matchDuration);
                matchValues.put(MatchEntry.COLUMN_QUEUE_TYPE, queueType);
                cVVector.add(matchValues);

            }
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(MatchEntry.CONTENT_URI, cvArray);


               // notifyWeather();
            }
            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return;
    }


   /* private void notifyWeather() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
                // Last sync was more than 1 day ago, let's send a notification with the weather.
                String locationQuery = Utility.getPreferredLocation(context);

                Uri weatherUri = MatchEntry.buildWeatherLocationWithDate(locationQuery, MatchContract.getDbDateString(new Date()));

                // we'll query our contentProvider, as always
                Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_MATCH_PROJECTION, null, null, null);

                if (cursor.moveToFirst()) {
                    int weatherId = cursor.getInt(INDEX_WEATHER_ID);
                    double high = cursor.getDouble(INDEX_MAX_TEMP);
                    double low = cursor.getDouble(INDEX_MIN_TEMP);
                    String desc = cursor.getString(INDEX_SHORT_DESC);

                    int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
                    String title = context.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = String.format(context.getString(R.string.format_notification),
                            desc,
                            Utility.formatTemperature(context, high),
                            Utility.formatTemperature(context, low));

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setSmallIcon(iconId)
                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());


                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.commit();
                }
            }
        }

    }*/


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        LolSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}
