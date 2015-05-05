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
 *//*

package com.loltimeline.m1miage.app.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.loltimeline.m1miage.app.FriendsActivity;
import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.SettingsActivity;
import com.loltimeline.m1miage.app.adapter.MatchListAdapter;
import com.loltimeline.m1miage.app.data.MatchContract.MatchEntry;
import com.loltimeline.m1miage.app.data.MatchContract.SummonerEntry;
import com.loltimeline.m1miage.app.sync.LolSyncAdapter;

*/
/**
 * Encapsulates fetching the forecast and displaying it as a {@link android.widget.ListView} layout.
 *//*

public class MatchListFragment extends Fragment implements LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MatchListFragment.class.getSimpleName();
    private MatchListAdapter mMatchListAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int MATCH_LOADER = 0;

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] MATCH_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MatchEntry.TABLE_NAME + "." + MatchEntry._ID,
            MatchEntry.COLUMN_ASSISTS,
            MatchEntry.COLUMN_CHAMPION_ID,
            MatchEntry.COLUMN_MATCH_CREATION,
            MatchEntry.COLUMN_DEATHS,
            MatchEntry.COLUMN_KILLS,
            MatchEntry.COLUMN_MATCH_DURATION,
            MatchEntry.COLUMN_WINNER,
            MatchEntry.COLUMN_QUEUE_TYPE,
            SummonerEntry.COLUMN_SUMMONER_NAME,
            MatchEntry.COLUMN_MAP_ID,
    };


    // These indices are tied to MATCH_COLUMNS.  If MATCH_COLUMNS changes, these
    // must change.

    public static final int COL_MATCH_ID = 0;
    public static final int COL_ASSISTS = 1;
    public static final int COL_CHAMPION_ID = 2;
    public static final int COL_MATCH_CREATION = 3;
    public static final int COL_DEATHS = 4;
    public static final int COL_KILLS = 5;
    public static final int COL_MATCH_DURATION = 6;
    public static final int COL_WINNER = 7;
    public static final int COL_QUEUE_TYPE = 8;
    public static final int COL_SUMMONER_NAME = 9;
    public static final int COL_MAP_ID = 10;


    */
/**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     *//*

    public interface Callback {
        */
/**
         * DetailFragmentCallback for when an item has been selected.
         *//*

        public void onItemSelected(String date);
    }

    public MatchListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this.getActivity(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_friends_add) {
            startActivity(new Intent(this.getActivity(), FriendsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mMatchListAdapter = new MatchListAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_match_list, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_match);
        mListView.setAdapter(mMatchListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mMatchListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback) getActivity())
                            .onItemSelected(cursor.getString(COL_MATCH_ID));
                }
                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MATCH_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMatch() {
        LolSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(MATCH_LOADER, null, this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

          // Sort order:  Ascending, by date.
        String sortOrder = MatchEntry.COLUMN_MATCH_CREATION + " ASC";

       // Uri matchUri = MatchEntry.buildMatchUri(Long.getLong(MatchEntry.COLUMN_MATCH_ID));
        Uri matchUri = MatchEntry.buildMatchUri(10833774);

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                matchUri,
                MATCH_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMatchListAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMatchListAdapter.swapCursor(null);
    }


}
*/
