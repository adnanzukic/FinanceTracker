package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class SleepTracker {
    private JPanel mainPanel;
    private JTextField hoursField;
    private JTextField dateField;
    private JTable sleepTable;
    private JLabel averageLabel;
    private JButton addButton;
    private JButton deleteButton;

    private MongoCollection<Document> collection;

    public SleepTracker() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("sleep_tracker");
        buildUI();
        loadData();
        updateStats();
    }

    private void buildUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(45, 45, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        JLabel naslov = new JLabel("SLEEP TRACKER", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 18));
        naslov.setForeground(Color.WHITE);
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(naslov);

        mainPanel.add(Box.createVerticalStrut(20));


        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(new Color(45, 45, 45));
        formPanel.setMaximumSize(new Dimension(500, 80));

        JLabel hoursLabel = new JLabel("Sati sna:");
        hoursLabel.setForeground(Color.WHITE);
        hoursField = new JTextField();

        JLabel dateLabel = new JLabel("Datum (npr. 2024-01-15):");
        dateLabel.setForeground(Color.WHITE);
        dateField = new JTextField(LocalDate.now().toString());

        formPanel.add(hoursLabel);
        formPanel.add(hoursField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(10));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(45, 45, 45));

        addButton = new JButton("Dodaj");
        addButton.setBackground(new Color(100, 60, 160));
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


        averageLabel = new JLabel("Prosječan san: 0.0 sati", SwingConstants.CENTER);
        averageLabel.setForeground(new Color(150, 200, 255));
        averageLabel.setFont(new Font("Arial", Font.BOLD, 13));
        averageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(averageLabel);

        mainPanel.add(Box.createVerticalStrut(10));


        sleepTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(sleepTable);
        mainPanel.add(scrollPane);


        addButton.addActionListener(e -> {
            try {
                double hours = Double.parseDouble(hoursField.getText().trim());
                String date = dateField.getText().trim();

                if (hours < 0 || hours > 24) {
                    JOptionPane.showMessageDialog(null, "Unesite validan broj sati (0-24)!");
                    return;
                }
                if (date.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Unesite datum!");
                    return;
                }

                Document doc = new Document("hours", hours).append("date", date);
                collection.insertOne(doc);
                loadData();
                updateStats();
                hoursField.setText("");
                dateField.setText(LocalDate.now().toString());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Sati moraju biti broj!");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = sleepTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite red iz tabele!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Jeste li sigurni da želite obrisati ovaj unos?",
                    "Potvrda", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String date = (String) sleepTable.getValueAt(selectedRow, 0);
                collection.deleteOne(new Document("date", date));
                loadData();
                updateStats();
            }
        });
    }

    private void loadData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Sati sna");

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            model.addRow(new Object[]{
                    d.getString("date"),
                    d.getDouble("hours")
            });
        }
        sleepTable.setModel(model);
    }

    private void updateStats() {
        double total = 0;
        int count = 0;
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            total += d.getDouble("hours");
            count++;
        }
        double average = count > 0 ? total / count : 0;
        averageLabel.setText(String.format("Prosječan san: %.1f sati", average));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}