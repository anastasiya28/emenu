package application.dao.interfaces;

import application.model.Measure;

import java.util.List;

public interface MeasureDAO {
    List<Measure> getAll();

    Measure getById(Integer id);

    List<Measure> getByShortName(String shortName);

    List<Measure> getByFullName(String fullName);

    Integer add(Measure measure);

    void update(Measure measure);

    void remove(Integer id);
}
