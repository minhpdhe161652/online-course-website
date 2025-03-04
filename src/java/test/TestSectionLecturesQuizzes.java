package test;

import dao.CourseSectionDAO;
import dao.LectureDAO;
import dao.QuizzesDAO;
import model.CourseSection;
import model.Lecture;
import model.Quizzes;

import java.util.List;

public class TestSectionLecturesQuizzes {
    public static void main(String[] args) {
        int courseId = 1; // Replace with a valid course ID

        // Test CourseSectionDAO
        CourseSectionDAO courseSectionDAO = new CourseSectionDAO();
        List<CourseSection> sections = courseSectionDAO.getSectionsByCourseId(courseId);
        System.out.println("Sections for course ID " + courseId + ":");
        for (CourseSection section : sections) {
            System.out.println("Section ID: " + section.getSectionID());
            System.out.println("Section Name: " + section.getSectionName());
            System.out.println("Section Description: " + section.getSectionDescription());
            System.out.println("Section Order: " + section.getSectionOrder());
            System.out.println("-----");
        }

        // Test LectureDAO
        LectureDAO lectureDAO = new LectureDAO();
        List<Lecture> lectures = lectureDAO.getLecturesByCourseId(courseId);
        System.out.println("Lectures for course ID " + courseId + ":");
        for (Lecture lecture : lectures) {
            System.out.println("Lecture ID: " + lecture.getLectureID());
            System.out.println("Lecture Name: " + lecture.getLectureName());
            System.out.println("Lecture Description: " + lecture.getLectureDescription());
            System.out.println("Section ID: " + lecture.getSectionID());
            System.out.println("-----");
        }

        // Test QuizzesDAO
        QuizzesDAO quizzesDAO = new QuizzesDAO();
        for (CourseSection section : sections) {
            List<Quizzes> quizzes = quizzesDAO.getQuizzesBySectionId(section.getSectionID());
            System.out.println("Quizzes for section ID " + section.getSectionID() + ":");
            for (Quizzes quiz : quizzes) {
                System.out.println("Quiz ID: " + quiz.getQuizID());
                System.out.println("Quiz Name: " + quiz.getQuizName());
                System.out.println("Quiz Description: " + quiz.getQuizDescription());
                System.out.println("Section ID: " + quiz.getSectionID());
                System.out.println("-----");
            }
        }
    }
}
