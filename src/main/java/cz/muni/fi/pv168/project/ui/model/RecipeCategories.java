package cz.muni.fi.pv168.project.ui.model;

public enum RecipeCategories {
    PRILOHA("Příloha"),
    HLAVNI_JIDLO("Hlavní jídlo"),
    ZAKUSEK("Zákusek"),
    NONE("Bez kategorie");

    private final String category;

    RecipeCategories(String s) {
        this.category = s;
    }

    public String getCategory() {
        return category;
    }
}
