package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

public record UnitEntity(Long id, String guid, String number, String name) {
    public UnitEntity(Long id, String guid, String number, String name) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.number = Objects.requireNonNull(number, "number must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
    }
}
