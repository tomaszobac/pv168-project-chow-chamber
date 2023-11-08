package cz.muni.fi.pv168.project.ui.filters.matchers.unit;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.model.entities.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
public class UnitUnitTypeMatcher extends EntityMatcher<Unit> {
    private final UnitType unitType;

    public UnitUnitTypeMatcher(UnitType unitType) {
        this.unitType = unitType;
    }

    @Override
    public boolean evaluate(Unit unit) {
        return unit.getType() == unitType;
    }
}
