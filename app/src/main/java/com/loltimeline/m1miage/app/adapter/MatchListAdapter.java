package com.loltimeline.m1miage.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.Utility;
import com.loltimeline.m1miage.app.data.Match;
import com.loltimeline.m1miage.app.volley.AppController;

import java.util.List;


public class MatchListAdapter extends BaseAdapter {


    private Activity activity;
    private LayoutInflater inflater;
    private List<Match> matchList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MatchListAdapter(Activity activity, List<Match> matchList) {
        this.activity = activity;
        this.matchList = matchList;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int location) {
        return matchList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_match, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView champion_icon = (NetworkImageView) convertView
                .findViewById(R.id.match_champ_icon);

        TextView kda = (TextView) convertView.findViewById(R.id.text_kda);
        TextView winner = (TextView) convertView.findViewById(R.id.text_winner);
        TextView map = (TextView) convertView.findViewById(R.id.text_map);
        TextView queue = (TextView) convertView.findViewById(R.id.text_queue);
        TextView date = (TextView) convertView.findViewById(R.id.text_date);
        TextView duration = (TextView) convertView.findViewById(R.id.text_duration);
        TextView name = (TextView) convertView.findViewById(R.id.text_name);

        final Match match = matchList.get(position);

        // On rempli les champs text
        champion_icon.setImageUrl(Utility.getChampionImageUriById(match.getChampion_id()), imageLoader);
        kda.setText(match.getKILLS() + "/" + match.getDEATHS() + "/" + match.getASSISTS());
        Log.d("kda", match.getKILLS() + "/" + match.getDEATHS() + "/" + match.getASSISTS());
        winner.setText(match.getWinner());
        Log.d("winner", match.getWinner());
        map.setText(Utility.getMapNameById(match.getMap_id()));
        queue.setText(match.getQueue_type());
        date.setText(String.valueOf(match.getMatch_creation()));
        duration.setText(String.valueOf(match.getMatch_duration()));
        name.setText(match.getSummoner_name());





        return convertView;
    }

}