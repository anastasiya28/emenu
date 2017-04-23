package application.controller;

import application.dao.interfaces.IngredientDAO;
import application.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Controller
public class IngredientController {
    @Autowired
    private IngredientDAO ingredientRepository;

    @RequestMapping(value = "/api/ingredient", method = RequestMethod.GET)
    @ResponseBody
    public List<Ingredient> getAll() {
        return ingredientRepository.getAll();
    }

    @RequestMapping(value = "/api/ingredient/{id}")
    @ResponseBody
    public Ingredient getById(@PathVariable("id") int id) {
        return ingredientRepository.getById(id);
    }

    @RequestMapping(value = "/api/ingredient/name/{name}")
    @ResponseBody
    public List<Ingredient> getByName(@PathVariable("name") String name) throws UnsupportedEncodingException {
        return ingredientRepository.getByName(URLDecoder.decode(name, "UTF-8"));
    }
}
