package application.dao.interfaces;

import application.model.Ingredient;

import java.util.List;

public interface IngredientDAO {
    List<Ingredient> getAll();

    Ingredient getById(Integer id);

    List<Ingredient> getByName(String name);

    Integer add(Ingredient ingredient);

    void update(Ingredient ingredient);

    void remove(Integer id);
}
