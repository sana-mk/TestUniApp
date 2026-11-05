package ir.shariaty.myjava.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CourseModels {

    public static class CourseApiResponse {
        @SerializedName("result")
        private CourseResult result;

        public CourseResult getResult() {
            return result;
        }
    }

    public static class CourseResult {
        @SerializedName("totalCount")
        private int totalCount;

        @SerializedName("items")
        private List<CourseItem> items;

        public int getTotalCount() { return totalCount; }
        public List<CourseItem> getItems() { return items; }
    }

    public static class CourseItem {
        @SerializedName("course")
        private Course course;

        @SerializedName("reshteOnvan")
        private String reshteOnvan;

        public Course getCourse() { return course; }
        public String getReshteOnvan() { return reshteOnvan; }

        //MockData برای
        public void setCourse(Course course) { this.course = course; }
        public void setReshteOnvan(String reshteOnvan) { this.reshteOnvan = reshteOnvan; }
    }

    public static class Course {
        @SerializedName("id")
        private int id;

        @SerializedName("courseCode")
        private String courseCode;

        @SerializedName("onvan")
        private String onvan;

        @SerializedName("tedadVahed")
        private int tedadVahed;

        @SerializedName("academicDegree")
        private int academicDegree;

        @SerializedName("isActive")
        private boolean isActive;

        @SerializedName("reshteId")
        private int reshteId;

        public int getId() { return id; }
        public String getCourseCode() { return courseCode; }
        public String getOnvan() { return onvan; }
        public int getTedadVahed() { return tedadVahed; }
        public int getAcademicDegree() { return academicDegree; }
        public boolean isActive() { return isActive; }
        public int getReshteId() { return reshteId; }

        //MockData برای
        public void setId(int id) { this.id = id; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public void setOnvan(String onvan) { this.onvan = onvan; }
        public void setTedadVahed(int tedadVahed) { this.tedadVahed = tedadVahed; }
        public void setAcademicDegree(int academicDegree) { this.academicDegree = academicDegree; }
        public void setActive(boolean active) { isActive = active; }
        public void setReshteId(int reshteId) { this.reshteId = reshteId; }
    }
}