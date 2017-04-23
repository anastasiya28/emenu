package application.controller;

import application.dao.interfaces.OrganizationDAO;
import application.model.Organization;
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
public class OrganizationController {
    @Autowired
    private OrganizationDAO organizationRepository;

    @RequestMapping(value = "/api/org", method = RequestMethod.GET)
    @ResponseBody
    public List<Organization> getAll() {
        return organizationRepository.getAll();
    }

    @RequestMapping(value = "/api/org/{id}")
    @ResponseBody
    public Organization getById(@PathVariable("id") int id) {
        return organizationRepository.getById(id);
    }

    @RequestMapping(value = "/api/org/name/{name}")
    @ResponseBody
    public List<Organization> getByName(@PathVariable("name") String name) throws UnsupportedEncodingException {
        return organizationRepository.getByName(URLDecoder.decode(name, "UTF-8"));
    }

    @RequestMapping(value = "/api/org/inn/{inn}")
    @ResponseBody
    public List<Organization> getByINN(@PathVariable("inn") String inn) throws UnsupportedEncodingException {
        return organizationRepository.getByINN(URLDecoder.decode(inn, "UTF-8"));
    }
}
