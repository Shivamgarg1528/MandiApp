package ig.com.digitalmandi.base_package;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.R;
import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by shivam.garg on 06-10-2016.
 */

public abstract class ParentActivity<T> extends AppCompatActivity {

    protected AppCompatActivity mRunningActivity;
    public Resources mResources;
    protected Toolbar mToolBar;
    protected List<T> dataList     = new ArrayList<>();
    protected List<T> backUpList   = new ArrayList<>();
    public    Call<T> apiEnqueueObject;

    public void onShowOrHideBar(boolean showHide){
        try {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.id_fragment_progressbar);
            progressBar.setIndeterminate(true);

            if (progressBar != null) {
                if (showHide)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.INVISIBLE);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(apiEnqueueObject != null)
            apiEnqueueObject.cancel();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void onCreateWeakReference(AppCompatActivity appCompatActivity) {
        mRunningActivity = new WeakReference<AppCompatActivity>(appCompatActivity).get();
        mResources = getResources();
    }

    /**
     * we are overridng this method to do our work
     *
     * @param savedInstanceState same bundle passed here from child activity
     * @param layoutId           that you want to render over to screen
     */
    protected void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        onCreateWeakReference(this);
        ApplicationClass.getInstance();
        try {
            if (layoutId > 0)
                setContentView(layoutId);
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolBar);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String message){
        Toast.makeText(mRunningActivity, message, Toast.LENGTH_SHORT).show();
    }
}
