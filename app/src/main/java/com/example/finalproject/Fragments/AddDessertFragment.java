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
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.Model.AppContext;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

import java.io.IOException;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDessertFragment extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;

    protected static final int ADD_MODE = 0;
    protected static final int EDIT_MODE = 1;

    private int mode;
    private Dessert newDessert;
    private String selectedImagePath;
    private Bitmap oldImageBitmap;
    private Bitmap selectedImageBitmap;

    ProgressBar progressBar;

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

        // Set the progress bar
        progressBar = (ProgressBar) view.findViewById(R.id.addProgressBar);
        progressBar.setVisibility(view.GONE);

        // Camera button
        ImageButton addImage = (ImageButton) view.findViewById(R.id.addNew);
        addImage.setFocusableInTouchMode(true);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageDialog();
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

            Model.instance().getDessertImage(getNewDessert(), 0, new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    ((ImageView) view.findViewById(R.id.addImg)).setImageBitmap(image);
                    oldImageBitmap = image;
                }

                @Override
                public void onFail() {
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
        if (mode == ADD_MODE) {
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
                selectedImageBitmap = ((BitmapDrawable) ((ImageView) getView().findViewById(R.id.addImg)).getDrawable()).getBitmap();

                // validation
                if ((getNewDessert().getName() == null) || (getNewDessert().getName().equals(""))) {
                    ((EditText) getView().findViewById(R.id.addLable)).setError(getString(R.string.emptyField));
                } else {

                    // If there is not need to update the image
                    if (oldImageBitmap == selectedImageBitmap) {
                        selectedImageBitmap = null;
                    }
                    progressBar.setVisibility(getView().VISIBLE);
                    // Add / update the dessert
                    Model.instance().addDessert(getNewDessert(), selectedImageBitmap, new Model.SuccessListener() {
                        @Override
                        public void onResult(boolean result) {
                            if (result) {
                                // Display message
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.updateSuccessfuly),
                                        Toast.LENGTH_SHORT).show();

                                getActivity().finish();
                                // Return to the list activity
                                Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                                startActivity(intent);
                            } else {
                                // Display message
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorOccure),
                                        Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(getView().GONE);
                        }
                    });
                }
                return true;
            }
            case R.id.menuDel:
                startDeleteDialog();
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

    private void startImageDialog() {
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

                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        }
                    }
                });
        optionsAlert.show();
    }

    private void startDeleteDialog() {
        final AlertDialog.Builder optionsAlert = new AlertDialog.Builder(
                getActivity());
        optionsAlert.setTitle(getString(R.string.deleteTitle));
        optionsAlert.setMessage(getString(R.string.deleteQuestion));

        /** OK option**/
        optionsAlert.setPositiveButton(getString(R.string.deleteOK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Model.instance().deleteDessert(getNewDessert().getId(), getNewDessert().getImageUrl(), new Model.SuccessListener() {
                            @Override
                            public void onResult(boolean result) {
                                // Display message
                                if (result) {
                                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.deleteSuccessfuly),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorOccure),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        getActivity().finish();
                        // Return to the list activity
                        Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                        startActivity(intent);
                    }
                });

        /** Cancle option**/
        optionsAlert.setNegativeButton(getString(R.string.deleteCancle),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        optionsAlert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        progressBar.setVisibility(getView().VISIBLE);
        super.onActivityResult(requestCode, resultCode, intent);

        selectedImagePath = null;

        if ((resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) ||
                (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST)) {
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

            // Creating the bitmap and make the changes
            if (selectedImagePath != null) {
                /** Get selected image as bitmap **/
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                selectedImageBitmap = BitmapFactory.decodeFile(selectedImagePath);
                selectedImageBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 768, 1024, false);

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
                progressBar.setVisibility(getView().GONE);
            }
        }
    }
}