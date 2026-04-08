package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MoodTracker {
    private JPanel mainPanel;
    private JComboBox<String> moodCombo;
    private JTextField dateField;
    private JTextField noteField;
    private JTable moodTable;
    private JLabel statsLabel;
    private JButton addButton;
    private JButton deleteButton;

    private MongoCollection<Document> collection;

    public MoodTracker() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("mood_tracker");
        buildUI();
        loadData();
        updateStats();
    }

    private void buildUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(45, 45, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        JLabel naslov = new JLabel("MOOD TRACKER", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 18));
        naslov.setForeground(Color.WHITE);
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(naslov);

        mainPanel.add(Box.createVerticalStrut(20));


        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(new Color(45, 45, 45));
        formPanel.setMaximumSize(new Dimension(500, 110));

        JLabel moodLabel = new JLabel("Raspoloženje:");
        moodLabel.setForeground(Color.WHITE);
        moodCombo = new JComboBox<>(new String[]{
                "😊 Odlično", "🙂 Dobro", "😐 Neutralno", "😕 Loše", "😢 Užasno"
        });

        JLabel dateLabel = new JLabel("Datum:");
        dateLabel.setForeground(Color.WHITE);
        dateField = new JTextField(LocalDate.now().toString());

        JLabel noteLabel = new JLabel("Bilješka:");
        noteLabel.setForeground(Color.WHITE);
        noteField = new JTextField();

        formPanel.add(moodLabel);
        formPanel.add(moodCombo);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(noteLabel);
        formPanel.add(noteField);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(10));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(45, 45, 45));

        addButton = new JButton("Dodaj");
        addButton.setBackground(new Color(180, 80, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 13));

        deleteButton = new JButton("Obriši");
        deleteButton.setBackground(new Color(100, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 13));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createVerticalStrut(10));


        statsLabel = new JLabel("Najčešće raspoloženje: -", SwingConstants.CENTER);
        statsLabel.setForeground(new Color(150, 200, 255));
        statsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(statsLabel);

        mainPanel.add(Box.createVerticalStrut(10));


        moodTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(moodTable);
        mainPanel.add(scrollPane);


        addButton.addActionListener(e -> {
            String mood = (String) moodCombo.getSelectedItem();
            String date = dateField.getText().trim();
            String note = noteField.getText().trim();

            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Unesite datum!");
                return;
            }

            Document doc = new Document("mood", mood)
                    .append("date", date)
                    .append("note", note);
            collection.insertOne(doc);
            loadData();
            updateStats();
            noteField.setText("");
            dateField.setText(LocalDate.now().toString());
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = moodTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite red iz tabele!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Jeste li sigurni da želite obrisati ovaj unos?",
                    "Potvrda", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String date = (String) moodTable.getValueAt(selectedRow, 0);
                collection.deleteOne(new Document("date", date));
                loadData();
                updateStats();
            }
        });
    }

    private void loadData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Raspoloženje");
        model.addColumn("Bilješka");

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            model.addRow(new Object[]{
                    d.getString("date"),
                    d.getString("mood"),
                    d.getString("note")
            });
        }
        moodTable.setModel(model);
    }

    private void updateStats() {
        int odlicno = 0, dobro = 0, neutralno = 0, lose = 0, uzasno = 0;

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            String mood = d.getString("mood");
            if (mood.contains("Odlično")) odlicno++;
            else if (mood.contains("Dobro")) dobro++;
            else if (mood.contains("Neutralno")) neutralno++;
            else if (mood.contains("Loše")) lose++;
            else if (mood.contains("Užasno")) uzasno++;
        }

        int max = Math.max(odlicno, Math.max(dobro, Math.max(neutralno, Math.max(lose, uzasno))));
        String najcesce = "-";
        if (max == 0) {
            najcesce = "Nema podataka";
        } else if (max == odlicno) najcesce = "😊 Odlično";
        else if (max == dobro) najcesce = "🙂 Dobro";
        else if (max == neutralno) najcesce = "😐 Neutralno";
        else if (max == lose) najcesce = "😕 Loše";
        else if (max == uzasno) najcesce = "😢 Užasno";

        statsLabel.setText("Najčešće raspoloženje: " + najcesce);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}