package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class HabitTracker {
    private JPanel mainPanel;
    private JTextField habitField;
    private JTextField dateField;
    private JComboBox<String> statusCombo;
    private JTable habitTable;
    private JLabel statsLabel;
    private JButton addButton;
    private JButton deleteButton;

    private MongoCollection<Document> collection;

    public HabitTracker() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("habit_tracker");
        buildUI();
        loadData();
        updateStats();
    }

    private void buildUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(45, 45, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        JLabel naslov = new JLabel("HABIT TRACKER", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 18));
        naslov.setForeground(Color.WHITE);
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(naslov);

        mainPanel.add(Box.createVerticalStrut(20));


        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(new Color(45, 45, 45));
        formPanel.setMaximumSize(new Dimension(500, 110));

        JLabel habitLabel = new JLabel("Navika:");
        habitLabel.setForeground(Color.WHITE);
        habitField = new JTextField();

        JLabel dateLabel = new JLabel("Datum:");
        dateLabel.setForeground(Color.WHITE);
        dateField = new JTextField(LocalDate.now().toString());

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(Color.WHITE);
        statusCombo = new JComboBox<>(new String[]{
                "✅ Završeno", "❌ Nije završeno"
        });

        formPanel.add(habitLabel);
        formPanel.add(habitField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(statusLabel);
        formPanel.add(statusCombo);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(10));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(45, 45, 45));

        addButton = new JButton("Dodaj");
        addButton.setBackground(new Color(60, 160, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 13));

        deleteButton = new JButton("Obriši");
        deleteButton.setBackground(new Color(180, 60, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 13));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalStrut(10));


        statsLabel = new JLabel("Uspješnost: 0%", SwingConstants.CENTER);
        statsLabel.setForeground(new Color(150, 200, 255));
        statsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(statsLabel);

        mainPanel.add(Box.createVerticalStrut(10));


        habitTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(habitTable);
        mainPanel.add(scrollPane);


        addButton.addActionListener(e -> {
            String habit = habitField.getText().trim();
            String date = dateField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();

            if (habit.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Unesite naziv navike!");
                return;
            }
            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Unesite datum!");
                return;
            }

            Document doc = new Document("habit", habit)
                    .append("date", date)
                    .append("status", status);
            collection.insertOne(doc);
            loadData();
            updateStats();
            habitField.setText("");
            dateField.setText(LocalDate.now().toString());
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = habitTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite red iz tabele!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Jeste li sigurni da želite obrisati ovaj unos?",
                    "Potvrda", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String habit = (String) habitTable.getValueAt(selectedRow, 1);
                String date = (String) habitTable.getValueAt(selectedRow, 0);
                collection.deleteOne(new Document("habit", habit).append("date", date));
                loadData();
                updateStats();
            }
        });
    }

    private void loadData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Navika");
        model.addColumn("Status");

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            model.addRow(new Object[]{
                    d.getString("date"),
                    d.getString("habit"),
                    d.getString("status")
            });
        }
        habitTable.setModel(model);
    }

    private void updateStats() {
        int total = 0;
        int completed = 0;

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            total++;
            if (d.getString("status").contains("Završeno")) {
                completed++;
            }
        }

        double percentage = total > 0 ? (completed * 100.0 / total) : 0;
        statsLabel.setText(String.format(
                "Uspješnost: %.1f%% (%d/%d navika završeno)", percentage, completed, total));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}