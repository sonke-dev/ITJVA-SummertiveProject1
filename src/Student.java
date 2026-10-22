import java.util.Objects;

/**
 * 1.1 - Student class with studentID and name attributes.
 * equals() and hashCode() are overridden based on studentID
 * to prevent duplicate students in collections.
 */
public class Student {

    private int studentID;
    private String name;

    // Constructor
    public Student(int studentID, String name) {
        this.studentID = studentID;
        this.name = name;
    }

    // Getters
    public int getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Two students are equal if they share the same studentID.
     * This prevents duplicates in HashSet and HashMap-based collections.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentID == student.studentID;
    }

    /**
     * hashCode is based solely on studentID so that equal students
     * produce the same hash bucket in hash-based collections.
     */
    @Override
    public int hashCode() {
        return Objects.hash(studentID);
    }

    @Override
    public String toString() {
        return "Student{ID=" + studentID + ", Name='" + name + "'}";
    }
}