package com.example.finalproject.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.Model.AppContext;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;


import java.io.File;
import java.io.IOException;
import java.util.Date;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDessertFragment extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    protected static final int PICK_CAMERA_IMAGE = 0;

    protected static final int ADD_MODE = 0;
    protected static final int EDIT_MODE = 1;

    private int mode;
    private Dessert newDessert;
    private String selectedImagePath;
    private Bitmap selectedImageBitmap;

    public AddDessertFragment() {
        // Required empty public constructor
    }

    public Dessert getNewDessert() {
        return newDessert;
    }

    public void setNewDessert(Dessert newDessert) {
        this.newDessert = newDessert;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_dessert, container, false);

        // Action bar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        // Camera button
        ImageButton addImage = (ImageButton) view.findViewById(R.id.addNew);
        addImage.setFocusableInTouchMode(true);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
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

        // Edit mode
        if (getNewDessert() != null) {
            // Update the screen
            ((EditText) view.findViewById(R.id.addLable)).setText(getNewDessert().getName());
            ((EditText) view.findViewById(R.id.addDesc)).setText(getNewDessert().getDescription());
            ((EditText) view.findViewById(R.id.addCost)).setText((getNewDessert().getCost()));
            ((TextView) view.findViewById(R.id.addDates)).setText(getNewDessert().getDatesAvailable());

            Model.instance().getDessertImage(getNewDessert(), 8, new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    ((ImageView) view.findViewById(R.id.addImg)).setImageBitmap(Bitmap.createScaledBitmap(image, image.getWidth(),180, false));
                }

                @Override
                public void onFail() {
                    // TODO: default image
                }
            });

            mode = EDIT_MODE;
        }
        // Add mode
        else {
            newDessert = new Dessert();
            newDessert.setId(Model.getCurrentKey());

            mode = ADD_MODE;
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.add_menu, menu);

        // If add mode - remove delete button
        if (mode == ADD_MODE){
            menu.removeItem(R.id.menuDel);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSave: {
                // Update the dessert object
                getNewDessert().setName(((EditText) getView().findViewById(R.id.addLable)).getText().toString());
                getNewDessert().setDescription(((EditText) getView().findViewById(R.id.addDesc)).getText().toString());
                getNewDessert().setCost(((EditText) getView().findViewById(R.id.addCost)).getText().toString());
                getNewDessert().setDatesAvailable(((TextView) getView().findViewById(R.id.addDates)).getText().toString());

                // Add the dessert
                Model.instance().addDessert(getNewDessert(), selectedImageBitmap, new Model.SuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            // Display message
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.updateSuccessfuly),
                                    Toast.LENGTH_SHORT).show();

                            // Return to the list activity
                            Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                            startActivity(intent);
                        } else {
                            // Display message
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorOccure),
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        TextView dates = (TextView) getView().findViewById(R.id.addDates);
        dates.setText(startDay + "/" + startMonth + "/" + startYear + " - " + endDay + "/" + endMonth + "/" + endYear);
        dates.setTextColor(getResources().getColor(R.color.blackText));
    }

    private void startDialog() {
        AlertDialog.Builder optionsAlert = new AlertDialog.Builder(
                getActivity());
        optionsAlert.setTitle(getString(R.string.uploadOptionsTitle));
        optionsAlert.setMessage(getString(R.string.uploadOptionsQuestion));

        /** Gallery option**/
        optionsAlert.setPositiveButton(getString(R.string.uploadGalleryOption),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);

                    }
                });

        /** Camera option **/
        AlertDialog.Builder camera = optionsAlert.setNegativeButton(getString(R.string.uploadCameraOption),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        /** Check permissions ***/
                        boolean hasPermission = (ContextCompat.checkSelfPermission(AppContext.getAppContext(),
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

                        if (!hasPermission) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_REQUEST);
                        } else {

                            String name = String.valueOf(getNewDessert().getId() + 1);
                            File destination = new File(Environment
                                    .getExternalStorageDirectory(), name + getString(R.string.jpg));

                            Uri photoURI = FileProvider.getUriForFile(AppContext.getAppContext(),
                                    AppContext.getAppContext().getApplicationContext().getPackageName() + getString(R.string.provider),
                                    destination);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(intent, PICK_CAMERA_IMAGE);

                        }
                    }
                });
        optionsAlert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (intent != null) {
                /** Get the selected image path **/
                Uri selectedImage = intent.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                selectedImagePath = cursor.getString(columnIndex);
                cursor.close();
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            /** Get the new image path **/
            File file = new File(Environment.getExternalStorageDirectory().toString());

            for (File temp : file.listFiles()) {
                if (temp.getName().equals(String.valueOf(getNewDessert().getId() + ".jpg"))) {
                    file = temp;
                    break;
                }
            }

            // If the new image was not found
            if (!file.exists()) {
                Toast.makeText(getActivity().getApplicationContext(),
                        R.string.errorImage, Toast.LENGTH_LONG).show();
                return;
            } else {
                selectedImagePath = file.getAbsolutePath();
            }
        }

        // Creating the bitmap and make the changes
        if (selectedImagePath != null) {
            /** Get selected image as bitmap **/
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            selectedImageBitmap = BitmapFactory.decodeFile(selectedImagePath);

            /** Rotating image **/
            int rotate = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(selectedImagePath);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

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


                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                selectedImageBitmap = Bitmap.createBitmap(selectedImageBitmap, 0, 0, selectedImageBitmap.getWidth(),
                        selectedImageBitmap.getHeight(), matrix, true);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),
                        R.string.errorImage, Toast.LENGTH_LONG).show();
                return;
            }

            /** Update the screen **/
            ImageView dessertImage = (ImageView) getView().findViewById(R.id.addImg);
            dessertImage.setImageBitmap(selectedImageBitmap);
        }
    }


}