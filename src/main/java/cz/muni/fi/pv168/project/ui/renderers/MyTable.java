package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.MainWindowUtilities;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MyTable<T> extends JTable {
    protected final List<T> infoTables = new ArrayList<>();
    protected JFrame infoFrame = null;
    protected JTabbedPane infoTabs = null;
    protected int inTabs = 0;

    public MyTable(TableModel model) {
        super(model);
        UnifiedTableCellRenderer renderer = new UnifiedTableCellRenderer();
        setDefaultRenderer(Object.class, renderer);
        getTableHeader().setDefaultRenderer(renderer);
        setFocusable(false);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);
        Color alternateColor = new Color(100, 100, 100);
        Color mainColor = getBackground();

        if(!component.getBackground().equals(getSelectionBackground())) {
            Color rowColor = (row % 2 != 0 ? alternateColor : mainColor);
            component.setBackground(rowColor);
        }

        return component;
    }

    public void setMouseListener(MyTable<T> table) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(table);
                    if(index != -1) {
                        MainWindowUtilities.switchToTab(index, infoTabs);
                        infoFrame.setVisible(true);
                    } else{
                        inTabs++;
                        infoTables.add((T) getValueAt(getSelectedRow(), 0));
                        openInfoWindow(table);
                    }
                }
            }
        });
    }
    public void setMouseListener(MyTable<T> table, CrudService<RecipeIngredient> recIngCrud, Repository<Ingredient> ingRepository, Repository<Recipe> recRepository, GuidProvider provider) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(table);
                    if(index != -1) {
                        MainWindowUtilities.switchToTab(index, infoTabs);
                        infoFrame.setVisible(true);
                    } else{
                        inTabs++;
                        infoTables.add((T) getValueAt(getSelectedRow(), 0));
                        openInfoWindow(table, recIngCrud, ingRepository, recRepository, provider);
                    }
                }
            }
        });
    }

    protected JButton getJButton(JTabbedPane singleInfo) {
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(16, 16));

        closeButton.addActionListener(e -> {
            int tabIndex = infoTabs.indexOfComponent(singleInfo);
            if (tabIndex != -1) {
                infoTabs.remove(tabIndex);
                infoTables.remove(tabIndex);
                inTabs--;
                if (inTabs == 0) {
                    infoFrame.dispose();
                }
            }
        });
        return closeButton;
    }

    protected int findTable(MyTable<T> table){
        boolean flag = true;
        for (int j = 0; j < infoTables.size(); j++) {
            for (int i = 0; i < 4; i++) {
                if (!infoTables.get(j).equals(table.getValueAt(table.getSelectedRow(), 0))) {
                    flag = true;
                    break;
                }
                flag = false;
            }
            if (!flag) {
                return j;
            }
        }
        return -1;
    }

    protected void createNewTab(JTabbedPane singleRecipeInfo, String name) {
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel(name);
        JButton closeButton = getJButton(singleRecipeInfo);
        customTabComponent.add(titleLabel);
        customTabComponent.add(closeButton);
        infoTabs.addTab(null, singleRecipeInfo);
        int tabIndex = infoTabs.indexOfComponent(singleRecipeInfo);
        infoTabs.setTabComponentAt(tabIndex, customTabComponent);
        infoTabs.setSelectedIndex(tabIndex);
    }

    protected void openInfoWindow(MyTable<T> table) {
        // Meant to be overriden
    }
    protected void openInfoWindow(MyTable<T> table, CrudService<RecipeIngredient> recIngCrud, Repository<Ingredient> ingRepository, Repository<Recipe> recRepository, GuidProvider provider) {
        // Meant to be overriden
    }
}