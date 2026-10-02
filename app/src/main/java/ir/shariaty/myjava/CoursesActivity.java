package ir.shariaty.myjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.shariaty.myjava.adapter.CourseAdapter;
import ir.shariaty.myjava.api.ApiService;
import ir.shariaty.myjava.api.RetrofitClient;
import ir.shariaty.myjava.model.CourseModels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesActivity extends BaseActivity {

    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        setupBottomNav(R.id.nav_home);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setImageResource(R.drawable.logout);
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(CoursesActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        RecyclerView rvCourses = findViewById(R.id.rvCourses);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter();
        rvCourses.setAdapter(adapter);

        String token = getIntent().getStringExtra("TOKEN");
        if (token != null && !token.isEmpty()) {
            fetchCourses("Bearer " + token);
        } else {
            Toast.makeText(this, "توکن یافت نشد!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCourses(String bearerToken) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();

        apiService.getCourses(bearerToken).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CourseModels.CourseApiResponse> call, @NonNull Response<CourseModels.CourseApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {

                    int totalCount = response.body().getResult().getTotalCount();

                    if (totalCount == 0) {
                        Toast.makeText(CoursesActivity.this, "لیست دروس خالی است", Toast.LENGTH_SHORT).show();

                        //MockData برای دیدن ظاهر آیتم نتایج در صفحه
//                        CourseModels.Course mockCourse = new CourseModels.Course();
//                        mockCourse.setId(101);
//                        mockCourse.setCourseCode("CE-4521");
//                        mockCourse.setOnvan("برنامه‌نویسی موبایل پیشرفته");
//                        mockCourse.setTedadVahed(3);
//                        mockCourse.setAcademicDegree(1);
//                        mockCourse.setActive(true);
//                        mockCourse.setReshteId(50);
//
//                        CourseModels.CourseItem mockItem = new CourseModels.CourseItem();
//                        mockItem.setCourse(mockCourse);
//                        mockItem.setReshteOnvan("مهندسی کامپیوتر - نرم‌افزار");
//
//                        List<CourseModels.CourseItem> mockList = new ArrayList<>();
//                        mockList.add(mockItem);
//                        mockList.add(mockItem);
//                        adapter.submitList(mockList);

                    } else {
                        adapter.submitList(response.body().getResult().getItems());
                    }

                } else {
                    Toast.makeText(CoursesActivity.this, "خطا در دریافت لیست دروس", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CourseModels.CourseApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(CoursesActivity.this, "خطای شبکه", Toast.LENGTH_SHORT).show();
            }
        });
    }
}