package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitUnitTest {

    @Test
    public void testConstructorAndGetters() {
        String guid = "123";
        String name = "TestUnit";
        UnitType type = UnitType.Weight;
        double conversionToBase = 0.001;

        Unit unit = new Unit(guid, name, type, conversionToBase);

        assertEquals(guid, unit.getGuid());
        assertEquals(name, unit.getName());
        assertEquals(type, unit.getType());
        assertEquals(conversionToBase, unit.getConversionToBase());
    }

    @Test
    public void testConstructorWithInvalidConversionToBase() {
        assertThrows(NumberFormatException.class, () -> {
            new Unit("TestUnit", UnitType.Weight, 0.0);
        });
    }

    @Test
    public void testConstructorWithNullName() {
        assertThrows(NullPointerException.class, () -> {
            new Unit(null, UnitType.Weight, 0.001);
        });
    }

    @Test
    public void testConstructorWithNullType() {
        assertThrows(NullPointerException.class, () -> {
            new Unit("TestUnit", null, 0.001);
        });
    }

    @Test
    public void testSetterMethods() {
        Unit unit = new Unit("TestUnit", UnitType.Weight, 0.001);

        String newName = "UpdatedUnit";
        UnitType newType = UnitType.Volume;
        double newConversionToBase = 0.000001;

        unit.setName(newName);
        unit.setType(newType);
        unit.setConversionToBase(newConversionToBase);

        assertEquals(newName, unit.getName());
        assertEquals(newType, unit.getType());
        assertEquals(newConversionToBase, unit.getConversionToBase());
    }

    @Test
    public void testToStringMethod() {
        String guid = "123";
        String name = "TestUnit";
        UnitType type = UnitType.Weight;
        double conversionToBase = 0.001;

        Unit unit = new Unit(guid, name, type, conversionToBase);

        String expectedToString = String.format("Unit{guid: %s; name: %s; type: %s; toBase: %.3f}",
                guid, name, type.name(), conversionToBase);

        assertEquals(expectedToString, unit.toString());
    }
}