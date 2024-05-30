import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class softwareEvidence {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Software> softwareList;

    public softwareEvidence() {
        softwareList = new ArrayList<>();

        frame = new JFrame("Evidence softwaru");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Název","Autor","Délka skladby (s)","Cena"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Přidat software");
        addButton.addActionListener(new AddButtonActionListener());
        buttonsPanel.add(addButton);

        JButton deleteButton = new JButton("Smazat software");
        deleteButton.addActionListener(new DeleteButtonActionListener());
        buttonsPanel.add(deleteButton);

        JButton exportButton = new JButton("Exportovat do souboru");
        exportButton.addActionListener(new ExportButtonActionListener());
        buttonsPanel.add(exportButton);

        JButton calculateButton = new JButton("Spočítat průměry");
        calculateButton.addActionListener(new CalculateButtonActionListener());
        buttonsPanel.add(calculateButton);

        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nazev = JOptionPane.showInputDialog("Zadejte název softwaru:");
            String autor = JOptionPane.showInputDialog("Zadejte výrobce:");
            String cena = JOptionPane.showInputDialog("Zadejte cenu:");

            if (nazev != null && autor != null && cena != null) {
                Software software = new Software(String nazev, String autor, Double.parseDouble(cena));
                softwareList.add(software);
                tableModel.addRow(new Object[]{software.getNazev(), software.getVyrobce(), software.getCena()});
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                softwareList.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter writer = new FileWriter("software.txt")) {
                for (Software software : softwareList) {
                    writer.write(software.getNazev() + ";" + software.getVyrobce() + ";"  + software.getCena() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Údaje byly úspěšně exportovány do souboru software.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Chyba při exportu do souboru: " + ex.getMessage());
            }
        }
    }

    private class CalculateButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double sumCena = 0;

            for (Software software : softwareList) {
                sumCena += software.getCena();
            }

            double avgCena = sumCena / softwareList.size();

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            JOptionPane.showMessageDialog(frame,
                    "Průměrná cena softwaru: " + decimalFormat.format(avgCena) + "\n" +
                            "Celková cena softwaru: " + decimalFormat.format(sumCena));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(softwareEvidence::new);
    }

    private static class Software {
        private String nazev;
        private String vyrobce;
        private double cena;

        public Software(String nazev, String vyrobce, double cena) {
            this.nazev = nazev;
            this.vyrobce = vyrobce;
            this.cena = cena;
        }

        public String getNazev() {
            return nazev;
        }

        public String getVyrobce() {
            return vyrobce;
        }

        public double getCena() {
            return cena;
        }
    }
}