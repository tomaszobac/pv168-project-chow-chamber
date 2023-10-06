package cz.muni.fi.pv168.project.ui.model;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The `Column` class represents a data column that can store values of a specific type (`T`)
 * for entities of type (`E`)
 *
 * @param <E> The type of entities that this column can store values for
 * @param <T> The type of values that this column can store
 */
public class Column<E, T> {
    // The name of the column
    private final String name;

    // A function that retrieves the value from an entity
    private final Function<E, T> valueGetter;

    // A function that sets the value for an entity. It may be null
    private final BiConsumer<E, T> valueSetter;

    // The class type of values that this column can store
    private final Class<T> columnType;

    private Column(String name, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.columnType = Objects.requireNonNull(columnClass, "column class cannot be null");
        this.valueGetter = Objects.requireNonNull(valueGetter, "value getter cannot be null");
        this.valueSetter = valueSetter;
    }

    // TODO: Remove constructor and changeToEditable, these are placeholders
    private Column(String name, Class<T> columnClass) {
        this.name = name;
        this.columnType = columnClass;
        this.valueGetter = null;
        this.valueSetter = null;
    }
    public static <E, T> Column<E, T> changeToEditable(String name, Class<T> columnClass) {
        return new Column<>(name, columnClass);
    }

    public static <E, T> Column<E, T> editable(String name, Class<T> columnClass, Function<E, T> valueGetter,
                                               BiConsumer<E, T> valueSetter) {
        return new Column<>(name, columnClass, valueGetter, Objects.requireNonNull(valueSetter, "value setter cannot be null"));
    }

    public static <E, T> Column<E, T> readonly(String name, Class<T> columnClass, Function<E, T> valueGetter) {
        return new Column<>(name, columnClass, valueGetter, null);
    }

    public String getName() {
        return name;
    }
}
