package ig.com.digitalmandi.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

import ig.com.digitalmandi.util.Utils;

public class ImageDialog {

    public static final int REQUEST_CODE_IMAGE = 100;

    private AppCompatActivity mActivity;
    private OnItemSelectedListener mListener;
    private int mImageWidth, mImageHeight;

    public ImageDialog(AppCompatActivity pActivity, OnItemSelectedListener pListener, int pImageWidth, int pImageHeight) {
        this.mActivity = pActivity;
        this.mListener = pListener;
        this.mImageWidth = pImageWidth;
        this.mImageHeight = pImageHeight;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                    if (images != null && images.size() > 0) {
                        if (!Utils.isEmpty(images.get(0).getPath())) {
                            mListener.onImageReceived(Utils.getBitmapFromPath(images.get(0).getPath(), mImageWidth, mImageHeight));
                        }
                    }
                    break;
            }
        }
    }

    public void show() {
        final CharSequence[] items = {"Select Image", "Remove", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: {
                        ImagePicker
                                .create(mActivity)
                                .folderMode(true)
                                .folderTitle("Images")
                                .single()
                                .start(REQUEST_CODE_IMAGE);
                        break;
                    }
                    case 1:
                        mListener.onRemoveItemTap();
                        break;

                    case 2:
                        mListener.onCancelItemTap();
                        break;
                }
            }
        });
        builder.show();
    }

    public interface OnItemSelectedListener {
        void onRemoveItemTap();

        void onCancelItemTap();

        void onImageReceived(Bitmap pBitmap);
    }
}
