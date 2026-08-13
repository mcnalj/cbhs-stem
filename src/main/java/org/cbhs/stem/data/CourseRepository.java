package org.cbhs.stem.data;

import org.cbhs.stem.model.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> findAll();

    List<Course> findByDepartment(String department);
}
