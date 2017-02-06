package com.example.finalproject.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Danielle Cohen on 27/01/2017.
 */

public class ImageLocal {
    public static void saveLocalImage(Bitmap imageBitmap, String imageFileName) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Bakery");

            if (!dir.exists()) {
                dir.mkdir();
            }

            File imageFile = new File(dir, imageFileName + ".jpg");
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadLocalImage(String imageFileName, int size) {
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Bakery");
            File imageFile = new File(dir, imageFileName + ".jpg");
            InputStream inputStream = new FileInputStream(imageFile);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = size;

            bitmap = BitmapFactory.decodeStream(inputStream, null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void deleteLocalImage (String imageFileName){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Bakery/" + imageFileName + ".jpg");
        if(file.exists())
        {
            file.delete();
        }
    }
}
