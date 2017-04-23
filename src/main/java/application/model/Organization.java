package application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *  Класс "Organization" описывает содержание и поведение сущности организация.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Organization {
    private Integer id;
    private String name;
    private Integer inn;
    private String legalAddress;

    public Organization() {
    }

    public Integer getId() {
        return id;
    }

    public Organization setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Organization setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getInn() {
        return inn;
    }

    public Organization setInn(Integer inn) {
        this.inn = inn;
        return this;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public Organization setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
        return this;
    }

    @Override
    public String toString() {
        return "\nOrganization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inn=" + inn +
                ", legalAddress='" + legalAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!inn.equals(that.inn)) return false;
        return legalAddress.equals(that.legalAddress);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((inn == null) ? 0 : inn.hashCode());
        result = 31 * result + ((legalAddress == null) ? 0 : legalAddress.hashCode());
        return result;
    }
}
