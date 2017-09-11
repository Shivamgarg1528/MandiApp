package ig.com.digitalmandi.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ig.com.digitalmandi.BuildConfig;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.seller.PurchaseResponse;
import okhttp3.ResponseBody;

public class Helper {

    public static boolean isEmpty(String pString) {
        return pString == null || pString.trim().length() == 0;
    }

    public static boolean isFloat(String pValue) {
        try {
            float v = Float.parseFloat(pValue);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void setImage(Context pContext, String pUrl, View pView) {
        ImageView imageView = (ImageView) pView;
        if (URLUtil.isValidUrl(pUrl)) {
            Glide.with(pContext).load(pUrl).placeholder(R.mipmap.ic_launcher).into(imageView);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public static Bitmap getBitmapFromPath(String pCurrentPhotoPath, int pTargetW, int pTargetH) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((pTargetW > 0) || (pTargetH > 0)) {
            scaleFactor = Math.min(photoW / pTargetW, photoH / pTargetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(pCurrentPhotoPath, bmOptions);
    }

    public static String getStringImage(Bitmap pBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static String getDeviceId(Context mContext) {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * to start any android activity
     *
     * @param mContext         specify the context from where you want to start that activity
     * @param isNoHistoryTrue  true if you want to finish previous activity otherwise false
     * @param flagsArray       a flag array that you want to bind with requested component or new int[]{} if you want to skip
     * @param predefinedIntent A reference of intent that you want to fire otherwise null in case (if you want to specify the targetClassName)
     * @param targetClassName  A targeted component name that you want to fire or null if you already specify the predefinedIntent Intent
     */

    public static <T> void onActivityStart(Context mContext, boolean isNoHistoryTrue, int[] flagsArray, Intent predefinedIntent, Class<T> targetClassName) {
        if (predefinedIntent == null) {
            predefinedIntent = new Intent(mContext, targetClassName);
            if (flagsArray != null) {
                for (int aFlag : flagsArray) {
                    predefinedIntent.addFlags(aFlag);
                }
            }
        }
        mContext.startActivity(predefinedIntent);
        if (isNoHistoryTrue && mContext instanceof Activity)
            ((Activity) mContext).finish();
    }

    /**
     * to start any android activity for result
     *
     * @param pActivity        specify the reference of activity from where you want to start new activity
     * @param isNoHistoryTrue  true if you want to finish previous activity otherwise false
     * @param flagsArray       A flag array that you want to bind with requested component or new int[]{} if you want to skip
     * @param predefinedIntent A reference of intent that you want to fire otherwise null in case (if you want to specify the targetClassName)
     * @param targetClassName  A targeted component name that you want to fire or null if you already specify the predefinedIntent Intent
     * @param requestCode      Any integer code
     */


    public static <T> void onActivityStartForResult(Activity pActivity, boolean isNoHistoryTrue, int[] flagsArray, Intent predefinedIntent, Class<T> targetClassName, int requestCode) {
        if (predefinedIntent == null) {
            predefinedIntent = new Intent(pActivity, targetClassName);
            if (flagsArray != null) {
                for (int aFlag : flagsArray) {
                    predefinedIntent.addFlags(aFlag);
                }
            }
        }
        pActivity.startActivityForResult(predefinedIntent, requestCode);
        if (isNoHistoryTrue)
            pActivity.finish();
    }

    /**
     * to start any android activity for result but in fragment
     *
     * @param fragmentRef      specify the reference of fragment from where you want to start new activity
     * @param isNoHistoryTrue  true if you want to finish previous activity otherwise false
     * @param flagsArray       A flag array that you want to bind with requested component or new int[]{} if you want to skip
     * @param predefinedIntent A reference of intent that you want to fire otherwise null in case (if you want to specify the targetClassName)
     * @param targetClassName  A targeted component name that you want to fire or null if you already specify the predefinedIntent Intent
     * @param requestCode      Any integer code
     */

    public static <T> void onActivityStartForResultInFragment(Fragment fragmentRef, boolean isNoHistoryTrue, int[] flagsArray, Intent predefinedIntent, Class<T> targetClassName, int requestCode) {
        if (fragmentRef != null) {
            if (predefinedIntent == null) {
                predefinedIntent = new Intent(fragmentRef.getActivity(), targetClassName);
            }
            if (flagsArray != null) {
                for (int flags : flagsArray) {
                    predefinedIntent.addFlags(flags);
                }
            }
            fragmentRef.startActivityForResult(predefinedIntent, requestCode);
            if (isNoHistoryTrue)
                fragmentRef.getActivity().finish();
        }
    }

    /**
     * format the float value in 2 precision
     *
     * @param value float value in String that you want to format
     * @return a formatted value in string with 2 precision
     */

    public static String formatStringUpTo2Precision(String value) {
        if (isEmpty(value))
            return "0.00";
        else if (!isFloat(value))
            return "0.00";
        else
            return String.format(Locale.getDefault(), "%.2f", Float.parseFloat(value));
    }

    /**
     * Hiding the KeyBoard from screen
     *
     * @param mContext         specify the context from where you call this operation
     * @param obtainedEditText a view from which you want to take token for hiding the keyboard
     */

    public static void hideSoftKeyBoard(Context mContext, View obtainedEditText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(obtainedEditText.getWindowToken(), 0);
    }

    /**
     * convert into base64 of given string
     *
     * @param pString given string that you want to convert in base64
     * @return base64 String
     */

    public static String getBase64String(String pString) {
        try {
            byte[] data = pString.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * to get the date object of given dateString
     *
     * @param pDateStr given string that you want to convert
     * @param pFormat  given format in which you want to convert default format is 'yyyy-MM-dd HH:mm:ss'
     * @return convert date object
     */

    public static Date getDateObject(String pDateStr, String pFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pFormat, Locale.getDefault());
            return df.parse(pDateStr);
        } catch (Exception ex) {
            return new Date();
        }
    }

    public static String getDateString(long pMilliSeconds, String pFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
        return formatter.format(pMilliSeconds);
    }

    public static String getAppDateFormatFromApiDateFormat(String pDateStr) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(AppConstant.API_DATE_FORMAT);
        SimpleDateFormat toFormat = new SimpleDateFormat(AppConstant.APP_DATE_FORMAT);
        Date date;
        try {
            date = fromFormat.parse(pDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return pDateStr;
        }
        return toFormat.format(date);
    }

    public static boolean isAutoTimeEnableInDevice(Context pContext) {
        if (Build.VERSION.SDK_INT > 17) {
            return android.provider.Settings.Global.getInt(pContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0) != 0;
        } else {
            return android.provider.Settings.System.getInt(pContext.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) != 0;
        }
    }

    public static int getNumberOfDaysBetweenDate(Date pStartDate, Date pEndDate) {
        long diff = pStartDate.getTime() - pEndDate.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static boolean writePdf(ResponseBody pResponseBody, String pPdfId, boolean pOrderOrPaymentBill) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), AppConstant.DIRECTORY_NAME);
            if (!dir.isDirectory()) {
                boolean isSuccess = dir.mkdir();
                if (BuildConfig.DEBUG) {
                    Log.d("Helper", "Dir Created ? true/false =" + isSuccess);
                }
            }
            String fileName = (pOrderOrPaymentBill ? AppConstant.ORDER_BILL_PREFIX : AppConstant.PAYMENT_BILL_PREFIX).concat(pPdfId).concat(".pdf");
            File pdfFile = new File(dir, fileName);
            if (!pdfFile.isFile()) {
                boolean isSuccess = pdfFile.createNewFile();
                if (BuildConfig.DEBUG) {
                    Log.d("Helper", "File Created ? true/false =" + isSuccess);
                }
            }
            byte[] bytes = new byte[4096];
            inputStream = pResponseBody.byteStream();
            outputStream = new FileOutputStream(pdfFile);

            while (true) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void readPdf(BaseActivity pBaseActivity, String pPdfId, boolean pOrderOrPaymentBill) {

        String filePath = Environment.getExternalStorageDirectory().toString()
                .concat("/")
                .concat(AppConstant.DIRECTORY_NAME)
                .concat("/")
                .concat((pOrderOrPaymentBill ? AppConstant.ORDER_BILL_PREFIX : AppConstant.PAYMENT_BILL_PREFIX).concat(pPdfId).concat(".pdf"));
        if (BuildConfig.DEBUG) {
            Log.d("Helper", filePath);
        }
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        try {
            pBaseActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            pBaseActivity.showToast("No Application available to view PDF");
        }
    }

    public static void changeSellerOrderResponse(List<PurchaseResponse.Purchase> pDataList) {
        for (int index = pDataList.size() - 1; index >= 0; index--) {
            pDataList.get(index).setSumOfProductInKg(pDataList.get(index).getSumOfProductInKg());
        }
    }

    public static boolean isEmailAddressOk(String pEmailAddress, BaseActivity pBaseActivity) {
        if (isEmpty(pEmailAddress) || !Patterns.EMAIL_ADDRESS.matcher(pEmailAddress).matches()) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.enter_valid_email_address));
            return false;
        }
        return true;
    }

    public static boolean isPasswordOk(String pPassword, BaseActivity pBaseActivity) {
        if (isEmpty(pPassword) || pPassword.length() < 6) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_password_at_least_six_char_long));
            return false;
        }
        return true;
    }

    public static boolean isTinNoOk(String pTinNumber, BaseActivity pBaseActivity) {
        if (isEmpty(pTinNumber)) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_enter_tin_number));
            return false;
        }
        return true;
    }

    public static boolean isFirmOk(String pFirmName, BaseActivity pBaseActivity) {
        if (isEmpty(pFirmName)) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_please_enter_firm_name));
            return false;
        }
        return true;
    }

    public static boolean isPhoneNoOk(String pPhoneNo, BaseActivity pBaseActivity) {
        if (isEmpty(pPhoneNo) || pPhoneNo.length() != 10) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_enter_valid_phone_number));
            return false;
        }
        return true;
    }

    public static boolean isPersonNameOk(String pName, BaseActivity pBaseActivity) {
        if (isEmpty(pName) || pName.length() < 3) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_name_at_least_three_char_long));
            return false;
        }
        return true;
    }

    public static SpinnerAdapter getAdapter(final BaseActivity pBaseActivity, int pStringArrayId) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(pBaseActivity, android.R.layout.simple_spinner_item, pBaseActivity.getResources().getStringArray(pStringArrayId)) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(pBaseActivity, R.color.colorWhite));
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    public static SpinnerAdapter getAdapter(final BaseActivity pBaseActivity, String[] pStringArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(pBaseActivity, android.R.layout.simple_spinner_item, pStringArray) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(pBaseActivity, R.color.colorWhite));
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }
}




