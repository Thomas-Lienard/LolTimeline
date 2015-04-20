package com.loltimeline.m1miage.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.fragment.MatchListFragment;
import com.loltimeline.m1miage.app.volley.AppController;

/**
 * {@link MatchListAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class MatchListAdapter extends CursorAdapter {
    ImageLoader imageLoader = null;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView icon;
        public final TextView name;
        public final TextView winner;
        public final TextView map;
        public final TextView queue;
        public final TextView kda;
        public final TextView date;
        public final TextView duration;


        public ViewHolder(View view) {

            icon = (ImageView) view.findViewById(R.id.match_champ_icon);
            name = (TextView) view.findViewById(R.id.text_name);
            winner = (TextView) view.findViewById(R.id.text_winner);
            map = (TextView) view.findViewById(R.id.text_map);
            queue = (TextView) view.findViewById(R.id.text_queue);
            kda = (TextView) view.findViewById(R.id.text_kda);
            date = (TextView) view.findViewById(R.id.text_date);
            duration = (TextView) view.findViewById(R.id.text_duration);
        }
    }

    public MatchListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View view = LayoutInflater.from(context).inflate(R.layout.fragment_match_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        String name = cursor.getString(MatchListFragment.COL_SUMMONER_NAME);
        viewHolder.name.setText(name);

        String winner = cursor.getString(MatchListFragment.COL_WINNER);
        viewHolder.winner.setText(winner);

        String map = cursor.getString(MatchListFragment.COL_MAP_ID);
        viewHolder.map.setText(map);

        String kda = cursor.getString(MatchListFragment.COL_KILLS).concat("/").concat(cursor.getString(MatchListFragment.COL_DEATHS)).concat("/").concat(cursor.getString(MatchListFragment.COL_ASSISTS));
        viewHolder.kda.setText(kda);

        String queue = cursor.getString(MatchListFragment.COL_QUEUE_TYPE);
        viewHolder.queue.setText(queue);

        String date = cursor.getString(MatchListFragment.COL_MATCH_CREATION);
        viewHolder.date.setText(date);

        String duration = cursor.getString(MatchListFragment.COL_MATCH_DURATION);
        viewHolder.duration.setText(duration);
    }


}