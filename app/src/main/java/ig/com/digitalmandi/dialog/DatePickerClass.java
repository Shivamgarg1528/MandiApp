package ig.com.digitalmandi.dialog;

import android.app.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class DatePickerClass {

    public static final int START_DATE = 0x1;
    public static final int END_DATE = 0x2;
    public static final int PAYMENT_DATE = 0x3;
    public static final int ORDER_DATE = 0x4;

    public static void showDatePicker(BaseActivity pBaseActivity, final int pId, final OnDateSelected pDateSelectedCallback) {

        if (!Helper.isAutoTimeEnableInDevice(pBaseActivity)) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_please_enable_auto_date_time_from_setting));
            return;
        }

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(pBaseActivity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, monthOfYear, dayOfMonth);
                String pDateApiFormat = Helper.getDateString(selectedCalendar.getTimeInMillis(), AppConstant.API_DATE_FORMAT);
                String pDateAppFormat = Helper.getDateString(selectedCalendar.getTimeInMillis(), AppConstant.APP_DATE_FORMAT);
                Date pDate = Helper.onConvertStringToDate(pDateApiFormat, AppConstant.API_DATE_FORMAT);
                pDateSelectedCallback.onDateSelectedCallBack(pId, pDate, pDateAppFormat, selectedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    public interface OnDateSelected {
        void onDateSelectedCallBack(int id, Date pDate, String pDateAppShownFormat, int pMaxDaysInSelectedMonth);
    }
}
