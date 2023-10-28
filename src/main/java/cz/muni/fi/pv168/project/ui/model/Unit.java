package cz.muni.fi.pv168.project.ui.model;

public class Unit {
    private final String name;
    private final UnitType type;
    private final double conversionToBase; // Conversion factor to a base unit (e.g., liters)

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
}