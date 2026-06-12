package javaapplication18;

import java.util.ArrayList;
import java.util.Scanner;
import message.Message;

public class JavaApplication18 {

    private static final ArrayList<Message> messages = new ArrayList<>();
    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();
    private static final ArrayList<String> messageRecipients = new ArrayList<>();

    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;
    private static int totalMessagesToSend = 0;
    private static int messageCount = 0;

    public static void main(String[] args) {

        System.out.println("Welcome to QuickChat");

        login();

        if (isLoggedIn) {

            System.out.println("How many messages do you wish to enter? ");
            totalMessagesToSend = scanner.nextInt();
            scanner.nextLine();

            int choice;

            do {

                displayMenu();

                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {

                    sendMessages();

                } else if (choice == 2) {

                    showMessages();

                } else if (choice == 3) {

                    System.out.println("Goodbye!");

                } else if (choice == 4) {

                    loadStoredMessagesFromJson();
                    storedMessagesMenu();

                } else {

                    System.out.println("Invalid option.");
                }

            } while (choice != 3);
        }
    }

    private static void login() {

        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        if (username.equals("abu") && password.equals("abu18")) {

            isLoggedIn = true;
            System.out.println("Login successful!");

        } else {

            System.out.println("Login failed.");
            login();
        }
    }

    private static void displayMenu() {

        System.out.println("\n--- QuickChat Menu ---");
        System.out.println("1) Send Messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
        System.out.println("4) Stored Messages");
        System.out.println("Choose option: ");
    }

    private static void sendMessages() {

        for (int i = messageCount; i < totalMessagesToSend; i++) {

            System.out.println("\n--- Message " + (i + 1) + " ---");

            System.out.println("Enter recipient number: ");
            String recipient = scanner.nextLine();

            String messageText;

            do {

                System.out.println("Enter message (max 250 characters): ");
                messageText = scanner.nextLine();

                if (messageText.length() > 250) {

                    System.out.println("Message too long.");
                }

            } while (messageText.length() > 250);

            Message message = new Message(recipient, messageText, messageCount + 1);

            messages.add(message);

            displayMessageDetails(message);

            handleMessageOptions(message);

            messageCount++;
        }

        System.out.println("\nTotal messages sent: " + messageCount);
    }

    private static void handleMessageOptions(Message message) {

        System.out.println("\n1) Send");
        System.out.println("2) Delete");
        System.out.println("3) Store");
        System.out.print("Choose option: ");

        String option = scanner.nextLine();

        if (option.equals("1")) {

            System.out.println("Message successfully sent");

        } else if (option.equals("2")) {

            messages.remove(message);
            System.out.println("Message deleted");

        } else if (option.equals("3")) {

            System.out.println("Message stored");

        } else {

            System.out.println("Invalid option");
        }
    }

    private static void displayMessageDetails(Message message) {

        System.out.println("\n--- Message Details ---");
        System.out.println("Message ID: " + message.getMessageId());
        System.out.println("Message Hash: " + message.getMessageHash());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Message: " + message.getMessageText());
    }

    private static void showMessages() {

        if (messages.isEmpty()) {

            System.out.println("No messages found.");

        } else {

            for (Message message : messages) {

                System.out.println("\n--- Stored Message ---");
                System.out.println("Recipient: " + message.getRecipient());
                System.out.println("Message: " + message.getMessageText());
            }
        }
    }

    private static void loadStoredMessagesFromJson() {
        
    try {
        java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.FileReader("C:/NetBeansProjects/JavaApplication18/messages.json"));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        String json = sb.toString().trim();
        json = json.substring(1, json.length() - 1);

        String[] entries = json.split("\\},\\s*\\{");

        for (String entry : entries) {
            entry = entry.replace("{", "").replace("}", "");

            String recipient = extractValue(entry, "recipient");
            String message = extractValue(entry, "message");
            String idStr = extractValue(entry, "id");

            storedMessages.add(message);
            messageRecipients.add(recipient);
            messageIDs.add(idStr);
            messageHashes.add(idStr + ":" + recipient.substring(0, 3) + ":" + message.split(" ")[0].toUpperCase() + message.split(" ")[message.split(" ").length - 1].toUpperCase());
        }

    } catch (Exception e) {
        System.out.println("Could not load stored messages: " + e.getMessage());
    }

     } 

    private static void storedMessagesMenu() {

        int choice;

        do {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search messages by recipient");
            System.out.println("5) Delete a message using message hash");
            System.out.println("6) Display report");
            System.out.println("7) Back to menu");
            System.out.println("Choose option: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {

                for (int i = 0; i < storedMessages.size(); i++) {
                    System.out.println("ID: " + messageIDs.get(i) + " | Message: " + storedMessages.get(i));
                }

            } else if (choice == 2) {

                String longest = "";
                for (String msg : storedMessages) {
                    if (msg.length() > longest.length()) {
                        longest = msg;
                    }
                }
                System.out.println("Longest message: " + longest);

            } else if (choice == 3) {

                System.out.println("Enter message ID: ");
                String searchId = scanner.nextLine();
                boolean found = false;

                for (int i = 0; i < messageIDs.size(); i++) {
                    if (messageIDs.get(i).equals(searchId)) {
                        System.out.println("Message: " + storedMessages.get(i));
                        found = true;
                    }
                }

                if (!found) System.out.println("Message ID not found.");

            } else if (choice == 4) {

                System.out.println("Enter recipient number: ");
                String searchRecipient = scanner.nextLine();
                boolean found = false;

                for (int i = 0; i < storedMessages.size(); i++) {
                    if (messageRecipients.get(i).equals(searchRecipient)) {
                        System.out.println("Message: " + storedMessages.get(i));
                        found = true;
                    }
                }

                if (!found) System.out.println("No messages found for that recipient.");

            } else if (choice == 5) {

                System.out.println("Enter message hash to delete: ");
                String searchHash = scanner.nextLine();
                boolean found = false;

                for (int i = 0; i < messageHashes.size(); i++) {
                    if (messageHashes.get(i).equals(searchHash)) {
                        System.out.println("Message: \"" + storedMessages.get(i) + "\" successfully deleted.");
                        storedMessages.remove(i);
                        messageHashes.remove(i);
                        messageIDs.remove(i);
                        messageRecipients.remove(i);
                        found = true;
                        break;
                    }
                }

                if (!found) System.out.println("Hash not found.");

            } else if (choice == 6) {

                System.out.println("\n--- Stored Messages Report ---");
                for (int i = 0; i < storedMessages.size(); i++) {
                    System.out.println("Hash: " + messageHashes.get(i));
                    System.out.println("---");
                }
            }

        } while (choice != 7);
    }

  private static String extractValue(String entry, String key) {
    String search = "\"" + key + "\"";
    int idx = entry.indexOf(search);
    if (idx == -1) return "";
    int colon = entry.indexOf(":", idx);
    String value = entry.substring(colon + 1).trim();
    value = value.split(",")[0].trim();
    value = value.replace("\"", "").trim();
    return value;

  }
}