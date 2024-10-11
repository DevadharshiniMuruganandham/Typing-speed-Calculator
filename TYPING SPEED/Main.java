import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Main class
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypingTestGUI());
    }
}

// Class containing the text to be typed
class Text {
    public static String text = "As technology advances, we are seeing a shift towards a more interconnected world. "
            + "The internet has become a ubiquitous presence in our daily lives, connecting us with people and information "
            + "from all over the globe. With this increased connectivity comes a wealth of opportunities for businesses and "
            + "individuals alike. For example, e-commerce sales are expected to reach $4.9 trillion by 2021, a staggering "
            + "number that showcases the growth and importance of online shopping. Additionally, social media platforms "
            + "like Facebook and Instagram are playing a larger role in our lives, with over 2.7 billion active users "
            + "across both platforms. These technological advancements have also led to the creation of new industries, "
            + "such as big data analysis and cybersecurity, both of which are crucial to ensuring the stability and "
            + "security of our connected world. In this digital age, it's important to embrace the opportunities that come "
            + "with new technologies while also being mindful of the challenges they present. After all, with great power "
            + "comes great responsibility.";
}

// GUI Class for the Typing Test
class TypingTestGUI extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JTextArea inputArea;
    private JButton startButton;
    private JButton timeButton;
    private JLabel resultLabel;
    private JSpinner timeSpinner;
    private Timer timer; // Timer for the specified time mode
    private long startTime; // To track the time for the test

    public TypingTestGUI() {
        setTitle("Typing Test");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text Area for the prompt text
        textArea = new JTextArea();
        textArea.setText(Text.text);
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Input Area for user input
        inputArea = new JTextArea();
        inputArea.setBorder(BorderFactory.createTitledBorder("Type Here:"));
        add(new JScrollPane(inputArea), BorderLayout.SOUTH);

        // Panel for buttons and time input
        JPanel panel = new JPanel();
        startButton = new JButton("Type Full Text");
        timeButton = new JButton("Type for Specified Time");
        timeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1)); // Time limit in minutes

        // Add action listeners
        startButton.addActionListener(this);
        timeButton.addActionListener(this);

        panel.add(startButton);
        panel.add(timeButton);
        panel.add(new JLabel("Minutes:"));
        panel.add(timeSpinner);
        add(panel, BorderLayout.NORTH);

        // Result Label
        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultLabel, BorderLayout.EAST);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startFullTextTest();
        } else if (e.getSource() == timeButton) {
            startTimedTest();
        }
    }

    private void startFullTextTest() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please type the text.");
            return;
        }

        long startTime = System.currentTimeMillis();
        int correctCount = calculateCorrectCharacters(input);
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // seconds

        double typingSpeed = (double) correctCount / elapsedTime;
        int accuracy = (int) ((double) correctCount / input.length() * 100);

        displayResult(typingSpeed, accuracy);
    }

    private void startTimedTest() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please type the text.");
            return;
        }

        // Reset input area and result label
        inputArea.setText("");
        resultLabel.setText("");

        int timeLimit = (int) timeSpinner.getValue();
        long timeInMillis = timeLimit * 60 * 1000; // Convert to milliseconds

        // Start a timer for the specified time
        timer = new Timer((int) timeInMillis, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop(); // Stop the timer
                int correctCount = calculateCorrectCharacters(inputArea.getText());
                double typingSpeed = (double) correctCount / (timeLimit * 60); // Speed in characters per second
                int accuracy = (int) ((double) correctCount / Text.text.length() * 100);
                displayResult(typingSpeed, accuracy);
            }
        });

        timer.setRepeats(false); // Execute only once
        timer.start(); // Start the timer
    }

    private int calculateCorrectCharacters(String input) {
        int correct = 0;
        for (int i = 0; i < Text.text.length(); i++) {
            if (i < input.length() && input.charAt(i) == Text.text.charAt(i)) {
                correct++;
            }
        }
        return correct;
    }

    private void displayResult(double typingSpeed, int accuracy) {
        String result = String.format("Typing Speed: %.2f characters per second\nAccuracy: %d%%",
                typingSpeed, accuracy);
        resultLabel.setText(result);
    }
}
