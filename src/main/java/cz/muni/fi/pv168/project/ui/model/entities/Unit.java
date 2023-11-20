package cz.muni.fi.pv168.project.ui.model.entities;

import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import java.util.Objects;

public class Unit {
    private String name;
    private UnitType type;
    private double conversionToBase; // Conversion factor to a base unit (e.g., liters)

    public Unit(String name, UnitType type, double conversionToBase) {
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

    public double convertToBase(double value) {
        return value * conversionToBase;
    }

    public double convertFromBase(double value) {
        return value / conversionToBase;
    }

    public double calculateDifference(double value1, double value2) {
        return Math.abs(value1 - value2); //TODO: Discuss the need of this function
    }

    @Override
    public String toString() {
        return String.format("Unit{name: %s; type: %s; toBase: %.3f}",
                name, type.name(), conversionToBase);
    }
}