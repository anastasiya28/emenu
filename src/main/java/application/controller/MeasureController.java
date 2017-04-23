package application.controller;

import application.dao.interfaces.MeasureDAO;
import application.model.Measure;
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
public class MeasureController {
    @Autowired
    private MeasureDAO measureRepository;

    @RequestMapping(value = "/api/measure", method = RequestMethod.GET)
    @ResponseBody
    public List<Measure> getAll() {
        return measureRepository.getAll();
    }

    @RequestMapping(value = "/api/measure/{id}")
    @ResponseBody
    public Measure getById(@PathVariable("id") int id) {
        return measureRepository.getById(id);
    }

    @RequestMapping(value = "/api/measure/shortname/{shortName}")
    @ResponseBody
    public List<Measure> getByShortName(@PathVariable("shortName") String shortName) throws UnsupportedEncodingException {
        return measureRepository.getByShortName(URLDecoder.decode(shortName, "UTF-8"));
    }

    @RequestMapping(value = "/api/measure/fullname/{fullName}")
    @ResponseBody
    public List<Measure> getByFullName(@PathVariable("fullName") String fullName) throws UnsupportedEncodingException {
        return measureRepository.getByFullName(URLDecoder.decode(fullName, "UTF-8"));
    }
}
