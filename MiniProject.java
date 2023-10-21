import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

//class Activity to store the name , start and end time of workout
class Activity {
    private String name;
    private String startTime;
    private String endTime;

    public Activity(String name, String startTime, String endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    //methods to return values 
    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    //method to calculate workout duration
    public String getDuration() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm"); //using java inbuild function to to create a format object
        try { 
            Date date1 = format.parse(startTime); //type caste the start time in "HH:mm" format and storing it in date1
            Date date2 = format.parse(endTime); //type caste the end time in "HH:mm" format and storing it in date2
            long difference = date2.getTime() - date1.getTime();    //calculate the difference bettween the two times
            long minutes = difference / (60 * 1000); //convert difference into minutes
            long hours = minutes / 60;  //convert minutes into hours
            minutes = minutes % 60; //mod to get remaining minutes 
            return String.format("%02d:%02d", hours, minutes); //return the total duration in "HH:m"
        } catch (ParseException e) { //exceptional handelling  for error while parsingsimple date format
            e.printStackTrace(); 
            return "00:00";
        }
    }
}

//using inheritance
//class user to store the user details
class User extends Activity {
    private int age;
    private double height;
    private double weight;

    public User(String name, int age, double height, double weight) {
        super(name, "", "");
        this.age = age;
        this.height = height;
        this.weight = weight;
    }
    //methods to return values
    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}

//using multiple inhertitance
//used to calculate calories burnt 
class Exercise extends Activity {
    private String user;
    private static final double CALORIE_BURN_RATE = 1.3;

    public Exercise(String name, String startTime, String endTime, String user) {
        super(name, startTime, endTime);
        this.user = user;
    }

    public String getUser() {
        return user;
    }
    //calculates calories burnt
    public double getCaloriesBurnt() {
        int workoutDurationMinutes = getWorkoutDurationMinutes();
        return workoutDurationMinutes * CALORIE_BURN_RATE;
    }

    private int getWorkoutDurationMinutes() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date date1 = format.parse(getStartTime());
            Date date2 = format.parse(getEndTime());
            long difference = date2.getTime() - date1.getTime();
            return (int) (difference / (60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

public class MiniProject {
    private static final double CALORIE_BURN_RATE = 1.3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<User> users = new ArrayList<>();
        List<Exercise> workouts = new ArrayList<>();

        //list of options for the user
        //menu screen
        boolean exit = false;
        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Add a user");
            System.out.println("2. Display all users");
            System.out.println("3. Delete a user");
            System.out.println("4. Add a workout to a user");
            System.out.println("5. Display workouts of a user");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            

            //exceptional handeling for invalid data as user input
            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        User newUser = createUser(scanner);
                        users.add(newUser);
                        break;
                    case 2:
                        displayUsers(users);
                        break;
                    case 3:
                        if (!users.isEmpty()) {
                            deleteUser(scanner, users, workouts);
                        } else {
                            System.out.println("No users to delete.");
                        }
                        break;
                    case 4:
                        if (!users.isEmpty()) {
                            addWorkoutToUser(scanner, users, workouts);
                        } else {
                            System.out.println("Please create a user first.");
                        }
                        break;
                    case 5:
                        if (!users.isEmpty()) {
                            displayUserWorkouts(scanner, users, workouts);
                        } else {
                            System.out.println("Please create a user first.");
                        }
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice (numeric value).");
                scanner.next(); // Clear the invalid input from the scanner
            }
        }
    }

    //create a user
    private static User createUser(Scanner scanner) {
        System.out.println("Create a new user:");
        System.out.println("Enter your name: ");
        String userName = scanner.next();
        int userAge = getIntInput(scanner, "Enter your age: ");
        double userHeight = getDoubleInput(scanner, "Enter your height (in meters): ");
        double userWeight = getDoubleInput(scanner, "Enter your weight (in kilograms):");

        return new User(userName, userAge, userHeight, userWeight);
    }

    //exception to check if the input is int 
    private static int getIntInput(Scanner scanner, String prompt) {
        int input = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(prompt);
                input = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Clear the invalid input from the scanner
            }
        }

        return input;
    }

    //check if the input id double
    private static double getDoubleInput(Scanner scanner, String prompt) {
        double input = 0.0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(prompt);
                input = scanner.nextDouble();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid double.");
                scanner.next(); // Clear the invalid input from the scanner
            }
        }

        return input;
    }

    //display user data
    private static void displayUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users to display.");
        } else {
            System.out.println("All Users:");
            System.out.println("---------------------------------------------------------------------------------");
            System.out.printf("| %-4s | %-15s | %-4s | %-10s | %-10s |\n", "No.", "Name", "Age", "Height (m)", "Weight (kg)");
            System.out.println("---------------------------------------------------------------------------------");
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.printf("| %-4d | %-15s | %-4d | %-10.2f | %-10.2f |\n", i + 1, user.getName(), user.getAge(), user.getHeight(), user.getWeight());
            }
            System.out.println("---------------------------------------------------------------------------------");
        }
    }
    //display user workout based on the selected user
    private static void displayUserWorkouts(Scanner scanner, List<User> users, List<Exercise> workouts) {
        displayUsers(users);
        System.out.print("Select a user to display their workouts (enter the user's number): ");

        try {
            int selectedUserIndex = scanner.nextInt();

            if (selectedUserIndex > 0 && selectedUserIndex <= users.size()) {
                User selectedUser = users.get(selectedUserIndex - 1);
                System.out.println("Workouts for user: " + selectedUser.getName());
                System.out.println("-----------------------------------------------------------------------");
                System.out.printf("| %-20s | %-8s | %-8s | %-15s | %-15s |\n", "Exercise Name", "Start Time", "End Time", "Workout Duration", "Calories Burnt");
                System.out.println("-----------------------------------------------------------------------");
                for (Exercise exercise : workouts) {
                    if (exercise.getUser().equals(selectedUser.getName())) {
                        System.out.printf("| %-20s | %-8s | %-8s | %-15s | %-15.2f |\n", exercise.getName(), exercise.getStartTime(),
                                exercise.getEndTime(), exercise.getDuration(), exercise.getCaloriesBurnt());
                    }
                }
                System.out.println("-----------------------------------------------------------------------");
            } else {
                System.out.println("Invalid user selection.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid user number.");
            scanner.next(); // Clear the invalid input from the scanner
        }
    }


    //add workout for the specific user
    private static void addWorkoutToUser(Scanner scanner, List<User> users, List<Exercise> workouts) {
        displayUsers(users);
        System.out.print("Select a user to add a workout (enter the user's number): ");

        try {
            int selectedUserIndex = scanner.nextInt();

            if (selectedUserIndex > 0 && selectedUserIndex <= users.size()) {
                User selectedUser = users.get(selectedUserIndex - 1);
                addWorkout(scanner, workouts, selectedUser.getName());
                System.out.println("Workout added to user: " + selectedUser.getName());
            } else {
                System.out.println("Invalid user selection. Workout not added.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid user number.");
            scanner.next(); // Clear the invalid input from the scanner
        }
    }

    //add wokout for a selected user calls this method 
    private static void addWorkout(Scanner scanner, List<Exercise> workouts, String userName) {
        System.out.println("Enter exercise name: ");
        String exerciseName = scanner.next();
        System.out.println("Enter start time (HH:mm): ");
        String startTime = scanner.next();
        System.out.println("Enter end time (HH:mm): ");
        String endTime = scanner.next();

        //create new  exercise obj and add the obj to the workouts
        Exercise exercise = new Exercise(exerciseName, startTime, endTime, userName);
        workouts.add(exercise);
    }

    //delete an user
    private static void deleteUser(Scanner scanner, List<User> users, List<Exercise> workouts) {
        displayUsers(users);
        System.out.print("Select a user to delete (enter the user's number): ");

        try {
            int selectedUserIndex = scanner.nextInt();

            //the selected index should exisit else invalid selsction and throws an exception
            if (selectedUserIndex > 0 && selectedUserIndex <= users.size()) {
                User selectedUser = users.get(selectedUserIndex - 1);

                // Remove the user's workouts
                workouts.removeIf(exercise -> exercise.getUser().equals(selectedUser.getName()));

                // Remove the user
                users.remove(selectedUser);
                System.out.println("User " + selectedUser.getName() + " has been deleted.");
            } else {
                System.out.println("Invalid user selection. User not deleted.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid user number.");
            scanner.next(); // Clear the invalid input from the scanner
        }
    }
}
