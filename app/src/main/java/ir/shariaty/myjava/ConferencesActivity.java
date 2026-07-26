package ir.shariaty.myjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.shariaty.myjava.adapter.ConferenceAdapter;
import ir.shariaty.myjava.database.AppDatabaseHelper;
import ir.shariaty.myjava.model.Conference;

public class ConferencesActivity extends BaseActivity {

    public static final String EXTRA_REFRESH = "refresh";

    private RecyclerView rvConferences;
    private ConferenceAdapter adapter;
    private AppDatabaseHelper databaseHelper;
    private List<Conference> allConferences = new ArrayList<>();
    private EditText etSearch;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);

        databaseHelper = new AppDatabaseHelper(this);
        initViews();
        setupRecyclerView();
        loadConferences();
        setupSearch();
        setupFocusBehavior();
        setupBottomNav(R.id.nav_home);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(EXTRA_REFRESH, false)) {
            loadConferences();
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) filterConferences(query);
        }
    }

    private void initViews() {
        rvConferences = findViewById(R.id.rvConferences);
        etSearch      = findViewById(R.id.Searches);
        ivBack        = findViewById(R.id.ivBack);

        etSearch.clearFocus();

        ivBack.setOnClickListener(v -> {
            hideKeyboardAndClearFocus();
            Intent intent = new Intent(ConferencesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupFocusBehavior() {
        etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showKeyboard(v);
            else hideKeyboard(v);
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();
            if (currentFocus instanceof EditText) {
                int[] location = new int[2];
                currentFocus.getLocationOnScreen(location);
                float x = event.getRawX();
                float y = event.getRawY();
                boolean inside =
                        x >= location[0] && x <= location[0] + currentFocus.getWidth() &&
                        y >= location[1] && y <= location[1] + currentFocus.getHeight();
                if (!inside) hideKeyboardAndClearFocus();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void hideKeyboardAndClearFocus() {
        etSearch.clearFocus();
        hideKeyboard(etSearch);
    }

    private void setupRecyclerView() {
        adapter = new ConferenceAdapter();
        rvConferences.setLayoutManager(new LinearLayoutManager(this));
        rvConferences.setAdapter(adapter);

        adapter.setOnFavoriteClickListener(conference -> {
            conference.setFavorite(!conference.isFavorite());
            databaseHelper.updateFavoriteStatus(conference);
            adapter.notifyDataSetChanged();
        });

        adapter.setOnConferenceClickListener(this::openDetailScreen);
    }

    private void loadConferences() {
        allConferences = databaseHelper.getAllConferences();
        adapter.submitList(new ArrayList<>(allConferences));
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterConferences(s.toString());
            }
        });
    }

    private void filterConferences(String query) {
        List<Conference> filtered = new ArrayList<>();
        for (Conference c : allConferences) {
            if (c.getTitle().contains(query)
                    || c.getOrganizer().contains(query)
                    || c.getPlace().contains(query)) {
                filtered.add(c);
            }
        }
        adapter.submitList(filtered);
    }

    private void openDetailScreen(Conference conference) {
        Intent intent = new Intent(this, ConferenceDetailActivity.class);
        intent.putExtra(ConferenceDetailActivity.EXTRA_CONFERENCE_ID, conference.getId());
        intent.putExtra(ConferenceDetailActivity.EXTRA_TITLE,        conference.getTitle());
        intent.putExtra(ConferenceDetailActivity.EXTRA_ORGANIZER,    conference.getOrganizer());
        intent.putExtra(ConferenceDetailActivity.EXTRA_SUMMARY,      conference.getSummary());
        intent.putExtra(ConferenceDetailActivity.EXTRA_ADDRESS,      conference.getAddress());
        intent.putExtra(ConferenceDetailActivity.EXTRA_DATE,         conference.getDate());
        intent.putExtra(ConferenceDetailActivity.EXTRA_TIME,         conference.getTime());
        intent.putExtra(ConferenceDetailActivity.EXTRA_CAPACITY,     conference.getRemainingCapacity());
        intent.putExtra(ConferenceDetailActivity.EXTRA_NOTE,         conference.getNote());
        intent.putExtra(ConferenceDetailActivity.EXTRA_STATUS,       conference.getStatus().name());
        intent.putExtra(ConferenceDetailActivity.EXTRA_IS_ONLINE,    conference.isOnline());
        intent.putExtra(ConferenceDetailActivity.EXTRA_DOWNLOAD_URL, conference.getDownloadUrl());
        startActivity(intent);
    }
}
