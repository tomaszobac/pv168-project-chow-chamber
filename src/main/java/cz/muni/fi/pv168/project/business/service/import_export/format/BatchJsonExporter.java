package cz.muni.fi.pv168.project.business.service.import_export.format;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.service.import_export.DataManipulationException;
import cz.muni.fi.pv168.project.business.service.import_export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchExporter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BatchJsonExporter implements BatchExporter {
    private static final Format FORMAT = new Format("JSON", List.of("json"));
    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public void exportBatch(Batch batch, String filePath) {

        try (var writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            String data = "{";
            writer.write(data);
            String recipes = "\"recipes\":[";
            writer.write(recipes);
            boolean start = true;

            for (var recipe : batch.recipes()) {
                StringBuilder line = new StringBuilder();
                if (!start){
                    line.append(",");
                }
                createJsonRecipeLine(line, recipe);
                writer.write(line.toString());
                start = false;
            }
            String ending = "],";
            writer.write(ending);
            writer.newLine();

            String units = "\"units\":[";
            writer.write(units);
            start = true;

            for (var unit : batch.units()) {
                StringBuilder line = new StringBuilder();
                if (!start){
                    line.append(",");
                }
                createJsonUnitLine(line, unit);
                writer.write(line.toString());
                start = false;
            }
            writer.write(ending);
            writer.newLine();

            String ingredients = "\"ingredients\":[";
            writer.write(ingredients);
            start = true;

            for (var ingredient : batch.ingredients()) {
                StringBuilder line = new StringBuilder();
                if (!start){
                    line.append(",");
                }
                createJsonIngredientLine(line, ingredient);
                writer.write(line.toString());
                start = false;
            }
            writer.write(ending);
            writer.newLine();

            String recipeIngredients = "\"recipeIngredients\":[";
            writer.write(recipeIngredients);
            start = true;

            for (var recipeIngredient : batch.recipeIngredients()) {
                StringBuilder line = new StringBuilder();
                if (!start){
                    line.append(",");
                }
                createJsonRecipeIngredientLine(line, recipeIngredient);
                writer.write(line.toString());
                start = false;
            }
            writer.write("]");
            writer.write("}");
            writer.newLine();
        } catch (IOException exception) {
            throw new DataManipulationException("Unable to write to file", exception);
        }
    }
    public void createJsonRecipeLine(StringBuilder line, Recipe recipe){
        line.append("{");
        line.append("\"guid\":");
        line.append("\"" + recipe.getGuid() + "\",");
        line.append("\"name\":");
        line.append("\"" + recipe.getName() + "\",");
        line.append("\"instructions\":");
        line.append("\"" + recipe.getInstructions() + "\",");
        line.append("\"category\":");
        line.append("\"" + recipe.getCategory() + "\",");
        line.append("\"time\":");
        line.append("\"" + recipe.getTime().toString() + "\",");
        line.append("\"portions\":");
        line.append("\"" + recipe.getPortions() + "\"");
        line.append("}");
    }
    public void createJsonUnitLine(StringBuilder line, Unit unit){
        line.append("{");
        line.append("\"guid\":");
        line.append("\"" + unit.getGuid() + "\",");
        line.append("\"name\":");
        line.append("\"" + unit.getName() + "\",");
        line.append("\"type\":");
        line.append("\"" + unit.getType().toString() + "\",");
        line.append("\"conversionToBase\":");
        line.append("\"" + unit.getConversionToBase() + "\"");
        line.append("}");
    }
    public void createJsonIngredientLine(StringBuilder line, Ingredient ingredient){
        line.append("{");
        line.append("\"guid\":");
        line.append("\"" + ingredient.getGuid() + "\",");
        line.append("\"name\":");
        line.append("\"" + ingredient.getName() + "\",");
        line.append("\"calories\":");
        line.append("\"" + ingredient.getCalories() + "\",");
        line.append("\"unit\":");
        line.append("{");
        line.append("\"name\":");
        line.append("\"" + ingredient.getUnit().getName() + "\",");
        line.append("\"type\":");
        line.append("\"" + ingredient.getUnit().getType().toString() + "\",");
        line.append("\"conversionToBase\":");
        line.append("\"" + ingredient.getUnit().getConversionToBase() + "\"");
        line.append("}");
        line.append("}");
    }

    public void createJsonRecipeIngredientLine(StringBuilder line, RecipeIngredient recipeIngredient) {
        line.append("{");
        line.append("\"recipeGuid\":");
        line.append("\"" + recipeIngredient.getRecipe().getGuid() + "\",");
        line.append("\"ingredientGuid\":");
        line.append("\"" + recipeIngredient.getIngredient().getGuid() + "\",");
        line.append("\"unit\":");
        createJsonUnitLine(line, recipeIngredient.getUnit());
        line.append(",");
        line.append("\"amount\":");
        line.append("\"" + recipeIngredient.getAmount() + "\"");
        line.append("}");
    }
}