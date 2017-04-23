package application.controller;

import application.dao.interfaces.MenuDAO;
import application.dao.interfaces.OrganizationDAO;
import application.model.Menu;
import application.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

@Controller
public class MenuController {
    @Autowired
    private MenuDAO menuRepository;

    @Autowired
    private OrganizationDAO organizationRepository;

    @RequestMapping(value = "/api/menu", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> getAll() {
        return menuRepository.getAll();
    }

    @RequestMapping(value = "/api/menu/{id}")
    @ResponseBody
    public Menu getById(@PathVariable("id") int id) {
        return menuRepository.getById(id);
    }

    @RequestMapping(value = "/api/org/{orgId}/menu")
    @ResponseBody
    public List<Menu> getOrganization(@PathVariable("id") int id) throws UnsupportedEncodingException {
        Organization org = organizationRepository.getById(id);
        if (org != null) {
            return menuRepository.getByOrganization(org);
        }
        return null;
    }
}
