package com.google.firebase.quickstart.database.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Photo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by john on 12/19/16.
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {


    public TextView contractorView;
    public TextView dateTimeView;
    public ImageView thumbnailView;

    public PhotoViewHolder(View itemView) {
        super(itemView);

        contractorView = (TextView) itemView.findViewById(R.id.Contractor);
        dateTimeView = (TextView) itemView.findViewById(R.id.dateTime);
        thumbnailView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    public void bindToPhoto (Photo photo) {
        Bitmap b = decodeDatabaseImage(photo.getImgBase64());
        thumbnailView.setImageBitmap(b);
        //float aspectRatio = b.getWidth() / (float) b.getHeight();
        //int width = 75;
        //int height = Math.round(width / aspectRatio);

        //thumbnailView.setImageBitmap(Bitmap.createScaledBitmap(b, width, height, false));
        //b.recycle();

        contractorView.setText(photo.getContractor());
        if (photo.getScantime() != null) {
            dateTimeView.setText(photo.getScantime().toString());
        }
    }


    public static Bitmap decodeDatabaseImage(String encode) {
        byte[] byteArrayDecoded = android.util.Base64.decode(encode, android.util.Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(byteArrayDecoded);
        return BitmapFactory.decodeStream(inputStream);
    }


}
