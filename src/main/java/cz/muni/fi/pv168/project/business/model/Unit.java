package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import java.util.Objects;

public class Unit extends Entity {
    private String name;
    private UnitType type;
    private double conversionToBase;

    public Unit(String guid, String name, UnitType type, double conversionToBase) {
        super(guid);
        this.name = name;
        this.conversionToBase = conversionToBase;
        this.type = type;
    }

    @JsonCreator
    public Unit(@JsonProperty("name") String name,
                @JsonProperty("type") UnitType type,
                @JsonProperty("conversionToBase") double conversionToBase) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.conversionToBase = conversionToBase;
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public String getName() {
        return name;
    }

    public UnitType getType() {
        return type;
    }

    public double getConversionToBase() {
        return conversionToBase;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public void setType(UnitType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public void setConversionToBase(double conversionToBase) {
        this.conversionToBase = conversionToBase;
    }

    @Override
    public String toString() {
        return String.format("Unit{name: %s; type: %s; toBase: %.3f}",
                name, type.name(), conversionToBase);
    }
}