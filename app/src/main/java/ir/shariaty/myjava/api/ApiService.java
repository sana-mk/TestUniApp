package ir.shariaty.myjava.api;

import java.util.List;

import ir.shariaty.myjava.model.Conference;
import ir.shariaty.myjava.model.CourseModels;
import ir.shariaty.myjava.model.LoginModels;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/TokenAuth/Authenticate")
    Call<LoginModels.LoginResponse> authenticate(@Body LoginModels.LoginRequest request);

    @GET("api/services/app/Course/GetAll")
    Call<CourseModels.CourseApiResponse> getCourses(@Header("Authorization") String token);

    //باقی متد های مربوط به لاگین استفاده نشده اند
    @POST("auth/login/")
    Call<ResponseBody> login(@Body RequestBody body);

    @POST("auth/logout/")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @POST("auth/refresh-token/")
    Call<ResponseBody> refreshToken(@Header("Authorization") String refreshToken);

    @GET("user/profile/")
    Call<ResponseBody> getProfile(@Header("Authorization") String token);


    @GET("conferences/")
    Call<List<Conference>> getConferences(
            @Header("Authorization") String token,
            @Query("topic") String topic
    );


    @GET("conferences/{id}/")
    Call<Conference> getConferenceDetail(
            @Header("Authorization") String token,
            @Path("id") int conferenceId
    );


    @POST("conferences/{id}/register/")
    Call<ResponseBody> registerConference(
            @Header("Authorization") String token,
            @Path("id") int conferenceId
    );

    @POST("conferences/{id}/cancel/")
    Call<ResponseBody> cancelRegistration(
            @Header("Authorization") String token,
            @Path("id") int conferenceId
    );


    @PUT("conferences/{id}/favorite/")
    Call<ResponseBody> updateFavorite(
            @Header("Authorization") String token,
            @Path("id") int conferenceId,
            @Body RequestBody body
    );


    @POST("conferences/{id}/ticket/")
    Call<ResponseBody> sendCollaborationTicket(
            @Header("Authorization") String token,
            @Path("id") int conferenceId,
            @Body RequestBody body
    );


    @GET("conferences/{id}/download/")
    Call<ResponseBody> getDownloadInfo(
            @Header("Authorization") String token,
            @Path("id") int conferenceId
    );
}
