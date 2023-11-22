package cz.muni.fi.pv168.project.business.service.crud;


import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import java.util.List;

/**
 * Crud operations for the {@link Recipe} entity.
 */
public final class RecipeCrudService implements CrudService<Recipe> {

    private final Repository<Recipe> recipeRepository;
    private final Validator<Recipe> recipeValidator;
    private final GuidProvider guidProvider;

    public RecipeCrudService(Repository<Recipe> recipeRepository, Validator<Recipe> recipeValidator,
                               GuidProvider guidProvider) {
        this.recipeRepository = recipeRepository;
        this.recipeValidator = recipeValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public ValidationResult create(Recipe newEntity) {
        var validationResult = recipeValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (recipeRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Recipe with given guid already exists: " + newEntity.getGuid());
        }
        if (validationResult.isValid()) {
            recipeRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Recipe entity) {
        var validationResult = recipeValidator.validate(entity);
        if (validationResult.isValid()) {
            recipeRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        recipeRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        recipeRepository.deleteAll();
    }
}

