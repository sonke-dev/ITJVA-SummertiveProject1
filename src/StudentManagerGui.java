import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StudentManagerGUI - A Java Swing GUI for the AIAMS system.
 * Matches the sample output layout: ID, Name, Mobile fields with
 * Add, View Stud, Search, Delete buttons.
 */
public class StudentManagerGui extends JFrame {

    //  Backend
    private AcademicManager manager;

    //  GUI Components
    private JTextField idField;
    private JTextField nameField;
    private JTextField mobileField;
    private JTextArea outputArea;


    private HashMap<Integer, String> mobileStore = new HashMap<>();

    public StudentManagerGui() {
        manager = new AcademicManager();
        seedData();
        initUI();
    }

    /**
     * Seed some demo data so the system isn't empty on launch.
     */
    private void seedData() {
        manager.addStudent(new Student(1001, "Alice Nkosi"));
        manager.addStudent(new Student(1002, "Brian Dlamini"));
        manager.addStudent(new Student(1003, "Carla Botha"));
        mobileStore.put(1001, "0821234567");
        mobileStore.put(1002, "0839876543");
        mobileStore.put(1003, "0711122334");

        manager.enrollStudent(1001, "Java Programming");
        manager.enrollStudent(1002, "Java Programming");
        manager.enrollStudent(1003, "Data Structures");
        manager.enrollStudent(1001, "Data Structures");
    }

    /**
     * Build and lay out all UI components, matching the sample screenshot.
     */
    private void initUI() {
        setTitle("Student Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 12, 10, 12));
        mainPanel.setBackground(Color.WHITE);

        //  Form Panel (ID, Name, Mobile)
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 6));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel idLabel    = new JLabel("ID:");
        JLabel nameLabel  = new JLabel("Name:");
        JLabel mobileLabel = new JLabel("Mobile:");

        idField     = new JTextField();
        nameField   = new JTextField();
        mobileField = new JTextField();


        styleField(idField);
        styleField(nameField);
        styleField(mobileField);

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(mobileLabel);
        formPanel.add(mobileField);

        //  Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn    = new JButton("Add");
        JButton viewBtn   = new JButton("View Stud");
        JButton searchBtn = new JButton("Search");
        JButton deleteBtn = new JButton("Delete");

        styleButton(addBtn);
        styleButton(viewBtn);
        styleButton(searchBtn);
        styleButton(deleteBtn);

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(deleteBtn);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(new Color(250, 250, 250));
        outputArea.setBorder(new EmptyBorder(4, 4, 4, 4));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(370, 140));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        //  Wire Button Actions
        addBtn.addActionListener(e -> handleAdd());
        viewBtn.addActionListener(e -> handleViewAll());
        searchBtn.addActionListener(e -> handleSearch());
        deleteBtn.addActionListener(e -> handleDelete());

        //  Assemble
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    // =========================================================================
    // Button Handlers
    // =========================================================================

    /**
     * ADD: Reads ID, Name, Mobile and adds student to AcademicManager.
     * Prevents duplicates via AcademicManager.addStudent().
     */
    private void handleAdd() {
        String idText   = idField.getText().trim();
        String name     = nameField.getText().trim();
        String mobile   = mobileField.getText().trim();

        if (idText.isEmpty() || name.isEmpty()) {
            showOutput("ERROR: ID and Name are required.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showOutput("ERROR: ID must be a valid integer.");
            return;
        }

        if (manager.getStudents().containsKey(id)) {
            showOutput("DUPLICATE: Student with ID " + id + " already exists.");
            return;
        }

        Student s = new Student(id, name);
        manager.addStudent(s);

        if (!mobile.isEmpty()) {
            mobileStore.put(id, mobile);
        }

        showOutput("SUCCESS: Student added.\n"
                + "  ID: " + id + "\n"
                + "  Name: " + name + "\n"
                + "  Mobile: " + (mobile.isEmpty() ? "N/A" : mobile));
        clearFields();
    }

    /**
     * VIEW ALL: Retrieves all students sorted alphabetically by name (1.5).
     * Also shows enrollment count per course (1.6).
     */
    private void handleViewAll() {
        HashMap<Integer, Student> studentMap = manager.getStudents();
        if (studentMap.isEmpty()) {
            showOutput("No students in the system.");
            return;
        }

        // 1.5
        List<Student> sorted = new ArrayList<>(studentMap.values());
        sorted.sort(Comparator.comparing(Student::getName));

        StringBuilder sb = new StringBuilder();
        sb.append("=== All Students (Sorted A-Z) ===\n");
        for (Student s : sorted) {
            String mob = mobileStore.getOrDefault(s.getStudentID(), "N/A");
            sb.append("  ID: ").append(s.getStudentID())
                    .append(" | Name: ").append(s.getName())
                    .append(" | Mobile: ").append(mob).append("\n");
        }

        // 1.6
        sb.append("\n=== Enrollments Per Course ===\n");
        HashMap<String, java.util.Set<Student>> enrollments = manager.getCourseEnrollments();
        if (enrollments.isEmpty()) {
            sb.append("  No course enrollments recorded.\n");
        } else {
            enrollments.forEach((course, set) ->
                    sb.append("  ").append(course)
                            .append(": ").append(set.size()).append(" student(s)\n")
            );
        }

        showOutput(sb.toString());
    }

    /**
     * SEARCH: Finds a student by ID or by name.
     */
    private void handleSearch() {
        String idText = idField.getText().trim();
        String name   = nameField.getText().trim();

        if (!idText.isEmpty()) {
            // Search by ID
            try {
                int id = Integer.parseInt(idText);
                Student s = manager.getStudents().get(id);
                if (s == null) {
                    showOutput("NOT FOUND: No student with ID " + id + ".");
                } else {
                    String mob = mobileStore.getOrDefault(id, "N/A");
                    showOutput("FOUND:\n"
                            + "  ID: " + s.getStudentID() + "\n"
                            + "  Name: " + s.getName() + "\n"
                            + "  Mobile: " + mob);
                }
            } catch (NumberFormatException ex) {
                showOutput("ERROR: ID must be a valid integer.");
            }
        } else if (!name.isEmpty()) {
            // Search by name (case-insensitive)
            String nameLower = name.toLowerCase();
            List<Student> results = manager.getStudents().values().stream()
                    .filter(s -> s.getName().toLowerCase().contains(nameLower))
                    .sorted(Comparator.comparing(Student::getName))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                showOutput("NOT FOUND: No student matching '" + name + "'.");
            } else {
                StringBuilder sb = new StringBuilder("SEARCH RESULTS:\n");
                for (Student s : results) {
                    String mob = mobileStore.getOrDefault(s.getStudentID(), "N/A");
                    sb.append("  ID: ").append(s.getStudentID())
                            .append(" | Name: ").append(s.getName())
                            .append(" | Mobile: ").append(mob).append("\n");
                }
                showOutput(sb.toString());
            }
        } else {
            showOutput("Please enter an ID or Name to search.");
        }
    }

    /**
     * DELETE: Removes a student by ID.
     */
    private void handleDelete() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            showOutput("Please enter an ID to delete.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showOutput("ERROR: ID must be a valid integer.");
            return;
        }

        HashMap<Integer, Student> studentMap = manager.getStudents();
        if (!studentMap.containsKey(id)) {
            showOutput("NOT FOUND: No student with ID " + id + ".");
            return;
        }

        Student removed = studentMap.remove(id);
        mobileStore.remove(id);

        // Also remove from all course enrollments
        manager.getCourseEnrollments().values().forEach(set -> set.remove(removed));

        showOutput("DELETED: Student " + removed.getName()
                + " (ID: " + id + ") has been removed.");
        clearFields();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private void showOutput(String text) {
        outputArea.setText(text);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        mobileField.setText("");
    }

    private void styleField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 160, 210), 1),
                new EmptyBorder(2, 4, 2, 4)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(new Color(230, 230, 230));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // =========================================================================
    // Main
    // =========================================================================

    public static void main(String[] args) {
        // Run on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(StudentManagerGui::new);
    }
}