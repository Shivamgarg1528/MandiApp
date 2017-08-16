package ig.com.digitalmandi.dialogs;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shivam.garg on 25-10-2016.
 */

public class DatePickerClass {

    public static final int START_DATE     = 0x1;
    public static final int END_DATE       = 0x2;
    public static final int PAYMENT_DATE   = 0x3;
    public static final int ORDER_DATE     = 0x4;

    public interface OnDateSelected{
        public void onDateSelectedCallBack(int id, Date newCalendar1, String stringResOfDate,long milliSeconds,int numberOfDays);
    }

    public static void showDatePicker(final int id, final OnDateSelected dateSelected, AppCompatActivity mHostActivity, final String dateFormat){

        if (!Utils.isAutoTimeEnableInDevice(mHostActivity)) {
            Toast.makeText(mHostActivity, "Please Enable Auto Date From Device", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar newCalendar1 = Calendar.getInstance();
        DatePickerDialog startDatePicker1 = new DatePickerDialog(mHostActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                String stringResOfDate = Utils.onConvertDateToString(selectedDate.getTimeInMillis(),dateFormat);
                Date   date            = Utils.onConvertStringToDate(stringResOfDate,dateFormat);
                dateSelected.onDateSelectedCallBack(id,date,stringResOfDate,selectedDate.getTimeInMillis(),selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }, newCalendar1.get(Calendar.YEAR), newCalendar1.get(Calendar.MONTH), newCalendar1.get(Calendar.DAY_OF_MONTH));
        startDatePicker1.show();
    }
}
