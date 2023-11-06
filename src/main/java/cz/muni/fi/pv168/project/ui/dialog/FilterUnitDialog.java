package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.filters.UnitTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterUnitTypeValues;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterUnitTypeValuesRenderer;
import cz.muni.fi.pv168.project.ui.renderers.UnitTypeRenderer;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FilterUnitDialog extends EntityDialog<UnitTableFilter> {
    private final UnitTableFilter unitTableFilter;
    private final JTextField nameField = new JTextField();
    private final JComboBox<Either<SpecialFilterUnitTypeValues, UnitType>> unitTypeComboBox;

    public FilterUnitDialog(UnitTableFilter unitTableFilter) {
        this.unitTableFilter = unitTableFilter;
        this.unitTypeComboBox = createUnitTypeFilter(unitTableFilter);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(unitTableFilter.getName());
        unitTypeComboBox.setSelectedItem(unitTableFilter.getSelectedUnitType());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Unit Type:", unitTypeComboBox);
    }

    private static JComboBox<Either<SpecialFilterUnitTypeValues, UnitType>> createUnitTypeFilter(
            UnitTableFilter unitTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterUnitTypeValues.class, UnitType.values())
                .setSpecialValuesRenderer(new SpecialFilterUnitTypeValuesRenderer())
                .setValuesRenderer(new UnitTypeRenderer())
                .setFilter(unitTableFilter::filterUnitType)
                .build();
    }

    @Override
    UnitTableFilter getEntity() {
        unitTableFilter.filterName(nameField.getText());
        return this.unitTableFilter;
    }
}
