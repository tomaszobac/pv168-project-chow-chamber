package cz.muni.fi.pv168.project.ui;

import javax.swing.*;

public class MainWindow {
    private final JFrame frame;
    public MainWindow() {
        frame = createFrame();

        JButton button = new JButton("Click me!");
        button.setBounds(130, 100, 100, 40);
        frame.add(button);
    }

    private JFrame createFrame() {
        JFrame jFrame = new JFrame("Recipe DB");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(800, 400);
        jFrame.setLayout(null);
        jFrame.setVisible(true);
        return jFrame;
    }
}
