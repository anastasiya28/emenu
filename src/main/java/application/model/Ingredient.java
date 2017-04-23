package application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Класс "Ingredient" описывает содержание и поведение сущности ингредиент.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ingredient {
    private Integer id;
    private String name;

    public Ingredient() {
    }

    public Integer getId() {
        return id;
    }

    public Ingredient setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "\nIngredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        if (!id.equals(that.id)) return false;
        return name.equals(that.name);

    }

// Вариант переопределения метода: public int hashCode() {...}, который сделала я, с учетом проверки на null!!!
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
