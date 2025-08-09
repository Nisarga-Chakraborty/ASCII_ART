import javax.swing.*;
import java.awt.*;

public class Ascii extends JFrame {
    public Ascii(String asciiArt) {
        setTitle("ASCII Art Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        // setResizable(true);

        JLabel asciiLabel = new JLabel("ASCII Art Viewer", SwingConstants.CENTER);
        asciiLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        asciiLabel.setForeground(Color.WHITE);
        add(asciiLabel, BorderLayout.NORTH);

        JTextArea asciiTextArea = new JTextArea();
        asciiTextArea.setFont(new Font("Courier New", Font.BOLD, 11));
        asciiTextArea.setForeground(Color.WHITE);
        asciiTextArea.setBackground(Color.BLACK);
        asciiTextArea.setEditable(false);
        asciiTextArea.setText(asciiArt);// wrting the ascii in the JTextArea
        asciiTextArea.setLineWrap(false);
        asciiTextArea.setWrapStyleWord(false);

        add(new JScrollPane(asciiTextArea), BorderLayout.CENTER);// allowing the scrolling action to occur in JTextArea

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.white);
        menuBar.setForeground(Color.black);
        menuBar.setFont(new Font("Times new Roman", Font.BOLD, 20));
        add(menuBar, BorderLayout.NORTH);
        JMenu newMenu = new JMenu("+ NEW");
        newMenu.setFont(new Font("Times new Roman", Font.BOLD, 20));
        newMenu.setForeground(Color.black);
        newMenu.setBackground(Color.white);

        JMenuItem newItem = new JMenuItem("New Image");
        newItem.setFont(new Font("Times new Roman", Font.BOLD, 20));
        newItem.setForeground(Color.black);
        newItem.setBackground(Color.white);
        newMenu.add(newItem);

        menuBar.add(newMenu);

        newItem.addActionListener(e -> {
            String response = JOptionPane.showInputDialog(this,
                    "Do you want to select a new image and mak a new Ascii art? \n Enter yes or no");
            if (response.equalsIgnoreCase("yes")) {
                dispose();
                imagetoascii a = new imagetoascii();
                a.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No new image selected. Exiting.");
            }
        });

    }
}
