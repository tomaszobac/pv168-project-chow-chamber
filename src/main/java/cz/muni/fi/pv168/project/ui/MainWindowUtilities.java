package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.renderers.MyFrame;

import javax.swing.*;
import java.awt.*;

public class MainWindowUtilities {
    public static void switchToRecipeTab(int tabIndex, JTabbedPane openRecipesTab) {
        if (tabIndex >= 0 && tabIndex < openRecipesTab.getTabCount()) {
            openRecipesTab.setSelectedIndex(tabIndex);
        }
    }

    public static JLabel createLabel(String text, int fontType) {
        Font font = fontType == 0 ? new Font(null, Font.BOLD, 14) : new Font(null, Font.PLAIN, 14);
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    public static JFrame createFrame(Dimension min_size, Dimension max_size, String name) {
        MyFrame Mframe = new MyFrame();
        Mframe.setTitle(name);
        Mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mframe.setVisible(true);

        if (min_size == null) {
            min_size = new Dimension(800, 400);
        }
        if (max_size == null) {
            max_size = new Dimension(1920, 1080);
        }
        Mframe.setMinimumSize(min_size);
        Mframe.setMaximumSize(max_size);

        return Mframe;
    }

}