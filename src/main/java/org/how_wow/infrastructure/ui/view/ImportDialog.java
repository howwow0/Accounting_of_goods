package org.how_wow.infrastructure.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class ImportDialog extends JDialog {
    private JTextField filePathField;
    private JButton browseButton, importButton, cancelButton;

    public ImportDialog(JFrame parent) {
        super(parent, "Импорт данных", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Панель выбора файла
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.add(new JLabel("Выберите файл для импорта:"));

        filePathField = new JTextField(20);
        filePathField.setEditable(false);
        filePanel.add(filePathField);

        browseButton = new JButton("Обзор...");
        browseButton.addActionListener(e -> openFileChooser());
        filePanel.add(browseButton);

        mainPanel.add(filePanel, BorderLayout.NORTH);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        importButton = new JButton("Импортировать");
        cancelButton = new JButton("Отмена");

        importButton.addActionListener(e -> onImport());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(importButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void onImport() {
        // Логика импорта данных из файла
        String filePath = filePathField.getText();
        if (filePath != null && !filePath.isEmpty()) {
            // Реализуйте логику обработки файла
            System.out.println("Импорт данных из файла: " + filePath);
            dispose(); // Закрытие окна после импорта
        } else {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите файл для импорта.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
