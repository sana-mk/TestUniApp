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
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/TokenAuth/Authenticate")
    Call<LoginModels.LoginResponse> authenticate(@Body LoginModels.LoginRequest request);

    @GET("api/services/app/Course/GetAll")
    Call<CourseModels.CourseApiResponse> getCourses();

    @POST("auth/login/")
    Call<ResponseBody> login(@Body RequestBody body);

    @POST("auth/logout/")
    Call<ResponseBody> logout();

    @POST("api/TokenAuth/RefreshToken")
    Call<LoginModels.RefreshTokenResponse> refreshToken(@Query("refreshToken") String refreshToken);

    @GET("user/profile/")
    Call<ResponseBody> getProfile();

    @GET("conferences/")
    Call<List<Conference>> getConferences(@Query("topic") String topic);

    @GET("conferences/{id}/")
    Call<Conference> getConferenceDetail(@Path("id") int conferenceId);

    @POST("conferences/{id}/register/")
    Call<ResponseBody> registerConference(@Path("id") int conferenceId);

    @POST("conferences/{id}/cancel/")
    Call<ResponseBody> cancelRegistration(String authToken, @Path("id") int conferenceId);

    @PUT("conferences/{id}/favorite/")
    Call<ResponseBody> updateFavorite(@Path("id") int conferenceId, @Body RequestBody body);

    @POST("conferences/{id}/ticket/")
    Call<ResponseBody> sendCollaborationTicket(@Path("id") int conferenceId, @Body RequestBody body);

    @GET("conferences/{id}/download/")
    Call<ResponseBody> getDownloadInfo(@Path("id") int conferenceId);
}