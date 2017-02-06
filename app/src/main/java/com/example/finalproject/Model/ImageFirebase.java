package com.example.finalproject.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by Danielle Cohen on 27/01/2017.
 */

public class ImageFirebase {
    private static String firebaseUrl = "gs://androidfinalproject-a3be7.appspot.com/";

    public static void saveRemoteImage(Bitmap imageBmp, String name, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReferenceFromUrl(firebaseUrl).child("images").child(name + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imagesRef.putBytes(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }

    public static void loadRemoteImage(String url, final Model.GetImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;

        httpsReference.getBytes(10 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onFail();
            }
        });
    }

    public static void deleteRemoteImage(String url, Model.SuccessListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        httpsReference.delete();
        listener.onResult(true);
    }
}
