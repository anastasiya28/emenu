package application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Класс "Measure" описывает содержание и поведение сущности единица измерения.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Measure {
    private Integer id;
    private String shortName;
    private String fullName;

    public Measure() {
    }

    public String getFullName() {
        return fullName;
    }

    public Measure setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Measure setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public Measure setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    @Override
    public String toString() {
        return "\nMeasure{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measure measure = (Measure) o;

        if (!id.equals(measure.id)) return false;
        if (!shortName.equals(measure.shortName)) return false;
        return fullName.equals(measure.fullName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((shortName == null) ? 0 : shortName.hashCode());
        result = 31 * result + ((fullName == null) ? 0 : fullName.hashCode());
        return result;
    }
}
