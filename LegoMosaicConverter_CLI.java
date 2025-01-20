import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class LegoMosaicConverter {
    // Define a simplified set of Lego 1x1 round tile colors (RGB values)
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

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java LegoMosaicConverter <input-image> <output-image>");
            return;
        }
        
        try {
            BufferedImage inputImage = ImageIO.read(new File(args[0]));
            int width = 48;  // Scale width for Lego grid
            int height = (inputImage.getHeight() * width) / inputImage.getWidth();
            BufferedImage resizedImage = resizeImage(inputImage, width, height);
            BufferedImage legoMosaic = convertToLegoMosaic(resizedImage);
            ImageIO.write(legoMosaic, "png", new File(args[1]));
            System.out.println("Lego mosaic image saved as " + args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }
    
    private static BufferedImage convertToLegoMosaic(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color originalColor = new Color(image.getRGB(x, y));
                Color closestLegoColor = findClosestLegoColor(originalColor);
                output.setRGB(x, y, closestLegoColor.getRGB());
            }
        }
        return output;
    }
    
    private static Color findClosestLegoColor(Color color) {
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
    
    private static double colorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}
