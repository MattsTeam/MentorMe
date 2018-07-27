package me.chrislewis.mentorship.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.chrislewis.mentorship.SharedViewModel;

import static me.chrislewis.mentorship.MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static me.chrislewis.mentorship.MainActivity.PICK_IMAGE_REQUEST;

public class Camera {

    private Context context;
    private Fragment fragment;
    private SharedViewModel model;
    private File photoFile;
    private ParseFile parseFile;
    private ImageView imageView;
    private Bitmap photoBitmap;
    private String photoFileName = "photo.jpg";

    public Camera(Context context, Fragment fragment, SharedViewModel model) {
        this.context = context;
        this.fragment = fragment;
        this.model = model;
    }

    public File getPhotoFile() {
        if (photoFile != null) {
            return photoFile;
        } else {
            return null;
        }
    }

    public Bitmap getPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        return bitmap;
    }

    public void launchPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        photoFile = getPhotoFileUri(photoFileName);
    }

    public void launchCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(context, "me.chrislewis.mentorship", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            fragment.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public Bitmap getChosenPhoto(Intent data) {
        Uri uri = data.getData();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            Toast.makeText(context, "Picture wasn't chosen!", Toast.LENGTH_SHORT).show();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(photoFile.getAbsolutePath());
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MainActivity");

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("MainActivity", "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
}