package ig.com.digitalmandi.retrofit;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shivam.garg on 29-08-2016.
 */
public class RetrofitWebService {

    private String END_POINT = "http://www.studyextension.in/shivam/to/";
    private static RetrofitWebService ourInstance;

    public static RetrofitWebService getInstance() {
        if (ourInstance == null)
            ourInstance = new RetrofitWebService();
        return ourInstance;
    }

    private RetrofitWebService() {}

    public RetrofitInterface getInterface(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(15, TimeUnit.SECONDS).connectTimeout(15, TimeUnit.SECONDS).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(RetrofitInterface.class);
    }
}
