package com.google.firebase.quickstart.database.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.quickstart.database.MainActivity;
import com.google.firebase.quickstart.database.NewPhotoActivity;
import com.google.firebase.quickstart.database.PhotoReviewActivity;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Photo;
import com.google.firebase.quickstart.database.viewholder.PhotoViewHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoFragment extends Fragment {

    private static final String TAG = "ScanListFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Photo, PhotoViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FloatingActionButton button;
    String mCurrentPhotoPath;
    public Query photoQuery;

    public PhotoFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_photos, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.photos_list);
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
        photoQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Photo, PhotoViewHolder>(Photo.class, R.layout.photo_layout, PhotoViewHolder.class, photoQuery) {
            @Override
            protected void populateViewHolder(PhotoViewHolder viewHolder, Photo model, int position) {
                final DatabaseReference photoRef = getRef(position);

                // Set click listener for the whole photo view
                final String photoKey = photoRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PhotoReviewActivity.class);
                        intent.putExtra(PhotoReviewActivity.EXTRA_PHOTO_KEY, photoKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPhoto(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }


    private void dispatchTakePictureIntent() {
        //camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,6);

        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            //Bundle extras = data.getExtras();
            Bundle extras = new Bundle();
            extras.putString("path", mCurrentPhotoPath);
            Intent intent = new Intent(this.getContext(), NewPhotoActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        button = (FloatingActionButton) getView().findViewById(R.id.fab_take_photo);
        button.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        button.setVisibility(View.INVISIBLE);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        Query myTopPostsQuery = databaseReference.child("photos")
                .limitToLast(100);
        // [END recent_posts_query]

        return myTopPostsQuery;
    }

}
