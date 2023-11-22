package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.unit.UnitNameMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.unit.UnitUnitTypeMatcher;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterUnitTypeValues;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.table.TableRowSorter;
import java.util.stream.Stream;

public class UnitTableFilter {
    private final UnitTableFilter.UnitCompoundMatcher unitCompoundMatcher;
    private String name = "";
    private Either<SpecialFilterUnitTypeValues, UnitType> selectedUnitType = Either.left(SpecialFilterUnitTypeValues.ALL);

    public UnitTableFilter(TableRowSorter<UnitTableModel> rowSorter) {
        unitCompoundMatcher = new UnitTableFilter.UnitCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(unitCompoundMatcher);
    }

    public void filterName(String name) {
        this.name = name;
        unitCompoundMatcher.setNameMatcher(new UnitNameMatcher(name));
    }
    public void filterUnitType(Either<SpecialFilterUnitTypeValues, UnitType> selectedItem) {
        this.selectedUnitType = selectedItem;
        selectedItem.apply(
                l -> unitCompoundMatcher.setUnitTypeMatcher(l.getMatcher()),
                r -> unitCompoundMatcher.setUnitTypeMatcher(new UnitUnitTypeMatcher(r))
        );
    }

    public String getName() {
        return name;
    }

    public Either<SpecialFilterUnitTypeValues, UnitType> getSelectedUnitType() {
        return selectedUnitType;
    }

    /**
     * Container class for all matchers for the UnitTable.
     *
     * This Matcher evaluates to true, if all contained {@link EntityMatcher} instances
     * evaluate to true.
     */
    private static class UnitCompoundMatcher extends EntityMatcher<Unit> {

        private final TableRowSorter<UnitTableModel> rowSorter;
        private EntityMatcher<Unit> nameMatcher = EntityMatchers.all();
        private EntityMatcher<Unit> unitTypeMatcher = EntityMatchers.all();

        private UnitCompoundMatcher(TableRowSorter<UnitTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private  void setNameMatcher(EntityMatcher<Unit> nameMatcher) {
            this.nameMatcher = nameMatcher;
            rowSorter.sort();
        }

        private void setUnitTypeMatcher(EntityMatcher<Unit> unitTypeMatcher) {
            this.unitTypeMatcher = unitTypeMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(Unit unit) {
            return Stream.of(nameMatcher, unitTypeMatcher)
                    .allMatch(m -> m.evaluate(unit));
        }
    }
}
