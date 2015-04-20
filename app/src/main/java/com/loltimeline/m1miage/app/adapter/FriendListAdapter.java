package com.loltimeline.m1miage.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loltimeline.m1miage.app.FriendsActivity;
import com.loltimeline.m1miage.app.R;
import com.loltimeline.m1miage.app.volley.AppController;

public class FriendListAdapter extends CursorAdapter {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final NetworkImageView icon;
        public final TextView name;
        public final TextView id;
        public final TextView lvl;
        public final ImageButton delete;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.summoner_name);
            id = (TextView) view.findViewById(R.id.summoner_id);
            lvl = (TextView) view.findViewById(R.id.summoner_lvl);
            icon = (NetworkImageView) view
                    .findViewById(R.id.summoner_icon);
            delete = (ImageButton) view.findViewById(R.id.delete);
        }
    }

    public FriendListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);


        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        String name = cursor.getString(FriendsActivity.COL_NAME);
        viewHolder.name.setText(name);

        String id = cursor.getString(FriendsActivity.COL_SUMMONER_ID);
        viewHolder.id.setText(id);

        String lvl = cursor.getString(FriendsActivity.COL_LEVEL);
        viewHolder.lvl.setText(lvl);

        String icon = cursor.getString(FriendsActivity.COL_ICON);
        viewHolder.icon.setDefaultImageResId(R.drawable.ic_action_android);
        viewHolder.icon.setImageUrl(icon, imageLoader);

        // On active le bouton delete friend
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }


}