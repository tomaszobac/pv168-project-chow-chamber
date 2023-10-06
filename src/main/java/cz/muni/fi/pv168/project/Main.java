package cz.muni.fi.pv168.project;

import cz.muni.fi.pv168.project.ui.MainWindow;

/**
 * The entry point of the application.
 */
public class Main {

    private Main() {
        throw new AssertionError("This class is not intended for instantiation.");
    }

    public static void main(String[] args) {
        new MainWindow();
        System.out.println("Hello World!");
    }
}
