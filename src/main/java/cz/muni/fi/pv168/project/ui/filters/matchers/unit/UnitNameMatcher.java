package cz.muni.fi.pv168.project.ui.filters.matchers.unit;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.business.model.Unit;

public class UnitNameMatcher extends EntityMatcher<Unit> {
    String name;

    public UnitNameMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(Unit unit) {
        if (name.isEmpty()) {
            return true;
        }
        return unit.getName().toLowerCase().contains(this.name.toLowerCase());
    }
}
