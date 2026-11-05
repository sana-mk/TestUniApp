package ir.shariaty.myjava.model;

import com.google.gson.annotations.SerializedName;

public class LoginModels {

    public static class LoginRequest {
        @SerializedName("userNameOrEmailAddress")
        private String userNameOrEmailAddress;

        @SerializedName("password")
        private String password;

        public LoginRequest(String userNameOrEmailAddress, String password) {
            this.userNameOrEmailAddress = userNameOrEmailAddress;
            this.password = password;
        }
    }

    public static class LoginResponse {
        @SerializedName("result")
        private AuthResult result;

        public AuthResult getResult() {
            return result;
        }
    }

    public static class AuthResult {
        @SerializedName("accessToken")
        private String accessToken;

        @SerializedName("refreshToken")
        private String refreshToken;

        @SerializedName("expireInSeconds")
        private long expireInSeconds;

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public long getExpireInSeconds() { return expireInSeconds; }
    }

    public static class RefreshTokenResponse {
        @SerializedName("result")
        private Result result;

        @SerializedName("accessToken")
        private String accessToken;

        @SerializedName("expireInSeconds")
        private long expireInSeconds;

        public String getAccessToken() {
            return (result != null && result.accessToken != null) ? result.accessToken : accessToken;
        }

        public long getExpireInSeconds() {
            return result != null ? result.expireInSeconds : expireInSeconds;
        }

        public static class Result {
            @SerializedName("accessToken")
            public String accessToken;
            @SerializedName("expireInSeconds")
            public long expireInSeconds;
        }
    }
}