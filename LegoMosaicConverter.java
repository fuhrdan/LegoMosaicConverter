import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LegoMosaicConverter extends JFrame {
    private BufferedImage inputImage;
    private BufferedImage legoMosaic;
    private JLabel imageLabel;
    private JButton selectButton, convertButton;
    
    private static final Color[] LEGO_COLORS = {
        new Color(255, 255, 255), // White
        new Color(0, 0, 0),       // Black
        new Color(255, 0, 0),     // Red
        new Color(0, 0, 255),     // Blue
        new Color(0, 255, 0),     // Green
        new Color(255, 255, 0),   // Yellow
        new Color(255, 165, 0),   // Orange
        new Color(128, 0, 128)    // Purple
    };

    public LegoMosaicConverter() {
        setTitle("Lego Mosaic Converter");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        selectButton = new JButton("Select Image");
        convertButton = new JButton("Convert");
        panel.add(selectButton);
        panel.add(convertButton);
        add(panel, BorderLayout.SOUTH);
        
        imageLabel = new JLabel();
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });
        
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertImage();
            }
        });
    }
    
    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                inputImage = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(inputImage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void convertImage() {
        if (inputImage == null) {
            JOptionPane.showMessageDialog(this, "Please select an image first.");
            return;
        }
        
        int width = 48;
        int height = (inputImage.getHeight() * width) / inputImage.getWidth();
        BufferedImage resizedImage = resizeImage(inputImage, width, height);
        legoMosaic = convertToLegoMosaic(resizedImage);
        imageLabel.setIcon(new ImageIcon(legoMosaic));
    }
    
    private BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }
    
    private BufferedImage convertToLegoMosaic(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int scaledWidth = width * 3;
        int scaledHeight = height * 3;
        BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g = output.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, scaledWidth, scaledHeight);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color originalColor = new Color(image.getRGB(x, y));
                Color closestLegoColor = findClosestLegoColor(originalColor);
                g.setColor(closestLegoColor);
                g.fillRect(x * 3, y * 3, 2, 2);
            }
        }
        
        g.dispose();
        return output;
    }
    
    private Color findClosestLegoColor(Color color) {
        Color closestColor = LEGO_COLORS[0];
        double minDistance = Double.MAX_VALUE;
        
        for (Color legoColor : LEGO_COLORS) {
            double distance = colorDistance(color, legoColor);
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = legoColor;
            }
        }
        return closestColor;
    }
    
    private double colorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LegoMosaicConverter().setVisible(true);
        });
    }
}
