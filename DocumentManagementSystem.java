import java.util.*;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DocumentManagementSystem {
    // Custom hash table with separate chaining
    private static class HashTable {
        // Node for linked list in each bucket
        private class DocumentNode {
            Document document;
            DocumentNode next;

            DocumentNode(Document document) {
                this.document = document;
                this.next = null;
            }
        }

        // Internal array of linked list buckets
        private DocumentNode[] buckets;
        private int size;
        private static final int DEFAULT_CAPACITY = 16;

        public HashTable() {
            buckets = new DocumentNode[DEFAULT_CAPACITY];
            size = 0;
        }

        // Generate hash code for document ID
        private int hash(String key) {
            return Math.abs(key.hashCode()) % buckets.length;
        }

        // Add document to hash table
        public void add(Document document) {
            // Resize if load factor exceeds threshold
            if ((double) size / buckets.length > 0.75) {
                resize();
            }

            int index = hash(document.id);
            DocumentNode newNode = new DocumentNode(document);

            // Handle collision with separate chaining
            if (buckets[index] == null) {
                buckets[index] = newNode;
            } else {
                DocumentNode current = buckets[index];
                // Check for duplicate before adding
                while (current != null) {
                    if (current.document.id.equals(document.id)) {
                        System.out.println("Document with ID already exists!");
                        return;
                    }
                    if (current.next == null) break;
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        }

        // Resize hash table when load factor is high
        private void resize() {
            DocumentNode[] oldBuckets = buckets;
            buckets = new DocumentNode[oldBuckets.length * 2];
            size = 0;

            for (DocumentNode bucket : oldBuckets) {
                DocumentNode current = bucket;
                while (current != null) {
                    add(current.document);
                    current = current.next;
                }
            }
        }

        // Search for document by ID
        public Document search(String id) {
            int index = hash(id);
            DocumentNode current = buckets[index];

            while (current != null) {
                if (current.document.id.equals(id)) {
                    return current.document;
                }
                current = current.next;
            }
            return null;
        }

        // Delete document by ID
        public boolean delete(String id) {
            int index = hash(id);
            DocumentNode current = buckets[index];
            DocumentNode prev = null;

            while (current != null) {
                if (current.document.id.equals(id)) {
                    // Remove first node
                    if (prev == null) {
                        buckets[index] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    return true;
                }
                prev = current;
                current = current.next;
            }
            return false;
        }

        // List all documents
        public void listDocuments() {
            if (size == 0) {
                System.out.println("No documents in the system.");
                return;
            }

            for (DocumentNode bucket : buckets) {
                DocumentNode current = bucket;
                while (current != null) {
                    System.out.println(current.document);
                    current = current.next;
                }
            }
        }
    }

    // Document class to store metadata
    private static class Document {
        String id;
        String title;
        String author;
        String filePath;
        LocalDateTime creationDate;

        Document(String title, String author, String filePath) {
            this.id = generateUniqueId(title, filePath);
            this.title = title;
            this.author = author;
            this.filePath = filePath;
            this.creationDate = LocalDateTime.now();
        }

        // Generate unique ID using MD5 hash
        private String generateUniqueId(String title, String filePath) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                String input = title + filePath + System.currentTimeMillis();
                byte[] hashBytes = md.digest(input.getBytes());
                
                // Convert to hexadecimal
                StringBuilder hexString = new StringBuilder();
                for (byte hashByte : hashBytes) {
                    String hex = Integer.toHexString(0xFF & hashByte);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                return hexString.toString().substring(0, 10);
            } catch (NoSuchAlgorithmException e) {
                // Fallback to simple hash if MD5 is unavailable
                return String.valueOf(Math.abs((title + filePath).hashCode()));
            }
        }

        @Override
        public String toString() {
            return "Document{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", creationDate=" + creationDate +
                    '}';
        }
    }

    private HashTable documentSystem;
    private Scanner scanner;

    public DocumentManagementSystem() {
        documentSystem = new HashTable();
        scanner = new Scanner(System.in);
    }

    // Add a new document
    public void addDocument() {
        System.out.print("Enter document title: ");
        String title = scanner.nextLine();

        System.out.print("Enter document author: ");
        String author = scanner.nextLine();

        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine();

        Document newDocument = new Document(title, author, filePath);
        documentSystem.add(newDocument);
        System.out.println("Document added. Unique ID: " + newDocument.id);
    }

    // Search for a document
    public void searchDocument() {
        System.out.print("Enter document ID to search: ");
        String id = scanner.nextLine();

        Document doc = documentSystem.search(id);
        if (doc != null) {
            System.out.println("Document found: " + doc);
        } else {
            System.out.println("Document not found.");
        }
    }

    // Delete a document
    public void deleteDocument() {
        System.out.print("Enter document ID to delete: ");
        String id = scanner.nextLine();

        if (documentSystem.delete(id)) {
            System.out.println("Document deleted successfully.");
        } else {
            System.out.println("Document not found.");
        }
    }

    // List all documents
    public void listDocuments() {
        documentSystem.listDocuments();
    }

    // Main menu
    public void run() {
        while (true) {
            System.out.println("\n--- Document Management System ---");
            System.out.println("1. Add Document");
            System.out.println("2. Search Document");
            System.out.println("3. Delete Document");
            System.out.println("4. List All Documents");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addDocument();
                    break;
                case "2":
                    searchDocument();
                    break;
                case "3":
                    deleteDocument();
                    break;
                case "4":
                    listDocuments();
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
        new DocumentManagementSystem().run();
    }
}