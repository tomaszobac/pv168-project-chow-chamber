package cz.muni.fi.pv168.project.ui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;

public class Icons {
    public static final Icon QUIT_ICON = createIcon("power-off-line-icon.png");
    public static final Icon ADD_ICON = createIcon("plus-line-icon.png");
    public static final Icon EDIT_ICON = createIcon("edit-document-icon.png");
    public static final Icon DELETE_ICON = createIcon("recycle-bin-line-icon.png");
    public static final Icon IMPORT_ICON = createIcon("document-import-icon.png");
    public static final Icon EXPORT_ICON = createIcon("document-send-icon.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
