package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import java.util.Objects;

/**
 * Representation of Unit entity in a SQL database.
 */
public record UnitEntity(Long id, String guid, String name, UnitType type, double conversionToBase) {
    public UnitEntity(Long id, String guid, String name, UnitType type, double conversionToBase) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.conversionToBase = conversionToBase;
    }
}
