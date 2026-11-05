package ir.shariaty.myjava;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import ir.shariaty.myjava.api.ApiService;
import ir.shariaty.myjava.api.RetrofitClient;
import ir.shariaty.myjava.database.AppDatabaseHelper;
import ir.shariaty.myjava.model.Conference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConferenceDetailActivity extends BaseActivity {

    public static final String EXTRA_CONFERENCE_ID = "conference_id";
    public static final String EXTRA_TITLE         = "title";
    public static final String EXTRA_ORGANIZER     = "organizer";
    public static final String EXTRA_SUMMARY       = "summary";
    public static final String EXTRA_ADDRESS       = "address";
    public static final String EXTRA_DATE          = "date";
    public static final String EXTRA_TIME          = "time";
    public static final String EXTRA_CAPACITY      = "capacity";
    public static final String EXTRA_NOTE          = "note";
    public static final String EXTRA_STATUS        = "status";
    public static final String EXTRA_IS_ONLINE     = "is_online";
    public static final String EXTRA_DOWNLOAD_URL  = "download_url";

    private static final boolean USE_LOCAL_DB = true;

    private TextView       tvTitle, tvOrganizer, tvSummary, tvAddress;
    private TextView       tvDate, tvTime, tvCapacity, tvNote;
    private MaterialButton btnCollaboration, btnAction;
    private View           rootView;

    private int               conferenceId;
    private Conference.Status currentStatus;
    private boolean           isOnline;
    private String            downloadUrl;

    private AppDatabaseHelper dbHelper;
    private ApiService        apiService;

    private final String authToken = "Bearer YOUR_TOKEN_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_detail);

        dbHelper   = new AppDatabaseHelper(this);
        apiService = RetrofitClient.getInstance(this).getApiService();

        bindViews();
        loadDataFromIntent();
        setupHeader(v -> goHomeAndRefresh());
        setupBottomNav(R.id.nav_home);
    }

    private void bindViews() {
        rootView         = findViewById(android.R.id.content);
        tvTitle          = findViewById(R.id.tvDetailTitle);
        tvOrganizer      = findViewById(R.id.tvDetailOrganizer);
        tvSummary        = findViewById(R.id.tvDetailSummary);
        tvAddress        = findViewById(R.id.tvDetailAddress);
        tvDate           = findViewById(R.id.tvDetailDate);
        tvTime           = findViewById(R.id.tvDetailTime);
        tvCapacity       = findViewById(R.id.tvDetailCapacity);
        tvNote           = findViewById(R.id.tvDetailNote);
        btnCollaboration = findViewById(R.id.btnCollaboration);
        btnAction        = findViewById(R.id.btnAction);
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();

        conferenceId = intent.getIntExtra(EXTRA_CONFERENCE_ID, -1);
        isOnline     = intent.getBooleanExtra(EXTRA_IS_ONLINE, false);
        downloadUrl  = intent.getStringExtra(EXTRA_DOWNLOAD_URL);

        String statusStr = intent.getStringExtra(EXTRA_STATUS);
        try {
            currentStatus = Conference.Status.valueOf(statusStr);
        } catch (Exception e) {
            currentStatus = Conference.Status.REGISTERING;
        }

        tvTitle.setText(safe(intent.getStringExtra(EXTRA_TITLE), "عنوان همایش"));
        tvOrganizer.setText("ارائه دهنده: " + safe(intent.getStringExtra(EXTRA_ORGANIZER), "—"));
        tvSummary.setText(safe(intent.getStringExtra(EXTRA_SUMMARY), "—"));
        tvAddress.setText(safe(intent.getStringExtra(EXTRA_ADDRESS), "—"));
        tvDate.setText("تاریخ برگزاری: " + safe(intent.getStringExtra(EXTRA_DATE), "—"));
        tvTime.setText("ساعت: " + safe(intent.getStringExtra(EXTRA_TIME), "—"));

        int capacity = intent.getIntExtra(EXTRA_CAPACITY, -1);
        if (capacity >= 0) {
            tvCapacity.setText("ظرفیت باقی مانده: " + capacity + " نفر");
            tvCapacity.setVisibility(View.VISIBLE);
        } else {
            tvCapacity.setVisibility(View.GONE);
        }

        String note = intent.getStringExtra(EXTRA_NOTE);
        if (!TextUtils.isEmpty(note)) {
            tvNote.setText(note);
            tvNote.setVisibility(View.VISIBLE);
        } else {
            tvNote.setVisibility(View.GONE);
        }

        configureActionButton();
        btnCollaboration.setOnClickListener(v -> openCollaborationTicket());
    }

    private void configureActionButton() {
        switch (currentStatus) {
            case REGISTERING:
                btnAction.setText("ثبت نام در همایش");
                btnAction.setBackgroundTintList(tint(R.color.status_registered));
                btnAction.setEnabled(true);
                btnAction.setAlpha(1f);
                btnAction.setOnClickListener(v -> doRegister());
                btnCollaboration.setVisibility(View.VISIBLE);
                break;

            case REGISTERED:
                btnAction.setText("لغو ثبت‌نام");
                btnAction.setBackgroundTintList(tint(R.color.status_registering));
                btnAction.setEnabled(true);
                btnAction.setAlpha(1f);
                btnAction.setOnClickListener(v -> doCancel());
                btnCollaboration.setVisibility(View.GONE);
                break;

            case FULL:
                btnAction.setText("اتمام ظرفیت");
                btnAction.setBackgroundTintList(tint(R.color.status_full));
                btnAction.setEnabled(false);
                btnAction.setAlpha(0.7f);
                btnCollaboration.setVisibility(View.GONE);
                break;

            case EXPIRED:
                if (isOnline && !TextUtils.isEmpty(downloadUrl)) {
                    btnAction.setText("دانلود فیلم ضبط شده");
                    btnAction.setBackgroundTintList(tint(R.color.status_expired));
                    btnAction.setEnabled(true);
                    btnAction.setAlpha(1f);
                    btnAction.setOnClickListener(v -> openDownload(downloadUrl));
                } else {
                    btnAction.setText("همایش منقضی شده");
                    btnAction.setBackgroundTintList(tint(R.color.status_expired));
                    btnAction.setEnabled(false);
                    btnAction.setAlpha(0.7f);
                }
                btnCollaboration.setVisibility(View.GONE);
                break;
        }
    }

    private void openCollaborationTicket() {
        Intent intent = new Intent(this, CollaborationTicketActivity.class);
        intent.putExtra(CollaborationTicketActivity.EXTRA_CONFERENCE_ID, conferenceId);
        intent.putExtra(CollaborationTicketActivity.EXTRA_CONFERENCE_TITLE,
                getIntent().getStringExtra(EXTRA_TITLE));
        startActivity(intent);
    }

    private void doRegister() {
        btnAction.setEnabled(false);

        if (USE_LOCAL_DB) {
            dbHelper.updateConferenceStatus(conferenceId, Conference.Status.REGISTERED);
            currentStatus = Conference.Status.REGISTERED;
            showResultDialog(true, false);
            return;
        }

        apiService.registerConference(conferenceId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            dbHelper.updateConferenceStatus(conferenceId, Conference.Status.REGISTERED);
                            currentStatus = Conference.Status.REGISTERED;
                            showResultDialog(true, false);
                        } else {
                            btnAction.setEnabled(true);
                            showResultDialog(false, false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnAction.setEnabled(true);
                        showNetworkError();
                    }
                });
    }

    private void doCancel() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("لغو ثبت‌نام")
                .setMessage("آیا از لغو ثبت‌نام در این همایش اطمینان دارید؟")
                .setPositiveButton("بله، لغو کنید", (d, w) -> performCancel())
                .setNegativeButton("خیر", null)
                .show();
    }

    private void performCancel() {
        btnAction.setEnabled(false);

        if (USE_LOCAL_DB) {
            dbHelper.updateConferenceStatus(conferenceId, Conference.Status.REGISTERING);
            currentStatus = Conference.Status.REGISTERING;
            showResultDialog(true, true);
            return;
        }

        apiService.cancelRegistration(authToken, conferenceId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            dbHelper.updateConferenceStatus(conferenceId, Conference.Status.REGISTERING);
                            currentStatus = Conference.Status.REGISTERING;
                            showResultDialog(true, true);
                        } else {
                            btnAction.setEnabled(true);
                            showResultDialog(false, true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnAction.setEnabled(true);
                        showNetworkError();
                    }
                });
    }

    private void showResultDialog(boolean success, boolean isCancel) {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_result, null);

        TextView       tvIcon = v.findViewById(R.id.tvResultIcon);
        TextView       tvMsg  = v.findViewById(R.id.tvResultMessage);
        TextView       tvSub  = v.findViewById(R.id.tvResultSub);
        MaterialButton btnOk  = v.findViewById(R.id.btnResultOk);
        View           bar    = v.findViewById(R.id.cardResultBg);

        if (success) {
            tvIcon.setText("✓");
            if (!isCancel) {
                tvMsg.setText("ثبت‌نام موفقیت‌آمیز بود!");
                tvSub.setText("شما با موفقیت در این همایش ثبت‌نام شدید.");
                bar.setBackgroundColor(color(R.color.status_registering));
            } else {
                tvMsg.setText("لغو ثبت‌نام انجام شد");
                tvSub.setText("ثبت‌نام شما با موفقیت لغو گردید.");
                bar.setBackgroundColor(color(R.color.status_full));
            }
            btnOk.setBackgroundTintList(tint(R.color.primary_dark));
        } else {
            tvIcon.setText("✗");
            tvMsg.setText("عملیات ناموفق بود");
            tvSub.setText("مشکلی پیش آمد. لطفاً دوباره تلاش کنید.");
            bar.setBackgroundColor(color(R.color.status_expired));
            btnOk.setBackgroundTintList(tint(R.color.status_expired));
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(v)
                .setCancelable(false)
                .create();

        btnOk.setOnClickListener(btn -> {
            dialog.dismiss();
            if (success) goHomeAndRefresh();
        });

        dialog.show();
    }

    private void openDownload(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Toast.makeText(this, "خطا در باز کردن لینک دانلود", Toast.LENGTH_SHORT).show();
        }
    }

    private void goHomeAndRefresh() {
        Intent intent = new Intent(this, ConferencesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ConferencesActivity.EXTRA_REFRESH, true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void navigateToHome() {
        goHomeAndRefresh();
    }

    private ColorStateList tint(int colorRes) {
        return ColorStateList.valueOf(ContextCompat.getColor(this, colorRes));
    }

    private int color(int colorRes) {
        return ContextCompat.getColor(this, colorRes);
    }

    private String safe(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
    }

    private void showNetworkError() {
        Snackbar.make(rootView, "خطای شبکه. اتصال اینترنت را بررسی کنید.", Snackbar.LENGTH_LONG).show();
    }
}
