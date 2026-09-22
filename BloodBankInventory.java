import java.util.Scanner;

public class BloodBankInventory {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] donorNames = new String[5];
        String[] bloodGroups = new String[5];

        int count = 0;

        System.out.println("=== Blood Bank Inventory & Donor Matcher ===");

        // Add Donors
        System.out.println("Enter number of donors: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Donor " + (i + 1));

            System.out.println("Enter Donor Name: ");
            donorNames[i] = sc.nextLine();

            System.out.println("Enter Blood Group (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
            bloodGroups[i] = sc.nextLine();

            count++;
        }

        // Display Inventory
        System.out.println("=== Blood Inventory ===");
        for (int i = 0; i < count; i++) {
            System.out.println(donorNames[i] + " - " + bloodGroups[i]);
        }

        // Match Donor
        System.out.println("Enter Required Blood Group: ");
        String requiredGroup = sc.nextLine();

        boolean found = false;

        System.out.println("Matching Donors:");

        for (int i = 0; i < count; i++) {
            if (bloodGroups[i].equalsIgnoreCase(requiredGroup)) {
                System.out.println("Donor Name: " + donorNames[i]);
                System.out.println("Blood Group: " + bloodGroups[i]);
                System.out.println("----------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching donor found.");
        }

        sc.close();
    }
}