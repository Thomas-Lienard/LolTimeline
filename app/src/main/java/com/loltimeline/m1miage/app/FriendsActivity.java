package com.loltimeline.m1miage.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loltimeline.m1miage.app.adapter.FriendListAdapter;
import com.loltimeline.m1miage.app.data.DatabaseHandler;
import com.loltimeline.m1miage.app.data.Summoner;
import com.loltimeline.m1miage.app.fragment.AddFriendDialogFragment;
import com.loltimeline.m1miage.app.volley.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 14/04/2015.
 */

public class FriendsActivity extends ActionBarActivity implements AddFriendDialogFragment.AddFriendDialogListener {

    private static final String TAG = FriendsActivity.class.getSimpleName();
    private ListView listView;
    private FriendListAdapter listAdapter;
    private List<Summoner> summonerList;
    protected DatabaseHandler db = new DatabaseHandler(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }

    protected void onResume() {
        super.onResume();
        listView = (ListView) findViewById(R.id.list_friends);

        summonerList = new ArrayList<Summoner>();

        // Reading all contacts
        summonerList = db.getAllSummoners();
        listAdapter = new FriendListAdapter(this, summonerList);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

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


        String URL = Utility.getSummonerUrlByName(value);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.d(TAG, "ok1");
                        Summoner summoner = Utility.parseJsonSummoner(response, value);
                        //summoner.setSummonerId(JSonParser.parseJSONSummonerId(response, value));
                        Log.d("OK2", String.valueOf(summoner.getSummonerId()));
                        Log.d("OK3", String.valueOf(summoner.getSummonerId()));
                        db.addSummoner(summoner);
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
        this.recreate();
        db.close();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    private Activity getActivity() {
        return this.getActivity();
    }
}