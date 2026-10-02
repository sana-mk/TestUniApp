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

        public String getAccessToken() {
            return accessToken;
        }
    }
}