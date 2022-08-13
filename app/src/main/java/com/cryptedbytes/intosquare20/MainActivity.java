package com.cryptedbytes.intosquare20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {


    private static final byte PICK_IMAGE = 21;
    int selectedColor = Color.DKGRAY;
    Bitmap pickedBmp;

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

    public static Bitmap BlurImage(Bitmap bm, Context context, float radius) {
        //Assuming my original Bitmap is "bm"
        Bitmap outputBitmap = Bitmap.createBitmap(bm.getWidth(),
                bm.getHeight(), Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs,
                Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, bm);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(radius);/* www.  j  a v a2s  .  c  om*/
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        bm.recycle();
        rs.destroy();

        return outputBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.menu_item_1) {
           getImage();
        }
        return super.onOptionsItemSelected(item);
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
       // canvas.drawColor(Color.DKGRAY);
        canvas.drawColor(selectedColor);
       // Toast.makeText(this, "color read: " + selectedColor, Toast.LENGTH_SHORT).show();
        canvas.drawBitmap(bmp, (newWidth - width) / 2, (newHeight - height) / 2, null);
        bmp.recycle();

        if(isThumbnail){
            canvasBmp = ThumbnailUtils.extractThumbnail(canvasBmp, thumbnailImageView.getWidth() / 2, thumbnailImageView.getWidth() / 2);
            Log.d("squareify", "thumbnail size: w: "+ thumbnailImageView.getWidth() + " h: " + thumbnailImageView.getHeight());
        }
        return canvasBmp;
    }


    private void getImage() {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        File path = new File(directory, "img.png");

        if(path.exists()){
            path.delete();
        }
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
                    pickedBmp = null;
                    try {
                        pickedBmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));

                        saveBitmapToFile(pickedBmp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView previewView = findViewById(R.id.imageView);



//
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
//
//                        saveBitmapToFile(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    ImageView previewView = findViewById(R.id.imageView);
//





                    if (bitmap != null) {
                        previewView.setImageBitmap(squareify(readBitmapFromFile(),true,previewView));
                       // previewView.setImageBitmap(squareify(bitmap, true, previewView));
                    }
                    else{
                        throw new NullPointerException("bitmap is null");
                    }





                   /*
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView previewView = findViewById(R.id.imageView);




                    if (bitmap != null) {
                        previewView.setImageBitmap(squareify(bitmap, true, previewView));
                    }
                    else{
                        throw new NullPointerException("bitmap is null");
                    }

                    */
                    break;
                }

        }
    }

    public void bottomBarClicked(View view) {
        switch(view.getId()){
            case R.id.tile1_preview:
                Toast.makeText(this, "tile1_preview",   Toast.LENGTH_SHORT).show();
                ImageView previewView = findViewById(R.id.imageView);
                previewView.setImageBitmap(BlurImage(readBitmapFromFile(), this, 25));

                break;
            case R.id.tileCustomColorPick_preview:
                Toast.makeText(this, "tileCustomColorPick_preview", Toast.LENGTH_SHORT).show();
                break;
            default:
                ColorDrawable viewColor = (ColorDrawable) findViewById(view.getId()).getBackground();
                selectedColor = viewColor.getColor();
                //Toast.makeText(this, "color set: " + selectedColor, Toast.LENGTH_SHORT).show();

                 previewView = findViewById(R.id.imageView);
                previewView.setImageBitmap(squareify(readBitmapFromFile(),true,previewView));
                break;
        }
    }



    public void saveBitmapToFile(Bitmap bmp){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        File path = new File(directory, "img.png");

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();
        } catch (Exception e) {
            Log.e("saveBitmapToFile err:", e.getMessage(), e);
        }


       /* new Thread(new Runnable() {
            @Override
            public void run() {


                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("images", Context.MODE_PRIVATE);

                File path = new File(directory, "img.png");

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    outputStream.close();
                } catch (Exception e) {
                    Log.e("saveBitmapToFile err:", e.getMessage(), e);
                }


            }
        });*/


    }

    public Bitmap readBitmapFromFile(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File path = new File(directory, "img.png");
        Bitmap bmp = BitmapFactory.decodeFile(path.getAbsolutePath());
        return bmp;
    }
}