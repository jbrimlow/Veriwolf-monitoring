package com.google.firebase.quickstart.database.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.PostDetailActivity;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Post;
import com.google.firebase.quickstart.database.models.Scan;
import com.google.firebase.quickstart.database.models.Worker;
import com.google.firebase.quickstart.database.viewholder.PostViewHolder;

import java.util.HashMap;

public abstract class ScanListFragment extends Fragment {

    private static final String TAG = "ScanListFragment";

    public DatabaseReference mDatabase;

    public FirebaseRecyclerAdapter<Scan, PostViewHolder> mAdapter;
    public RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public Query query;

    HashMap<String, String> workerHashMap;

    public ScanListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list); //see fragment_all_posts.xml
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        query = getQuery(mDatabase);

        //set up for substituting worker names for id numbers
        workerHashMap = new HashMap<>();
        Query workerQuery = mDatabase.child("workers").orderByChild("gateScanId");
        workerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Worker worker = postSnapshot.getValue(Worker.class);
                    String name = worker.getFirstname() + " " + worker.getLastname();
                    workerHashMap.put(worker.getGateScanId(), name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public void displayFirebaseQuery(Query photoQuery){
        mAdapter = new FirebaseRecyclerAdapter<Scan, PostViewHolder>(Scan.class, R.layout.item_post,
                PostViewHolder.class, photoQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Scan scan, final int position) {
                viewHolder.bindToPost(scan, mDatabase, workerHashMap);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
