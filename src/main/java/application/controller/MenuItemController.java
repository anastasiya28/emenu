package application.controller;

import application.dao.interfaces.MenuItemDAO;
import application.dao.interfaces.MenuSectionDAO;
import application.model.MenuItem;
import application.model.MenuSection;
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
public class MenuItemController {
    @Autowired
    private MenuItemDAO menuItemRepository;

    @Autowired
    private MenuSectionDAO menuSectionRepository;

    @RequestMapping(value = "/api/menuitem", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuItem> getAll() {
        return menuItemRepository.getAll();
    }

    @RequestMapping(value = "/api/menuitem/{id}")
    @ResponseBody
    public MenuItem getById(@PathVariable("id") int id) {
        return menuItemRepository.getById(id);
    }

    @RequestMapping(value = "/api/menuitem/name/{name}")
    @ResponseBody
    public List<MenuItem> getByName(@PathVariable("name") String name) throws UnsupportedEncodingException {
        return menuItemRepository.getByName(URLDecoder.decode(name, "UTF-8"));
    }

    @RequestMapping(value = "/api/menusection/{menusectionId}/menuitem")
    @ResponseBody
    public List<MenuItem> getMenuSection(@PathVariable("id") int id) throws UnsupportedEncodingException {
        MenuSection menuSection = menuSectionRepository.getById(id);
        if(menuSection != null){
            return menuItemRepository.getByMenuSection(menuSection);
        }
        return null;
    }
}
