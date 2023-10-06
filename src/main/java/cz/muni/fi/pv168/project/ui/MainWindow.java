package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.AddAction;
import cz.muni.fi.pv168.project.ui.action.DeleteAction;
import cz.muni.fi.pv168.project.ui.action.EditAction;
import cz.muni.fi.pv168.project.ui.action.QuitAction;

import javax.swing.*;

public class MainWindow {
    private final JFrame frame;
    private final Action quitAction = new QuitAction();
    private final Action addAction;
    private final Action editAction;
    private final Action deleteAction;
    public MainWindow() {
        frame = createFrame();

        addAction = new AddAction();
        editAction = new EditAction();
        deleteAction = new DeleteAction();
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
