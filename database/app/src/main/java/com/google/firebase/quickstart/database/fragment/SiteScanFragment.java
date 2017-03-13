package com.google.firebase.quickstart.database.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Location;
import com.google.firebase.quickstart.database.models.Scan;
import com.google.firebase.quickstart.database.viewholder.PostViewHolder;

import java.util.ArrayList;

public class SiteScanFragment extends ScanListFragment {

    Button button;
    public static final String PREFS_NAME = "ChosenGates";
    public SharedPreferences mSharedPreferences;
    ArrayList<String> gates;
    ArrayList<Integer> selectedIndices;
    ArrayList<String> locFilter;

    public SiteScanFragment() {}

    public RecyclerView mScanRecycler;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gates = new ArrayList<>(5);
        locFilter = new ArrayList<>();


        //add button for filtering
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        Query locQuery = mDatabase.child("locations").orderByChild("name");
        locQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Location location = postSnapshot.getValue(Location.class);
                    gates.add(location.name);
                    if(mSharedPreferences.getBoolean(location.name,true)) {
                        locFilter.add(location.name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        button = (Button) getView().findViewById(R.id.actionButton);
        button.setText("Filter");
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        displayFirebaseQuery(query);
    }

    private void showFilterDialog() {
        selectedIndices = new ArrayList<>();
        mSharedPreferences = this.getContext().getSharedPreferences(PREFS_NAME, 0);
        boolean[] chosen = new boolean[gates.size()];
        for (int j = 0; j < gates.size(); j++){
            chosen[j] = mSharedPreferences.getBoolean(gates.get(j), true);
            if(mSharedPreferences.getBoolean(gates.get(j), true)) {
                selectedIndices.add(j);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Choose gates");
        String filterArray[] = new String[gates.size()];
        builder.setMultiChoiceItems(gates.toArray(filterArray), chosen, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked && !selectedIndices.contains(which)) {
                    selectedIndices.add(which);
                    final SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(gates.get(which), true);
                    editor.commit();

                } else if (selectedIndices.contains(which)) {
                    selectedIndices.remove(Integer.valueOf(which));
                    final SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(gates.get(which), false);
                    editor.commit();
                }

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                locFilter = new ArrayList<String>();
                for (Integer k : selectedIndices){
                    locFilter.add(gates.get(k));
                }

                displayFirebaseQuery(query);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //remove AlertDialog
            }
        });
        builder.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        button.setVisibility(View.INVISIBLE);
    }


    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query siteScanQuery = databaseReference.child("scans").limitToLast(100);
        return siteScanQuery;
    }

    @Override
    public void displayFirebaseQuery(Query photoQuery){
        mAdapter = new FirebaseRecyclerAdapter<Scan, PostViewHolder>(Scan.class, R.layout.item_post,
                PostViewHolder.class, photoQuery) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Scan scan, int position) {
                if (locFilter.contains(scan.getLocation())) {
                    viewHolder.bindToPost(scan, mDatabase, workerHashMap);
                }
            }
        };
        mRecycler.setAdapter(mAdapter);
    }


}
