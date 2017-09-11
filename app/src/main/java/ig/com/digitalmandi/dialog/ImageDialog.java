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

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class ImageDialog {

    private final AppCompatActivity mActivity;
    private final OnItemSelectedListener mListener;
    private final int mImageWidth;
    private final int mImageHeight;

    public ImageDialog(BaseActivity pBaseActivity, OnItemSelectedListener pListener, int pImageWidth, int pImageHeight) {
        this.mActivity = pBaseActivity;
        this.mListener = pListener;
        this.mImageWidth = pImageWidth;
        this.mImageHeight = pImageHeight;
    }

    public void show() {
        final CharSequence[] items = {mActivity.getString(R.string.string_select_image), mActivity.getString(R.string.string_remove), mActivity.getString(R.string.string_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.string_photo);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: {
                        ImagePicker
                                .create(mActivity)
                                .folderMode(true)
                                .folderTitle(mActivity.getString(R.string.string_images))
                                .single()
                                .start(AppConstant.REQUEST_CODE_IMAGE);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case AppConstant.REQUEST_CODE_IMAGE:
                    ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                    if (images != null && images.size() > 0) {
                        if (!Helper.isEmpty(images.get(0).getPath())) {
                            mListener.onImageReceived(Helper.getBitmapFromPath(images.get(0).getPath(), mImageWidth, mImageHeight));
                        }
                    }
                    break;
            }
        }
    }

    public interface OnItemSelectedListener {
        void onRemoveItemTap();

        void onCancelItemTap();

        void onImageReceived(Bitmap pBitmap);
    }
}
