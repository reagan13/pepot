import java.util.*;

public class SupportTicketSystem {
    private Map<String, Ticket> tickets;
    private Scanner scanner;

    public SupportTicketSystem() {
        tickets = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    // Ticket class to store ticket details
    private class Ticket {
        
        String id;
        String customerName;
        String issueDescription;
        String status;

        Ticket(String id, String customerName, String issueDescription) {
            this.id = id;
            this.customerName = customerName;
            this.issueDescription = issueDescription;
            this.status = "New";
        }

        @Override
        public String toString() {
            return "Ticket{" +
                    "ID='" + id + '\'' +
                    ", Customer='" + customerName + '\'' +
                    ", Issue='" + issueDescription + '\'' +
                    ", Status='" + status + '\'' +
                    '}';
        }
    }

    // Add a new ticket
    public void addTicket() {
        System.out.print("Enter Ticket ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter Issue Description: ");
        String issueDescription = scanner.nextLine();

        Ticket newTicket = new Ticket(id, customerName, issueDescription);
        tickets.put(id, newTicket);
        System.out.println("Ticket added successfully!");
    }

    // Update an existing ticket
    public void updateTicket() {
        System.out.print("Enter Ticket ID to update: ");
        String id = scanner.nextLine();

        Ticket ticket = tickets.get(id);
        if (ticket == null) {
            System.out.println("Ticket not found!");
            return;
        }

        System.out.println("Current Ticket Details: " + ticket);
        
        System.out.print("Enter new status (or press Enter to skip): ");
        String newStatus = scanner.nextLine();
        if (!newStatus.isEmpty()) {
            ticket.status = newStatus;
        }

        System.out.println("Ticket updated successfully!");
    }

    // Search for a ticket by ID
    public void searchTicket() {
        System.out.print("Enter Ticket ID to search: ");
        String id = scanner.nextLine();

        Ticket ticket = tickets.get(id);
        if (ticket == null) {
            System.out.println("Ticket not found!");
        } else {
            System.out.println("Ticket Details: " + ticket);
        }
    }

    // List all tickets
    public void listAllTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No tickets found.");
            return;
        }

        System.out.println("All Tickets:");
        for (Ticket ticket : tickets.values()) {
            System.out.println(ticket);
        }
    }

    // Main menu
    public void run() {
        while (true) {
            System.out.println("\n--- Support Ticket System ---");
            System.out.println("1. Add Ticket");
            System.out.println("2. Update Ticket");
            System.out.println("3. Search Ticket");
            System.out.println("4. List All Tickets");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addTicket();
                    break;
                case "2":
                    updateTicket();
                    break;
                case "3":
                    searchTicket();
                    break;
                case "4":
                    listAllTickets();
                    break;
                case "5":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        new SupportTicketSystem().run();
    }
}