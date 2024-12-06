import java.util.*;

public class EventGuestManager {
    private Map<String, Set<Guest>> events;
    private Scanner scanner;

    public EventGuestManager() {
        events = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    // Guest class to represent unique attendees
    private class Guest {
        String email;
        String id;

        Guest(String email, String id) {
            this.email = email;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Guest guest = (Guest) o;
            return Objects.equals(email, guest.email) || Objects.equals(id, guest.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, id);
        }

        @Override
        public String toString() {
            return "Guest{email='" + email + "', id='" + id + "'}";
        }
    }

    // Add guest to a specific event
    public void addGuest() {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        
        System.out.print("Enter guest email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter guest ID: ");
        String id = scanner.nextLine();

        events.computeIfAbsent(eventName, k -> new HashSet<>())
               .add(new Guest(email, id));
        
        System.out.println("Guest added to " + eventName);
    }

    // Remove guest from a specific event
    public void removeGuest() {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        
        System.out.print("Enter guest email or ID: ");
        String identifier = scanner.nextLine();

        Set<Guest> eventGuests = events.get(eventName);
        if (eventGuests != null) {
            eventGuests.removeIf(guest -> 
                guest.email.equals(identifier) || guest.id.equals(identifier));
            System.out.println("Guest removed from " + eventName);
        }
    }

    // Check attendance for a guest
    public void checkAttendance() {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        
        System.out.print("Enter guest email or ID: ");
        String identifier = scanner.nextLine();

        Set<Guest> eventGuests = events.get(eventName);
        if (eventGuests != null) {
            boolean isAttending = eventGuests.stream()
                .anyMatch(guest -> guest.email.equals(identifier) || guest.id.equals(identifier));
            System.out.println("Guest " + (isAttending ? "is" : "is not") + " registered");
        }
    }

    // List all guests for an event
    public void listGuests() {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();

        Set<Guest> eventGuests = events.get(eventName);
        if (eventGuests != null) {
            System.out.println("Guests for " + eventName + ":");
            eventGuests.forEach(System.out::println);
        }
    }

    // Perform set operations between events
    public void setOperations() {
        System.out.println("1. Union\n2. Intersection\n3. Difference");
        System.out.print("Choose operation: ");
        String op = scanner.nextLine();

        System.out.print("Enter first event name: ");
        String event1 = scanner.nextLine();
        
        System.out.print("Enter second event name: ");
        String event2 = scanner.nextLine();

        Set<Guest> set1 = events.get(event1);
        Set<Guest> set2 = events.get(event2);

        if (set1 == null || set2 == null) {
            System.out.println("One or both events not found");
            return;
        }

        Set<Guest> result;
        switch (op) {
            case "1": // Union
                result = new HashSet<>(set1);
                result.addAll(set2);
                break;
            case "2": // Intersection
                result = new HashSet<>(set1);
                result.retainAll(set2);
                break;
            case "3": // Difference
                result = new HashSet<>(set1);
                result.removeAll(set2);
                break;
            default:
                System.out.println("Invalid operation");
                return;
        }

        System.out.println("Result guests:");
        result.forEach(System.out::println);
    }

    // Main menu
    public void run() {
        while (true) {
            System.out.println("\n--- Event Guest Management ---");
            System.out.println("1. Add Guest\n2. Remove Guest");
            System.out.println("3. Check Attendance\n4. List Guests");
            System.out.println("5. Set Operations\n6. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addGuest(); break;
                case "2": removeGuest(); break;
                case "3": checkAttendance(); break;
                case "4": listGuests(); break;
                case "5": setOperations(); break;
                case "6": return;
                default: System.out.println("Invalid option");
            }
        }
    }

    public static void main(String[] args) {
        new EventGuestManager().run();
    }
}