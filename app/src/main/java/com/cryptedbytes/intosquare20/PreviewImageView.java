package com.cryptedbytes.intosquare20;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PreviewImageView extends androidx.appcompat.widget.AppCompatImageView {

    public PreviewImageView(Context context) {
        super(context);
    }

    public PreviewImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
try{
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    int newWidth;
    int newHeight;

    if(width > height){
        newHeight = width;
        newWidth = width;
        setMeasuredDimension(newWidth, newHeight);
    }
    else {
        newHeight = height;
        newWidth = height;
        setMeasuredDimension(newWidth, newHeight);
    }

}
catch (Exception e){

}


    }
}
