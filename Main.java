import java.util.*;

// Base User class
abstract class User {
    protected String name;
    protected String email;
    protected int id;
    
    public User(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }
    
    public abstract void displayMenu();
    
    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getId() { return id; }
}

// Student class
class Student extends User {
    private int age;
    private Course enrolledCourse;
    private List<Subject> selectedSubjects;
    private Map<Subject, Integer> examResults;
    
    public Student(String name, String email, int id, int age) {
        super(name, email, id);
        this.age = age;
        this.selectedSubjects = new ArrayList<>();
        this.examResults = new HashMap<>();
    }
    
    @Override
    public void displayMenu() {
        System.out.println("\n=== STUDENT MENU ===");
        System.out.println("1. View Available Courses");
        System.out.println("2. Select Course");
        System.out.println("3. Choose Subjects");
        System.out.println("4. Take Exam");
        System.out.println("5. View My Results");
        System.out.println("6. Logout");
    }
    
    public void enrollInCourse(Course course) {
        this.enrolledCourse = course;
        System.out.println("Successfully enrolled in: " + course.getName());
    }
    
    public void selectSubject(Subject subject) {
        if (enrolledCourse != null && enrolledCourse.hasSubject(subject)) {
            if (!selectedSubjects.contains(subject)) {
                selectedSubjects.add(subject);
                System.out.println("Subject added: " + subject.getName());
            } else {
                System.out.println("Subject already selected!");
            }
        } else {
            System.out.println("Subject not available in your course!");
        }
    }
    
    public void takeExam(Subject subject, Scanner scanner) {
        if (!selectedSubjects.contains(subject)) {
            System.out.println("You haven't selected this subject!");
            return;
        }
        
        List<Question> questions = subject.getQuestions();
        if (questions.size() < 5) {
            System.out.println("Not enough questions available for this subject!");
            return;
        }
        
        int score = 0;
        System.out.println("\n=== EXAM: " + subject.getName() + " ===");
        
        for (int i = 0; i < Math.min(5, questions.size()); i++) {
            Question q = questions.get(i);
            System.out.println("\nQ" + (i+1) + ": " + q.getQuestion());
            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((char)('A' + j) + ") " + options[j]);
            }
            
            System.out.print("Your answer (A/B/C/D): ");
            String answer = scanner.nextLine().toUpperCase();
            
            if (answer.equals(String.valueOf((char)('A' + q.getCorrectAnswer())))) {
                score++;
                System.out.println("Correct! ✓");
            } else {
                System.out.println("Wrong! ✗ Correct answer: " + 
                    (char)('A' + q.getCorrectAnswer()));
            }
        }
        
        examResults.put(subject, score);
        System.out.println("\nExam completed! Score: " + score + "/5");
        System.out.println("Result: " + (score >= 3 ? "PASS" : "FAIL"));
    }
    
    public void viewResults() {
        System.out.println("\n=== MY EXAM RESULTS ===");
        if (examResults.isEmpty()) {
            System.out.println("No exam results yet!");
        } else {
            for (Map.Entry<Subject, Integer> entry : examResults.entrySet()) {
                int score = entry.getValue();
                System.out.println(entry.getKey().getName() + ": " + score + "/5 - " + 
                    (score >= 3 ? "PASS" : "FAIL"));
            }
        }
    }
    
    // Getters
    public int getAge() { return age; }
    public Course getEnrolledCourse() { return enrolledCourse; }
    public List<Subject> getSelectedSubjects() { return selectedSubjects; }
    public Map<Subject, Integer> getExamResults() { return examResults; }
}

// Admin class
class Admin extends User {
    public Admin(String name, String email, int id) {
        super(name, email, id);
    }
    
    @Override
    public void displayMenu() {
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Add Course");
        System.out.println("2. Add Subject to Course");
        System.out.println("3. View All Courses");
        System.out.println("4. View All Students");
        System.out.println("5. View Student Results");
        System.out.println("6. Add Questions to Subject");
        System.out.println("7. Logout");
    }
}

// Course class
class Course {
    private String name;
    private String description;
    private List<Subject> subjects;
    
    public Course(String name, String description) {
        this.name = name;
        this.description = description;
        this.subjects = new ArrayList<>();
    }
    
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }
    
    public boolean hasSubject(Subject subject) {
        return subjects.contains(subject);
    }
    
    public void displayInfo() {
        System.out.println("Course: " + name);
        System.out.println("Description: " + description);
        System.out.println("Subjects: ");
        for (Subject subject : subjects) {
            System.out.println("  - " + subject.getName());
        }
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Subject> getSubjects() { return subjects; }
}

// Subject class
class Subject {
    private String name;
    private String description;
    private List<Question> questions;
    
    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
        this.questions = new ArrayList<>();
    }
    
    public void addQuestion(Question question) {
        questions.add(question);
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Question> getQuestions() { return questions; }
}

// Question class
class Question {
    private String question;
    private String[] options;
    private int correctAnswer; // 0-3 for A-D
    
    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
    
    // Getters
    public String getQuestion() { return question; }
    public String[] getOptions() { return options; }
    public int getCorrectAnswer() { return correctAnswer; }
}

// Main System class
class StudentManagementSystem {
    private List<Course> courses;
    private List<Student> students;
    private Admin admin;
    private Scanner scanner;
    
    public StudentManagementSystem() {
        courses = new ArrayList<>();
        students = new ArrayList<>();
        scanner = new Scanner(System.in);
        
        // Initialize admin
        admin = new Admin("System Admin", "admin@sms.com", 1001);
        
        // Add sample data
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Create courses
        Course javaCourse = new Course("Java Programming", "Learn Java from basics to advanced");
        Course pythonCourse = new Course("Python Programming", "Master Python development");
        Course webCourse = new Course("Web Development", "Full-stack web development");
        
        // Create subjects
        Subject javaBasics = new Subject("Java Basics", "Fundamentals of Java");
        Subject javaOOP = new Subject("Java OOP", "Object-Oriented Programming in Java");
        Subject pythonBasics = new Subject("Python Basics", "Python fundamentals");
        Subject webHTML = new Subject("HTML/CSS", "Web markup and styling");
        Subject webJS = new Subject("JavaScript", "Client-side scripting");
        
        // Add questions
        addSampleQuestions(javaBasics);
        addSampleQuestions(javaOOP);
        addSampleQuestions(pythonBasics);
        addSampleQuestions(webHTML);
        addSampleQuestions(webJS);
        
        // Link subjects to courses
        javaCourse.addSubject(javaBasics);
        javaCourse.addSubject(javaOOP);
        pythonCourse.addSubject(pythonBasics);
        webCourse.addSubject(webHTML);
        webCourse.addSubject(webJS);
        
        // Add courses to system
        courses.add(javaCourse);
        courses.add(pythonCourse);
        courses.add(webCourse);
    }
    
    private void addSampleQuestions(Subject subject) {
        // Add 5+ sample questions for each subject
        switch (subject.getName()) {
            case "Java Basics":
                subject.addQuestion(new Question("What is Java?", 
                    new String[]{"A programming language", "A coffee brand", "An island", "A framework"}, 0));
                subject.addQuestion(new Question("Java is platform independent. True or False?", 
                    new String[]{"True", "False", "Sometimes", "Depends"}, 0));
                subject.addQuestion(new Question("Which method is the entry point of Java program?", 
                    new String[]{"start()", "main()", "run()", "init()"}, 1));
                subject.addQuestion(new Question("Java is compiled to:", 
                    new String[]{"Machine code", "Bytecode", "Assembly", "Native code"}, 1));
                subject.addQuestion(new Question("JVM stands for:", 
                    new String[]{"Java Virtual Machine", "Java Variable Method", "Java Vector Model", "None"}, 0));
                break;
                
            case "Java OOP":
                subject.addQuestion(new Question("What are the four pillars of OOP?", 
                    new String[]{"Encapsulation, Inheritance, Polymorphism, Abstraction", "Classes, Objects, Methods, Variables", "Public, Private, Protected, Default", "None"}, 0));
                subject.addQuestion(new Question("Which keyword is used for inheritance in Java?", 
                    new String[]{"implements", "extends", "inherits", "derive"}, 1));
                subject.addQuestion(new Question("Can a class extend multiple classes in Java?", 
                    new String[]{"Yes", "No", "Sometimes", "Depends"}, 1));
                subject.addQuestion(new Question("What is method overriding?", 
                    new String[]{"Same method name with different parameters", "Redefining parent class method", "Creating new method", "None"}, 1));
                subject.addQuestion(new Question("Abstract classes can have concrete methods. True or False?", 
                    new String[]{"True", "False", "Only static methods", "Only private methods"}, 0));
                break;
                
            default:
                // Generic questions for other subjects
                for (int i = 1; i <= 5; i++) {
                    subject.addQuestion(new Question("Sample question " + i + " for " + subject.getName(), 
                        new String[]{"Option A", "Option B", "Option C", "Option D"}, i % 4));
                }
        }
    }
    
    public void run() {
        System.out.println("🎓 Welcome to Student Management System! 🎓");
        
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Registration");
            System.out.println("3. Student Login");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    studentRegistration();
                    break;
                case 3:
                    studentLogin();
                    break;
                case 4:
                    System.out.println("Thank you for using Student Management System!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private void adminLogin() {
        System.out.print("Enter admin password (default: admin123): ");
        String password = scanner.nextLine();
        
        if (password.equals("admin123")) {
            System.out.println("Admin login successful!");
            adminMenu();
        } else {
            System.out.println("Invalid password!");
        }
    }
    
    private void adminMenu() {
        while (true) {
            admin.displayMenu();
            System.out.print("Choose option: ");
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    addSubjectToCourse();
                    break;
                case 3:
                    viewAllCourses();
                    break;
                case 4:
                    viewAllStudents();
                    break;
                case 5:
                    viewStudentResults();
                    break;
                case 6:
                    addQuestionsToSubject();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    private void addCourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        System.out.print("Enter course description: ");
        String description = scanner.nextLine();
        
        courses.add(new Course(name, description));
        System.out.println("Course added successfully!");
    }
    
    private void addSubjectToCourse() {
        if (courses.isEmpty()) {
            System.out.println("No courses available!");
            return;
        }
        
        System.out.println("Available courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
        
        System.out.print("Select course (number): ");
        int courseIndex = getIntInput() - 1;
        
        if (courseIndex >= 0 && courseIndex < courses.size()) {
            System.out.print("Enter subject name: ");
            String name = scanner.nextLine();
            System.out.print("Enter subject description: ");
            String description = scanner.nextLine();
            
            courses.get(courseIndex).addSubject(new Subject(name, description));
            System.out.println("Subject added successfully!");
        } else {
            System.out.println("Invalid course selection!");
        }
    }
    
    private void viewAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available!");
        } else {
            System.out.println("\n=== ALL COURSES ===");
            for (Course course : courses) {
                course.displayInfo();
                System.out.println("---");
            }
        }
    }
    
    private void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students registered!");
        } else {
            System.out.println("\n=== ALL STUDENTS ===");
            for (Student student : students) {
                System.out.println("ID: " + student.getId() + " | Name: " + student.getName() + 
                    " | Email: " + student.getEmail() + " | Age: " + student.getAge());
                if (student.getEnrolledCourse() != null) {
                    System.out.println("  Enrolled in: " + student.getEnrolledCourse().getName());
                }
                System.out.println("---");
            }
        }
    }
    
    private void viewStudentResults() {
        if (students.isEmpty()) {
            System.out.println("No students registered!");
            return;
        }
        
        System.out.println("Select student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }
        
        System.out.print("Enter choice: ");
        int choice = getIntInput() - 1;
        
        if (choice >= 0 && choice < students.size()) {
            students.get(choice).viewResults();
        } else {
            System.out.println("Invalid selection!");
        }
    }
    
    private void addQuestionsToSubject() {
        // Find all subjects across all courses
        List<Subject> allSubjects = new ArrayList<>();
        for (Course course : courses) {
            allSubjects.addAll(course.getSubjects());
        }
        
        if (allSubjects.isEmpty()) {
            System.out.println("No subjects available!");
            return;
        }
        
        System.out.println("Available subjects:");
        for (int i = 0; i < allSubjects.size(); i++) {
            System.out.println((i + 1) + ". " + allSubjects.get(i).getName());
        }
        
        System.out.print("Select subject: ");
        int subjectIndex = getIntInput() - 1;
        
        if (subjectIndex >= 0 && subjectIndex < allSubjects.size()) {
            Subject subject = allSubjects.get(subjectIndex);
            
            System.out.print("Enter question: ");
            String questionText = scanner.nextLine();
            
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter option " + (char)('A' + i) + ": ");
                options[i] = scanner.nextLine();
            }
            
            System.out.print("Enter correct answer (0-3 for A-D): ");
            int correctAnswer = getIntInput();
            
            if (correctAnswer >= 0 && correctAnswer <= 3) {
                subject.addQuestion(new Question(questionText, options, correctAnswer));
                System.out.println("Question added successfully!");
            } else {
                System.out.println("Invalid correct answer!");
            }
        } else {
            System.out.println("Invalid subject selection!");
        }
    }
    
    private void studentRegistration() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = getIntInput();
        
        int studentId = 2000 + students.size() + 1;
        Student student = new Student(name, email, studentId, age);
        students.add(student);
        
        System.out.println("Registration successful! Your Student ID is: " + studentId);
    }
    
    private void studentLogin() {
        System.out.print("Enter Student ID: ");
        int id = getIntInput();
        
        Student student = findStudentById(id);
        if (student != null) {
            System.out.println("Login successful! Welcome " + student.getName());
            studentMenu(student);
        } else {
            System.out.println("Student not found!");
        }
    }
    
    private void studentMenu(Student student) {
        while (true) {
            student.displayMenu();
            System.out.print("Choose option: ");
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewAvailableCourses();
                    break;
                case 2:
                    selectCourse(student);
                    break;
                case 3:
                    chooseSubjects(student);
                    break;
                case 4:
                    takeExam(student);
                    break;
                case 5:
                    student.viewResults();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    private void viewAvailableCourses() {
        viewAllCourses();
    }
    
    private void selectCourse(Student student) {
        if (courses.isEmpty()) {
            System.out.println("No courses available!");
            return;
        }
        
        System.out.println("Available courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
        
        System.out.print("Select course: ");
        int choice = getIntInput() - 1;
        
        if (choice >= 0 && choice < courses.size()) {
            student.enrollInCourse(courses.get(choice));
        } else {
            System.out.println("Invalid selection!");
        }
    }
    
    private void chooseSubjects(Student student) {
        if (student.getEnrolledCourse() == null) {
            System.out.println("Please enroll in a course first!");
            return;
        }
        
        List<Subject> subjects = student.getEnrolledCourse().getSubjects();
        if (subjects.isEmpty()) {
            System.out.println("No subjects available in your course!");
            return;
        }
        
        System.out.println("Available subjects:");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i).getName());
        }
        
        System.out.print("Select subject: ");
        int choice = getIntInput() - 1;
        
        if (choice >= 0 && choice < subjects.size()) {
            student.selectSubject(subjects.get(choice));
        } else {
            System.out.println("Invalid selection!");
        }
    }
    
    private void takeExam(Student student) {
        List<Subject> selectedSubjects = student.getSelectedSubjects();
        if (selectedSubjects.isEmpty()) {
            System.out.println("No subjects selected!");
            return;
        }
        
        System.out.println("Your selected subjects:");
        for (int i = 0; i < selectedSubjects.size(); i++) {
            System.out.println((i + 1) + ". " + selectedSubjects.get(i).getName());
        }
        
        System.out.print("Select subject for exam: ");
        int choice = getIntInput() - 1;
        
        if (choice >= 0 && choice < selectedSubjects.size()) {
            student.takeExam(selectedSubjects.get(choice), scanner);
        } else {
            System.out.println("Invalid selection!");
        }
    }
    
    private Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
    
    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        sms.run();
    }
}