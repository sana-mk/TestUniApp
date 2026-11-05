package ir.shariaty.myjava.api;

import android.content.Context;
import java.io.IOException;
import ir.shariaty.myjava.model.LoginModels;
import ir.shariaty.myjava.utils.TokenManager;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://shariaty-api.pth3.ir/";
    private static RetrofitClient instance;
    private Retrofit retrofit;

    private RetrofitClient(Context context) {

        Interceptor authInterceptor = chain -> {
            Request.Builder builder = chain.request().newBuilder();
            String path = chain.request().url().encodedPath();

            if (!path.contains("Authenticate") && !path.contains("RefreshToken")) {
                TokenManager tokenManager = new TokenManager(context);
                String token = tokenManager.getToken();
                if (token != null) {
                    builder.addHeader("Authorization", "Bearer " + token);
                }
            }
            return chain.proceed(builder.build());
        };

        Authenticator tokenAuthenticator = (route, response) -> {

            if (response.request().url().encodedPath().contains("RefreshToken")) {
                new TokenManager(context).clearToken();
                return null;
            }

            TokenManager tokenManager = new TokenManager(context);
            String refreshToken = tokenManager.getRefreshToken();

            if (refreshToken == null) {
                return null;
            }

            try {
                retrofit2.Response<LoginModels.RefreshTokenResponse> refreshResponse =
                        getApiService().refreshToken(refreshToken).execute();

                if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                    String newAccessToken = refreshResponse.body().getAccessToken();
                    long expireIn = refreshResponse.body().getExpireInSeconds();

                    tokenManager.updateAccessToken(newAccessToken, expireIn);

                    return response.request().newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer " + newAccessToken)
                            .build();
                } else {
                    tokenManager.clearToken();
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .authenticator(tokenAuthenticator)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
        return instance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}