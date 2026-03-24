package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FinanceTrackerForm {

    private JPanel mainPanel;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeCombo;
    private JButton addButton;
    private JTable transactionTable;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private JLabel text1;
    private JLabel text2;
    private JLabel naslov;

    private TransactionManager manager;

    public FinanceTrackerForm() {
        manager = new TransactionManager();
        buildUI();
        loadDataIntoTable();
        updateSummary();
    }

    private void buildUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // ---- NASLOV ----
        naslov = new JLabel("PRAĆENJE LIČNIH FINANSIJA", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 14));
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
        naslov.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        mainPanel.add(naslov);

        // ---- LABEL "Unesite iznos" ----
        text1 = new JLabel("Unesite iznos vašeg prihoda:");
        text1.setBorder(BorderFactory.createEmptyBorder(4, 6, 2, 0));
        mainPanel.add(text1);

        // ---- POLJE ZA IZNOS ----
        amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        amountField.setBackground(Color.WHITE);
        mainPanel.add(amountField);

        // ---- SEPARATOR ----
        mainPanel.add(new JSeparator());

        // ---- LABEL "Opišite" ----
        text2 = new JLabel("Opišite ovaj izvor prihoda:");
        text2.setBorder(BorderFactory.createEmptyBorder(4, 6, 2, 0));
        mainPanel.add(text2);

        // ---- POLJE ZA OPIS ----
        descriptionField = new JTextField();
        descriptionField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        descriptionField.setBackground(Color.WHITE);
        mainPanel.add(descriptionField);

        // ---- SEPARATOR ----
        mainPanel.add(new JSeparator());

        // ---- COMBOBOX ----
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        comboPanel.setBackground(Color.LIGHT_GRAY);
        typeCombo = new JComboBox<>(new String[]{"Prihod", "Rashod"});
        typeCombo.setPreferredSize(new Dimension(100, 25));
        comboPanel.add(typeCombo);
        mainPanel.add(comboPanel);

        // ---- DUGME ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        addButton = new JButton("Izračunaj");
        addButton.setPreferredSize(new Dimension(100, 28));
        buttonPanel.add(addButton);
        mainPanel.add(buttonPanel);

        // ---- SEPARATOR ----
        mainPanel.add(new JSeparator());

        // ---- SAŽETAK (Prihod / Rashod / Saldo u jednom redu) ----
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3));
        summaryPanel.setBackground(Color.LIGHT_GRAY);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        incomeLabel = new JLabel("Prihod: 0.0");
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        expenseLabel = new JLabel("Rashod: 0.0", SwingConstants.CENTER);
        expenseLabel.setFont(new Font("Arial", Font.BOLD, 12));

        balanceLabel = new JLabel("Saldo: 0.0", SwingConstants.RIGHT);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));

        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);
        mainPanel.add(summaryPanel);

        // ---- TABELA ----
        transactionTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBackground(Color.WHITE);
        mainPanel.add(scrollPane);

        // ---- ACTION LISTENER ----
        addButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Opis ne može biti prazan!");
                    return;
                }
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Iznos mora biti veći od nule!");
                    return;
                }

                Transaction t = new Transaction(type, amount, description);
                manager.addTransaction(t);
                loadDataIntoTable();
                updateSummary();

                amountField.setText("");
                descriptionField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Iznos mora biti broj! (npr. 25.50)");
            }
        });
    }

    private void loadDataIntoTable() {
        ArrayList<Transaction> list = manager.getAllTransactions();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vrsta");
        model.addColumn("Iznos");
        model.addColumn("Opis");

        for (Transaction t : list) {
            model.addRow(new Object[]{
                    t.getType(),
                    t.getAmount(),
                    t.getDescription()
            });
        }
        transactionTable.setModel(model);
    }

    private void updateSummary() {
        double income = manager.getTotalIncome();
        double expense = manager.getTotalExpense();
        double balance = income - expense;

        incomeLabel.setText("Prihod: " + income);
        expenseLabel.setText("Rashod: " + expense);
        balanceLabel.setText("Saldo: " + balance);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}