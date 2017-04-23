package application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс "MenuSection" описывает содержание и поведение сущности раздел меню.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuSection {
    private Integer id;
    private String name;
    private Menu menu;
    private Map<Integer, MenuItem> menuItems = new HashMap<>();

    public MenuSection() {
    }

    public Integer getId() {
        return id;
    }

    public MenuSection setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuSection setName(String name) {
        this.name = name;
        return this;
    }

    public Menu getMenu() {
        return menu;
    }

    public MenuSection setMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public Map<Integer, MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Map<Integer, MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public MenuSection addMenuItem(Integer id, MenuItem menuItem){
        this.menuItems.put(id, menuItem);
        return this;
    }

    public MenuSection removeMenuItem(Integer id){
        this.menuItems.remove(id);
        return this;
    }

    @Override
    public String toString() {
        return "\nMenuSection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                ", menuItems=" + menuItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuSection that = (MenuSection) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!menu.equals(that.menu)) return false;
        return menuItems.equals(that.menuItems);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((menu == null) ? 0 : menu.hashCode());
        result = 31 * result + ((menuItems == null) ? 0 : menuItems.hashCode());
        return result;
    }
}
