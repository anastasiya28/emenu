package applicationTest;

import application.dao.interfaces.MeasureDAO;
import application.dao.interfaces.MenuItemDAO;
import application.dao.interfaces.MenuSectionDAO;
import application.model.Measure;
import application.model.MenuItem;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static applicationTest.TestUtils.*;
import static org.hamcrest.core.Is.is;

@ContextConfiguration(classes = ConfigTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MenuItemTest {
    private static final Logger logger = Logger.getLogger(MenuItemTest.class);

    private MenuItem menuItemTestObj1;
    private MenuItem menuItemTestObj2;
    private MenuItem menuItemTestObj3;

    @Autowired
    private MenuSectionDAO menuSectionRepository;

    @Autowired
    private MenuItemDAO menuItemRepository;

    @Autowired
    private MeasureDAO measureRepository;

    @Rule
    public TestName testName = new TestName();

    @PostConstruct
    private void init() {
        Measure measure = measureRepository.getById(1);

        menuItemTestObj1 = new MenuItem()
                .setName("Ассорти  мясное")
                .setPrice(490.00)
                .setPortionVolume(200)
                .setMeasure(measureRepository.getById(3))
                .setMenuSection(menuSectionRepository.getById(1));
        menuItemTestObj2 = new MenuItem()
                .setName("Салат с ананасами и тигровыми креветками в корзинке из сыра \"Пармезан\"")
                .setPrice(370.00)
                .setPortionVolume(120)
                .setMeasure(measureRepository.getById(3))
                .setMenuSection(menuSectionRepository.getById(2));
        menuItemTestObj3 = new MenuItem()
                .setName("Минестроне")
                .setPrice(290.00)
                .setPortionVolume(350)
                .setMeasure(measureRepository.getById(3))
                .setMenuSection(menuSectionRepository.getById(4));
    }

    @Before
    public void beforeTest() {
        logger.info("beforeTest------------------------------------");
        logger.info("Starting test: " + testName.getMethodName());
        logger.info("The database currently contains " + menuItemRepository.getAll().size() + " records.");
        logger.info("------------------------------------");
    }

    @After
    public void afterTest() {
        logger.info("------------------------------------");
        logger.info("The database currently contains " + menuItemRepository.getAll().size() + " records.");
        logger.info("afterTest------------------------------------\n\n");
    }

    /**
     * Определяем количество записей в таблице "menuItem" и полученное значение сравниваем с ожидаемым.<br/>
     * Ожидаемое значение равно 13. Они должны быть равны.
     */
    @Test
    public void menuItemGetAllTest() {
        assertThat(menuItemRepository.getAll().size(), is(13), "menuItemRepository.getAll().size(), is(13)");
    }

    /**
     * Определяем количество записей в таблице "menuItem", сохраняем это значение в переменной. <br/>
     * Добавляем в базу данных два тестовых объекта. Проверяем, что эти объекты не null. <br/>
     * Далее снова определяем количество записей в таблице "menuItem" и вычисляем разницу между текущим значение и
     * предыдущим.<br/>
     * Разница должна быть равна 2,так как в базу данных должны были добавиться два тестовых объекта. <br/>
     * После тестовые объекты из базы данных удаляем и проверяем, что они null.
     */
    @Test
    public void menuItemAddAndRemoveTest() {
        int sizeBeforeAddingObjects = menuItemRepository.getAll().size();

        Integer menuItemTestObj1Id = menuItemRepository.add(menuItemTestObj1);
        Integer menuItemTestObj2Id = menuItemRepository.add(menuItemTestObj2);
        menuItemTestObj1.setId(menuItemTestObj1Id);
        menuItemTestObj2.setId(menuItemTestObj2Id);
        assertNotNull(menuItemRepository.getById(menuItemTestObj1Id), "menuItemRepository.getById(menuItemTestObj1Id)");
        assertNotNull(menuItemRepository.getById(menuItemTestObj2Id), "menuItemRepository.getById(menuItemTestObj2Id)\n");

        int sizeAfterAddingObjects = menuItemRepository.getAll().size();
        int deviationSize = sizeAfterAddingObjects - sizeBeforeAddingObjects;
        assertThat(deviationSize, is(2), "deviationSize, is(2)\n");

        menuItemRepository.remove(menuItemTestObj1Id);
        menuItemRepository.remove(menuItemTestObj2Id);
        assertNull(menuItemRepository.getById(menuItemTestObj1Id), "menuItemRepository.getById(menuItemTestObj1Id");
        assertNull(menuItemRepository.getById(menuItemTestObj2Id), "menuItemRepository.getById(menuItemTestObj2Id)");
    }

    /**
     * Добавляем в базу данных 2 тестовых объекта. Запоминаем их id. <br/>
     * Далее id 2 объектов передаем в качестве входных параметров в метод: public MenuItem getById(Integer id){...}. <br/>
     * Получаем из базы данных 2 объекта и сравниваем их с тестовыми объектами, которые имеют соответствующий id.<br/>
     * Сравнение объектов проводится в разрезе полей.
     */
    @Test
    public void menuItemGetByIdTest() {
        Integer menuItemTestObj1Id = menuItemRepository.add(menuItemTestObj1);
        Integer menuItemTestObj3Id = menuItemRepository.add(menuItemTestObj3);
        menuItemTestObj1.setId(menuItemTestObj1Id);
        menuItemTestObj3.setId(menuItemTestObj3Id);

        MenuItem menuItemFromDB1 = menuItemRepository.getById(menuItemTestObj1Id);
        MenuItem menuItemFromDB3 = menuItemRepository.getById(menuItemTestObj3Id);
        assertEqualsMenuItem(menuItemTestObj1, menuItemFromDB1);
        assertEqualsMenuItem(menuItemTestObj3, menuItemFromDB3);

        menuItemRepository.remove(menuItemTestObj1Id);
        menuItemRepository.remove(menuItemTestObj3Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "menuItem". Критерий отбора - значение поля "name".<br/>
     * С помощью метода: List<MenuItem> getByName(String name){..} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие. Сравнение объектов проводится в разрезе полей.
     */
    @Test
    public void menuItemGetByNameTest() {
        List<MenuItem> expectedMenuItems = new ArrayList<>();
        expectedMenuItems.add(menuItemRepository.getById(2));
        expectedMenuItems.add(menuItemRepository.getById(3));

        List<MenuItem> actualMenuItems = menuItemRepository.getByName("Салат");

        assertEquals(expectedMenuItems.size(), actualMenuItems.size(), "expectedMenuItems.size(), " +
                "actualMenuItems.size()), selectionParameter shortName: \"Салат\"");

        assertEqualsMenuItem(expectedMenuItems.get(0), actualMenuItems.get(0));
        assertEqualsMenuItem(expectedMenuItems.get(1), actualMenuItems.get(1));
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "menuItem". Критерий отбора - значение поля "menuSection".<br/>
     * Метод: public Menu getByOrganization(Organization organization){...}, возвращает массив актуальных значений. <br/>
     * Далее сравниваем эти два массива на соответствие.
     */
    @Test
    public void menuItemGetByMenuSectionTest() {
        List<MenuItem> expectedMenuItems = new ArrayList<>();
        expectedMenuItems.add(menuItemRepository.getById(6));
        expectedMenuItems.add(menuItemRepository.getById(7));

        List<MenuItem> actualMenuItems = menuItemRepository.getByMenuSection(menuSectionRepository.getById(4));

        assertEquals(expectedMenuItems.size(), actualMenuItems.size(), "expectedMenuItems.size(), " +
                "actualMenuItems.size(); selectionParameter sectionMenu: \"Супы (id = 4)\";");

        assertEqualsMenuItem(expectedMenuItems.get(0), actualMenuItems.get(0));
        assertEqualsMenuItem(expectedMenuItems.get(1), actualMenuItems.get(1));
    }

    /**
     * Добавляем в базу данных тестовый объект. Запоминаем значения всех его полей.<br/>
     * Далее в этом объекте изменияем значение поле "price" и снова запоминаем значения всех полей этого объекта.<br/>
     * Сравниваем 2 этих объекта на соответствие по полям. Значения полей этих объектов, кроме того, которое изменено,
     * должны совпадать.
     */
    @Test
    public void menuItemUpdateTest() {
        Integer menuItemTestObj1Id = menuItemRepository.add(menuItemTestObj1);
        menuItemTestObj1.setId(menuItemTestObj1Id);

        logger.info("Saving the values of all fields of this object before updating.\n");
        Integer idBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getId();
        String nameBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getName();
        Double priceBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getPrice();
        Integer portionVolumeBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getPortionVolume();
        Integer measureIdBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMeasure().getId();
        String measureNameBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMeasure().getShortName();
        Integer menuSectionIdBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMenuSection().getId();
        String menuSectionNameBeforeUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMenuSection().getName();

        Double updatedPrice = 550.00;
        menuItemRepository.update(menuItemRepository.getById(menuItemTestObj1Id).setPrice(updatedPrice));
        logger.info("In this object changed the value of the field \"price\".\n.");

        logger.info("Saving the values of all fields of this object after updating.\n");
        Integer idAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getId();
        String nameAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getName();
        Double priceAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getPrice();
        Integer portionVolumeAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getPortionVolume();
        Integer measureIdAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMeasure().getId();
        String measureNameAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMeasure().getShortName();
        Integer menuSectionIdAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMenuSection().getId();
        String menuSectionNameAfterUpdate = menuItemRepository.getById(menuItemTestObj1Id).getMenuSection().getName();

        assertEquals(idBeforeUpdate, idAfterUpdate, "idBeforeUpdate, idAfterUpdate");
        assertEquals(nameBeforeUpdate, nameAfterUpdate, "nameBeforeUpdate, nameAfterUpdate");
        assertNotEquals(priceBeforeUpdate, priceAfterUpdate, "priceBeforeUpdate, priceAfterUpdate");
        assertEquals(portionVolumeBeforeUpdate, portionVolumeAfterUpdate, "portionVolumeBeforeUpdate, " +
                "portionVolumeAfterUpdate");
        assertEquals(measureIdBeforeUpdate, measureIdAfterUpdate, "measureIdBeforeUpdate, measureIdAfterUpdate");
        assertEquals(measureNameBeforeUpdate, measureNameAfterUpdate, "measureNameBeforeUpdate, measureNameAfterUpdate");
        assertEquals(menuSectionIdBeforeUpdate, menuSectionIdAfterUpdate, "menuSectionIdBeforeUpdate, " +
                "menuSectionIdAfterUpdate");
        assertEquals(menuSectionNameBeforeUpdate, menuSectionNameAfterUpdate, "menuSectionNameBeforeUpdate, " +
                "menuSectionNameAfterUpdate");
        assertEquals(updatedPrice, priceAfterUpdate, "updatedPrice, priceAfterUpdate");

        menuItemRepository.remove(menuItemTestObj1Id);
    }

    private void assertEqualsMenuItem(MenuItem expectedMenuItem, MenuItem actualMenuItem) {
        Integer expectedMenuItemId = expectedMenuItem.getId();
        Integer actualMenuItemId = actualMenuItem.getId();
        logger.info("expectedMenuItem.id = " + expectedMenuItemId + "," + "actualMenuItem.id = " + actualMenuItemId);
        assertEquals(expectedMenuItem.getId(), actualMenuItem.getId(), "expectedMenuItem.id, actualMenuItem.id");
        assertEquals(expectedMenuItem.getName(), actualMenuItem.getName(), "expectedMenuItem.name, actualMenuItem.name");
        assertEquals(expectedMenuItem.getPrice(), actualMenuItem.getPrice(), "expectedMenuItem.price, actualMenuItem.price");
        assertEquals(expectedMenuItem.getMeasure().getId(), actualMenuItem.getMeasure().getId(),
                "expectedMenuItem.measureId, actualMenuItem.measureId");
        assertEquals(expectedMenuItem.getMeasure().getShortName(), actualMenuItem.getMeasure().getShortName(),
                "expectedMenuItem.measureShortName, actualMenuItem.measureShortName");
        assertEquals(expectedMenuItem.getMenuSection().getId(), actualMenuItem.getMenuSection().getId(),
                "expectedMenuItem.menuSectionId, actualMenuItem.menuSectionId");
        assertEquals(expectedMenuItem.getMenuSection().getName(), actualMenuItem.getMenuSection().getName(),
                "expectedMenuItem.menuSectionName, actualMenuItem.menuSectionName");
    }
}
