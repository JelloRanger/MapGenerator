package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import noise.Noise;

public class BasicNoiseDialog {

	public static void plotNoise(Noise noise) {
		BufferedImage image = new BufferedImage(noise.getWidth(), noise.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < noise.getHeight(); y++) {
			for (int x = 0; x < noise.getWidth(); x++) {
				
				double elevation = noise.getGrid().getPoint(x, y).getElevation();
				int colorValue = (int) ((elevation * 128.0) + 128);
				if (colorValue > 255) {
					colorValue = 255;
				} else if (colorValue < 0) {
					colorValue = 0;
				}
				Color elevationColor = null;
				try {
					elevationColor = new Color(colorValue, colorValue, colorValue);
				} catch (IllegalArgumentException e) {
					System.out.println("elevation: " + elevation);
					System.out.println("colorValue: " + colorValue);
					e.printStackTrace();
					System.exit(1);
				}
				image.setRGB(x, y, elevationColor.getRGB());
			}
		}
		
		createFrame(image);
	}
	
	public static void createFrame(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
