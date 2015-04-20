package com.loltimeline.m1miage.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loltimeline.m1miage.app.data.MatchContract;
import com.loltimeline.m1miage.app.data.MatchContract.MatchEntry;
import com.loltimeline.m1miage.app.data.MatchContract.SummonerEntry;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loltimeline.m1miage.app.fragment.AddFriendDialogFragment;
import com.loltimeline.m1miage.app.volley.AppController;

import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Thomas on 14/04/2015.
 */

public class FriendsActivity extends ActionBarActivity implements AddFriendDialogFragment.AddFriendDialogListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = FriendsActivity.class.getSimpleName();


    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] SUMMONER_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MatchContract.SummonerEntry.TABLE_NAME + "." + MatchContract.SummonerEntry._ID,
            MatchContract.SummonerEntry.COLUMN_SUMMONER_ICON,
            MatchContract.SummonerEntry.COLUMN_SUMMONER_NAME,
            MatchContract.SummonerEntry.COLUMN_SUMMONER_LEVEL
    };

    // These indices are tied to SUMMONER_COLUMNS.  If SUMMONER_COLUMNS changes, these
    // must change.
    public static final int COL_SUMMONER_ID = 0;
    public static final int COL_ICON = 1;
    public static final int COL_NAME = 2;
    public static final int COL_LEVEL = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }

    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar match clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_friends_add) {
            showNoticeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddFriendDialogFragment();
        dialog.show(getFragmentManager(), "AddFriendDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, final String value) {

        //summoner.setName(value);

        String URL = Utility.getSummonerUrlByName(value);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Vector<ContentValues> cVVector = new Vector<ContentValues>();
                        // These are the names of the JSON objects that need to be extracted.
                        // Dans Match Summary
                        final String SUMMONER_ID = "summonerId";
                        final String ICON = "profileIconId";
                        final String LEVEL = "summonerLevel";
                        ContentValues summonerValues = new ContentValues();
                        long id, lvl;
                        String icon;

                        JsonElement root = new JsonParser().parse(String.valueOf(response));
                        id = root.getAsJsonObject().get(value).getAsJsonObject().get(SUMMONER_ID).getAsLong();
                        lvl = root.getAsJsonObject().get(value).getAsJsonObject().get(LEVEL).getAsLong();
                        icon = Utility.getSummonerIconUrlbyId(root.getAsJsonObject().get(value).getAsJsonObject().get(ICON).getAsInt());

                        summonerValues.put(MatchContract.SummonerEntry.COLUMN_SUMMONER_ID, id);
                        summonerValues.put(MatchContract.SummonerEntry.COLUMN_SUMMONER_ICON, icon);
                        summonerValues.put(MatchContract.SummonerEntry.COLUMN_SUMMONER_LEVEL, lvl);
                        summonerValues.put(MatchContract.SummonerEntry.COLUMN_SUMMONER_NAME, value);

                        if (cVVector.size() > 0) {
                            ContentValues[] cvArray = new ContentValues[cVVector.size()];
                            cVVector.toArray(cvArray);
                            getApplicationContext().getContentResolver().bulkInsert(MatchContract.MatchEntry.CONTENT_URI, cvArray);
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        //CustomToast.printErrorToast(getActivity(), "Ce joueur n'existe pas bb");
                        Toast.makeText(getApplicationContext(), "Ce joueur n'existe pas", Toast.LENGTH_LONG).show();


                    }

                });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

        dialog.dismiss();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



