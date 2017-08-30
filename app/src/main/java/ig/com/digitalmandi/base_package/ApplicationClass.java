package ig.com.digitalmandi.base_package;

import android.app.Application;

public class ApplicationClass extends Application {

    private static ApplicationClass sInstance = new ApplicationClass();

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
