package annn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookstoreGUI {
    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public void createAndShowMainGUI() {
        try {
            connection = DatabaseConnector.connect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        frame = new JFrame("Bookstore Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null); // Center the frame

        JPanel panel = new JPanel(null); // Use null layout

        // Display project name
        JLabel projectNameLabel = new JLabel("Bookstore Management System");
        projectNameLabel.setBounds(10, 0, 300, 20);
        panel.add(projectNameLabel);

        // Create table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Price");

        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(10, 30, 560, 200); // Set bounds for the table
        panel.add(scrollPane);

        // Buttons
        JButton addButton = new JButton("Add Book");
        addButton.setBounds(10, 240, 120, 30); // Set bounds for the Add button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
                refreshTable();
            }
        });

        JButton updateButton = new JButton("Update Book");
        updateButton.setBounds(140, 240, 120, 30); // Set bounds for the Update button
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBook();
                refreshTable();
            }
        });

        JButton deleteButton = new JButton("Delete Book");
        deleteButton.setBounds(270, 240, 120, 30); // Set bounds for the Delete button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteBook();
                refreshTable();
            }
        });

        JButton retrieveButton = new JButton("Retrieve Book");
        retrieveButton.setBounds(400, 240, 120, 30); // Set bounds for the Retrieve button
        retrieveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                retrieveBook();
            }
        });

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(retrieveButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);

        refreshTable();
    }

    private void addBook() {
        try {
            String title = JOptionPane.showInputDialog(frame, "Enter Title:");
            String author = JOptionPane.showInputDialog(frame, "Enter Author:");
            double price = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Price:"));

            String query = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setDouble(3, price);
                preparedStatement.executeUpdate();
            }
            JOptionPane.showMessageDialog(frame, "Book added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Book ID to Update:"));
            String title = JOptionPane.showInputDialog(frame, "Enter New Title:");
            String author = JOptionPane.showInputDialog(frame, "Enter New Author:");
            double price = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter New Price:"));

            String query = "UPDATE books SET title=?, author=?, price=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setDouble(3, price);
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
            }
            JOptionPane.showMessageDialog(frame, "Book updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Book ID to Delete:"));

            String query = "DELETE FROM books WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
            JOptionPane.showMessageDialog(frame, "Book deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retrieveBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Book ID to Retrieve:"));

            String query = "SELECT * FROM books WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String result = "Book ID: " + resultSet.getInt("id") +
                                "\nTitle: " + resultSet.getString("title") +
                                "\nAuthor: " + resultSet.getString("author") +
                                "\nPrice: " + resultSet.getDouble("price");
                        JOptionPane.showMessageDialog(frame, result, "Book Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        // Clear existing data
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        try {
            String query = "SELECT * FROM books";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] row = {resultSet.getInt("id"), resultSet.getString("title"),
                            resultSet.getString("author"), resultSet.getDouble("price")};
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookstoreGUI().createAndShowMainGUI());
    }
}
