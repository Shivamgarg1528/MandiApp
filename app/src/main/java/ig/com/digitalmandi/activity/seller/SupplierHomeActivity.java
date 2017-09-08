package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.fragment.BaseFragment;
import ig.com.digitalmandi.fragment.supplier.CustomerFragment;
import ig.com.digitalmandi.fragment.supplier.EmptyFragment;
import ig.com.digitalmandi.fragment.supplier.LogoutFragment;
import ig.com.digitalmandi.fragment.supplier.ProductFragment;
import ig.com.digitalmandi.fragment.supplier.PurchaseFragment;
import ig.com.digitalmandi.fragment.supplier.UnitFragment;
import ig.com.digitalmandi.util.Helper;

public class SupplierHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawerView;
    private ActionBarDrawerToggle mActionBarToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_supplier_home);
        setToolbar(true);

        mDrawerView = findViewById(R.id.drawer_layout);
        mActionBarToggle = new ActionBarDrawerToggle(this, mDrawerView, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Helper.hideSoftKeyBoard(mBaseActivity, drawerView);
            }
        };
        mDrawerView.addDrawerListener(mActionBarToggle);
        mActionBarToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(this);
        CircleImageView mImageViewUser = headerView.findViewById(R.id.layout_activity_sign_up_btn_user_image);
        Helper.setImage(mBaseActivity, mLoginUser.getUserImageUrl(), mImageViewUser);
        AppCompatTextView mTextViewUserName = headerView.findViewById(R.id.mTextViewUserInfoName);
        mTextViewUserName.setText(mLoginUser.getUserName() + "\n" + mLoginUser.getUserMobileNo());
        onItemSelected(R.id.supplier_nav_menu_customer, getString(R.string.customers));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerView.isDrawerOpen(GravityCompat.START)) {
            mDrawerView.closeDrawer(GravityCompat.START);
        } else {
            mDrawerView.removeDrawerListener(mActionBarToggle);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem pItem) {
        pItem.setChecked(true);
        mDrawerView.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onItemSelected(pItem.getItemId(), pItem.getTitle().toString());
            }
        }, 200);
        return true;
    }


    private void onItemSelected(int pItemId, String pItemName) {
        setTitle(pItemName);
        BaseFragment fragment = null;
        switch (pItemId) {

            case R.id.supplier_nav_menu_customer:
                fragment = new CustomerFragment();
                break;

            case R.id.supplier_nav_menu_balance_sheet:
                fragment = new CustomerFragment();
                break;

            case R.id.supplier_nav_menu_product:
                fragment = new ProductFragment();
                break;

            case R.id.supplier_nav_menu_unit:
                fragment = new UnitFragment();
                break;

            case R.id.supplier_nav_menu_purchase:
                fragment = new PurchaseFragment();
                break;

            case R.id.supplier_nav_menu_settings:
                fragment = new EmptyFragment();
                break;

            case R.id.supplier_nav_menu_logout:
                fragment = new LogoutFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.supplier_home_container, fragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mBaseActivity, "I am tapped", Toast.LENGTH_SHORT).show();
    }
}
