import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FileEncryptorDecryptor extends JFrame implements ActionListener {
    private JTextField filePathField;
    private JButton browseButton, encryptButton, decryptButton;
    private JFileChooser fileChooser;
    private final int SHIFT_KEY = 5; // Simple Caesar Cipher key

    public FileEncryptorDecryptor() {
        setTitle("File Encryptor/Decryptor");
        setSize(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 5, 5));

        JPanel filePanel = new JPanel(new FlowLayout());
        filePathField = new JTextField(25);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(this);
        filePanel.add(new JLabel("Select File:"));
        filePanel.add(filePathField);
        filePanel.add(browseButton);
        add(filePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        encryptButton.addActionListener(this);
        decryptButton.addActionListener(this);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browseButton) {
            fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        } else if (e.getSource() == encryptButton) {
            processFile(true);
        } else if (e.getSource() == decryptButton) {
            processFile(false);
        }
    }

    private void processFile(boolean encrypt) {
        String filePath = filePathField.getText();
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder result = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                if (encrypt)
                    result.append((char) (ch + SHIFT_KEY));
                else
                    result.append((char) (ch - SHIFT_KEY));
            }

            String outputFilePath = filePath + (encrypt ? "_encrypted.txt" : "_decrypted.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                writer.write(result.toString());
            }

            JOptionPane.showMessageDialog(this, "File successfully " + (encrypt ? "encrypted" : "decrypted") +
                    ":\n" + outputFilePath, "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error processing file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileEncryptorDecryptor app = new FileEncryptorDecryptor();
            app.setVisible(true);
        });
    }
}
