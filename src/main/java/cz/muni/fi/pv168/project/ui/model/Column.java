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
    private final String name;
    private final Function<E, T> valueGetter;
    private final BiConsumer<E, T> valueSetter;
    private final Class<T> columnType;

    private Column(String name, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.columnType = Objects.requireNonNull(columnClass, "column class cannot be null");
        this.valueGetter = Objects.requireNonNull(valueGetter, "value getter cannot be null");
        this.valueSetter = valueSetter;
    }

    public static <E, T> Column<E, T> editable(String name, Class<T> columnClass, Function<E, T> valueGetter,
                                               BiConsumer<E, T> valueSetter) {
        return new Column<>(name, columnClass, valueGetter, Objects.requireNonNull(valueSetter, "value setter cannot be null"));
    }

    public static <E, T> Column<E, T> readonly(String name, Class<T> columnClass, Function<E, T> valueGetter) {
        return new Column<>(name, columnClass, valueGetter, null);
    }

    void setValue(Object value, E entity) {
        if (valueSetter == null) {
            throw new UnsupportedOperationException("Cannot set value in readonly column: '" + name + "'");
        }
        valueSetter.accept(entity, columnType.cast(value));
    }

    T getValue(E entity) {
        return valueGetter.apply(entity);
    }

    public String getName() {
        return name;
    }

    boolean isEditable() {
        return valueSetter != null;
    }
}
