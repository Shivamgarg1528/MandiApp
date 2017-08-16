package ig.com.digitalmandi.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import pub.devrel.easypermissions.EasyPermissions;


public class PhotoIntentActivity extends ParentActivity implements EasyPermissions.PermissionCallbacks {

    private static final int ACTION_TAKE_PHOTO_B = 1;
    public static final String TITLE_KEY         = "titleKey";
    public static final String BITMAP_IMAGE_PATH = "bitmapImagePath";
    private String mCurrentPhotoPath, mTitle;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Bundle mSavedInstanceState = null;

    @Override
    public void onBackPressed() {
        sorryText("Failed To Fetch Image");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.camera);
        mSavedInstanceState = savedInstanceState;
        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent() == null)
            sorryText("Intent Can't Be Null");

        if (!TextUtils.isEmpty(getIntent().getStringExtra(TITLE_KEY))) {
            mTitle = getIntent().getStringExtra(TITLE_KEY);
        } else
            mTitle = "CAMERA";

        setTitle(mTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        if (EasyPermissions.hasPermissions(this, permission)) {
            fireIntent();
        } else
            EasyPermissions.requestPermissions(this, "Please Allow Camera To Take Picture", 100, permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return mTitle;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(takePictureIntent, actionCode);
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                    sorryText("Failed To Create Image Path");
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case ACTION_TAKE_PHOTO_B:
                if (resultCode == RESULT_OK) {
                    if (mCurrentPhotoPath != null) {
                        Toast.makeText(mRunningActivity, "Pic Taken", Toast.LENGTH_SHORT).show();
                        galleryAddPic();
                        Intent intent = new Intent();
                        intent.putExtra(BITMAP_IMAGE_PATH, mCurrentPhotoPath);
                        setResult(RESULT_OK, intent);
                        finish();
                        return;
                    }
                    sorryText("Photo Path Null Found :(");
                } else
                    sorryText("You Pressed Back Button");
                break;

        }
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
        if(!TextUtils.isEmpty(mCurrentPhotoPath)){
            outState.putString(BITMAP_IMAGE_PATH,mCurrentPhotoPath);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String string = savedInstanceState.getString(BITMAP_IMAGE_PATH);
        if(!TextUtils.isEmpty(string))
            mCurrentPhotoPath = string;
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void fireIntent() {
        if (mSavedInstanceState == null && isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
            dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
        } else {
            sorryText("Sorry Intent Fire Failed :(");
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 100 && perms.size() == permission.length) {
            fireIntent();
            return;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        sorryText("Permission Denied By User :(");
    }

    private void sorryText(String message) {
        Toast.makeText(mRunningActivity, message, Toast.LENGTH_SHORT).show();
        finish();
        return;
    }
}