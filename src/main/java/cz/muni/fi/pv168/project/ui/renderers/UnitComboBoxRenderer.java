package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.model.entities.Unit;

import javax.swing.JLabel;

public class UnitComboBoxRenderer extends AbstractRenderer<Unit> {
    public UnitComboBoxRenderer() {
        super(Unit.class);
    }

    @Override
    protected void updateLabel(JLabel label, Unit unit) {
        if (unit != null) {
            label.setText(unit.getName());
        }
    }
}
