package com.google.firebase.quickstart.database.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Post;
import com.google.firebase.quickstart.database.models.Scan;
import com.google.firebase.quickstart.database.models.Worker;

import java.util.Date;
import java.util.HashMap;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView bodyView;
    String id;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }


    public void bindToPost(Scan scan, DatabaseReference mDatabaseReference, final HashMap hashMap) {
        id = scan.workerid;
        id = id.replaceFirst("^0+(?!$)", "");
        if (hashMap.containsKey(id)) {
            if (hashMap.get(id) == null) {
                titleView.setText(id);
            } else {
                titleView.setText(hashMap.get(id).toString());
            }
        } else {
            Query workerQuery =mDatabaseReference.child("workers").orderByChild("gateScanId").startAt(id).endAt(id);
            workerQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = "";
                    Worker worker = dataSnapshot.child(id).getValue(Worker.class);
                    if (worker != null) {
                        name = worker.getFirstname() + " " + worker.getLastname();
                        hashMap.put(id, name);
                        titleView.setText(name);
                    }
                    if (name.equals("")) {
                        hashMap.put(id, null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            titleView.setText(id);
        }

        Date scanned = new Date(scan.scantime);
        authorView.setText(scan.location + ", " + scanned.toString());
    }
}
