import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class AssignmentCorrectorApp {
    // Main components
    private JFrame mainFrame;
    private JTextArea assignmentTextArea;
    private JTextArea resultTextArea;
    private JProgressBar progressBar;
    private JLabel scoreLabel;
    private JLabel plagiarismLabel;
    private JLabel similarityLabel;

    // Sample data for plagiarism checking
    private List<String> sampleSources = Arrays.asList(
            "Artificial Intelligence is the simulation of human intelligence processes by machines.",
            "Machine learning is a subset of AI that enables systems to learn and improve from experience.",
            "AI systems can perform tasks such as visual perception, speech recognition, and decision-making.",
            "Natural Language Processing allows computers to understand and interpret human language.",
            "Deep learning uses neural networks with multiple layers to analyze various factors of data."
    );

    // Model answers for different topics
    private Map<String, String> modelAnswers = new HashMap<>();

    public AssignmentCorrectorApp() {
        initializeModelAnswers();
        createAndShowGUI();
    }

    private void initializeModelAnswers() {
        modelAnswers.put("AI Fundamentals",
                "Artificial Intelligence (AI) refers to the simulation of human intelligence in machines. " +
                        "These systems are programmed to think like humans and mimic their actions. " +
                        "Key components include learning, reasoning, problem-solving, perception, and language understanding. " +
                        "Machine learning, a subset of AI, focuses on algorithms that improve automatically through experience.");

        modelAnswers.put("Machine Learning",
                "Machine Learning is a method of data analysis that automates analytical model building. " +
                        "It is a branch of artificial intelligence based on the idea that systems can learn from data, " +
                        "identify patterns and make decisions with minimal human intervention. " +
                        "Common approaches include supervised learning, unsupervised learning, and reinforcement learning.");

        modelAnswers.put("Data Science",
                "Data Science is an interdisciplinary field that uses scientific methods, processes, " +
                        "algorithms and systems to extract knowledge and insights from structured and unstructured data. " +
                        "It employs techniques and theories drawn from many fields within mathematics, statistics, " +
                        "computer science, and information science.");
    }

    private void createAndShowGUI() {
        // Create main frame
        mainFrame = new JFrame("AI Assignment Corrector");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout(10, 10));

        // Create header
        JPanel headerPanel = createHeaderPanel();
        mainFrame.add(headerPanel, BorderLayout.NORTH);

        // Create main content area
        JPanel contentPanel = createContentPanel();
        mainFrame.add(contentPanel, BorderLayout.CENTER);

        // Create results panel
        JPanel resultsPanel = createResultsPanel();
        mainFrame.add(resultsPanel, BorderLayout.SOUTH);

        // Configure frame
        mainFrame.setSize(900, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Title
        JLabel titleLabel = new JLabel("AI Assignment Corrector", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Intelligent Assignment Evaluation with Plagiarism Detection", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 2, 15, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left panel - Input
        JPanel inputPanel = createInputPanel();
        contentPanel.add(inputPanel);

        // Right panel - Results
        JPanel outputPanel = createOutputPanel();
        contentPanel.add(outputPanel);

        return contentPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBorder(new TitledBorder(
                new LineBorder(new Color(52, 152, 219), 2),
                "Assignment Input",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(52, 152, 219)
        ));

        // Topic selection
        JPanel topicPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topicPanel.add(new JLabel("Topic:"));
        JComboBox<String> topicComboBox = new JComboBox<>(new String[]{
                "AI Fundamentals", "Machine Learning", "Data Science"
        });
        topicPanel.add(topicComboBox);

        // Assignment text area
        assignmentTextArea = new JTextArea(15, 30);
        assignmentTextArea.setLineWrap(true);
        assignmentTextArea.setWrapStyleWord(true);
        assignmentTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Set sample text
        assignmentTextArea.setText("Enter your assignment here...\n\nExample:\nArtificial Intelligence is the simulation of human intelligence in machines. AI systems can perform tasks like learning, problem-solving, and pattern recognition. Machine learning is a subset of AI that focuses on algorithms improving through experience.");

        JScrollPane scrollPane = new JScrollPane(assignmentTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Control buttons - Using custom styled buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton correctButton = createStyledButton("Correct Assignment",
                new Color(46, 204, 113), Color.WHITE, true); // Green
        correctButton.addActionListener(e -> correctAssignment(
                topicComboBox.getSelectedItem().toString()
        ));

        JButton clearButton = createStyledButton("Clear",
                new Color(231, 76, 60), Color.WHITE, false); // Red
        clearButton.addActionListener(e -> {
            assignmentTextArea.setText("");
            resultTextArea.setText("");
            resetMetrics();
        });

        buttonPanel.add(correctButton);
        buttonPanel.add(clearButton);

        // Add components to input panel
        inputPanel.add(topicPanel, BorderLayout.NORTH);
        inputPanel.add(scrollPane, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputPanel;
    }

    // Custom method to create properly styled buttons
    private JButton createStyledButton(String text, Color bgColor, Color fgColor, boolean isBold) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw border
                g2.setColor(bgColor.darker());
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                // Draw text
                g2.setColor(fgColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(150, 40));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private JPanel createOutputPanel() {
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout(10, 10));
        outputPanel.setBorder(new TitledBorder(
                new LineBorder(new Color(155, 89, 182), 2),
                "Correction Results",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(155, 89, 182)
        ));

        // Results text area
        resultTextArea = new JTextArea(15, 30);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultTextArea.setEditable(false);

        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Metrics panel
        JPanel metricsPanel = createMetricsPanel();

        outputPanel.add(resultScrollPane, BorderLayout.CENTER);
        outputPanel.add(metricsPanel, BorderLayout.SOUTH);

        return outputPanel;
    }

    private JPanel createMetricsPanel() {
        JPanel metricsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        metricsPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(241, 196, 15), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        metricsPanel.setBackground(new Color(253, 227, 167));

        // Score
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scoreLabel = new JLabel("Score: --/100");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(44, 62, 80));
        scorePanel.add(scoreLabel);

        // Plagiarism
        JPanel plagiarismPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        plagiarismLabel = new JLabel("Plagiarism: --%");
        plagiarismLabel.setFont(new Font("Arial", Font.BOLD, 14));
        plagiarismPanel.add(plagiarismLabel);

        // Similarity
        JPanel similarityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        similarityLabel = new JLabel("Similarity: --%");
        similarityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        similarityPanel.add(similarityLabel);

        // Progress bar
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
        progressBar.setValue(0);
        progressBar.setString("Waiting for analysis...");
        progressPanel.add(new JLabel("Analysis Progress:"), BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        metricsPanel.add(scorePanel);
        metricsPanel.add(plagiarismPanel);
        metricsPanel.add(similarityPanel);
        metricsPanel.add(progressPanel);

        return metricsPanel;
    }

    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        resultsPanel.setBackground(new Color(236, 240, 241));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel footerLabel = new JLabel("AI Assignment Corrector v1.0 - Powered by Java Swing");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(127, 140, 141));

        resultsPanel.add(footerLabel);
        return resultsPanel;
    }

    private void correctAssignment(String topic) {
        String submission = assignmentTextArea.getText().trim();

        if (submission.isEmpty() || submission.equals("Enter your assignment here...")) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter your assignment text!",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset progress bar
        progressBar.setValue(0);
        progressBar.setString("Analyzing...");

        // Simulate analysis progress
        simulateProgress();

        // Perform analysis
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Analyze the assignment
                AnalysisResult result = analyzeAssignment(submission, topic);

                // Update UI in EDT
                SwingUtilities.invokeLater(() -> displayResults(result));
                return null;
            }
        };

        worker.execute();
    }

    private void simulateProgress() {
        // Use javax.swing.Timer explicitly to avoid ambiguity
        javax.swing.Timer timer = new javax.swing.Timer(50, null);
        timer.addActionListener(new ActionListener() {
            int progress = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 2;
                progressBar.setValue(progress);
                progressBar.setString("Analyzing... " + progress + "%");

                if (progress >= 100) {
                    progressBar.setString("Analysis Complete!");
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private AnalysisResult analyzeAssignment(String submission, String topic) {
        AnalysisResult result = new AnalysisResult();

        // Calculate plagiarism score
        result.plagiarismScore = calculatePlagiarismScore(submission);

        // Calculate similarity with model answer
        String modelAnswer = modelAnswers.get(topic);
        result.similarityScore = calculateSimilarity(submission, modelAnswer);

        // Calculate overall score
        result.overallScore = calculateOverallScore(submission, result.plagiarismScore, result.similarityScore);

        // Generate feedback
        result.feedback = generateFeedback(submission, result.overallScore, result.plagiarismScore);

        // Identify mistakes
        result.mistakes = identifyMistakes(submission);

        return result;
    }

    private double calculatePlagiarismScore(String submission) {
        String cleanSubmission = submission.toLowerCase();
        double maxSimilarity = 0;

        for (String source : sampleSources) {
            double similarity = calculateTextSimilarity(cleanSubmission, source.toLowerCase());
            maxSimilarity = Math.max(maxSimilarity, similarity);
        }

        return maxSimilarity * 100;
    }

    private double calculateSimilarity(String text1, String text2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.toLowerCase().split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.toLowerCase().split("\\s+")));

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size() * 100;
    }

    private double calculateTextSimilarity(String text1, String text2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.split("\\s+")));

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    private double calculateOverallScore(String submission, double plagiarismScore, double similarityScore) {
        double baseScore = 70;

        // Adjust based on length
        int wordCount = submission.split("\\s+").length;
        if (wordCount < 50) baseScore -= 20;
        else if (wordCount > 150) baseScore += 10;

        // Adjust based on plagiarism (penalize high plagiarism)
        if (plagiarismScore > 50) baseScore -= 30;
        else if (plagiarismScore > 25) baseScore -= 15;

        // Adjust based on similarity with model answer
        if (similarityScore > 70) baseScore += 15;
        else if (similarityScore < 30) baseScore -= 10;

        // Ensure score is within bounds
        return Math.max(0, Math.min(100, baseScore));
    }

    private String generateFeedback(String submission, double score, double plagiarismScore) {
        StringBuilder feedback = new StringBuilder();

        feedback.append("=== ASSIGNMENT ANALYSIS REPORT ===\n\n");

        // Overall assessment
        if (score >= 90) {
            feedback.append("🌟 EXCELLENT WORK! Outstanding understanding and presentation.\n\n");
        } else if (score >= 80) {
            feedback.append("✅ VERY GOOD! Solid comprehension with minor areas for improvement.\n\n");
        } else if (score >= 70) {
            feedback.append("📝 GOOD EFFORT! Demonstrates understanding but needs refinement.\n\n");
        } else if (score >= 60) {
            feedback.append("⚠️ SATISFACTORY! Basic understanding shown, significant improvement needed.\n\n");
        } else {
            feedback.append("❌ NEEDS IMPROVEMENT! Fundamental concepts require more study.\n\n");
        }

        // Content feedback
        feedback.append("CONTENT ANALYSIS:\n");
        feedback.append("• Word Count: ").append(submission.split("\\s+").length).append(" words\n");

        if (submission.length() < 200) {
            feedback.append("• 📋 Consider expanding your content with more details and examples\n");
        } else {
            feedback.append("• 📋 Good content length with adequate detail\n");
        }

        if (submission.toLowerCase().contains("artificial intelligence") ||
                submission.toLowerCase().contains("ai")) {
            feedback.append("• ✅ Appropriate use of technical terminology\n");
        } else {
            feedback.append("• ❌ Include more technical terms relevant to the topic\n");
        }

        // Plagiarism feedback
        feedback.append("\nORIGINALITY CHECK:\n");
        if (plagiarismScore < 10) {
            feedback.append("• ✅ Excellent originality - minimal similarity with known sources\n");
        } else if (plagiarismScore < 25) {
            feedback.append("• ⚠️ Acceptable originality - some common phrases detected\n");
        } else if (plagiarismScore < 50) {
            feedback.append("• ⚠️ Moderate similarity - ensure proper citation of sources\n");
        } else {
            feedback.append("• ❌ High similarity detected - review academic integrity guidelines\n");
        }

        // Improvement suggestions
        feedback.append("\nRECOMMENDATIONS FOR IMPROVEMENT:\n");
        if (score < 80) {
            feedback.append("• 📚 Expand on key concepts with specific examples\n");
            feedback.append("• 🔍 Include more technical details and applications\n");
            feedback.append("• ✍️  Improve sentence structure and flow\n");
            feedback.append("• 📖 Review course materials for deeper understanding\n");
        } else {
            feedback.append("• 🎯 Excellent work! Continue with current study approach\n");
            feedback.append("• 💡 Consider exploring advanced topics in this area\n");
        }

        return feedback.toString();
    }

    private List<String> identifyMistakes(String submission) {
        List<String> mistakes = new ArrayList<>();
        String lowerSubmission = submission.toLowerCase();

        // Check for common issues
        if (lowerSubmission.contains("ai ") && !lowerSubmission.contains("artificial intelligence")) {
            mistakes.add("Use 'Artificial Intelligence' instead of 'AI' in formal writing");
        }

        if (lowerSubmission.contains("think like humans") || lowerSubmission.contains("human thinking")) {
            mistakes.add("Clarify that AI simulates intelligence rather than replicates human thought");
        }

        int sentenceCount = submission.split("[.!?]+").length;
        if (sentenceCount < 3) {
            mistakes.add("Assignment is too brief - expand with more complete thoughts");
        }

        // Check for very long sentences
        String[] sentences = submission.split("[.!?]+");
        for (String sentence : sentences) {
            if (sentence.trim().split("\\s+").length > 40) {
                mistakes.add("Consider breaking long sentences into shorter, clearer statements");
                break;
            }
        }

        return mistakes;
    }

    private void displayResults(AnalysisResult result) {
        // Update metrics
        scoreLabel.setText(String.format("Score: %.1f/100", result.overallScore));
        scoreLabel.setForeground(getScoreColor(result.overallScore));

        plagiarismLabel.setText(String.format("Plagiarism: %.1f%%", result.plagiarismScore));
        plagiarismLabel.setForeground(getPlagiarismColor(result.plagiarismScore));

        similarityLabel.setText(String.format("Similarity: %.1f%%", result.similarityScore));

        // Display detailed results
        StringBuilder resultsText = new StringBuilder();
        resultsText.append(result.feedback);

        if (!result.mistakes.isEmpty()) {
            resultsText.append("\nIDENTIFIED AREAS FOR IMPROVEMENT:\n");
            for (int i = 0; i < result.mistakes.size(); i++) {
                resultsText.append("• ").append(result.mistakes.get(i)).append("\n");
            }
        }

        resultsText.append("\n").append(getEncouragementMessage(result.overallScore));

        resultTextArea.setText(resultsText.toString());
        resultTextArea.setCaretPosition(0); // Scroll to top
    }

    private Color getScoreColor(double score) {
        if (score >= 90) return new Color(39, 174, 96);
        if (score >= 80) return new Color(46, 204, 113);
        if (score >= 70) return new Color(241, 196, 15);
        if (score >= 60) return new Color(230, 126, 34);
        return new Color(231, 76, 60);
    }

    private Color getPlagiarismColor(double plagiarism) {
        if (plagiarism < 10) return new Color(39, 174, 96);
        if (plagiarism < 25) return new Color(241, 196, 15);
        if (plagiarism < 50) return new Color(230, 126, 34);
        return new Color(231, 76, 60);
    }

    private String getEncouragementMessage(double score) {
        if (score >= 90) return "🎉 Outstanding achievement! Your hard work is evident.";
        if (score >= 80) return "👍 Excellent work! You demonstrate strong understanding.";
        if (score >= 70) return "💪 Good job! With some refinement, you'll excel.";
        if (score >= 60) return "📚 Keep studying! Review the feedback and try again.";
        return "🔄 Don't give up! Use this feedback to improve your next attempt.";
    }

    private void resetMetrics() {
        scoreLabel.setText("Score: --/100");
        plagiarismLabel.setText("Plagiarism: --%");
        similarityLabel.setText("Similarity: --%");
        progressBar.setValue(0);
        progressBar.setString("Waiting for analysis...");
        scoreLabel.setForeground(new Color(44, 62, 80));
        plagiarismLabel.setForeground(Color.BLACK);
        similarityLabel.setForeground(Color.BLACK);
    }

    // Analysis result container class
    private static class AnalysisResult {
        double overallScore;
        double plagiarismScore;
        double similarityScore;
        String feedback;
        List<String> mistakes;

        AnalysisResult() {
            this.mistakes = new ArrayList<>();
        }
    }

    // Main method
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            new AssignmentCorrectorApp();
        });
    }
}