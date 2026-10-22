import java.util.*;

/**
 * 1.2 - AcademicManager handles all student and enrollment management.
 *
 * Collection choices:
 * - HashMap<Integer, Student> students:
 *     Key = studentID for O(1) fast lookup by ID. (Requirement: "Fast retrieval by ID")
 *
 * - HashMap<String, Set<Student>> courseEnrollments:
 *     Key = courseName, Value = HashSet<Student> to store enrolled students.
 *     HashSet prevents duplicate enrollments (Student.equals/hashCode on studentID).
 *     HashMap allows O(1) course lookup.
 */
public class AcademicManager {

    // 1.2
    private HashMap<Integer, Student> students;

    // 1.2
    private HashMap<String, Set<Student>> courseEnrollments;

    // Constructor
    public AcademicManager() {
        students = new HashMap<>();
        courseEnrollments = new HashMap<>();
    }

    // -------------------------------------------------------------------------
    // 1.3
    // -------------------------------------------------------------------------

    /**
     * Adds a student to the system.
     * If a student with the same studentID already exists, the entry is rejected.
     *
     * @param student The Student object to add.
     */
    public void addStudent(Student student) {
        if (students.containsKey(student.getStudentID())) {
            System.out.println("Student with ID " + student.getStudentID()
                    + " already exists. Duplicate not added.");
        } else {
            students.put(student.getStudentID(), student);
            System.out.println("Student added: " + student);
        }
    }

    // -------------------------------------------------------------------------
    // 1.4
    // -------------------------------------------------------------------------

    /**
     * Enrolls an existing student into a course.
     * - Prevents enrollment if student does not exist in the system.
     * - Prevents duplicate enrollments using a HashSet.
     *
     * @param studentID  The ID of the student to enroll.
     * @param courseName The name of the course to enroll the student into.
     */
    public void enrollStudent(int studentID, String courseName) {
        // Check student exists
        if (!students.containsKey(studentID)) {
            System.out.println("Student with ID " + studentID
                    + " does not exist. Cannot enroll.");
            return;
        }

        Student student = students.get(studentID);

        // If course doesn't exist yet, creating a new HashSet for it
        courseEnrollments.putIfAbsent(courseName, new HashSet<>());

        Set<Student> enrolled = courseEnrollments.get(courseName);

        // HashSet.add() returns false if element already exists
        if (!enrolled.add(student)) {
            System.out.println("Student " + student.getName()
                    + " is already enrolled in " + courseName + ".");
        } else {
            System.out.println("Student " + student.getName()
                    + " enrolled in " + courseName + ".");
        }
    }

    // -------------------------------------------------------------------------
    // 1.5
    // -------------------------------------------------------------------------

    /**
     * Retrieves all students, sorts them alphabetically by name using a
     * Comparator, and displays the sorted list.
     */
    public void displaySortedStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }

        // Collecting all students into a list for sorting
        List<Student> sortedList = new ArrayList<>(students.values());

        // Sorting alphabetically by name using Comparator
        sortedList.sort(Comparator.comparing(Student::getName));

        System.out.println("\n--- Sorted Student List (Alphabetical) ---");
        for (Student s : sortedList) {
            System.out.println("  ID: " + s.getStudentID() + " | Name: " + s.getName());
        }
        System.out.println("------------------------------------------\n");
    }

    // -------------------------------------------------------------------------
    // 1.6
    // -------------------------------------------------------------------------

    /**
     * Displays the number of students enrolled in each course.
     */
    public void displayEnrollmentCounts() {
        if (courseEnrollments.isEmpty()) {
            System.out.println("No courses with enrollments found.");
            return;
        }

        System.out.println("\n--- Enrollment Count Per Course ---");
        for (Map.Entry<String, Set<Student>> entry : courseEnrollments.entrySet()) {
            System.out.println("  Course: " + entry.getKey()
                    + " | Students Enrolled: " + entry.getValue().size());
        }
        System.out.println("------------------------------------\n");
    }

    // -------------------------------------------------------------------------
    // Helper: Display all students in a course
    // -------------------------------------------------------------------------

    public void displayStudentsInCourse(String courseName) {
        if (!courseEnrollments.containsKey(courseName)) {
            System.out.println("Course '" + courseName + "' not found.");
            return;
        }
        System.out.println("\n--- Students in " + courseName + " ---");
        for (Student s : courseEnrollments.get(courseName)) {
            System.out.println("  " + s);
        }
        System.out.println("------------------------------\n");
    }

    // -------------------------------------------------------------------------
    // Helper: Display all courses a student is enrolled in
    // -------------------------------------------------------------------------

    public void displayCoursesForStudent(int studentID) {
        if (!students.containsKey(studentID)) {
            System.out.println("Student ID " + studentID + " not found.");
            return;
        }
        Student target = students.get(studentID);
        System.out.println("\n--- Courses for " + target.getName() + " ---");
        for (Map.Entry<String, Set<Student>> entry : courseEnrollments.entrySet()) {
            if (entry.getValue().contains(target)) {
                System.out.println("  " + entry.getKey());
            }
        }
        System.out.println("------------------------------\n");
    }

    // Getter for GUI access
    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    public HashMap<String, Set<Student>> getCourseEnrollments() {
        return courseEnrollments;
    }
}