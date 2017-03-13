package com.google.firebase.quickstart.database.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.quickstart.database.R;

public class RecentScansFragment extends ScanListFragment {



    public RecentScansFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        displayFirebaseQuery(query);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        Query recentScansQuery = databaseReference.child("scans")
                .limitToLast(100);
        // [END recent_posts_query]

        return recentScansQuery;
    }
}
