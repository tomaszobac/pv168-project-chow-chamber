package cz.muni.fi.pv168.project.ui.model.enums;

public enum RecipeCategory {
    PRILOHA("Příloha"),
    HLAVNI_JIDLO("Hlavní jídlo"),
    ZAKUSEK("Zákusek"),
    NONE("Bez kategorie");

    private final String category;

    RecipeCategory(String s) {
        this.category = s;
    }

    public String getCategory() {
        return category;
    }
}
