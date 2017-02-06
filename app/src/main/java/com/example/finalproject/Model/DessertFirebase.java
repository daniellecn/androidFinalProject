package com.example.finalproject.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Danielle Cohen on 22/01/2017.
 */

public class DessertFirebase {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static void addDessert(Dessert dessert, final Model.SuccessListener listener){
        DatabaseReference myRef = database.getReference("desserts").child(String.valueOf(dessert.getId()));
        myRef.setValue(dessert.toMap());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onResult(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false);
            }
        });
    }

    private void saveImage(Bitmap imageBmp, String name, final Model.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
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

    private void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;

        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onFail();
            }
        });
    }

//    public static void getAllDesserts(final Model.GetAllDessertsListener listener){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("desserts");
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Dessert> desserts = new LinkedList<Dessert>();
//                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
//                    Dessert dessert = stSnapshot.getValue(Dessert.class);
//                    desserts.add(dessert);
//                }
//                listener.onComplete(desserts);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                listener.onComplete(null);
//            }
//        });
//    }

    public static void getDessertById(double id, final Model.GetDessertListener listener){
        DatabaseReference myRef = database.getReference("desserts").child(String.valueOf(id));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Dessert dessert = snapshot.getValue(Dessert.class);
                //Log.d("TAG", dessert.getName() + " - " + dessert.getId());
                listener.onComplete(dessert);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getDessertsFromDate(double lastUpdateDate, final Model.GetAllDessertsListener listener) {
        final int[] maxKey = {-1};

        // Get all the desserts from the last update
        DatabaseReference myRef = database.getReference("desserts");
        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdateDate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Dessert> dessertList = new LinkedList<Dessert>();

                // Create the desserts list
                for (DataSnapshot dstSnapshot : dataSnapshot.getChildren()) {
                    Dessert dessert = dstSnapshot.getValue(Dessert.class);

                    if (maxKey[0] < dessert.getId()){
                        maxKey[0] = dessert.getId();
                    }

                    dessertList.add(dessert);
                }
                listener.onComplete(dessertList, maxKey[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancel();
            }
        });
    }

    public static void deleteDessert(int id, Model.SuccessListener listener){
        database.getReference("desserts").child(String.valueOf(id)).removeValue();
    }
}


