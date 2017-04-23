package application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс "Menu" описывает содержание и поведение сущности меню.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu {
    private Integer id;
    private String name;
    private Organization organization;
    private Map<Integer, MenuSection> menuSections = new HashMap();

    public Menu() {
    }

    public Integer getId() {
        return id;
    }

    public Menu setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Menu setOrganization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Map<Integer, MenuSection> getMenuSections() {
        return menuSections;
    }

    public void setMenuSections(Map<Integer, MenuSection> menuSections) {
        this.menuSections = menuSections;
    }

    public Menu addMenuSection(Integer id, MenuSection menuSection) {
        this.menuSections.put(id, menuSection);
        return this;
    }

    public Menu removeMenuSection(Integer id) {
        this.menuSections.remove(id);
        return this;
    }

    @Override
    public String toString() {
        return "\nMenu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", organization=" + organization +
                ", menuSections=" + menuSections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (!id.equals(menu.id)) return false;
        if (!name.equals(menu.name)) return false;
        if (!organization.equals(menu.organization)) return false;
        return menuSections.equals(menu.menuSections);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((organization == null) ? 0 : organization.hashCode());
        result = 31 * result + ((menuSections == null) ? 0 : menuSections.hashCode());
        return result;
    }
}
