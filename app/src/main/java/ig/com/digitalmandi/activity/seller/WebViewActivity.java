package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.util.AppConstant;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView view = new WebView(this);
        view.getSettings().setJavaScriptEnabled(true);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.loadUrl(getIntent().getStringExtra(AppConstant.KEY_OBJECT));
        setContentView(view);
    }
}
