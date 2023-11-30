package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import java.util.List;
import java.util.Objects;

/**
 * Crud operations for the {@link Unit} entity.
 */
public final class UnitCrudService implements CrudService<Unit> {

    private final Repository<Unit> unitRepository;
    private final Validator<Unit> unitValidator;
    private final GuidProvider guidProvider;

    public UnitCrudService(Repository<Unit> unitRepository, Validator<Unit> unitValidator,
                             GuidProvider guidProvider) {
        this.unitRepository = Objects.requireNonNull(unitRepository);
        this.unitValidator = Objects.requireNonNull(unitValidator);
        this.guidProvider = Objects.requireNonNull(guidProvider);
    }

    @Override
    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    @Override
    public ValidationResult create(Unit newEntity) {
        var validationResult = unitValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (unitRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Unit with given guid already exists: " + newEntity.getGuid());
        }
        if (validationResult.isValid()) {
            unitRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Unit entity) {
        var validationResult = unitValidator.validate(entity);
        if (validationResult.isValid()) {
            unitRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        unitRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        unitRepository.deleteAll();
    }
}

