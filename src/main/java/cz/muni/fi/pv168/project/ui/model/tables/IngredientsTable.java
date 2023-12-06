package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class IngredientsTable extends MyTable {
    private final List<List<String>> infoTables = new ArrayList<>();
    private JFrame ingredientInfoFrame = null;
    private JTabbedPane ingredientInfoTabs = null;
    private int ingredientInTabs = 0;
    public IngredientsTable(AbstractTableModel model) {
        super(model);
    }

    public void setMouseListener(MyTable ingredientTable) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(ingredientTable);
                    if(index != -1) {
                        MainWindowUtilities.switchToTab(index, ingredientInfoTabs);
                        ingredientInfoFrame.setVisible(true);
                    } else{
                        ingredientInTabs++;
                        List<String> newIngredient = new ArrayList<>();
                        newIngredient.add(getValueAt(getSelectedRow(), 0).toString());
                        newIngredient.add(getValueAt(getSelectedRow(), 1).toString());
                        newIngredient.add(getValueAt(getSelectedRow(), 2).toString());
                        infoTables.add(newIngredient);
                        openInfoWindow(ingredientTable);
                    }
                }
            }
        });
    }

    private int findTable(MyTable ingredientTable){
        boolean flag = true;
        for (int j = 0; j < infoTables.size(); j++) {
            for (int i = 0; i < 4; i++) {
                if (!infoTables.get(j).get(i).equals(ingredientTable.getValueAt(ingredientTable.getSelectedRow(), i).toString())) {
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
    /**
     * This method opens new window(s) upon clicking on Ingredient(s). One window contains two tabs, first tab contains basic info,
     * second tab contains more info about one Ingredient.
     *
     * @param ingredientTable represents table of stored Ingredients.
     */
    private void openInfoWindow(MyTable ingredientTable) {
        if (ingredientInfoFrame == null) {
            ingredientInfoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Ingredient");
            ingredientInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ingredientInfoTabs = new JTabbedPane();
        }
        JTabbedPane singleIngredientInfo = new JTabbedPane();
        Ingredient ingredient = (Ingredient) ingredientTable.getValueAt(ingredientTable.getSelectedRow(), 0);

        JPanel infoPanel = createInfoPanel(ingredient);

        singleIngredientInfo.addTab("Basic info", null, infoPanel, "First Tab");
        createNewTab(singleIngredientInfo, ingredient.getName());
        MainWindowUtilities.switchToTab(ingredientInTabs - 1, ingredientInfoTabs);
        ingredientInfoFrame.add(ingredientInfoTabs);
        ingredientInfoFrame.pack();
        ingredientInfoFrame.setVisible(true);
    }

    private void createNewTab(JTabbedPane singleIngredientInfo, String name) {
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel(name);
        JButton closeButton = getJButton(singleIngredientInfo);
        customTabComponent.add(titleLabel);
        customTabComponent.add(closeButton);
        ingredientInfoTabs.addTab(null, singleIngredientInfo);
        int tabIndex = ingredientInfoTabs.indexOfComponent(singleIngredientInfo);
        ingredientInfoTabs.setTabComponentAt(tabIndex, customTabComponent);
        ingredientInfoTabs.setSelectedIndex(tabIndex);
    }

    private JPanel createInfoPanel(Ingredient ingredient) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(ingredient.getName(), 0));
        infoPanel.add(MainWindowUtilities.createLabel("Calories:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(Double.toString(ingredient.getCalories()), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Unit:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(ingredient.getUnit().toString(), 1));
        return infoPanel;
    }

    private JButton getJButton(JTabbedPane singleIngredientInfo) {
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(16, 16));

        closeButton.addActionListener(e -> {
            int tabIndex = ingredientInfoTabs.indexOfComponent(singleIngredientInfo);
            if (tabIndex != -1) {
                ingredientInfoTabs.remove(tabIndex);
                infoTables.remove(tabIndex);
                ingredientInTabs--;
                if (ingredientInTabs == 0) {
                    ingredientInfoFrame.dispose();
                }
            }
        });
        return closeButton;
    }
}
