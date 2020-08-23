package image_browser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Browser extends JFrame {
    private GUI gui;

    public Browser() throws IOException {
        gui = new GUI();
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setFocusable(true);
        addMouseListener(gui);
        addMouseWheelListener(gui);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(gui);
        pack();
        setVisible(true);
    }
}
