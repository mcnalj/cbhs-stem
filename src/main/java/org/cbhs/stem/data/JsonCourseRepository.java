package org.cbhs.stem.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.cbhs.stem.model.Course;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Loads courses from data/courses.json on the classpath.
 */
public class JsonCourseRepository implements CourseRepository {
    private static final Map<String, Integer> DEPARTMENT_ORDER = Map.of(
            "Science", 0,
            "Math", 1,
            "Computer Science", 2
    );

    private final List<Course> courses;

    public JsonCourseRepository() {
        this.courses = loadCourses();
    }

    @Override
    public List<Course> findAll() {
        List<Course> sorted = new ArrayList<>(courses);
        sorted.sort(Comparator
                .comparingInt((Course c) -> DEPARTMENT_ORDER.getOrDefault(c.getDepartment(), 99))
                .thenComparing(Course::getName));
        return sorted;
    }

    @Override
    public List<Course> findByDepartment(String department) {
        List<Course> filtered = new ArrayList<>();
        for (Course course : courses) {
            if (course.getDepartment().equalsIgnoreCase(department)) {
                filtered.add(course);
            }
        }
        return filtered;
    }

    private List<Course> loadCourses() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("data/courses.json");
        if (input == null) {
            throw new IllegalStateException("Could not find data/courses.json");
        }

        Gson gson = new Gson();
        Type type = new TypeToken<CourseDataFile>() {}.getType();
        CourseDataFile data = gson.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), type);
        return data.courses;
    }

    private static class CourseDataFile {
        private List<Course> courses;
    }
}
