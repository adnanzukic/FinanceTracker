package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FinanceTrackerForm {

    private JPanel mainPanel;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeCombo;
    private JComboBox<String> categoryCombo;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JTable transactionTable;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private JLabel naslov;
    private JLabel text1;
    private JLabel text2;

    private TransactionManager manager;
    private ArrayList<Transaction> currentList;

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

        // NASLOV
        naslov = new JLabel("PRAĆENJE LIČNIH FINANSIJA", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 14));
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
        naslov.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        mainPanel.add(naslov);

        // LABEL + IZNOS
        text1 = new JLabel("Unesite iznos vašeg prihoda:");
        text1.setBorder(BorderFactory.createEmptyBorder(4, 6, 2, 0));
        mainPanel.add(text1);

        amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        amountField.setBackground(Color.WHITE);
        mainPanel.add(amountField);

        mainPanel.add(new JSeparator());

        // LABEL + OPIS
        text2 = new JLabel("Opišite ovaj izvor prihoda:");
        text2.setBorder(BorderFactory.createEmptyBorder(4, 6, 2, 0));
        mainPanel.add(text2);

        descriptionField = new JTextField();
        descriptionField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        descriptionField.setBackground(Color.WHITE);
        mainPanel.add(descriptionField);

        mainPanel.add(new JSeparator());

        // VRSTA I KATEGORIJA
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        comboPanel.setBackground(Color.LIGHT_GRAY);

        typeCombo = new JComboBox<>(new String[]{"Prihod", "Rashod"});
        typeCombo.setPreferredSize(new Dimension(100, 25));
        comboPanel.add(new JLabel("Vrsta:"));
        comboPanel.add(typeCombo);

        categoryCombo = new JComboBox<>(new String[]{
                "Plata", "Hrana", "Racuni", "Zabava", "Prijevoz", "Ostalo"
        });
        categoryCombo.setPreferredSize(new Dimension(120, 25));
        comboPanel.add(new JLabel("Kategorija:"));
        comboPanel.add(categoryCombo);

        mainPanel.add(comboPanel);

        // DUGMAD
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        addButton = new JButton("Izračunaj");
        addButton.setPreferredSize(new Dimension(110, 28));

        updateButton = new JButton("Ažuriraj");
        updateButton.setPreferredSize(new Dimension(110, 28));

        deleteButton = new JButton("Briši");
        deleteButton.setPreferredSize(new Dimension(110, 28));

        exportButton = new JButton("Export");
        exportButton.setPreferredSize(new Dimension(110, 28));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        mainPanel.add(buttonPanel);

        mainPanel.add(new JSeparator());

        // SAŽETAK
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

        // TABELA
        transactionTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        mainPanel.add(scrollPane);

        // =====================
        // ACTION LISTENERI
        // =====================

        // DODAJ
        addButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Opis ne može biti prazan!");
                    return;
                }
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Iznos mora biti veći od nule!");
                    return;
                }

                Transaction t = new Transaction(type, amount, description, category);
                manager.addTransaction(t);
                loadDataIntoTable();
                updateSummary();
                clearFields();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Iznos mora biti broj! (npr. 25.50)");
            }
        });

        // AŽURIRAJ
        updateButton.addActionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite transakciju iz tabele!");
                return;
            }
            try {
                String type = (String) typeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Opis ne može biti prazan!");
                    return;
                }

                Transaction selected = currentList.get(selectedRow);
                manager.updateTransaction(selected.getId(), type, amount, description, category);
                loadDataIntoTable();
                updateSummary();
                clearFields();
                JOptionPane.showMessageDialog(null, "Transakcija uspješno ažurirana!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Iznos mora biti broj!");
            }
        });

        // BRIŠI
        deleteButton.addActionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite transakciju iz tabele!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Jeste li sigurni da želite izbrisati ovu transakciju?",
                    "Potvrda brisanja",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                Transaction selected = currentList.get(selectedRow);
                manager.deleteTransaction(selected.getId());
                loadDataIntoTable();
                updateSummary();
                clearFields();
                JOptionPane.showMessageDialog(null, "Transakcija obrisana!");
            }
        });

        // KLIK NA RED U TABELI - ucitaj u polja
        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow != -1 && currentList != null && selectedRow < currentList.size()) {
                Transaction t = currentList.get(selectedRow);
                amountField.setText(String.valueOf(t.getAmount()));
                descriptionField.setText(t.getDescription());
                typeCombo.setSelectedItem(t.getType());
                categoryCombo.setSelectedItem(t.getCategory());
            }
        });

        // EXPORT
        exportButton.addActionListener(e -> exportToFile());
    }

    private void exportToFile() {
        double income = manager.getTotalIncome();
        double expense = manager.getTotalExpense();
        double balance = income - expense;

        StringBuilder sb = new StringBuilder();
        sb.append("Ukupni prihod: ").append(income).append("\n");
        sb.append("Ukupni rashod: ").append(expense).append("\n");
        sb.append("Stanje: ").append(balance).append("\n");
        sb.append("\n");
        sb.append("Rashodi po kategorijama:\n");

        String[] categories = {"Plata", "Hrana", "Racuni", "Zabava", "Prijevoz", "Ostalo"};
        for (String cat : categories) {
            double catTotal = manager.getExpenseByCategory(cat);
            if (catTotal > 0) {
                sb.append(cat).append(": ").append(catTotal).append("\n");
            }
        }

        try {
            FileWriter writer = new FileWriter("finansije_export.txt");
            writer.write(sb.toString());
            writer.close();
            JOptionPane.showMessageDialog(null, "Export uspješan! Fajl: finansije_export.txt");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Greška pri exportu: " + ex.getMessage());
        }
    }

    private void loadDataIntoTable() {
        currentList = manager.getAllTransactions();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vrsta");
        model.addColumn("Iznos");
        model.addColumn("Opis");
        model.addColumn("Kategorija");

        for (Transaction t : currentList) {
            model.addRow(new Object[]{
                    t.getType(),
                    t.getAmount(),
                    t.getDescription(),
                    t.getCategory()
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

    private void clearFields() {
        amountField.setText("");
        descriptionField.setText("");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}