package cz.muni.fi.pv168.project.ui.filters.values;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.model.entities.Unit;

import java.util.Objects;

public enum SpecialFilterUnitTypeValues {
    ALL(EntityMatchers.all());

    private final EntityMatcher<Unit> matcher;

    SpecialFilterUnitTypeValues(EntityMatcher<Unit> matcher) {
        this.matcher = Objects.requireNonNull(matcher, "matcher cannot be null");
    }

    public EntityMatcher<Unit> getMatcher() {
        return matcher;
    }
}
