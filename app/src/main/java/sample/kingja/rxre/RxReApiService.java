package sample.kingja.rxre;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Description:TODO
 * Create Time:2018/4/7 20:03
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface RxReApiService {
    @FormUrlEncoded
    @POST("api/admin/login")
    Observable<Result<User>> login(@Field("username") String uername, @Field("password") String password);
}
