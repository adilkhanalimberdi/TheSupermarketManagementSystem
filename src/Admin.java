import java.util.Scanner;

class Admin {
    static String password = "admin123";
    static String welcomeMessage = "Welcome to Adilkhan's supermarket!";
    static Scanner input = new Scanner(System.in);

    public static void run() {
        System.out.print("Enter password: ");
        check();
        adminPanel(0);
    }

    public static void adminPanel(int type) {
        if (type == 0) System.out.println("\nHere you can do the following activities: ");
        String[] actions = {
                "0. Exit admin mode.",
                "1. Change Welcome Message.",
                "2. Change Menu.",
                "3. Change SubMenu.",
                "4. Change Price.",
                "5. Display Welcome Message.",
                "6. Display Menu.",
                "7. Display SubMenu and Prices.",
        };

        if (type == 0) {
            for (var i : actions) System.out.println("\t" + i);
        }

        System.out.print("\nChoose an activity: ");
        String usr = input.nextLine();
        switch (usr) {
            case "0":
                Main.run(0);
                break;
            case "1":
                changeWelcomeMessage();
                break;
            case "2":
                Menu.choose();
                break;
            case "3":
                Menu.subMenuChoose();
                break;
            case "4":
                Menu.priceChoose();
                break;
            case "5":
                showWelcomeMessage();
                break;
            case "6":
                Menu.showMenu(1);
                break;
            case "7":
                Menu.showSubMenu( 999);
                break;
            default:
                System.out.println("\nInvalid command!");
                adminPanel(1);
        }
    }

    public static void changeWelcomeMessage() {
        System.out.print("\nEnter the new welcome message: ");
        welcomeMessage = input.nextLine();
        adminPanel(0);
    }

    public static void showWelcomeMessage() {
        System.out.println("\nThe welcome message: " + welcomeMessage);
        adminPanel(0);
    }

    public static void check() {
        String usr = input.nextLine();

        if (!usr.equals(password)) {
            System.out.print("You entered wrong password, please try again: ");
            check();
        }
    }
}