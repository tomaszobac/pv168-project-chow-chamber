package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;

import java.util.List;

/**
 * Service for creation, read, update, and delete operations.
 *
 * @param <T> entity type.
 */
public interface CrudService<T extends Entity> {

    /**
     * Find all entities.
     */
    List<T> findAll();

    /**
     * Validate and store the given {@code newEntity}.
     *
     * @throws EntityAlreadyExistsException if there is already an existing entity with the same guid
     */
    ValidationResult create(T newEntity);

    /**
     * Updates the given {@code entity}.
     */
    ValidationResult update(T entity);

    /**
     * Delete entity with given guid.
     */
    void deleteByGuid(String guid);

    /**
     * Delete all entities.
     */
    void deleteAll();
}
