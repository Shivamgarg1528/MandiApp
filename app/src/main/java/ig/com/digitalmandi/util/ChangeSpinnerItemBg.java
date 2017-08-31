package ig.com.digitalmandi.util;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ig.com.digitalmandi.R;

/**
 * Created by Shivam.Garg on 09-11-2016.
 */

public class ChangeSpinnerItemBg {

    public static ArrayAdapter<String> onChangeSpinnerBgWhite(final AppCompatActivity mContext,String[] array, AppCompatSpinner mSpinner) {

        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, array) {

                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    return view;
                }
            };
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(arrayAdapter);
            return arrayAdapter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void onChangeSpinnerBgColorPrimary(final AppCompatActivity mContext, ArrayAdapter<String> arrayAdapter, String[] array, AppCompatSpinner mSpinner) {
        try {
            arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, array) {

                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    return view;
                }
            };

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(arrayAdapter);
            mSpinner.setPopupBackgroundResource(R.color.colorWhite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
