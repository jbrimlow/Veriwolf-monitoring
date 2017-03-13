package com.google.firebase.quickstart.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.models.Contractor;
import com.google.firebase.quickstart.database.models.Photo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by john on 12/22/16.
 */
public class NewPhotoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ImageView photo;
    private EditText description;
    private Button cancel;
    private Button upload;
    private Spinner contractor;
    Bitmap imageBitmap;
    List<String> list;

    String contractorSelection;
    String localPath;
    String firebasePath;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://veriwolf-test.appspot.com");

        photo = (ImageView) findViewById(R.id.imageView2);
        description = (EditText) findViewById(R.id.descView);

        addItemsToSpinner();


        upload = (Button) findViewById(R.id.uploadButton);
        upload.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(this);


        //display photo
        Bundle extras = getIntent().getExtras();
        localPath = extras.getString("path");
        imageBitmap = BitmapFactory.decodeFile(localPath);
        photo.setImageBitmap(imageBitmap);

    }

    private void addItemsToSpinner() {
        list = new ArrayList<String>();

        Query contractorQuery = mDatabase.child("contractors").orderByChild("name");
        contractorQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot conSnapshot : dataSnapshot.getChildren()){
                    Contractor contractor = conSnapshot.getValue(Contractor.class);
                    if(contractor.getName() != null) {
                        list.add(contractor.getName());
                    }
                }

                contractor = (Spinner) findViewById(R.id.contractorSpinner);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(NewPhotoActivity.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                contractor.setAdapter(dataAdapter);
                contractor.setOnItemSelectedListener(NewPhotoActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.uploadButton) {
            postPhoto();
            finish();
        }
        if (i == R.id.cancelButton) {
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        contractorSelection = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    private void postPhoto() {
        String comment = description.getText().toString();

        storeImage();

        Photo toSave = new Photo();

        toSave.setContractor(contractorSelection);
        toSave.setDescription(comment);
        toSave.setFilepath(firebasePath);

        //TODO: only save thumbnail here
        toSave.setImgBase64(scalePhoto());
        toSave.setScantime(Calendar.getInstance().getTime());

        mDatabase.child("photos").push().setValue(toSave);
    }

    private String scalePhoto() {
        float aspectRatio = imageBitmap.getWidth() / (float) imageBitmap.getHeight();
        int width = 75;
        int height = Math.round(width / aspectRatio);

        Bitmap thumbnail = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static String encodeDatabaseImage(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap bigBitmap = Bitmap.createScaledBitmap(bitmap, (int)(width *.5), (int)(height * .5), false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bigBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public void storeImage() {
        Uri uri = Uri.parse(localPath);
        firebasePath = uri.getLastPathSegment();

        UploadTask uploadTask = storageReference.child(firebasePath).putFile(uri);

    }
}
