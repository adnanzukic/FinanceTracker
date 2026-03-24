package financeapp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Praćenje ličnih finansija");
            frame.setContentPane(new FinanceTrackerForm().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 550);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}