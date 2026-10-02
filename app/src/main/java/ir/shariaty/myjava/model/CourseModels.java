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
    }

    public static class Course {
        @SerializedName("courseCode")
        private String courseCode;

        @SerializedName("onvan")
        private String onvan;

        public String getCourseCode() { return courseCode; }
        public String getOnvan() { return onvan; }
    }
}