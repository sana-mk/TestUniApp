package ir.shariaty.myjava;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import ir.shariaty.myjava.api.ApiService;
import ir.shariaty.myjava.api.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class CollaborationTicketActivity extends BaseActivity {

    public static final String EXTRA_CONFERENCE_ID    = "conference_id";
    public static final String EXTRA_CONFERENCE_TITLE = "conference_title";

    private static final boolean USE_LOCAL_DB = true;

    private EditText       etTicketTitle;
    private EditText       etTicketMessage;
    private Spinner        spinnerUnit;
    private Spinner        spinnerPriority;
    private TextView       tvTicketNumber;
    private MaterialButton btnSend;

    private int        conferenceId;
    private ApiService apiService;
    private String     authToken = "Bearer YOUR_TOKEN_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ticket);

        apiService   = RetrofitClient.getInstance(this).getApiService();
        conferenceId = getIntent().getIntExtra(EXTRA_CONFERENCE_ID, -1);
        String conferenceTitle = getIntent().getStringExtra(EXTRA_CONFERENCE_TITLE);

        bindViews();
        setupSpinners();
        setupTicketNumber(conferenceTitle);
        setupHeader(true);
        setupBottomNav(R.id.nav_home);

        btnSend.setOnClickListener(v -> submitTicket());
    }

    private void bindViews() {
        etTicketTitle   = findViewById(R.id.et_ticket_title);
        etTicketMessage = findViewById(R.id.etTicketMessage);
        spinnerUnit     = findViewById(R.id.spinner_unit);
        spinnerPriority = findViewById(R.id.spinner_priority);
        tvTicketNumber  = findViewById(R.id.tv_ticket_number);
        btnSend         = findViewById(R.id.btnSendTicket);
    }

    private void setupSpinners() {
        String[] units = {"انتخاب واحد", "آموزش", "پژوهش", "فرهنگی", "مالی", "علمی", "فنی"};
        spinnerUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, units));

        String[] priorities = {"انتخاب اولویت", "کم", "متوسط", "فوری"};
        spinnerPriority.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, priorities));
    }

    private void setupTicketNumber(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTicketNumber.setText("تیکت همایش: " + title);
        } else {
            tvTicketNumber.setText("تیکت شماره: " + conferenceId);
        }
    }

    @Override
    protected void navigateToHome() {
        Intent intent = new Intent(this, ConferencesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void submitTicket() {
        String title   = etTicketTitle.getText().toString().trim();
        String message = etTicketMessage.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etTicketTitle.setError("عنوان تیکت را وارد کنید");
            etTicketTitle.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(message)) {
            etTicketMessage.setError("متن پیام را وارد کنید");
            etTicketMessage.requestFocus();
            return;
        }

        btnSend.setEnabled(false);

        if (USE_LOCAL_DB) {
            Toast.makeText(this, "تیکت با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("title",         title);
            json.put("message",       message);
            json.put("unit",          spinnerUnit.getSelectedItem().toString());
            json.put("priority",      spinnerPriority.getSelectedItem().toString());
            json.put("conference_id", conferenceId);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json; charset=utf-8"));

            apiService.sendCollaborationTicket(conferenceId, body)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            btnSend.setEnabled(true);
                            if (response.isSuccessful()) {
                                Toast.makeText(CollaborationTicketActivity.this,
                                        "تیکت با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CollaborationTicketActivity.this,
                                        "ارسال ناموفق بود. دوباره تلاش کنید", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            btnSend.setEnabled(true);
                            Toast.makeText(CollaborationTicketActivity.this,
                                    "خطای شبکه. اتصال اینترنت را بررسی کنید", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (JSONException e) {
            btnSend.setEnabled(true);
            Toast.makeText(this, "خطا در ارسال", Toast.LENGTH_SHORT).show();
        }
    }
}
