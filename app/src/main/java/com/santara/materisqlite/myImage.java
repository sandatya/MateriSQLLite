package com.santara.materisqlite;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class myImage {
    protected Bitmap automatic_rotateImg(Bitmap img,String pathImg){
        Bitmap rotatedBitmap=null;
        try{
            ExifInterface exifInterface = new ExifInterface(pathImg);
            int orientation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap=rotateBitmap(img,90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap=rotateBitmap(img,180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap=rotateBitmap(img,270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL: default:
                    rotatedBitmap=img;
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    protected Bitmap rotateBitmap(Bitmap imgSource, float angle) {
        Matrix matrix=new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(imgSource,0,0,imgSource.getWidth(),imgSource.getHeight(),matrix,true);
    }
}
