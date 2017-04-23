package application.controller;

import application.dao.interfaces.MenuDAO;
import application.dao.interfaces.MenuItemDAO;
import application.dao.interfaces.MenuSectionDAO;
import application.model.Menu;
import application.model.MenuItem;
import application.model.MenuSection;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class MenuSectionController {
    @Autowired
    private MenuSectionDAO menuSectionRepository;

    @Autowired
    private MenuDAO menuRepository;


    @RequestMapping(value = "/api/menusection", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuSection> getAll() {
        return menuSectionRepository.getAll();
    }

    @RequestMapping(value = "/api/menusection/{id}")
    @ResponseBody
    public MenuSection getById(@PathVariable("id") int id) {
        return menuSectionRepository.getById(id);
    }

    @RequestMapping(value = "/api/menusection/name/{name}")
    @ResponseBody
    public List<MenuSection> getByName(@PathVariable("name") String name) throws UnsupportedEncodingException {
        return menuSectionRepository.getByName(URLDecoder.decode(name, "UTF-8"));
    }

    @RequestMapping(value = "/api/menu/{menuId}/menusection")
    @ResponseBody
    public List<MenuSection> getByMenu(@PathVariable("id") int id) throws UnsupportedEncodingException {
        Menu menu = menuRepository.getById(id);
        if (menu != null) {
            return menuSectionRepository.getByMenu(menu);
        }
        return null;
    }
}
