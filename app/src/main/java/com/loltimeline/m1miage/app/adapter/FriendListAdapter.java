package com.loltimeline.m1miage.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.Utility;
import com.loltimeline.m1miage.app.data.DatabaseHandler;
import com.loltimeline.m1miage.app.data.Summoner;
import com.loltimeline.m1miage.app.volley.AppController;

import java.util.List;


/**
 * Created by Thomas on 04/04/2015.
 */
public class FriendListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Summoner> summoners;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FriendListAdapter(Activity activity, List<Summoner> summoners) {
        this.activity = activity;
        this.summoners = summoners;
    }

    @Override
    public int getCount() {
        return summoners.size();
    }

    @Override
    public Object getItem(int location) {
        return summoners.get(location);
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
            convertView = inflater.inflate(R.layout.friend, null);
      if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        TextView name = (TextView) convertView.findViewById(R.id.summoner_name);
        TextView id = (TextView) convertView.findViewById(R.id.summoner_id);
        TextView lvl = (TextView) convertView.findViewById(R.id.summoner_lvl);
        NetworkImageView icon = (NetworkImageView) convertView
                .findViewById(R.id.summoner_icon);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);
        final Summoner summoner = summoners.get(position);

        // On rempli les champs text
        id.setText(String.valueOf(summoner.getSummonerId()));
        name.setText(summoner.getName());
        lvl.setText(String.valueOf(summoner.getLvl()));

        // On put l'icone summoner
       icon.setImageUrl(Utility.getSummonerIconUrlbyId(summoner.getIcon()), imageLoader);


        // On active le bouton delete friend
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(activity);
                db.deleteAllMatchSummoner(summoner);
                db.deleteSummoner(summoner);

                activity.recreate();
            }
        });


        return convertView;
    }
}