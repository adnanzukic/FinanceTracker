package financeapp;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    private JPanel mainPanel;
    private User currentUser;

    public MainMenu(User user) {
        this.currentUser = user;
        buildUI();
    }

    private void buildUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(45, 45, 45));

        JLabel naslov = new JLabel("LIFE MANAGEMENT SYSTEM - Main Menu", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 18));
        naslov.setForeground(Color.WHITE);
        naslov.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(naslov, BorderLayout.NORTH);

        JLabel welcome = new JLabel("Dobrodošli, " + currentUser.getUsername() + "!", SwingConstants.CENTER);
        welcome.setForeground(new Color(150, 200, 255));
        welcome.setFont(new Font("Arial", Font.ITALIC, 13));
        mainPanel.add(welcome, BorderLayout.CENTER);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        gridPanel.setBackground(new Color(45, 45, 45));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JButton financeBtn = createButton("Finance Tracker", new Color(70, 130, 180));
        JButton sleepBtn = createButton("Sleep Tracker", new Color(100, 60, 160));
        JButton moodBtn = createButton("Mood Tracker", new Color(180, 80, 80));
        JButton habitBtn = createButton("Habit Tracker", new Color(60, 160, 80));
        JButton accountBtn = createButton("Account Details", new Color(160, 120, 40));
        JButton logoutBtn = createButton("Odjavi se", new Color(100, 100, 100));

        gridPanel.add(financeBtn);
        gridPanel.add(sleepBtn);
        gridPanel.add(moodBtn);
        gridPanel.add(habitBtn);
        gridPanel.add(accountBtn);
        gridPanel.add(logoutBtn);

        mainPanel.add(gridPanel, BorderLayout.SOUTH);

        financeBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Finance Tracker");
            frame.setContentPane(new FinanceTrackerForm().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 550);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        sleepBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Sleep Tracker");
            frame.setContentPane(new SleepTracker().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        moodBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Mood Tracker");
            frame.setContentPane(new MoodTracker().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        habitBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Habit Tracker");
            frame.setContentPane(new HabitTracker().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        accountBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Korisnik: " + currentUser.getUsername() + "\nTema: " + currentUser.getTheme());
        });

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Jeste li sigurni da se želite odjaviti?",
                    "Odjava", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JFrame loginFrame = new JFrame("Life Management System");
                loginFrame.setContentPane(new LoginForm().getMainPanel());
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(500, 450);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
                SwingUtilities.getWindowAncestor(mainPanel).dispose();
            }
        });
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}