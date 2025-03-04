package test;

import dao.CourseDAO;
import model.Course;

public class TestCourse {
    public static void main(String[] args) {
        CourseDAO courseDAO = new CourseDAO();
        int courseId = 1; // Replace with a valid course ID
        Course course = courseDAO.getCourseById(courseId);
        
        if (course != null) {
            System.out.println("Course ID: " + course.getCourseID());
            System.out.println("Course Name: " + course.getCourseName());
            System.out.println("Course Description: " + course.getDescription());
            System.out.println("Course Price: " + course.getPrice());
        } else {
            System.out.println("No course found with ID: " + courseId);
        }
    }
}
