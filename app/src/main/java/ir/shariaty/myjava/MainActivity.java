package ir.shariaty.myjava;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import ir.shariaty.myjava.api.ApiService;
import ir.shariaty.myjava.api.RetrofitClient;
import ir.shariaty.myjava.database.AppDatabaseHelper;
import ir.shariaty.myjava.model.LoginModels;
import ir.shariaty.myjava.utils.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final boolean USE_LOCAL_DATABASE = false;

    private EditText editTextNationalCode;
    private EditText editTextPassword;
    private EditText editTextCaptcha;
    private ImageView imageViewCaptcha;
    private Button buttonLogin;

    private String currentCaptcha;
    private Random random = new Random();

    private AppDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenManager tokenManager = new TokenManager(this);
        if (tokenManager.isLoggedIn()) {
            navigateToCourses();
            return;
        }

        setContentView(R.layout.activity_main);

        databaseHelper = new AppDatabaseHelper(this);

        initViews();
        addNumberConverters();
        generateCaptcha();

        buttonLogin.setOnClickListener(v -> handleLogin());
        imageViewCaptcha.setOnClickListener(v -> generateCaptcha());
    }

    private void initViews() {
        editTextNationalCode = findViewById(R.id.editText_national_code);
        editTextPassword     = findViewById(R.id.editText_password);
        editTextCaptcha      = findViewById(R.id.editText_captcha);
        buttonLogin          = findViewById(R.id.button_login);
        imageViewCaptcha     = findViewById(R.id.imageView_captcha);
    }

    private void addNumberConverters() {
        editTextNationalCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String converted = convertPersianToEnglish(s.toString());
                if (!converted.equals(s.toString())) {
                    editTextNationalCode.setText(converted);
                    editTextNationalCode.setSelection(converted.length());
                }
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String converted = convertPersianToEnglish(s.toString());
                if (!converted.equals(s.toString())) {
                    editTextPassword.setText(converted);
                    editTextPassword.setSelection(converted.length());
                }
            }
        });

        editTextCaptcha.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String converted = convertPersianToEnglish(s.toString());
                if (!converted.equals(s.toString())) {
                    editTextCaptcha.setText(converted);
                    editTextCaptcha.setSelection(converted.length());
                }
            }
        });
    }

    private String convertPersianToEnglish(String input) {
        if (TextUtils.isEmpty(input)) return input;

        char[] persianDigits = {'۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};
        char[] englishDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            boolean found = false;
            for (int i = 0; i < persianDigits.length; i++) {
                if (c == persianDigits[i]) {
                    result.append(englishDigits[i]);
                    found = true;
                    break;
                }
            }
            if (!found) result.append(c);
        }
        return result.toString();
    }

    private void generateCaptcha() {
        int captchaNum = 1000 + random.nextInt(9000);
        currentCaptcha = String.valueOf(captchaNum);

        android.graphics.Bitmap captchaBitmap = android.graphics.Bitmap.createBitmap(
                120, 48, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(captchaBitmap);
        android.graphics.Paint paint = new android.graphics.Paint();

        canvas.drawColor(android.graphics.Color.parseColor("#D9D9D9"));

        paint.setColor(android.graphics.Color.BLACK);
        paint.setTextSize(32);
        paint.setTextAlign(android.graphics.Paint.Align.CENTER);
        canvas.drawText(currentCaptcha, 60, 35, paint);

        paint.setColor(android.graphics.Color.GRAY);
        paint.setStrokeWidth(2);
        for (int i = 0; i < 4; i++) {
            float startX = random.nextInt(120);
            float startY = random.nextInt(48);
            float endX   = random.nextInt(120);
            float endY   = random.nextInt(48);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }

        imageViewCaptcha.setImageBitmap(captchaBitmap);
    }

    private void handleLogin() {
        String nationalCode = editTextNationalCode.getText().toString().trim();
        String password     = editTextPassword.getText().toString().trim();
        String captchaInput = convertPersianToEnglish(editTextCaptcha.getText().toString().trim());

        if (!validateInputs(nationalCode, password, captchaInput)) {
            return;
        }

        if (USE_LOCAL_DATABASE) {
            loginWithLocalDatabase(nationalCode, password);
        } else {
            loginWithApi(nationalCode, password);
        }
    }

    private boolean validateInputs(String nationalCode, String password, String captchaInput) {
        if (TextUtils.isEmpty(nationalCode)) {
            Toast.makeText(this, "لطفاً کد ملی را وارد کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "لطفاً رمز عبور را وارد کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(captchaInput)) {
            Toast.makeText(this, "لطفاً کد امنیتی را وارد کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!captchaInput.equals(currentCaptcha)) {
            Toast.makeText(this, "کد امنیتی اشتباه است", Toast.LENGTH_SHORT).show();
            generateCaptcha();
            editTextCaptcha.setText("");
            return false;
        }
        return true;
    }

    private void loginWithLocalDatabase(String nationalCode, String password) {
        if (databaseHelper.checkUser(nationalCode, password)) {
            Toast.makeText(this, "ورود موفق", Toast.LENGTH_SHORT).show();
            navigateToConferences();
        } else {
            Toast.makeText(this, "کد ملی یا رمز عبور اشتباه است", Toast.LENGTH_SHORT).show();
            generateCaptcha();
            editTextCaptcha.setText("");
        }
    }

    private void loginWithApi(String username, String password) {
        buttonLogin.setEnabled(false);
        ApiService apiService = RetrofitClient.getInstance(MainActivity.this).getApiService();

        LoginModels.LoginRequest request = new LoginModels.LoginRequest(username, password);

        apiService.authenticate(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginModels.LoginResponse> call, @NonNull Response<LoginModels.LoginResponse> response) {
                buttonLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {

                    LoginModels.AuthResult result = response.body().getResult();
                    String accessToken = result.getAccessToken();
                    String refreshToken = result.getRefreshToken();
                    long expireInSeconds = result.getExpireInSeconds();

                    TokenManager tokenManager = new TokenManager(MainActivity.this);
                    tokenManager.saveTokens(accessToken, refreshToken, expireInSeconds);

                    Toast.makeText(MainActivity.this, "خوش آمدید!", Toast.LENGTH_SHORT).show();

                    navigateToCourses();

                } else {
                    Toast.makeText(MainActivity.this, "اطلاعات ورود اشتباه است", Toast.LENGTH_SHORT).show();
                    generateCaptcha();
                    editTextCaptcha.setText("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginModels.LoginResponse> call, @NonNull Throwable t) {
                buttonLogin.setEnabled(true);
                Toast.makeText(MainActivity.this, "خطای شبکه", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToCourses() {
        Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToConferences() {
        Intent intent = new Intent(MainActivity.this, ConferencesActivity.class);
        startActivity(intent);
        finish();
    }
}