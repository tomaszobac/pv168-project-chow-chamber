package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.ui.model.EntityTableModel;

import javax.swing.RowFilter;

/**
 * General entity matcher which can be extended by implementing the {@link EntityMatcher#evaluate(Object)}
 * method.
 *
 * @param <T> entity type
 */
public abstract class EntityMatcher<T> extends RowFilter<EntityTableModel<T>, Integer> {

    /**
     * Determines whether a particular row should be included in the filtered view.
     *
     * @param entry The entry representing the row in the table model.
     * @return {@code true} if the row should be included, {@code false} otherwise.
     *
     * @throws IndexOutOfBoundsException If the entry identifier is out of bounds in the table model.
     */
    @Override
    public boolean include(Entry<? extends EntityTableModel<T>, ? extends Integer> entry) {
        EntityTableModel<T> tableModel = entry.getModel();
        int rowIndex = entry.getIdentifier();
        T entity = tableModel.getEntity(rowIndex);

        return evaluate(entity);
    }

    public abstract boolean evaluate(T entity);
}

