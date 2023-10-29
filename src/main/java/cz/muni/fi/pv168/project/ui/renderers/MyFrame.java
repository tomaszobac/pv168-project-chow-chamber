package cz.muni.fi.pv168.project.ui.renderers;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

}

