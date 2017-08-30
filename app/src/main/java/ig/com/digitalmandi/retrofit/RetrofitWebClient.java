package ig.com.digitalmandi.retrofit;

import java.util.concurrent.TimeUnit;

import ig.com.digitalmandi.utils.AppConstant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWebClient {

    private static RetrofitWebClient sInstance;
    private final RetrofitInterface mRetrofitInterface;

    private RetrofitWebClient() {
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

    public static RetrofitWebClient getInstance() {
        if (sInstance == null)
            sInstance = new RetrofitWebClient();
        return sInstance;
    }

    public RetrofitInterface getInterface() {
        return mRetrofitInterface;
    }
}
