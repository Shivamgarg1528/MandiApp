package ig.com.digitalmandi.base_package;

import android.app.Application;
import ig.com.digitalmandi.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shiva on 10/10/2016.
 */

public class ApplicationClass extends Application {

    private static ApplicationClass instance = new ApplicationClass();

    public static ApplicationClass getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NanumGothic-Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
