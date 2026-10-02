package ir.shariaty.myjava.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ir.shariaty.myjava.R;
import ir.shariaty.myjava.model.CourseModels;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<CourseModels.CourseItem> courseList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<CourseModels.CourseItem> list) {
        this.courseList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseModels.CourseItem item = courseList.get(position);
        CourseModels.Course course = item.getCourse();

        if (course != null) {
            holder.tvTitle.setText(course.getOnvan());

            String major = item.getReshteOnvan() != null ? item.getReshteOnvan() : "نامشخص";
            holder.tvMajor.setText("رشته: " + major);

            holder.tvCode.setText("کد: " + course.getCourseCode());
            holder.tvUnits.setText("تعداد واحد: " + course.getTedadVahed());

            String degree = course.getAcademicDegree() == 1 ? "کارشناسی" :
                    course.getAcademicDegree() == 2 ? "کارشناسی ارشد" :
                            "مقطع: " + course.getAcademicDegree();
            holder.tvDegree.setText(degree);

            if (course.isActive()) {
                holder.tvStatus.setText("فعال");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else {
                holder.tvStatus.setText("غیرفعال");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMajor, tvCode, tvUnits, tvDegree, tvStatus;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvMajor = itemView.findViewById(R.id.tvCourseMajor);
            tvCode = itemView.findViewById(R.id.tvCourseCode);
            tvUnits = itemView.findViewById(R.id.tvCourseUnits);
            tvDegree = itemView.findViewById(R.id.tvCourseDegree);
            tvStatus = itemView.findViewById(R.id.tvCourseStatus);
        }
    }
}