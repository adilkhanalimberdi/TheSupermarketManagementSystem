import java.util.Scanner;

class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        run(0);
    }

    public static void run(int type) {
        if (type == 0) System.out.println("\nAre you an Admin or a Client?");

        System.out.print("Enter 'Admin', 'Client', or 'Exit' to close the program: ");
        String usr = input.nextLine();

        switch (usr) {
            case "Admin":
                Admin.run();
                break;
            case "Client":
                Client.run(0);
                break;
            case "Exit":
                return;
            default:
                System.out.println("\nChoose a correct option!");
                run(1);
        }
    }
}

