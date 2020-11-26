package com.example.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<ListNotifications> {
        private ArrayList<ListNotifications> notifications;
        private Context context;

        public NotificationAdapter(Context context, ArrayList<ListNotifications> notifications){
            super(context,0, notifications);
            this.notifications = notifications;
            this.context = context;
        }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_notifications, parent,false);
        }


        TextView title = view.findViewById(R.id.TitleView);
        TextView owner = view.findViewById(R.id.owner);
        TextView requester = view.findViewById(R.id.requester);
        TextView status = view.findViewById(R.id.StatusView);
        TextView date = view.findViewById(R.id.date);


        final ListNotifications notification = notifications.get(position);

        //TODO: need to update TextViews with names rather than IDs. I suppose a "fine" solution would be to get the fields right here.

        return view;

    }
}
