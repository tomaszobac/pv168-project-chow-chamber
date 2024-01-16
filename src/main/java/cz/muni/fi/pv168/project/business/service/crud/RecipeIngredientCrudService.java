package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import java.util.List;
import java.util.Objects;

/** 
 * Crud operations for the {@link RecipeIngredient} entity.
 **/
public final class RecipeIngredientCrudService implements CrudService<RecipeIngredient> {

    private final Repository<RecipeIngredient> recipeIngredientRepository;
    private final Validator<RecipeIngredient> recipeIngredientValidator;
    private final GuidProvider guidProvider;

    public RecipeIngredientCrudService(Repository<RecipeIngredient> recipeIngredientRepository, Validator<RecipeIngredient> recipeIngredientValidator, GuidProvider guidProvider) {
        this.recipeIngredientRepository = Objects.requireNonNull(recipeIngredientRepository);
        this.recipeIngredientValidator = Objects.requireNonNull(recipeIngredientValidator);
        this.guidProvider = Objects.requireNonNull(guidProvider);
    }

    @Override
    public List<RecipeIngredient> findAll() {
        return recipeIngredientRepository.findAll();
    }

    @Override
    public ValidationResult create(RecipeIngredient newEntity) {
        var validationResult = recipeIngredientValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (recipeIngredientRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("RecipeIngredient with given guid already exists: " + newEntity.getGuid());
        }
        if (validationResult.isValid()) {
            recipeIngredientRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(RecipeIngredient entity) {
        var validationResult = recipeIngredientValidator.validate(entity);
        if (validationResult.isValid()) {
            recipeIngredientRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        recipeIngredientRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        recipeIngredientRepository.deleteAll();
    }
}
