package com.cryptedbytes.intosquare20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {


    private static final byte PICK_IMAGE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getImage();

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getImage();
            }
        });
    }


    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private Bitmap squareify(Bitmap bmp, boolean isThumbnail, @Nullable ImageView thumbnailImageView) {

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int newWidth;
        int newHeight;

        if(width > height){
            newHeight = width;
            newWidth = width;
        }
        else {
            newHeight = height;
            newWidth = height;
        }

        Log.d("squareify", "width: " + width + " height: " + height + " newWidth: " + newWidth + " newHeight: " + newHeight);

        //Bitmap canvasBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Bitmap canvasBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(canvasBmp);
        canvas.drawColor(Color.DKGRAY);
        canvas.drawBitmap(bmp, (newWidth - width) / 2, (newHeight - height) / 2, null);
        bmp.recycle();

        if(isThumbnail){
            canvasBmp = ThumbnailUtils.extractThumbnail(canvasBmp, thumbnailImageView.getWidth() / 2, thumbnailImageView.getWidth() / 2);
            Log.d("squareify", "thumbnail size: w: "+ thumbnailImageView.getWidth() + " h: " + thumbnailImageView.getHeight());
        }
        return canvasBmp;
    }


    private void getImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
        /*
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 21);
        */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_IMAGE):
                if (resultCode == RESULT_OK) {


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView previewView = findViewById(R.id.imageView);

                    previewView.setImageBitmap(bitmap);
                    if (bitmap != null) {
                        previewView.setImageBitmap(squareify(bitmap, true, previewView));
                    }
                    else{
                        throw new NullPointerException("bitmap is null");
                    }
                    break;
                }
        }
    }

    public void bottomBarClicked(View view) {
    }
}