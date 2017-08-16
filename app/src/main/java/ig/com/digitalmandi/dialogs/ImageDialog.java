package ig.com.digitalmandi.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import ig.com.digitalmandi.camera.PhotoIntentActivity;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shivam.garg on 17-10-2016.
 */

public class ImageDialog {

    private AppCompatActivity mHostActivity;
    private OnItemSelectedListener listener;
    public  static final int CAMERA_REQ_CODE  = 100;
    public  static final int GALLERY_REQ_CODE = 101;
    private int imageWidth,imageHeight;

    public ImageDialog(AppCompatActivity mHostActivity, OnItemSelectedListener listener, int imageWidth, int imageHeight) {
        this.mHostActivity = mHostActivity;
        this.listener = listener;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public interface OnItemSelectedListener {
        public void onRemoveItemTap();
        public void onCancelItemTap();
        public void onImageReceived(Bitmap bitmap);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case CAMERA_REQ_CODE:
                    String imagePath = data.getStringExtra(PhotoIntentActivity.BITMAP_IMAGE_PATH);
                    if (!TextUtils.isEmpty(imagePath)) {
                        Bitmap bitmap = Utils.getBitmapFromPath(imagePath,imageWidth,imageHeight);
                        listener.onImageReceived(bitmap);
                    }
                    break;

                case GALLERY_REQ_CODE:
                   /* ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                    if (images != null && images.size() > 0) {
                        if (!TextUtils.isEmpty(images.get(0).getURL())) {
                            onSetImage(images.get(0).getURL());
                        }
                    }*/
                    break;
            }
        }
    }



    public void onShowDialog(){

        if(mHostActivity instanceof OnItemSelectedListener){
            final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Remove", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {

                        case 0:
                            Intent intent = new Intent(mHostActivity, PhotoIntentActivity.class);
                            intent.putExtra(PhotoIntentActivity.TITLE_KEY, "CAMERA");
                            Utils.onActivityStartForResult(mHostActivity, false, new int[]{}, intent, null, CAMERA_REQ_CODE);
                            break;

                        case 1:
                        /*ImagePicker.create(mRunningActivity)
                                .folderMode(false)
                                .imageTitle("Tap To Select")
                                .single()
                                .limit(1)
                                .showCamera(false)
                                .start(GALLERY_REQ_CODE);
                            break;*/

                        case 2:
                            listener.onRemoveItemTap();
                            break;

                        case 3:
                            listener.onCancelItemTap();
                            break;
                    }
                }
            });
            builder.show();
        }
        else
            throw new ClassCastException(mHostActivity.getClass().getName() + "Must Implement "+OnItemSelectedListener.class.getSimpleName() +" Interface :)" );
    }
}
