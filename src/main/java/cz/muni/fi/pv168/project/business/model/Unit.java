package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Unit class represents a unit of measurement.
 * It extends the Entity class and has fields for the unit's name, type, and conversion factor to the base unit.
 */
public class Unit extends Entity implements Serializable {
    private String name;
    private UnitType type;
    private double conversionToBase;

    /**
     * Creates a new Unit with the given parameters.
     *
     * @param guid The unique identifier of the unit.
     * @param name The name of the unit.
     * @param type The type of the unit.
     * @param conversionToBase The conversion factor to the base unit.
     */
    @JsonCreator
    public Unit(@JsonProperty("guid") String guid,
                @JsonProperty("name") String name,
                @JsonProperty("type") UnitType type,
                @JsonProperty("conversionToBase") double conversionToBase) {
        super(guid);
        this.name = Objects.requireNonNull(name);
        this.conversionToBase = conversionToBase;
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Creates a new Unit with the given parameters.
     *
     * @param name The name of the unit. Must not be null.
     * @param type The type of the unit. Must not be null.
     * @param conversionToBase The conversion factor to the base unit.
     * @throws NullPointerException if either name or type is null.
     */
    public Unit(String name, UnitType type, double conversionToBase) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.conversionToBase = conversionToBase;
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    /**
     * Returns the name of the unit.
     *
     * @return the name of the unit
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the unit.
     *
     * @return the type of the unit
     */
    public UnitType getType() {
        return type;
    }

    /**
     * Returns the conversion factor to convert a unit to its base unit.
     *
     * @return the conversion factor to convert a unit to its base unit
     */
    public double getConversionToBase() {
        return conversionToBase;
    }

    /**
     * Sets the name of an object.
     *
     * @param name the name to be set
     * @throws NullPointerException if the name is null
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    /**
     * Sets the type of the unit.
     *
     * @param type the type of the unit
     * @throws NullPointerException if the specified type is null
     */
    public void setType(UnitType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    /**
     * Sets the conversion factor to convert a value to the base unit.
     *
     * @param conversionToBase the conversion factor to set
     */
    public void setConversionToBase(double conversionToBase) {
        this.conversionToBase = conversionToBase;
    }

    /**
     * Returns a string representation of the Unit object.
     *
     * @return a string representation of the Unit object.
     */
    @Override
    public String toString() {
        return String.format("Unit{guid: %s; name: %s; type: %s; toBase: %.3f}",
                getGuid(), name, type.name(), conversionToBase);
    }
}