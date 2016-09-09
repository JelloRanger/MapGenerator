package test;

import image.HeatmapImageManager;
import map.PerlinMap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageManagerTester {

    public static void main(String[] arg) {
        PerlinMap map = new PerlinMap(1000,
                1000,
                Math.random(),
                Math.random(),
                0.0,
                34,
                0.6,
                0.5,
                -0.0125,
                3,
                50,
                0.5,
                8,
                true,
                true,
                true,
                true,
                false,
                false,
                true,
                false,
                true);

        HeatmapImageManager heatmapImageManager = new HeatmapImageManager(map);
        heatmapImageManager.generateImage();
        createFrame(heatmapImageManager.getImage());
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
