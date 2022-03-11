package com.example.passcode.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.passcode.R;
import com.example.passcode.activity.adapter.RecyclerViewAdapter;
import com.example.passcode.databinding.ActivityDashboardBinding;
import com.impulsiveweb.galleryview.ActionCallback;
import com.impulsiveweb.galleryview.GalleryView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    public static int PICK_IMAGE = 100;
    public static int PICK_VIDEO = 101;
    private String[] cameraPermissions;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private static final int CAMERA_REQUEST_CODE = 300;
    ActivityDashboardBinding activityDashboardBinding;
    public ArrayList<String> paths = new ArrayList<>();
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions();

        activityDashboardBinding.buttonPickFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE);
                } catch (Exception e) {
                    Log.e("Error ==> ", "" + e);

                }
            }
        });

        activityDashboardBinding.buttonShowGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGallery();
            }
        });

        activityDashboardBinding.buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraPermission()) {
                    pickFromCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });


    }

    public void showGallery() {
//        GalleryView.show(DashboardActivity.this, paths);
        Log.e("Paths", String.valueOf(paths));
        GalleryView.show(this, paths, 0, new ActionCallback() {

            @Override
            public void onAction(String path, int position) {
                Log.e("Action", "Done");
            }
        });
    }

    private void requestPermissions() {
        if (checkPermission()) {
            // if the permissions are already granted we are calling
            // a method to get all images from our external storage.
            Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show();
        } else {
            // if the permissions are not granted we are
            // calling a method to request permissions.
            requestPermission();
        }
    }

    private boolean checkPermission() {
        // in this method we are checking if the permissions are granted or not and returning the result.
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultCamera;
    }

    private void requestPermission() {
        //on below line we are requesting the rea external storage permissions.
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == Activity.RESULT_OK && requestCode == PICK_VIDEO) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                String selectedVideoPath = getVideoPath(contentURI);
//                Log.d("path",selectedVideoPath);
//                paths.add(selectedVideoPath);
//            }
//        }
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                String selectedImagePath = getImagePath(contentURI);
                Log.e("path", selectedImagePath);
                paths.add(selectedImagePath);
            }
        } else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK) {
            if (imageUri != null) {
                String path = getImagePath(imageUri);
                paths.add(path);
                Log.e("Done in path", path);
            }

        }
    }

    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called after permissions has been granted.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // we are checking the permission code.
            case PERMISSION_REQUEST_CODE: {
                // in this case we are checking if the permissions are accepted or not.
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // if the permissions are accepted we are displaying a toast message
                        // and calling a method to get image path.
                        Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show();
                    } else {
                        // if permissions are denied we are closing the app and displaying the toast message.
                        Toast.makeText(this, "Permissions denied, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getApplicationContext(), "Camera and storage permission required..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
    }

    private void pickFromCamera() {
        //using media to pic high quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_image_title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_image_description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent pickCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pickCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(pickCameraIntent, IMAGE_PICK_CAMERA_CODE);
        Log.e("After pick ", String.valueOf(imageUri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}