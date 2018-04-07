package sample.kingja.rxre;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:TODO
 * Create Time:2018/4/7 19:59
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class Api {

    private final ReApiService reApiService;

    public Api() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.102/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        reApiService = retrofit.create(ReApiService.class);
    }

    public Call<Result<User>> login(String username, String password) {
        return reApiService.login(username, password);
    }
}
