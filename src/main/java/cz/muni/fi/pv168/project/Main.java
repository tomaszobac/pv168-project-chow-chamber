package cz.muni.fi.pv168.project;

import java.awt.EventQueue;

import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.wiring.ProductionDependencyProvider;


/**
 * The entry point of the application.
 */
public class Main {

    private Main() {
        throw new AssertionError("This class is not intended for instantiation.");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainWindow(new ProductionDependencyProvider()).show());
    }
}
