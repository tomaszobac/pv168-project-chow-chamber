package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

public class Unit extends Entity {
    private String name;
    private UnitType type;
    private double conversionToBase; // Conversion factor to a base unit (e.g., liters)

    public Unit() {
    }

    public Unit(String guid, String name, UnitType type, double conversionToBase) {
        super(guid);
        this.name = name;
        this.conversionToBase = conversionToBase;
        this.type = type;
    }

    public Unit(String name, UnitType type, double conversionToBase) {
        this.name = name;
        this.conversionToBase = conversionToBase;
        this.type = type;
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
        this.name = name;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public void setConversionToBase(double conversionToBase) {
        this.conversionToBase = conversionToBase;
    }

    // Method to convert a value from this unit to the base unit
    public double convertToBase(double value) {
        return value * conversionToBase;
    }

    // Method to convert a value from the base unit to this unit
    public double convertFromBase(double value) {
        return value / conversionToBase;
    }

    // Method to calculate the difference between two units in this unit
    public double calculateDifference(double value1, double value2) {
        return Math.abs(value1 - value2);
    }

    @Override
    public String toString() {
        return name;
    }
}