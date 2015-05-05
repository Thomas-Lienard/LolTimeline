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
import android.content.Intent;
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
import com.loltimeline.m1miage.app.adapter.MatchListAdapter;
import com.loltimeline.m1miage.app.data.DatabaseHandler;
import com.loltimeline.m1miage.app.data.Match;
import com.loltimeline.m1miage.app.data.Summoner;
import com.loltimeline.m1miage.app.volley.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;
    private List<Summoner> summonerList;
    private List<Match> matchList;
    private ListView listView;
    private MatchListAdapter listAdapter;
    protected DatabaseHandler db = new DatabaseHandler(this);

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.match_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.match_detail_container, new MatchDetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        MatchListFragment matchListFragment =  ((MatchListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.match_list_fragment));

        LolSyncAdapter.initializeSyncAdapter(this);

    }*/



   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.fragment_match_list);
   }

    protected void onResume() {
        super.onResume();
        listView = (ListView) findViewById(R.id.listview_match);

        getAllMatch();

        matchList = new ArrayList<Match>();
        matchList = db.getAllMatchs();

        listAdapter = new MatchListAdapter(this, matchList);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

    }

    public Activity getActivity(){
        return this.getActivity();
    }

    private void getAllMatch(){
        summonerList = db.getAllSummoners();

        for (int i=0; i<summonerList.size(); i++){
            String URL = Utility.getHistoryUrlBySummonerId(summonerList.get(i).getSummonerId());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());

                            List<Match> matches = Utility.parseJsonHistory(response);
                            for (int j=0; j<matches.size() ; j++){
                                db.addMatch(matches.get(j));
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.toString());
                            Toast.makeText(getApplicationContext(), "Ce match n'existe pas", Toast.LENGTH_LONG).show();

                        }
                    });
            // Access the RequestQueue through your singleton class.
            AppController.getInstance().addToRequestQueue(jsObjRequest);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_friends) {
            startActivity(new Intent(this, FriendsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


   /* @Override
    public void onItemSelected(String date) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(MatchDetailActivity.DATE_KEY, date);

            MatchDetailFragment fragment = new MatchDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.match_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MatchDetailActivity.class)
                    .putExtra(MatchDetailActivity.DATE_KEY, date);
            startActivity(intent);
        }
    }*/
}
