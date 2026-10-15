import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class BloodBank {
    static Scanner sc = new Scanner(System.in);
    static String[] donorId = new String[100];
    static String[] donorName = new String[100];
    static int[] donorAge = new int[100];
    static String[] donorGroup = new String[100];
    static String[] donorPhone = new String[100];
    static int donorCount = 0;

    static String[] groups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    static int[] stock = new int[8];

    // Educational example only. Do not use for real transfusion decisions.
    // Rows are donor groups and columns are recipient groups.
    static boolean[][] compatible = {
        {true,false,false,false,false,false,false,false},
        {true,true,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false},
        {false,false,true,true,false,false,false,false},
        {true,false,true,false,true,false,false,false},
        {true,true,true,true,true,true,false,false},
        {true,false,false,false,false,false,true,false},
        {true,true,true,true,true,true,true,true}
    };

    public static void main(String[] args) {
        loadDonors();
        loadStock();
        int choice;
        do {
            System.out.println("\n===== BLOOD BANK INVENTORY & DONOR MATCHER =====");
            System.out.println("1. Add Donor");
            System.out.println("2. View Donors");
            System.out.println("3. Search Donor by Blood Group");
            System.out.println("4. Add Blood Stock");
            System.out.println("5. View Blood Stock");
            System.out.println("6. Find Compatible Stock");
            System.out.println("7. Issue Blood");
            System.out.println("8. Save and Exit");
            System.out.print("Enter Your choice: ");
            while (!sc.hasNextInt()) {
                System.out.print("Enter a number from 1 to 8: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: addDonor(); break;
                case 2: viewDonors(); break;
                case 3: searchDonor(); break;
                case 4: addStock(); break;
                case 5: viewStock(); break;
                case 6: findCompatibleStock(); break;
                case 7: issueBlood(); break;
                case 8:
                    saveDonors();
                    saveStock();
                    System.out.println("Data saved. Thank you!");
                    break;
                default: System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 8);
    }

    static int findGroup(String group) {
        for (int i = 0; i < groups.length; i++) {
            if (groups[i].equalsIgnoreCase(group)) return i;
        }
        return -1;
    }

    static void addDonor() {
        if (donorCount == donorId.length) {
            System.out.println("Donor list is full.");
            return;
        }
        System.out.print("Enter donor ID: ");
        String id = sc.nextLine();
        for (int i = 0; i < donorCount; i++) {
            if (donorId[i].equalsIgnoreCase(id)) {
                System.out.println("This donor ID already exists.");
                return;
            }
        }
        System.out.print("Enter donor name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        while (!sc.hasNextInt()) {
            System.out.print("Enter age as a number: ");
            sc.next();
        }
        int age = sc.nextInt();
        sc.nextLine();
        if (age < 18 || age > 65) {
            System.out.println("For this demo, age must be between 18 and 65.");
            return;
        }
        System.out.print("Enter blood group (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
        String group = sc.nextLine().toUpperCase();
        if (findGroup(group) == -1) {
            System.out.println("Invalid blood group.");
            return;
        }
        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();
        if (phone.length() != 10) {
            System.out.println("Phone number must contain 10 digits.");
            return;
        }
        donorId[donorCount] = id;
        donorName[donorCount] = name;
        donorAge[donorCount] = age;
        donorGroup[donorCount] = group;
        donorPhone[donorCount] = phone;
        donorCount++;
        System.out.println("Donor added successfully.");
        saveDonors();
    }

    static void viewDonors() {
        if (donorCount == 0) {
            System.out.println("No donor records found.");
            return;
        }
        for (int i = 0; i < donorCount; i++) {
            System.out.println(donorId[i] + " | " + donorName[i] + " | Age: "
                    + donorAge[i] + " | " + donorGroup[i] + " | " + donorPhone[i]);
        }
    }

    static void searchDonor() {
        System.out.print("Enter blood group to search: ");
        String wanted = sc.nextLine().toUpperCase();
        if (findGroup(wanted) == -1) {
            System.out.println("Invalid blood group.");
            return;
        }
        boolean found = false;
        for (int i = 0; i < donorCount; i++) {
            if (donorGroup[i].equals(wanted)) {
                System.out.println(donorId[i] + " - " + donorName[i]
                        + " - " + donorGroup[i] + " - " + donorPhone[i]);
                found = true;
            }
        }
        if (!found) System.out.println("No donor found for " + wanted);
    }

    static void addStock() {
        System.out.print("Enter blood group: ");
        String group = sc.nextLine().toUpperCase();
        int index = findGroup(group);
        if (index == -1) {
            System.out.println("Invalid blood group.");
            return;
        }
        System.out.print("Enter number of units to add: ");
        while (!sc.hasNextInt()) {
            System.out.print("Enter units as a number: ");
            sc.next();
        }
        int units = sc.nextInt();
        sc.nextLine();
        if (units <= 0) {
            System.out.println("Units must be greater than zero.");
            return;
        }
        stock[index] += units;
        System.out.println("Blood stock updated.");
        saveStock();
    }

    static void viewStock() {
        System.out.println("--- BLOOD STOCK ---");
        for (int i = 0; i < groups.length; i++) {
            System.out.println(groups[i] + " : " + stock[i] + " unit(s)");
        }
    }

    static void findCompatibleStock() {
        System.out.print("Enter recipient blood group: ");
        String recipient = sc.nextLine().toUpperCase();
        int r = findGroup(recipient);
        if (r == -1) {
            System.out.println("Invalid blood group.");
            return;
        }
        boolean found = false;
        for (int d = 0; d < groups.length; d++) {
            if (compatible[d][r] && stock[d] > 0) {
                System.out.println(groups[d] + " : " + stock[d] + " unit(s)");
                found = true;
            }
        }
        if (!found) System.out.println("No compatible stock currently available.");
    }

    static void issueBlood() {
        System.out.print("Enter recipient blood group: ");
        String recipient = sc.nextLine().toUpperCase();
        int r = findGroup(recipient);
        if (r == -1) {
            System.out.println("Invalid blood group.");
            return;
        }
        System.out.print("Enter units required: ");
        while (!sc.hasNextInt()) {
            System.out.print("Enter units as a number: ");
            sc.next();
        }
        int needed = sc.nextInt();
        sc.nextLine();
        if (needed <= 0) {
            System.out.println("Units must be greater than zero.");
            return;
        }
        int total = 0;
        for (int i = 0; i < groups.length; i++) {
            if (compatible[i][r]) total += stock[i];
        }
        if (total < needed) {
            System.out.println("Issue cancelled. Only " + total
                    + " compatible unit(s) available. Stock was not changed.");
            return;
        }
        int remaining = needed;
        for (int i = 0; i < groups.length; i++) {
            if (compatible[i][r] && stock[i] > 0) {
                int used = Math.min(stock[i], remaining);
                stock[i] -= used;
                remaining -= used;
                if (remaining == 0) break;
            }
        }
        saveStock();
        System.out.println("Issue recorded in this demo.");
        viewStock();
    }

    static void saveDonors() {
        try {
            PrintWriter out = new PrintWriter("donors.txt");
            for (int i = 0; i < donorCount; i++) {
                out.println(donorId[i] + "|" + donorName[i] + "|" + donorAge[i]
                        + "|" + donorGroup[i] + "|" + donorPhone[i]);
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not save donor file.");
        }
    }

    static void loadDonors() {
        try {
            Scanner file = new Scanner(new File("donors.txt"));
            while (file.hasNextLine() && donorCount < donorId.length) {
                String[] p = file.nextLine().split("\\|");
                if (p.length == 5) {
                    try {
                        donorId[donorCount] = p[0];
                        donorName[donorCount] = p[1];
                        donorAge[donorCount] = Integer.parseInt(p[2]);
                        donorGroup[donorCount] = p[3];
                        donorPhone[donorCount] = p[4];
                        donorCount++;
                    } catch (NumberFormatException e) {
                        // Ignore a damaged record.
                    }
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            // First run: donor file does not exist yet.
        }
    }

    static void saveStock() {
        try {
            PrintWriter out = new PrintWriter("inventory.txt");
            for (int i = 0; i < groups.length; i++) {
                out.println(groups[i] + "|" + stock[i]);
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not save inventory file.");
        }
    }

    static void loadStock() {
        try {
            Scanner file = new Scanner(new File("inventory.txt"));
            while (file.hasNextLine()) {
                String[] p = file.nextLine().split("\\|");
                if (p.length == 2) {
                    int index = findGroup(p[0]);
                    if (index != -1) {
                        try {
                            stock[index] = Integer.parseInt(p[1]);
                        } catch (NumberFormatException e) {
                            // Ignore invalid stock values.
                        }
                    }
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            // Stock starts at zero on the first run.
        }
    }
}
