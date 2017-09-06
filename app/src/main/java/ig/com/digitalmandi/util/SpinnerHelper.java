package ig.com.digitalmandi.util;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;

public class SpinnerHelper {

    public static void setAdapterAndChangeBg(final BaseActivity pBaseActivity, AppCompatSpinner pAppCompatSpinner, String[] pItemArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(pBaseActivity, android.R.layout.simple_spinner_item, pItemArray) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(pBaseActivity, R.color.colorWhite));
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pAppCompatSpinner.setAdapter(arrayAdapter);
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
