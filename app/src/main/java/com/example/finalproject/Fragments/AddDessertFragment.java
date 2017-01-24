package com.example.finalproject.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.DessertActivity;
import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;


import java.io.File;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDessertFragment extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    ImageButton addImage;

    String selectedImagePath;

    Dessert newDessert;

    public AddDessertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_dessert, container, false);

        // Action bar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        // Camera button
        addImage = (ImageButton) view.findViewById(R.id.addNew);
        addImage.setFocusableInTouchMode(true);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
                //startActivity(new Intent(Intent.ACTION_VIEW));
            }
        });

        // Date range button
        TextView dateRange = (TextView) view.findViewById(R.id.addDates);
        dateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(AddDessertFragment.this, false);
                dateRangePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSave:
            {
                // Create the dessert object
                ImageView dessertImage = (ImageView) getView().findViewById(R.id.addImg);
                EditText dessertName = (EditText) getView().findViewById(R.id.addLable);
                EditText dessertDesc = (EditText) getView().findViewById(R.id.addDesc);
                EditText dessertCost = (EditText) getView().findViewById(R.id.addCost);
                TextView dessertDates = (TextView) getView().findViewById(R.id.addDates);

                newDessert = new Dessert(Model.getNextDessertId(),dessertName.getText().toString(),
                        dessertDesc.getText().toString(), " ", dessertCost.getText().toString(),
                        dessertDates.getText().toString());

                // Add the dessert
                Model.instance().addDessert(newDessert, new Model.SuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result){
                            // Display message
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.addedSuccessfuly),
                                    Toast.LENGTH_SHORT).show();

                            // Return to the list activity
                            Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                            startActivity(intent);
                        }
                        else{
                            // Display message
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorAdding),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        TextView dates = (TextView) getView().findViewById(R.id.addDates);
        dates.setText(startDay+"/"+startMonth+"/"+startYear+" - " + endDay + "/" + endMonth + "/" + endYear);
        dates.setTextColor(getResources().getColor(R.color.blackText));
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                        startActivityForResult(intent, CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getActivity().getApplicationContext(),
                        "Error while capturing image", Toast.LENGTH_LONG)
                        .show();
                return;

            }

            try {
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);


                addImage.setImageBitmap(bitmap);
                //storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                if (selectedImagePath != null) {
                    //txt_image_path.setText(selectedImagePath); TODO
                }
                ImageView dessertImage = (ImageView) getView().findViewById(R.id.addImg);
                dessertImage.setImageURI(selectedImage);
                //newDessert.setImageUrl(selectedImaged);
                //bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                //bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);

                //addImage.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


}