package org.example;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.io.*;

public class CollegeAdmissions {

    public static void main(String[] args) {

        try {
            URL studentResourceUrl = CollegeAdmissions.class.getResource("/students.csv");
            assert studentResourceUrl != null;
            File studentFile = new File(studentResourceUrl.toURI());

            URL collegeResourceUrl = CollegeAdmissions.class.getResource("/colleges.csv");
            assert collegeResourceUrl != null;
            File collegeFile = new File(collegeResourceUrl.toURI());

            List<Student> students = createStudentsFromCsv(studentFile.getPath());
            List<College> colleges = createCollegesFromCsv(collegeFile.getPath());

            updateStudentPreferences(studentFile.getPath(), students, colleges);
            updateCollegePreferences(collegeFile.getPath(), colleges, students);

            System.out.println("Executing the SODA Algorithm:");
            MatchMaker matchMaker = new MatchMaker(students, colleges);
            matchMaker.sodaMakeMatches();

            for(Student s : students){
                System.out.println(s + " is matched with " + s.getMatch());
            }

            for(College c : colleges){
                System.out.println(c + " is matched with " + c.getMatches());
            }

        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Student> createStudentsFromCsv(String filename) throws FileNotFoundException {
        List<Student> students = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) { // skip the first line
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                Student student = new Student(parts[0]);
                students.add(student);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file " + filename + " was not found. Please check the file path and try again.");
        }
        return students;
    }

    public static List<College> createCollegesFromCsv(String filename) throws FileNotFoundException {
        List<College> colleges = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) { // skip the first line
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                College college = new College(parts[0], Integer.parseInt(parts[1]));
                colleges.add(college);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file " + filename + " was not found. Please check the file path and try again.");
        }
        return colleges;
    }

    public static void updateStudentPreferences(String filename, List<Student> students, List<College> colleges) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) { // skip the first line
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                Student student = findStudentByName(students, parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    College college = findCollegeByName(colleges, parts[i]);
                    if (college != null) {
                        assert student != null;
                        student.insertLeastPreferredCollege(college);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file " + filename + " was not found. Please check the file path and try again.");
        }
    }

    public static void updateCollegePreferences(String filename, List<College> colleges, List<Student> students) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) { // skip the first line
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                College college = findCollegeByName(colleges, parts[0]);
                for (int i = 2; i < parts.length; i++) {
                    Student student = findStudentByName(students, parts[i]);
                    if (student != null) {
                        assert college != null;
                        college.insertLeastPreferredStudent(student);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file " + filename + " was not found. Please check the file path and try again.");
        }
    }

    private static College findCollegeByName(List<College> colleges, String name) {
        for (College college : colleges) {
            if (college.getName().equals(name)) {
                return college;
            }
        }
        return null;
    }

    private static Student findStudentByName(List<Student> students, String name) {
        for (Student student : students) {
            if (student.getName().equals(name)) {
                return student;
            }
        }
        return null;
    }

}
