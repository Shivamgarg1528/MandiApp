package ig.com.digitalmandi.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ig.com.digitalmandi.BuildConfig;
import ig.com.digitalmandi.activity.BaseActivity;
import okhttp3.ResponseBody;


/**
 * @author shivam.garg
 */

public class Utils {

    public static void vibrate(Context pContext) {
        Vibrator vb = (Vibrator) pContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 0};
        vb.vibrate(pattern, 0);
        vb.cancel();
    }

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

    public static void setImage(Context mContext, String url, View imageVew) {
        try {
            if (URLUtil.isValidUrl(url)) {
                Picasso.with(mContext).load(url).into((ImageView) imageVew);
            }
        } catch (Exception ignored) {
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

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    public static String getDeviceId(Context mContext) {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    @SuppressWarnings("unchecked")
    public static <T> T findViewByIdAndCast(int id, Activity activity) {
        return (T) activity.findViewById(id);
    }

    public static <T> T onGetWeakReference(T anyObject) {
        if (anyObject != null)
            return new WeakReference<T>(anyObject).get();
        else
            return null;
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
     * @param fragment         specify the reference of fragment from where you want to start new activity
     * @param isNoHistoryTrue  true if you want to finish previous activity otherwise false
     * @param flagsArray       A flag array that you want to bind with requested component or new int[]{} if you want to skip
     * @param predefinedIntent A reference of intent that you want to fire otherwise null in case (if you want to specify the targetClassName)
     * @param targetClassName  A targeted component name that you want to fire or null if you already specify the predefinedIntent Intent
     * @param requestCode      Any integer code
     */

    public static <T> void onActivityStartForResultInFragment(Fragment fragment, boolean isNoHistoryTrue, int[] flagsArray, Intent predefinedIntent, Class<T> targetClassName, int requestCode) {
        Fragment fragmentRef = onGetWeakReference(fragment);
        if (fragmentRef != null) {
            if (predefinedIntent == null) {
                predefinedIntent = new Intent(fragment.getActivity(), targetClassName);
                for (int flags : flagsArray) predefinedIntent.addFlags(flags);
            }
            fragmentRef.startActivityForResult(predefinedIntent, requestCode);
            if (isNoHistoryTrue)
                fragmentRef.getActivity().finish();
        }
    }


    /**
     * to start any android activity from outside Application context like 'Broadcast'
     *
     * @param mContext         specify the context from where you want to start that activity
     * @param targetClassName  A targeted component name that you want to fire or null if you already specify the predefinedIntent Intent
     * @param predefinedIntent A reference of intent that you want to fire otherwise null in case (if you want to specify the targetClassName)
     */

    public static <T> void onActivityStartFromOutSideContext(Context mContext, Class<T> targetClassName, Intent predefinedIntent) {
        if (predefinedIntent == null) {
            predefinedIntent = new Intent(mContext, targetClassName);
            predefinedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(predefinedIntent);
    }


    /**
     * to show the Toast with specified Message
     *
     * @param mContext specify the context from where you want to show Toast
     * @param toastMsg specify the message that you want to show on Toast
     */

    public static void onShowToast(Context mContext, String toastMsg) {
        Toast.makeText(mContext, toastMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * to show the Toast with default Message i.e 'Under Development You will see this feature soon :)'
     *
     * @param mContext specify the context from where you want to show Toast
     */

    public static void onShowToast(Context mContext) {
        Toast.makeText(mContext, "Under Development You will see this feature soon :)", Toast.LENGTH_SHORT).show();
    }

    /**
     * open play store from your applicaiton
     *
     * @param mContext specify the context from where you call this operation
     */

    public static void onOpenPlayStore(Context mContext) {
        final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    /**
     * format the float value in 2 precision
     *
     * @param value float value in String that you want to format
     * @return a formatted value in string with 2 precision
     */

    public static String formatStringUpTo2Precision(String value) {
        try {
            if (isEmpty(value))
                return "0.00";
            float conversion = Float.parseFloat(value);
            return String.format(Locale.getDefault(), "%.2f", conversion);
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }

    public static String onStringFormatForZeros(String value) {
        float conversion = Float.parseFloat(value);
        String convertedValue = String.format(Locale.getDefault(), "%.2f", conversion);
        return ("00000000000" + convertedValue).substring(convertedValue.length());
    }

    /**
     * Check Internet is Available or Not.
     *
     * @param mContext specify the context from where you call this operation
     * @return true if Internet is Available else return false.
     */

    public static boolean isInternetAvialable(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressWarnings("deprecation")
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    /**
     * @return IpAddress of Device
     */
    public static String onGetLocalIpAddressFromDevice() {
        String ipAddress = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ipAddress = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return ipAddress;
    }

    /**
     * Hiding the KeyBoard from screen
     *
     * @param mContext         specify the context from where you call this operation
     * @param obtainedEditText a view from which you want to take token for hiding the keyboard
     */

    public static void onHideSoftKeyBoard(Context mContext, View obtainedEditText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(obtainedEditText.getWindowToken(), 0);
    }

    /**
     * @param mContext specify the context from where you call this operation
     * @return true if GPS in on else return false
     */

    public static boolean isGpsOn(Context mContext) {
        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * to calculate the value of any view according to %
     *
     * @param screenValue can be width / height value of device
     * @param newValue    value in float for any percentage like 10% = 10.00
     * @return return calculate value in form of integer
     */

    public static int getCalculatedWidthOrHeight(int screenValue, double newValue) {
        return (int) Math.floor(screenValue * newValue / 100.0);
    }

    /**
     * to get any basic Auth value based on given two strings as params for rest api's
     *
     * @param stringOne first given string
     * @param stringTwo second given string
     * @return a new Converted basicauth value
     */

    public static String onGetBasicAuth(String stringOne, String stringTwo) {
        StringBuilder str = new StringBuilder();
        str.append("Basic ").append(getBase64String(stringOne.concat(":").concat(stringTwo)));
        return str.toString();
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
     * @param mContext mContext specify the context from where you call this operation
     * @return Application Version Code
     */

    public static String onGetAppVersionCode(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * if you want to open camera call this method
     *
     * @param activity    current activity that is calling this method
     * @param requestCode integer code
     */

    public static void onStartCamera(AppCompatActivity activity, int requestCode) {
        Activity weakRef = onGetWeakReference(activity);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (weakRef != null && takePictureIntent.resolveActivity(weakRef.getPackageManager()) != null) {
            weakRef.startActivityForResult(takePictureIntent, requestCode);
        }
    }

    /**
     * if you want to open gallery call this method
     *
     * @param activity    current activity that is calling this method
     * @param requestCode integer code
     */

    public static void onStartGallery(AppCompatActivity activity, int requestCode) {
        Activity weakRef = onGetWeakReference(activity);
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takePictureIntent.setType("image/*");
        if (weakRef != null && takePictureIntent.resolveActivity(weakRef.getPackageManager()) != null) {
            weakRef.startActivityForResult(takePictureIntent, requestCode);
        }
    }


    /**
     * to get the current date and time from system
     *
     * @return return current date and time of system in format 'yyyy/MM/dd hh:mm:ss'
     */

    public static String onGetCurrentDateWithTime() {
        return (String) DateFormat.format("yyyy/MM/dd hh:mm:ss", new Date().getTime());
    }

    /**
     * to get the current date from system
     *
     * @return return current date of system in format 'yyyy/MM/dd'
     */

    public static String onGetCurrentDate() {
        return (String) DateFormat.format("yyyy/MM/dd", new Date().getTime());
    }

    /**
     * to get the current time from system
     *
     * @return return current time of system in format 'hh:mm:ss'
     */
    public static String onGetCurrentTime() {
        return (String) DateFormat.format("hh:mm:ss", new Date().getTime());
    }


    /**
     * to get the date object of given dateString
     *
     * @param dateString given string that you want to convert
     * @param format     given format in which you want to convert default format is 'yyyy-MM-dd HH:mm:ss'
     * @return convert date object
     */

    public static Date onConvertStringToDate(String dateString, String format) {
        Date date = null;

        if (TextUtils.isEmpty(format))
            format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        try {
            date = df.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                date = df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
        }
        return date;
    }

    public static String getDateString(long pMilliSeconds, String pDateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(pDateFormat);
        return formatter.format(pMilliSeconds);
    }

    public static String onConvertDateStringToOtherStringFormat(String dateStr) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(AppConstant.API_DATE_FORMAT);
        SimpleDateFormat toFormat = new SimpleDateFormat(AppConstant.API_DATE_FORMAT);
        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
        return toFormat.format(date);
    }

    /**
     * Clear all cache memory of the Application
     *
     * @param context mContext specify the context from where you call this operation
     */

    public static void onClearCacheOfApp(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }

	/*    public static boolean isGoogleServiceAvailable(Activity activity) {
        Activity weakRef = onGetWeakReference(activity);

        if (weakRef != null) {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(weakRef);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog(weakRef, resultCode,9000).show();
                } else {
                    onShowToast(weakRef,"Your Device Does Not Have Google Play Service");
                    weakRef.finish();
                }
                return false;
            }
            return true;
        }
        return false;
    }*/

    /**
     * Check Application in Background or not.
     *
     * @param context mContext specify the context from where you call this operation
     * @return true if Application is in background else return false
     */

    public static boolean isAppInBackGround(Context context) {

        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            @SuppressWarnings("deprecation")
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    /**
     * check if any given application is exist on device or not
     *
     * @param mContext    mContext specify the context from where you call this operation
     * @param packageName package name of requested Application
     * @return true if application exist else return false
     */

    public static boolean isAppExistInDevice(Context mContext, String packageName) {

        boolean app_installed = false;
        try {
            mContext.getPackageManager().getApplicationInfo(packageName, 0);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static String getSha1(String password) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static boolean isAutoTimeEnableInDevice(Context mContext) {
        if (Build.VERSION.SDK_INT > 17) {
            return android.provider.Settings.Global.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0) != 0;
        } else {
            return android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) != 0;
        }
    }

    public static int getNumberOfDaysBetweenDate(Date paymentDate, Date purchaseDate) {
        long diff = paymentDate.getTime() - purchaseDate.getTime();
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
                    Log.d("Utils", "Dir Created ? true/false =" + isSuccess);
                }
            }
            String fileName = (pOrderOrPaymentBill ? AppConstant.ORDER_BILL_PREFIX : AppConstant.PAYMENT_BILL_PREFIX).concat(pPdfId).concat(".pdf");
            File pdfFile = new File(dir, fileName);
            if (!pdfFile.isFile()) {
                boolean isSuccess = pdfFile.createNewFile();
                if (BuildConfig.DEBUG) {
                    Log.d("Utils", "File Created ? true/false =" + isSuccess);
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
            Log.d("Utils", filePath);
        }
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        try {
            pBaseActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(pBaseActivity, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}




