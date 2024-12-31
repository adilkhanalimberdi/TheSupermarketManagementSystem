import java.util.Scanner;

class Menu {
    static SubMenu[] menu = new SubMenu[0];
    static Scanner input = new Scanner(System.in);

    public static void choose() {
        // Menudagy areketter
        System.out.println("\nHere you can do the following activities: ");
        System.out.println("\t0. Go back.");
        System.out.println("\t1. Add category.");
        System.out.println("\t2. Rename category.");
        System.out.println("\t3. Remove categories.");
        System.out.print("\nYour choice: ");
        String usr = input.nextLine();
        switch (usr) {
            case "0":
                Admin.adminPanel(0);
                break;
            case "1":
                add();
                break;
            case "2":
                renameMenu();
                break;
            case "3":
                remove(0);
                break;
            default:
                System.out.println("\nInvalid command!");
                choose();
                break;
        }
    }

    // Menuga jana category kosu
    public static void add() {
        System.out.print("Write the names of categories separated by comma (c1, c2, ...): \n> ");
        String[] items = AddInputFormatter(input.nextLine());
        SubMenu[] newMenu = new SubMenu[menu.length + items.length];

        // elementterdi kosu (janartu)
        int len = menu.length;
        for (int i = 0; i < menu.length; i++) newMenu[i] = menu[i];
        for (int i = 0; i < items.length; i++) newMenu[len + i] = new SubMenu(items[i]);
        menu = newMenu;

        System.out.println("Successfully added.");
        choose();
        // Admin.adminPanel(0);
    }



    public static void remove(int type) {
        if (menu.length == 0) {
            System.out.println("\nMenu is empty.");
            choose();
            return;
        }

        if (type == 0) {
            showMenu(2);
        }

        System.out.print("Enter indexes to remove categories or 0 to go back (c1, c2, ...): \n> ");
        String[] items = AddInputFormatter(input.nextLine());

        // artka kaity
        if (items.length == 1 && items[0].equals("0")) {
            choose();
            return;
        }

        int[] indexes = checkingRemoveInput(items, menu.length, 0);

        // elementterdi oshiru (janartu)
        SubMenu[] newMenu = new SubMenu[menu.length - items.length];
        int p = 0;
        for (int i = 0; i < menu.length; i++) {
            boolean flag = true;
            for (int j = 0; j < indexes.length; j++) {
                if ((i + 1) == indexes[j]) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                continue;
            }
            newMenu[p] = menu[i];
            p++;
        }
        menu = newMenu;
        System.out.println("Successfully removed.");
        choose();
        // Admin.adminPanel(0);
    }



    public static void subMenuChoose() {
        // areketter
        System.out.println("\nHere you can do the following activities: ");
        System.out.println("\t0. Go back.");
        System.out.println("\t1. Add subcategory.");
        System.out.println("\t2. Rename subcategory.");
        System.out.println("\t3. Remove subcategories.");
        System.out.print("\nYour choice: ");
        String usr = input.nextLine();
        switch (usr) {
            case "0":
                Admin.adminPanel(0);
                break;
            case "1":
                subMenuAdd(0);
                break;
            case "2":
                renameSubMenu(0);
                break;
            case "3":
                subMenuRemove(0);
                break;
            default:
                System.out.println("Invalid command!");
                subMenuChoose();
                break;
        }
    }


    public static void renameMenu() {
        if (menu.length == 0) {
            System.out.println("Menu is empty!");
            choose();
            return;
        }

        showMenu(0);

        System.out.print("Enter the index or 0 to go back: ");
        int index = getInteger(input.nextLine());

        if (index == 0) {
            choose();
            return;
        }


        if (index > menu.length || index < 0) {
            System.out.println("\nEnter the correct index!");
            renameMenu();
            return;
        }

        index--;

        System.out.print("Enter the new name: ");
        menu[index].name = input.nextLine();

        System.out.println("\nSuccessfully renamed!");
        choose();
    }


    public static void renameSubMenu(int type) {
        if (type == 0) {
            System.out.println("\nChoose a category for which you want to rename subcategories: ");
            showMenu(0);
        }

        if (menu.length == 0) {
            System.out.println("Menu is empty!");
            subMenuChoose();
            return;
        }

        System.out.print("\nEnter the index or 0 to go back: ");
        int menuIndex = getInteger(input.nextLine());

        // artqa
        if (menuIndex == 0) {
            subMenuChoose();
            return;
        }

        if (menuIndex > menu.length || menuIndex < 1) {
            System.out.println("\nEnter the correct index!");
            renameSubMenu(0);
            return;
        }


        menuIndex--;

        if (menu[menuIndex].items.length == 0) {
            System.out.println("SubMenu is empty!");
            subMenuChoose();
            return;
        }

        showSubMenu(menuIndex);
        System.out.print("Enter the index which subcategory you want to rename: ");
        int index = getInteger(input.nextLine()) - 1;

        if (index >= menu[menuIndex].items.length || index < 0) {
            System.out.println("Enter the correct index");
            subMenuChoose();
            return;
        }

        System.out.print("Enter the new name: ");
        menu[menuIndex].items[index] = input.nextLine();
        System.out.println("\nSuccessfully renamed!");
        subMenuChoose();
    }



    public static void subMenuAdd(int type) {
        if (type == 0) {
            System.out.println("\nChoose a category for which you want to add subcategories: ");
            showMenu(0);
        }

        System.out.print("\nEnter the index or 0 to go back: ");
        int menuIndex = getInteger(input.nextLine());

        // artqa
        if (menuIndex == 0) {
            subMenuChoose();
            return;
        }

        // index tekseru
        if (menuIndex > menu.length || menuIndex < 1) {
            System.out.println("Enter the correct index!");
            subMenuAdd(1);
            return;
        }

        System.out.print("Write the name of subcategories separated by comma (s1, s2, ...): \n> ");
        String[] items = AddInputFormatter(input.nextLine());

        menuIndex--;
        String[] currentItems = menu[menuIndex].items;
        String[] newItems = new String[currentItems.length + items.length];

        // elementterdi kosu (janartu)
        for (int i = 0; i < currentItems.length; i++) newItems[i] = currentItems[i];
        for (int i = 0; i < items.length; i++) newItems[i + currentItems.length] = items[i];
        menu[menuIndex].items = newItems;
        menu[menuIndex].prices = new double[newItems.length];

        System.out.println("Successfully added.");
        subMenuChoose();
        //Admin.adminPanel(0);
    }



    public static void subMenuRemove(int type) {

        System.out.println("Choose a category for which you want to remove subcategories: ");
        showMenu(0);

        System.out.print("\nEnter the index or 0 to go back: ");
        int menuIndex = getInteger(input.nextLine());

        // artqa
        if (menuIndex == 0) {
            subMenuChoose();
            return;
        }

        // index tekseru
        if (menuIndex > menu.length || menuIndex < 1) {
            System.out.println("Enter the correct index!");
            subMenuRemove(1);
            return;
        }

        menuIndex--;
        String[] currentItems = menu[menuIndex].items;

        if (currentItems.length == 0) {
            System.out.println("\nCategory is empty.");
            subMenuChoose();
            return;
        }

        showSubMenu(menuIndex);
        System.out.print("Enter indexes to remove subcategories or 0 to go back (c1, c2, ...): \n> ");
        String[] items = AddInputFormatter(input.nextLine());

        int[] indexes = checkingRemoveInput(items, currentItems.length, 1);

        String[] newItems = new String[currentItems.length - items.length];
        int p = 0;
        for (int i = 0; i < currentItems.length; i++) {
            boolean flag = true;
            for (int j = 0; j < items.length; j++) {
                if ((i + 1) == indexes[j]) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                continue;
            }
            newItems[p] = currentItems[i];
            p++;
        }
        menu[menuIndex].items = newItems;
        menu[menuIndex].prices = new double[newItems.length];

        System.out.println("Successfully removed.");
        subMenuChoose();
        //Admin.adminPanel(0);
    }



    public static String[] AddInputFormatter(String usr) {
        String[] newStr = usr.strip().split(",");
        int count = 0;
        for (int i = 0; i < newStr.length; i++) {
            newStr[i] = newStr[i].strip();
            if (newStr[i].isBlank()) {
                count++;
            }
        }
        String[] res = new String[newStr.length - count];
        int p = 0;
        for (int i = 0; i < newStr.length; i++) {
            if (!newStr[i].isBlank()) {
                res[p] = newStr[i];
                p++;
            }
        }
        return res;
    }



    public static int getInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.print("Enter the correct index!\n> ");
            String w = input.nextLine();
            return getInteger(w);
        }
    }



    public static double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            System.out.print("Enter the correct value!\n> ");
            String w = input.nextLine();
            return getDouble(w);
        }
    }



    public static int[] checkingRemoveInput(String[] usr, int limit, int type) {
        int[] error = {-1};

        // pustoi input ili pustoi menu
        if (usr.length > limit || limit == 0 || usr.length == 0) {
            System.out.println("\nEnter the correct indexes!");
            remove(1);
            return error;
        }

        // string -> int (convert)
        int[] indexes = new int[usr.length];
        for (int i = 0; i < usr.length; i++) {

            try {
                Integer.parseInt(usr[i]);
            } catch (Exception e) {
                System.out.println("\nEnter the indexes, not words/letters!");
                if (type == 0) {
                    remove(1);                                                       // TYPE 0 - REMOVE CATEGORY
                } else if (type == 1) {
                    subMenuRemove(1);                                                // TYPE 1 - REMOVE SUBCATEGORY
                } else if (type == 2) {
                    Admin.adminPanel(0);
                }
                return error;
            }

            indexes[i] = Integer.parseInt(usr[i]);
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

        // kaitalanatyn elementterdi tekseru
        boolean ok = true;
        for (int i = 1; i < indexes.length; i++) {
            if (indexes[i - 1] == indexes[i]) {
                ok = false;
            }
        }
        if (!ok) {
            System.out.println("\nDon't repeat indexes!");
            if (type == 0) {
                remove(1);
            } else if (type == 1) {
                subMenuRemove(1);
            } else if (type == 2) {
                Admin.adminPanel(0);
            }
            return error;
        }

        // indexter shekaradan asyp ketpeuin tekseru :)
        boolean ok2 = true;
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] > 0 && indexes[i] <= limit) {
                continue;
            }
            ok2 = false;
        }
        if (!ok2) {
            System.out.println("\nEnter the correct indexes!");
            if (type == 0) {
                remove(1);
            } else if (type == 1) {
                subMenuRemove(1);
            } else if (type == 2) {
                Admin.adminPanel(0);
            }
            return error;
        }

        return indexes;

    }


    public static void showMenu(int type) {
        if (menu.length == 0) {
            System.out.println("\nMenu is empty.");
            Admin.adminPanel(0);
            return;
        }
        if (type == 1 || type == 2) {
            System.out.println("\nMenu: ");
        }
        for (int i = 0; i < menu.length; i++) {
            System.out.println("\t" + (i + 1) + ". " + menu[i].name);
        }
        if (type == 1) {
            Admin.adminPanel(0);
        }
    }

    public static void showSubMenu(int type) {
        if (type != 999) {
            int usr = type;
            if (usr > menu.length) {
                System.out.println("Invalid input!");
                Admin.adminPanel(0);
                return;
            }
            System.out.println("\nSubMenu for " + menu[usr].name + ": ");
            for (int i = 0; i < menu[usr].items.length; i++) {
                System.out.println("\t" + (i + 1) + ". " + menu[usr].items[i] + " - " + menu[usr].prices[i] + " KZT.");
            }
            return;
        }

        showMenu(2);
        System.out.print("\nEnter the index: ");
        int usr = Integer.parseInt(input.nextLine()) - 1;

        // for (var i : menu) System.out.println(i.items.length);

        if (menu[usr].items.length == 0) {
            System.out.println("\nSubMenu is empty.");
            Admin.adminPanel(0);
            return;
        }
        System.out.println("\nSubMenu for " + menu[usr].name + ": ");
        for (int i = 0; i < menu[usr].items.length; i++) {
            System.out.println("\t" + (i + 1) + ". " + menu[usr].items[i] + " - " + menu[usr].prices[i] + " KZT.");
        }
        Admin.adminPanel(0);
    }

    public static void priceChoose() {
        System.out.println("\nHere you can do the following activities: ");
        System.out.println("\t0. Go back.");
        System.out.println("\t1. Add or change price.");
        System.out.print("\nYour choice: ");
        String usr = input.nextLine();

        switch (usr) {
            case "0":
                Admin.adminPanel(0);
                break;
            case "1":
                changePrice();
                break;
            default:
                System.out.println("Invalid command!");
                priceChoose();
                break;
        }
    }

    public static void changePrice() {
        showMenu(2);
        System.out.print("\nEnter the index or 0 to go back: ");
        int usr = getInteger(input.nextLine());

        // artqa
        if (usr == 0) {
            priceChoose();
            return;
        }

        // index tekseru
        if (usr > menu.length || usr < 1) {
            System.out.println("Enter the correct index!");
            changePrice();
            return;
        }

        if (menu[usr - 1].items.length == 0) {
            System.out.println("\nSubMenu is empty.");
            Admin.adminPanel(0);
            return;
        }

        usr--;
        showSubMenu(usr);
        System.out.print("\nEnter indexes to add or change price (p1, p2, ...): \n> ");
        String[] prices = AddInputFormatter(input.nextLine());

        int[] indexes = checkingRemoveInput(prices, menu[usr].items.length, 2);

        System.out.print("\nEnter the new value: ");
        double newValue = getDouble(input.nextLine());

        for (int i = 0; i < menu[usr].prices.length; i++) {
            boolean flag = true;
            for (int j = 0; j < indexes.length; j++) {
                if ((i + 1) == indexes[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            menu[usr].prices[i] = newValue;
        }
        priceChoose();
        // Admin.adminPanel(0);
    }
}