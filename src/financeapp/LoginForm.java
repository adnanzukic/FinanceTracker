package financeapp;

import javax.swing.*;
import java.awt.*;

public class LoginForm {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JComboBox<String> themeCombo;

    private UserManager userManager;

    public LoginForm() {
        userManager = new UserManager();
        buildUI();
    }

    private void buildUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(45, 45, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));


        JLabel naslov = new JLabel("LIFE MANAGEMENT SYSTEM", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov.setForeground(Color.WHITE);
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(naslov);

        mainPanel.add(Box.createVerticalStrut(30));


        JLabel usernameLabel = new JLabel("Korisničko ime:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameLabel);

        mainPanel.add(Box.createVerticalStrut(5));

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameField);

        mainPanel.add(Box.createVerticalStrut(15));


        JLabel passwordLabel = new JLabel("Lozinka:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordLabel);

        mainPanel.add(Box.createVerticalStrut(5));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordField);

        mainPanel.add(Box.createVerticalStrut(15));


        JLabel themeLabel = new JLabel("Odaberi temu:");
        themeLabel.setForeground(Color.WHITE);
        themeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(themeLabel);

        mainPanel.add(Box.createVerticalStrut(5));

        themeCombo = new JComboBox<>(new String[]{
                "Plava", "Zelena", "Roza", "Narandzasta", "Tamna"
        });
        themeCombo.setMaximumSize(new Dimension(300, 30));
        themeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(themeCombo);

        mainPanel.add(Box.createVerticalStrut(25));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(45, 45, 45));

        loginButton = new JButton("Prijavi se");
        loginButton.setPreferredSize(new Dimension(130, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));

        registerButton = new JButton("Registruj se");
        registerButton.setPreferredSize(new Dimension(130, 35));
        registerButton.setBackground(new Color(60, 160, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel);


        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Unesite korisničko ime i lozinku!");
                return;
            }

            User user = userManager.loginUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(null, "Dobrodošli, " + user.getUsername() + "!");
                openMainMenu(user);
            } else {
                JOptionPane.showMessageDialog(null, "Pogrešno korisničko ime ili lozinka!");
            }
        });


        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String theme = (String) themeCombo.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Unesite korisničko ime i lozinku!");
                return;
            }
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(null, "Lozinka mora imati najmanje 4 karaktera!");
                return;
            }

            boolean success = userManager.registerUser(username, password, theme);
            if (success) {
                JOptionPane.showMessageDialog(null, "Registracija uspješna! Možete se prijaviti.");
            } else {
                JOptionPane.showMessageDialog(null, "Korisnik sa tim imenom već postoji!");
            }
        });
    }

    private void openMainMenu(User user) {
        JFrame mainMenuFrame = new JFrame("Life Management System");
        mainMenuFrame.setContentPane(new MainMenu(user).getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(700, 500);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);


        SwingUtilities.getWindowAncestor(mainPanel).dispose();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}