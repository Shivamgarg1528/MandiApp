package ig.com.digitalmandi.retrofit;

import java.util.concurrent.TimeUnit;

import ig.com.digitalmandi.util.AppConstant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient sInstance;
    private final RetrofitInterface mRetrofitInterface;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(15, TimeUnit.SECONDS).connectTimeout(15, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mRetrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public static RetrofitClient getInstance() {
        if (sInstance == null)
            sInstance = new RetrofitClient();
        return sInstance;
    }

    public RetrofitInterface getInterface() {
        return mRetrofitInterface;
    }
}
