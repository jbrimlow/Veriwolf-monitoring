package com.google.firebase.quickstart.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.models.Photo;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by john on 12/22/16.
 */
public class PhotoReviewActivity extends BaseActivity {

    private DatabaseReference mPhotoReference;
    private ValueEventListener mPhotoListener;
    FirebaseStorage storage;
    StorageReference storageReference;

    public static final String EXTRA_PHOTO_KEY = "photo_key";

    private ImageView photoView;
    private TextView description;
    private String mPhotoKey;
    private String firebasePath;
    private Button done;
    private TextView contractor;
    private TextView date;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_review);

        mPhotoKey = getIntent().getStringExtra(EXTRA_PHOTO_KEY);
        if (mPhotoKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mPhotoReference = FirebaseDatabase.getInstance().getReference().child("photos").child(mPhotoKey);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://veriwolf-test.appspot.com");

        photoView = (ImageView) findViewById(R.id.imageView2);
        description = (TextView) findViewById(R.id.descView);
        contractor = (TextView) findViewById(R.id.contractorView);
        date = (TextView) findViewById(R.id.dateView);

        done = (Button) findViewById(R.id.doneButton);

    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener photoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);

                //photoView.setImageBitmap(decodeDatabaseImage(photo.getImgBase64()));
                firebasePath = photo.getFilepath();
                try {
                    final File localFile = File.createTempFile("images", "png");
                    storageReference.child(firebasePath).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            photoView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                description.setText(photo.getDescription());
                contractor.setText("Contractor: " + photo.getContractor());
                if (photo.getScantime() != null) {
                    date.setText("Taken on: " + photo.getScantime().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPhotoReference.addValueEventListener(photoListener);

        // Keep copy of photo listener so we can remove it when app stops
        mPhotoListener = photoListener;
    }

    @Override
    public void onResume(){
        super.onResume();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        // clean up photo listener
        if (mPhotoListener != null) {
            mPhotoReference.removeEventListener(mPhotoListener);
        }
    }

    public static Bitmap decodeDatabaseImage(String encode) {
        byte[] byteArrayDecoded = android.util.Base64.decode(encode, android.util.Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(byteArrayDecoded);
        return BitmapFactory.decodeStream(inputStream);
    }


}
