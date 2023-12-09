package cz.muni.fi.pv168.project.ui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;

public class Icons {
    public static final Icon QUIT_ICON = createIcon("mat3powerW.png");
    public static final Icon ADD_ICON = createIcon("mat3addW.png");
    public static final Icon EDIT_ICON = createIcon("mat3editW.png");
    public static final Icon DELETE_ICON = createIcon("mat3deleteW.png");
    public static final Icon IMPORT_ICON = createIcon("mat3importW.png");
    public static final Icon EXPORT_ICON = createIcon("mat3exportW.png");
    public static final Icon FILTER_ICON = createIcon("mat3filterW.png");
    public static final Icon BALANCE_ICON = createIcon("mat3balanceW.png");

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
