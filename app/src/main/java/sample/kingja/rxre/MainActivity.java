package sample.kingja.rxre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kingja.rxre.RxRe;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String BASE_URL="http://192.168.1.102/";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClient();
    }

    private void initClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    public void doNetByRetrofit(View view) {
        ReApiService reApiService = retrofit.create(ReApiService.class);
        Call<Result<User>> loginCall = reApiService.login("aaa", "bbb");
        loginCall.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                Result<User> body = response.body();
                Log.e(TAG, "【doNetByRetrofit】onResponse: "+body.toString() );
            }

            @Override
            public void onFailure(Call<Result<User>> call, Throwable t) {
                Log.e(TAG, "【doNetByRetrofit】onFailure: " + t.toString());
            }
        });
    }

    public void doNetByRxRe(View view) {
        RxReApiService rxReApiService = retrofit.create(RxReApiService.class);
        Observable<Result<User>> observable = rxReApiService.login("aaa", "bbb");
        Disposable disposable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result<User>>() {
            @Override
            public void accept(Result<User> userResult) throws Exception {
                Log.e(TAG, "【doNetByRxRe】accept: "+userResult.toString() );
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "【doNetByRxRe】throwable: "+throwable.toString() );
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.e(TAG, "【doNetByRxRe】run: ");
            }
        });
        RxRe.getInstance().add(this,disposable);
    }

    public void cancleRequests(View view) {
        RxRe.getInstance().cancle(this);
    }
}
