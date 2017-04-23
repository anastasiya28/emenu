package application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс "MenuItem" описывает содержание и поведение сущности пункт меню.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItem {
    private Integer id;
    private String name;
    private Integer portionVolume;
    private Measure measure;
    private Double price;
    private MenuSection menuSection;
    private Map<Integer, Ingredient> ingredients = new HashMap<>();

    public MenuItem() {
    }

    public Integer getId() {
        return id;
    }

    public MenuItem setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPortionVolume() {
        return portionVolume;
    }

    public MenuItem setPortionVolume(Integer portionVolume) {
        this.portionVolume = portionVolume;
        return this;
    }

    public Measure getMeasure() {
        return measure;
    }

    public MenuItem setMeasure(Measure measure) {
        this.measure = measure;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public MenuItem setPrice(Double price) {
        this.price = price;
        return this;
    }

    public MenuSection getMenuSection() {
        return menuSection;
    }

    public MenuItem setMenuSection(MenuSection menuSection) {
        this.menuSection = menuSection;
        return this;
    }

    public Map<Integer, Ingredient> getIngredients() {
        return ingredients;
    }

    public MenuItem setIngredients(Map<Integer, Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public MenuItem addIngredient(Integer id, Ingredient ingredient) {
        this.ingredients.put(id, ingredient);
        return this;
    }

    public MenuItem removeIngredient(Integer id) {
        this.ingredients.remove(id);
        return this;
    }

    @Override
    public String toString() {
        return "\nMenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", portionVolume=" + portionVolume +
                ", measure=" + measure +
                ", price=" + price +
                ", menuSection=" + menuSection +
                ", ingredients=" + ingredients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;

        if (!id.equals(menuItem.id)) return false;
        if (!name.equals(menuItem.name)) return false;
        if (!portionVolume.equals(menuItem.portionVolume)) return false;
        if (!measure.equals(menuItem.measure)) return false;
        if (!price.equals(menuItem.price)) return false;
        if (!menuSection.equals(menuItem.menuSection)) return false;
        return ingredients.equals(menuItem.ingredients);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((portionVolume == null) ? 0 : portionVolume.hashCode());
        result = 31 * result + ((measure == null) ? 0 : measure.hashCode());
        result = 31 * result + ((price == null) ? 0 : price.hashCode());
        result = 31 * result + ((menuSection == null) ? 0 : menuSection.hashCode());
        result = 31 * result + ((ingredients == null) ? 0 : ingredients.hashCode());
        return result;
    }
}
