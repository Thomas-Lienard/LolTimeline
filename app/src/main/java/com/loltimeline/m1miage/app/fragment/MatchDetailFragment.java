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
package com.loltimeline.m1miage.app.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loltimeline.m1miage.app.MatchDetailActivity;
import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.data.MatchContract.MatchEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MatchDetailFragment.class.getSimpleName();

    private static final String LOCATION_KEY = "location";
    private String mLocation;
    private String mForecast;
    private String mDateStr;

    private static final int DETAIL_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            MatchEntry.TABLE_NAME + "." + MatchEntry._ID,
            MatchEntry.COLUMN_CHAMPION_ID,
            MatchEntry.COLUMN_CHAMP_LEVEL,
            MatchEntry.COLUMN_KILLS,
            MatchEntry.COLUMN_DEATHS,
            MatchEntry.COLUMN_ASSISTS,
            MatchEntry.COLUMN_SPELL1_ID,
            MatchEntry.COLUMN_SPELL2_ID,
            MatchEntry.COLUMN_MINIONS_KILLED,
            MatchEntry.COLUMN_GOLD_EARNED,
            MatchEntry.COLUMN_WARDS_PLACED,
            MatchEntry.COLUMN_TOTAL_DAMAGE_DEALT,
            MatchEntry.COLUMN_TOTAL_DAMAGE_TAKEN,
            MatchEntry.COLUMN_ITEM0_ID,
            MatchEntry.COLUMN_ITEM1_ID,
            MatchEntry.COLUMN_ITEM2_ID,
            MatchEntry.COLUMN_ITEM3_ID,
            MatchEntry.COLUMN_ITEM4_ID,
            MatchEntry.COLUMN_ITEM5_ID,
            MatchEntry.COLUMN_ITEM6_ID,
    };

    private ImageView spell1;
    private ImageView spell2;
    private ImageView item0;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;

    private TextView level;
    private TextView kda;
    private TextView farm;
    private TextView gold;
    private TextView wards;
    private TextView dealt;
    private TextView taken;


    public MatchDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(LOCATION_KEY, mLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mDateStr = arguments.getString(MatchDetailActivity.DATE_KEY);
        }

        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_match_detail, container, false);

        spell1 = (ImageView) rootView.findViewById(R.id.spell1_icon);
        spell2 = (ImageView) rootView.findViewById(R.id.spell2_icon);
        item0 = (ImageView) rootView.findViewById(R.id.item0_icon);
        item1 = (ImageView) rootView.findViewById(R.id.item1_icon);
        item2 = (ImageView) rootView.findViewById(R.id.item2_icon);
        item3 = (ImageView) rootView.findViewById(R.id.item3_icon);
        item4 = (ImageView) rootView.findViewById(R.id.item4_icon);
        item5 = (ImageView) rootView.findViewById(R.id.item5_icon);
        item6 = (ImageView) rootView.findViewById(R.id.item6_icon);

        level = (TextView) rootView.findViewById(R.id.text_lvl);
        kda = (TextView) rootView.findViewById(R.id.text_kda);
        farm = (TextView) rootView.findViewById(R.id.text_farm);
        gold = (TextView) rootView.findViewById(R.id.text_gold);
        wards = (TextView) rootView.findViewById(R.id.text_wards);
        dealt = (TextView) rootView.findViewById(R.id.text_damage_dealt);
        taken = (TextView) rootView.findViewById(R.id.text_damage_taken);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(MatchDetailActivity.DATE_KEY)) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = MatchEntry.COLUMN_MATCH_CREATION + " ASC";
        Uri matchUri = MatchEntry.buildMatchUri(12233);

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                matchUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            int champion_lvl = data.getInt(data.getColumnIndex(MatchEntry.COLUMN_CHAMP_LEVEL));
            level.setText(String.valueOf(champion_lvl));

           /* // Read weather condition ID from cursor
            int weatherId = data.getInt(data.getColumnIndex(MatchEntry.COLUMN_WEATHER_ID));
            // Use weather art image
            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
            item0.setImageURI();
            // Read date from cursor and update views for day of week and date
            String date = data.getString(data.getColumnIndex(MatchEntry.COLUMN_DATETEXT));
            String friendlyDateText = Utility.getDayName(getActivity(), date);
            String dateText = Utility.getFormattedMonthDay(getActivity(), date);
            mFriendlyDateView.setText(friendlyDateText);
            mDateView.setText(dateText);

            // Read description from cursor and update view
            String description = data.getString(data.getColumnIndex(
                    MatchEntry.COLUMN_SHORT_DESC));
            mDescriptionView.setText(description);

            // For accessibility, add a content description to the icon field
            mIconView.setContentDescription(description);

            // Read high temperature from cursor and update view
            boolean isMetric = Utility.isMetric(getActivity());

            double high = data.getDouble(data.getColumnIndex(MatchEntry.COLUMN_MAX_TEMP));
            String highString = Utility.formatTemperature(getActivity(), high);
            mHighTempView.setText(highString);

            // Read low temperature from cursor and update view
            double low = data.getDouble(data.getColumnIndex(MatchEntry.COLUMN_MIN_TEMP));
            String lowString = Utility.formatTemperature(getActivity(), low);
            mLowTempView.setText(lowString);

            // Read humidity from cursor and update view
            float humidity = data.getFloat(data.getColumnIndex(MatchEntry.COLUMN_HUMIDITY));
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            // Read wind speed and direction from cursor and update view
            float windSpeedStr = data.getFloat(data.getColumnIndex(MatchEntry.COLUMN_WIND_SPEED));
            float windDirStr = data.getFloat(data.getColumnIndex(MatchEntry.COLUMN_DEGREES));
            mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));

            // Read pressure from cursor and update view
            float pressure = data.getFloat(data.getColumnIndex(MatchEntry.COLUMN_PRESSURE));
            mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

            // We still need this for the share intent
            mForecast = String.format("%s - %s - %s/%s", dateText, description, high, low);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }*/
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
