package ir.shariaty.myjava.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "AppAuthPrefs";
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String KEY_EXPIRE_TIME = "EXPIRE_TIME_MS";

    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveTokens(String accessToken, String refreshToken, long expireInSeconds) {
        long expireTimeMs = System.currentTimeMillis() + (expireInSeconds * 1000);
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putLong(KEY_EXPIRE_TIME, expireTimeMs)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    public void clearToken() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        String token = getToken();
        if (token == null) {
            return false;
        }

        long expireTime = prefs.getLong(KEY_EXPIRE_TIME, 0);
        if (System.currentTimeMillis() >= expireTime) {
            clearToken();
            return false;
        }
        return true;
    }

    public void updateAccessToken(String newAccessToken, long expireInSeconds) {
        long expireTimeMs = System.currentTimeMillis() + (expireInSeconds * 1000);
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, newAccessToken)
                .putLong(KEY_EXPIRE_TIME, expireTimeMs)
                .apply();
    }
}