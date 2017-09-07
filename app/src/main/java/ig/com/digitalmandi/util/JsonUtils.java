package ig.com.digitalmandi.util;

import android.util.Log;

import com.google.gson.Gson;

final class JsonUtils {

    private final static Gson M_GSON = new Gson();
    private static final String LOG_TAG = "JsonUtils";

    static String jsonify(Object object) {
        return M_GSON.toJson(object);
    }

    static <T> T objectify(String pJson, Class<T> pType) {
        if (pJson == null || pJson.trim().length() == 0) {
            return null;
        }
        try {
            return M_GSON.fromJson(pJson, pType);
        } catch (Exception e) {
            Log.e(LOG_TAG, "JsonUtils#objectify() Class " + pType + ", Json: " + pJson, e);
        }
        return null;
    }
}
