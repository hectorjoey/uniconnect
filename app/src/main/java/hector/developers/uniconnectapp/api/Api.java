package hector.developers.uniconnectapp.api;

import co.paystack.android.api.model.ApiResponse;
import hector.developers.uniconnectapp.model.Transactions;
import hector.developers.uniconnectapp.model.Users;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("login")
    Call<Users> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user")
    Call<Users> createUser(
            @Field("username") String username,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("transactions")
    Call<Transactions> createTransactions(
            @Field("created_at") String created_at,
            @Field("status") String status,
            @Field("amount") int amount,
            @Field("reference") String reference,
            @Field("operator_id") String operator_id,
            @Field("product_id") String product_id,
            @Field("bill_type") String bill_type,
            @Field("operator_name") String operator_name,
            @Field("userId") String userId,
            @Field("token") String token
    );


    @Headers("Authorization: Bearer sk_live_648dec6240ffc21ad42aa3ff648dec6240ffc21ad42aa400")
    @GET("v1/bills/customer/validate/op_4iG4k2F6gAiotGfLfCrA67")
    Call<ApiResponse> validateBill(
            @Query("meter_type") String meterType,
            @Query("bill") String sectors,
            @Query("device_number") String deviceNumber
    );

    @Headers("Authorization: Bearer sk_live_648dec6240ffc21ad42aa3ff648dec6240ffc21ad42aa400")
    @GET("v1/bills/customer/validate/op_uD3ricdTkcDHz7XcKTydTn")
    Call<ApiResponse> validateDevice(
            @Query("bill") String sectors,
            @Query("device_number") String deviceNumber
    );


    @DELETE("user/delete/{id}")
    Call<ApiResponse> deleteAccount(@Path("id") Long id);
}