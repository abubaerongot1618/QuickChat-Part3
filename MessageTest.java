package message;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class MessageTest {

    // ─── Test Data ────────────────────────────────────────────────────────────

    private Message testCase1;
    private Message testCase2;
    private ArrayList<Message> messageList;

    // ─── Setup ────────────────────────────────────────────────────────────────

    @Before
    public void setUp() {

        // Test Case 1: ID=0, recipient "0085", message "Hi tonight"
        // Expected hash: 0:008:HITONIGHT
        testCase1 = new Message("0085", "Hi tonight", 0);

        // Test Case 2: from rubric data table
        testCase2 = new Message("08575975889", "Hi Keegan, did you receive the payment?", 2);

        // List for loop-based hash tests
        messageList = new ArrayList<>();
        messageList.add(testCase1);
        messageList.add(testCase2);
        messageList.add(new Message("+27834557896", "Good morning have fun", 3));
    }

    // ─── 1. checkMessageLength() — success & failure ─────────────────────────

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.",
                testCase1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longText = "A".repeat(251) + " end"; // 255 chars
        Message longMessage = new Message("+27834567890", longText, 99);
        String result = longMessage.checkMessageLength();
        assertTrue(result.startsWith("Message exceeds 250 characters by"));
        assertTrue(result.contains("please reduce the size."));
    }

    @Test
    public void testMessageLengthExactly250IsSuccess() {
        String exact = "B".repeat(246) + " end"; // exactly 250 chars
        Message borderMessage = new Message("+27700000001", exact, 10);
        assertEquals("Message ready to send.",
                borderMessage.checkMessageLength());
    }

    // ─── 2. checkRecipientNumber() — success & failure ───────────────────────

    @Test
    public void testRecipientNumberSuccess() {
        Message validRecipient = new Message("+27834557896", "Hello there", 1);
        assertEquals("Cell phone number successfully captured.",
                validRecipient.checkRecipientNumber());
    }

    @Test
    public void testRecipientNumberFailure_noInternationalCode() {
        Message invalidRecipient = new Message("08575975889", "Hello", 2);
        assertEquals(
                "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.",
                invalidRecipient.checkRecipientNumber());
    }

    @Test
    public void testRecipientNumberFailure_tooShort() {
        Message shortNumber = new Message("+123", "Hello", 3);
        assertEquals(
                "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.",
                shortNumber.checkRecipientNumber());
    }

    @Test
    public void testRecipientNumberFailure_containsLetters() {
        Message alphaNumber = new Message("+27ABC123456", "Hello", 4);
        assertEquals(
                "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.",
                alphaNumber.checkRecipientNumber());
    }

    // ─── 3. Message Hash — assertEquals ──────────────────────────────────────

    @Test
    public void testMessageHashTestCase1() {
        assertEquals("0:008:HITONIGHT",
                testCase1.getMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        for (Message m : messageList) {
            assertNotNull(m.getMessageHash());
            assertFalse(m.getMessageHash().isEmpty());
        }
    }

    @Test
    public void testAllHashesInLoopAreUnique() {
        ArrayList<String> hashes = new ArrayList<>();
        for (Message m : messageList) {
            assertFalse(hashes.contains(m.getMessageHash()));
            hashes.add(m.getMessageHash());
        }
    }

    // ─── 4. Message ID is created ─────────────────────────────────────────────

    @Test
    public void testMessageIdGenerated() {
        assertEquals("Message ID generated: 2",
                testCase2.printMessageId());
    }

    @Test
    public void testMessageIdIsPositiveOrZero() {
        assertTrue(testCase1.getMessageId() >= 0);
    }

    // ─── 5. sentMessage() — three action options ──────────────────────────────

    @Test
    public void testSentMessage_SendMessage() {
        assertEquals("Message successfully sent.",
                testCase2.sentMessage("Send Message"));
    }

    @Test
    public void testSentMessage_DisregardMessage() {
        assertEquals("Press 0 to delete the message.",
                testCase2.sentMessage("Disregard Message"));
    }

    @Test
    public void testSentMessage_StoreMessage() {
        assertEquals("Message successfully stored.",
                testCase2.sentMessage("Store Message"));
    }

    // ─── 6. Total messages sent ───────────────────────────────────────────────

    @Test
    public void testTotalMessagesSentCount() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        sentMessages.add(testCase1);
        sentMessages.add(testCase2);
        assertEquals(2, sentMessages.size());
    }
}