//package cz.muni.fi.pv168.project.storage.sql.entity.mapper;
//
//import cz.muni.fi.pv168.project.business.model.Ingredient;
//import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
//
///**
// * Mapper from the {@link IngredientEntity} to {@link Ingredient}.
// */
//public final class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {
//
//    @Override
//    public Ingredient mapToBusiness(IngredientEntity dbDepartment) {
//        return new Ingredient(
//                dbDepartment.guid(),
//                dbDepartment.name(),
//                dbDepartment.number()
//        );
//    }
//
//    @Override
//    public IngredientEntity mapNewEntityToDatabase(Ingredient entity) {
//        return getDepartmentEntity(entity, null);
//    }
//
//    @Override
//    public IngredientEntity mapExistingEntityToDatabase(Ingredient entity, Long dbId) {
//        return getDepartmentEntity(entity, dbId);
//    }
//
//    private static IngredientEntity getDepartmentEntity(Ingredient entity, Long dbId) {
//        return new IngredientEntity(
//                dbId,
//                entity.getGuid(),
//                entity.getNumber(),
//                entity.getName()
//        );
//    }
//}
