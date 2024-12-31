import java.io.*;
import java.util.Scanner;

class Client {
    static Scanner input = new Scanner(System.in);
    static String[] basket = new String[0];
    static double total = 0.0;
    static String bankCardNumber = "";
    static String cardDate = "";
    static String cvv = "";
    static double limit = 0.0;

    public static void run(int type) {
        if (type == 0) System.out.println("\n" + Admin.welcomeMessage + "\n");

        if (type == 0 || type == 2) {
            System.out.println("Client Mode Options: ");
            System.out.println("\t1. Show Menu Categories");
            System.out.println("\t2. View Basket");
            System.out.println("\t3. Add Bank Card");
            System.out.println("\t4. Checkout");
            System.out.println("\t5. Exit Client Mode");
        }

        System.out.print("\nChoose an option: ");
        String usr = input.nextLine();
        switch (usr) {
            case "1":
                showMenu();
                break;
            case "2":
                viewBasket(0);
                break;
            case "3":
                addBankCard(0);
                break;
            case "4":
                checkout();
                break;
            case "5":
                Main.run(0);
                bankCardNumber = "";
                cardDate = "";
                cvv = "";
                limit = 0.0;
                return;
            default:
                System.out.println("\nInvalid command!");
                run(1);
        }
    }

    public static void showMenu() {
        int[] error = {-1, -1, -1, -1, -1};
        System.out.println();

        if (Menu.menu.length == 0) {
            System.out.println("Menu is empty.");
            run(1);
        }

        for (var category : Menu.menu) {
            if (category.items.length == 0) {
                System.out.println("Category " + category.name + " is empty.\n");
                continue;
            }

            System.out.println("Category: " + category.name);
            System.out.println("Subcategories: ");
            for (int i = 0; i < category.items.length; i++) {
                System.out.println("\t" + (i + 1) + ". " + category.items[i] + " - " + category.prices[i] + " KZT.");
            }
            System.out.print("Select an items to add to your basket or enter 0 to see next category (i1, i2, ...): \n> ");
            String[] usr = Menu.AddInputFormatter(input.nextLine());

            if (usr.length == 1 && usr[0].equals("0")) continue;

            int[] indexes = transform(usr, category.items.length);

            boolean flag = (indexes.length == error.length);
            for (int i = 0; i < indexes.length; i++) {
                if (indexes[i] == error[i]) continue;
                flag = false;
            }
            if (flag) {
                showMenu();
                return;
            }

            String[] newBasket = new String[basket.length + usr.length];
            for (int i = 0; i < basket.length; i++) {
                newBasket[i] = basket[i];
            }
            for (int i = 0; i < usr.length; i++) {
                newBasket[i + basket.length] = category.items[indexes[i] - 1] + " - " + Double.toString(category.prices[indexes[i] - 1]);
                total += category.prices[indexes[i] - 1];
            }

            basket = newBasket;
            System.out.println("Successfully added!\n");
        }
        run(2);
    }


    public static void getReciept(String[] items, double total) {
        String info = "-".repeat(10) + " Shop Reciept " + "-".repeat(10) + "\n" +
                "Adress: Kaskelen, Abylaikhan, 1/1\n" +
                "Tel: +7 771 671 29 05\n" +
                "-".repeat(14 + 20) + "\n" +
                "Date: 01/12/2024 14:04\n\n";
        for (int i = 0; i < items.length; i++) {
            String[] x = items[i].split(" - ");
            String line = x[0];
            while (line.length() < 34 - x[1].length()) {
                line += " ";
            }
            line += x[1];
            info += line + "\n";
        }

        String totalcash = Double.toString(total);
        // 7 + price.len + " " == 22
        info += "-".repeat(34) + "\n";
        info += ("Total: " + " ".repeat(27 - totalcash.length()) + totalcash);
        info += "\nThank you for purchase!\n";

        try {
            FileWriter file = new FileWriter("Reciept.txt");
            System.out.println("You can get your reciept: Reciept.txt\n");
            file.write(info);
            file.close();
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }


    public static int[] transform(String[] usr, int limit) {
        int[] error = {-1, -1, -1, -1, -1};

        int[] indexes = new int[usr.length];
        int p = 0;
        for (int i = 0; i < usr.length; i++) {
            try {
                indexes[p] = Integer.parseInt(usr[i]);
            } catch (Exception e) {
                System.out.println("\nInvalid command!");
                return error;
            }
            indexes[p] = Integer.parseInt(usr[i]);
            p++;
        }

        // bubble sort
        for (int j = 0; j < indexes.length; j++) {
            for (int i = 1; i < indexes.length; i++) {
                if (indexes[i - 1] > indexes[i]) {
                    int temp = indexes[i - 1];
                    indexes[i - 1] = indexes[i];
                    indexes[i] = temp;
                }
            }
        }

        // kaitalanbauyn tekseru
        boolean ok = true;
        for (int i = 1; i < indexes.length; i++) {
            if (indexes[i - 1] == indexes[i]) {
                ok = false;
                break;
            }
        }
        if (!ok) {
            System.out.println("\nDon't repeat indexes!");
            return error;
        }

        // indexter shekaradan aspauyn tekseru
        boolean ok2 = true;
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] > 0 && indexes[i] <= limit) {
                continue;
            }
            ok2 = false;
        }
        if (!ok2) {
            System.out.println("Enter the correct indexes!");
            return error;
        }

        return indexes;
    }



    public static void viewBasket(int type) {
        if (basket.length == 0) {
            System.out.println("\nYour basket is empty.\n");
            run(2);
            return;
        }

        if (type == 0) System.out.println("\nItems in your basket: ");
        for (int i = 0; i < basket.length; i++) {
            System.out.println("\t" + (i + 1) + ". " + basket[i] + " KZT.");
        }
        System.out.println("Total: " + total + " KZT.\n");
        if (type == 0) run(2);
    }



    public static void addBankCard(int type) {
        // TYPE 1 - INVALID CARD NUMBER
        // TYPE 2 - INVALID CARD DATE
        // TYPE 3 - INVALID CVV
        if (!bankCardNumber.isEmpty() && !cardDate.isEmpty() && !cvv.isEmpty()) {
            System.out.println("\nBank Card already exists.\n");
            run(2);
            return;
        }

        if (bankCardNumber.isEmpty() || type == 1) {
            System.out.print("Enter bank card number (16 digits): ");
            String temp = input.nextLine();

            int count = 0;
            for (int i = 0; i < temp.length(); i++) {
                if (Character.isDigit(temp.charAt(i))) count++;
            }

            if (count == temp.length() && temp.length() == 16) {
                bankCardNumber = temp;
            } else {
                System.out.println("\nInvalid card number!\n");
                addBankCard(1);
                return;
            }
        }

        if (cardDate.isEmpty() || type == 2) {
            System.out.print("Enter card expiration date (MM/YY): ");
            String temp = input.nextLine();

            boolean flag1 = false, flag2 = false;
            if (temp.length() == 5 && temp.charAt(2) == '/') flag1 = true;

            if (
                    flag1 &&
                            Character.isDigit(temp.charAt(0)) &&
                            Character.isDigit(temp.charAt(1)) &&
                            Character.isDigit(temp.charAt(3)) &&
                            Character.isDigit(temp.charAt(4))
            ) flag2 = true;

            if (flag1 && flag2) {
                cardDate = temp;
            } else {
                System.out.println("\nInvalid card expiration date!\n");
                addBankCard(2);
                return;
            }
        }

        if (cvv.isEmpty() || type == 3) {
            System.out.print("Enter CVV (3 digits): ");
            String temp = input.nextLine();

            if (
                    temp.length() == 3 &&
                            Character.isDigit(temp.charAt(0)) &&
                            Character.isDigit(temp.charAt(1)) &&
                            Character.isDigit(temp.charAt(2))
            ) {
                cvv = temp;
            } else {
                System.out.println("\nInvalid CVV!\n");
                addBankCard(3);
                return;
            }
        }

        if (limit == 0.0) {
            boolean check = true;
            while (check) {
                System.out.print("Enter the limit: ");
                String temp = input.nextLine();
                try {
                    limit = Double.parseDouble(temp);
                    check = false;
                } catch (Exception e) {
                    System.out.println("\nEnter the correct limit!\n");
                }
            }
        }

        System.out.println("\nBank Card successfully added!\n");
        run(2);
    }

    public static void checkout() {
        if (basket.length == 0) {
            System.out.println("\nYour basket is empty.\n");
            run(2);
            return;
        }

        if (bankCardNumber.isEmpty() || cardDate.isEmpty() || cvv.isEmpty()) {
            System.out.println("\nBank Card not found.\n");
            run(2);
            return;
        }

        if (limit < total) {
            System.out.println("\nNot enought money.\n");
            run(2);
            return;
        }

        System.out.println("\nChecking out with the following items: ");
        viewBasket(1);
        System.out.println("Payment successful. Thank you for your purchase!");

        getReciept(basket, total);
        basket = new String[0];
        total = 0.0;

        run(2);
    }
}