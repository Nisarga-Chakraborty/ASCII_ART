import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.util.List;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class imagetoascii extends JFrame {

    private File droppedImageFile = null;

    public imagetoascii() {
        setTitle("Image to ASCII Art");
        setSize(800, 700);
        getContentPane().setBackground(Color.BLACK);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Create a label to display the ASCII art
        JLabel asciiLabel = new JLabel("ASCII Art");
        asciiLabel.setFont(new Font("Arial", Font.BOLD, 25));
        asciiLabel.setForeground(Color.WHITE);
        asciiLabel.setBounds(340, 18, 150, 50);
        asciiLabel.setOpaque(true);
        asciiLabel.setBackground(Color.BLACK);

        // Add the label to the frame
        add(asciiLabel, BorderLayout.CENTER);

        JLabel dropLabel = new JLabel("Drop an image here to convert to ASCII art", SwingConstants.CENTER);
        dropLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        dropLabel.setForeground(Color.WHITE);
        dropLabel.setBounds(200, 200, 400, 25);
        dropLabel.setOpaque(true);
        dropLabel.setBackground(Color.BLACK);
        // dropLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        add(dropLabel);

        JTextArea dropimageArea = new JTextArea();
        dropimageArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        dropimageArea.setForeground(Color.WHITE);
        dropimageArea.setBackground(Color.DARK_GRAY);
        dropimageArea.setBounds(200, 250, 400, 300);
        dropimageArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Drop Image Here",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                Color.WHITE));
        dropimageArea.setEditable(false);
        dropimageArea.setLineWrap(true);
        add(dropimageArea);

        new DropTarget(dropimageArea, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                dropimageArea.setBorder(BorderFactory.createLineBorder(Color.CYAN, 6));
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                dropimageArea.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        "Drop Image Here",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 12),
                        Color.WHITE));
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                dropimageArea.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        "Drop Image Here",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 12),
                        Color.WHITE));

                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                try {
                    Transferable transferable = dtde.getTransferable();
                    List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        if (isImageFile(file)) {
                            droppedImageFile = file;
                            dropimageArea.setText("Image dropped: " + file.getName());
                        } else {
                            JOptionPane.showMessageDialog(null, "Please drop a valid image file.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }
        });

        JButton dropButton = new JButton("SUBMIT");
        dropButton.setFont(new Font("Arial", Font.BOLD, 20));
        dropButton.setForeground(Color.WHITE);
        dropButton.setBackground(Color.GREEN);
        dropButton.setBounds(300, 600, 200, 50);
        dropButton.addActionListener(e -> {
            if (droppedImageFile != null) {
                String asciiArt = convertToAscii(droppedImageFile);
                dispose(); // close the current frame
                // Create a new Ascii viewer with the converted ASCII art
                Ascii asciiViewer = new Ascii(asciiArt);

                asciiViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Please drop an image file first.");
            }
        });

        add(dropButton);
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".bmp") || name.endsWith(".gif");// checking if the image extensions match jpg or png
                                                                  // or jpeg or bmf or gif only notice pdf is not
                                                                  // allowed
    }

    private String convertToAscii(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);

            // Resize for better readability
            int newWidth = 200;
            int newHeight = (image.getHeight() * newWidth) / image.getWidth();
            Image scaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(scaled, 0, 0, null);
            g2d.dispose();

            StringBuilder ascii = new StringBuilder();
            String chars = " .'`^\" :,;iIl!<>~+_-?][}{1)(|/tfjrnxuvczXYUJCLQ0OZmwqpbdkhba*#MW&8%B$@"; // Dark to
                                                                                                      // light

            for (int y = 0; y < newHeight; y += 2) { // Skip rows for compression
                for (int x = 0; x < newWidth; x++) {
                    int pixel = resized.getRGB(x, y);
                    Color color = new Color(pixel);
                    int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    int index = (gray * (chars.length() - 1)) / 255;
                    ascii.append(chars.charAt(index));
                }
                ascii.append("\n");
            }

            return ascii.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting image.";
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            imagetoascii frame = new imagetoascii();
            frame.setVisible(true);
        });
    }
}
