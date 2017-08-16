package ig.com.digitalmandi.activity.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.dialogs.CustomerImageTapDialog;
import ig.com.digitalmandi.fragment.supplier.CustomerFragment;
import ig.com.digitalmandi.fragment.supplier.EmptyFragment;
import ig.com.digitalmandi.fragment.supplier.LogoutFragment;
import ig.com.digitalmandi.fragment.supplier.ProductFragment;
import ig.com.digitalmandi.fragment.supplier.PurchaseFragment;
import ig.com.digitalmandi.fragment.supplier.UnitFragment;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;
import pub.devrel.easypermissions.EasyPermissions;

public class SupplierHomeActivity extends ParentActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {

    private BaseFragment baseFragment;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_supplier_home);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        CircleImageView mUserImageView   = (CircleImageView) hView.findViewById(R.id.mCircleImageViewUser);
        AppCompatTextView mTextViewUserN = (AppCompatTextView) hView.findViewById(R.id.mTextViewUserInfoName);
        mTextViewUserN.setText(MyPrefrences.getStringPrefrences(ConstantValues.USER_NAME , mRunningActivity)+"\n"+MyPrefrences.getStringPrefrences(ConstantValues.USER_MOBILE_NO , mRunningActivity));
        Utils.uploadImageIfUrlValid(mRunningActivity,MyPrefrences.getStringPrefrences(ConstantValues.USER_IMAGE_URL,mRunningActivity),mUserImageView);
        onItemSelected(R.id.supplier_nav_menu_customer, getString(R.string.customers));
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        item.setChecked(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onItemSelected(item.getItemId(),item.getTitle().toString());
            }
        }, 200);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void onItemSelected(int id, String s) {
        setTitle(s);
        switch (id) {

            case R.id.supplier_nav_menu_customer:
                baseFragment = new CustomerFragment();
                break;

            case R.id.supplier_nav_menu_balance_sheet:
                baseFragment = new EmptyFragment();
                break;

            case R.id.supplier_nav_menu_product:
                baseFragment = new ProductFragment();
                break;

            case R.id.supplier_nav_menu_unit:
                baseFragment = new UnitFragment();
                break;

            case R.id.supplier_nav_menu_purchase:
                baseFragment = new PurchaseFragment();
                break;

            case R.id.supplier_nav_menu_settings:
                baseFragment = new EmptyFragment();
                break;

            case R.id.supplier_nav_menu_logout:
                baseFragment = new LogoutFragment();
                break;

        }

        if (baseFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.supplier_home_container, baseFragment).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            if ( requestCode == CustomerImageTapDialog.CALL_PERM_CODE && baseFragment instanceof CustomerFragment) {
                CustomerFragment c = (CustomerFragment) baseFragment;
                if (c.mAdapter.dialog != null) {
                    c.mAdapter.dialog.onDialNumber();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
