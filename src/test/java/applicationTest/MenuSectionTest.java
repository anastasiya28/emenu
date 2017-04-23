package applicationTest;

import application.dao.interfaces.MenuDAO;
import application.dao.interfaces.MenuSectionDAO;
import application.model.MenuSection;
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
public class MenuSectionTest {
    private static final Logger logger = Logger.getLogger(MenuSectionTest.class);

    private MenuSection menuSectionTestObj1;
    private MenuSection menuSectionTestObj2;
    private MenuSection menuSectionTestObj3;
    private MenuSection menuSectionTestObj4;
    private MenuSection menuSectionTestObj5;

    @Autowired
    private MenuDAO menuRepository;

    @Autowired
    private MenuSectionDAO menuSectionRepository;

    @Rule
    public TestName testName = new TestName();

    @PostConstruct
    private void init() {
        menuSectionTestObj1 = new MenuSection().setName("Тест: Десерты").setMenu(menuRepository.getById(1));
        menuSectionTestObj2 = new MenuSection().setName("Тест: Мороженое").setMenu(menuRepository.getById(1));
        menuSectionTestObj3 = new MenuSection().setName("Тест: Основные блюда").setMenu(menuRepository.getById(3));
        menuSectionTestObj4 = new MenuSection().setName("Тест: Горячие закуски").setMenu(menuRepository.getById(3));
        menuSectionTestObj5 = new MenuSection().setName("Тест: Суши и роллы").setMenu(menuRepository.getById(3));
    }

    @Before
    public void beforeTest() {
        logger.info("beforeTest------------------------------------");
        logger.info("Starting test: " + testName.getMethodName());
        logger.info("The database currently contains " + menuSectionRepository.getAll().size() + " records.");
        logger.info("------------------------------------");
    }

    @After
    public void afterTest() {
        logger.info("------------------------------------");
        logger.info("The database currently contains " + menuSectionRepository.getAll().size() + " records.");
        logger.info("afterTest------------------------------------\n\n");
    }

    /**
     * Определяем количество записей в таблице "menuSection" и полученное значение сравниваем с ожидаемым.<br/>
     * Ожидаемое значение равно 8. Они должны быть равны.
     */
    @Test
    public void menuSectionGetAllTest() {
        assertThat(menuSectionRepository.getAll().size(), is(8), "menuSectionRepository.getAll().size()");
    }

    /**
     * Определяем количество записей в таблице "menuSection", сохраняем это значение в переменной. <br/>
     * Добавляем в базу данных два тестовых объекта. Проверяем, что эти объекты не null. <br/>
     * Далее снова определяем количество записей в таблице "menuSection" и вычисляем разницу между текущим значение и
     * предыдущим.<br/>
     * Разница должна быть равна 2,так как в базу данных должны были добавиться два тестовых объекта.<br/>
     * После тестовые объекты из базы данных удаляем и проверяем, что они null.
     */
    @Test
    public void menuSectionAddAndRemoveTest() {
        int sizeBeforeAddingObjects = menuSectionRepository.getAll().size();

        Integer menuSectionTestObj1Id = menuSectionRepository.add(menuSectionTestObj1);
        Integer menuSectionTestObj2Id = menuSectionRepository.add(menuSectionTestObj2);
        menuSectionTestObj1.setId(menuSectionTestObj1Id);
        menuSectionTestObj2.setId(menuSectionTestObj2Id);
        assertNotNull(menuSectionRepository.getById(menuSectionTestObj1Id), "menuSectionRepository.getById(menuSectionTestObj1Id)");
        assertNotNull(menuSectionRepository.getById(menuSectionTestObj2Id), "menuSectionRepository.getById(menuSectionTestObj2Id)\n");

        int sizeAfterAddingObjects = menuSectionRepository.getAll().size();
        int deviationSize = sizeAfterAddingObjects - sizeBeforeAddingObjects;
        assertThat(deviationSize, is(2), "deviationSize, is(2)\n");

        menuSectionRepository.remove(menuSectionTestObj1Id);
        menuSectionRepository.remove(menuSectionTestObj2Id);
        assertNull(menuSectionRepository.getById(menuSectionTestObj1Id), "menuSectionRepository.getById(menuSectionTestObj1Id)");
        assertNull(menuSectionRepository.getById(menuSectionTestObj2Id), "menuSectionRepository.getById(menuSectionTestObj2Id)");
    }

    /**
     * Добавляем в базу данных 3 тестовых объекта. Запоминаем их id. <br/>
     * Далее id 2 объектов передаем в качестве входных параметров в метод: public MenuSection getById(Integer id){...}. <br/>
     * Получаем из базы данных 2 объекта и сравниваем их с тестовыми объектами, которые имеют соответствующий id.<br/>
     * * Сравнение объектов проводится в разрезе полей.
     */
    @Test
    public void menuSectionGetByIdTest() {
        Integer menuSectionTestObj3Id = menuSectionRepository.add(menuSectionTestObj3);
        Integer menuSectionTestObj4Id = menuSectionRepository.add(menuSectionTestObj4);
        Integer menuSectionTestObj5Id = menuSectionRepository.add(menuSectionTestObj5);
        menuSectionTestObj3.setId(menuSectionTestObj3Id);
        menuSectionTestObj4.setId(menuSectionTestObj4Id);
        menuSectionTestObj5.setId(menuSectionTestObj5Id);

        MenuSection menuSectionFromDB3 = menuSectionRepository.getById(menuSectionTestObj3Id);
        MenuSection menuSectionFromDB4 = menuSectionRepository.getById(menuSectionTestObj4Id);
        assertEqualsMenuSection(menuSectionTestObj3, menuSectionFromDB3);
        assertEqualsMenuSection(menuSectionTestObj4, menuSectionFromDB4);

        menuSectionRepository.remove(menuSectionTestObj3Id);
        menuSectionRepository.remove(menuSectionTestObj4Id);
        menuSectionRepository.remove(menuSectionTestObj5Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "menuSection". Критерий отбора - значение поля "name".<br/>
     * С помощью метода: List<MenuSection> getByName(String name){..} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие. Сравнение объектов проводится в разрезе полей.
     */
    @Test
    public void menuSectionGetByNameTest() {
        Integer menuSectionTestObj3Id = menuSectionRepository.add(menuSectionTestObj3);
        Integer menuSectionTestObj4Id = menuSectionRepository.add(menuSectionTestObj4);
        menuSectionTestObj3.setId(menuSectionTestObj3Id);
        menuSectionTestObj4.setId(menuSectionTestObj4Id);

        List<MenuSection> expectedMenuSections = new ArrayList<>();
        expectedMenuSections.add(menuSectionRepository.getById(5));
        expectedMenuSections.add(menuSectionRepository.getById(menuSectionTestObj3Id));

        List<MenuSection> actualMenuSections = menuSectionRepository.getByName("Основные блюда");

        assertEquals(expectedMenuSections.size(), actualMenuSections.size(), "expectedMenuSectionList.size(), " +
                "realMenuSectionList.size(); selectionParameter name: \"Основные блюда\";\n");

        assertEqualsMenuSection(expectedMenuSections.get(0), actualMenuSections.get(0));
        assertEqualsMenuSection(expectedMenuSections.get(1), actualMenuSections.get(1));
        menuSectionRepository.remove(menuSectionTestObj3Id);
        menuSectionRepository.remove(menuSectionTestObj4Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "menuSection". Критерий отбора - значение поля "menu".<br/>
     * С помощью метода: List<MenuSection> getByMenu(Menu menu){...} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие.
     */
    @Test
    public void menuSectionGetByMenuTest() {
        Integer menuSectionTestObj4Id = menuSectionRepository.add(menuSectionTestObj4);
        Integer menuSectionTestObj5Id = menuSectionRepository.add(menuSectionTestObj5);
        menuSectionTestObj4.setId(menuSectionTestObj4Id);
        menuSectionTestObj5.setId(menuSectionTestObj5Id);

        List<MenuSection> expectedMenuSections = new ArrayList<>();
        expectedMenuSections.add(menuSectionRepository.getById(menuSectionTestObj4Id));
        expectedMenuSections.add(menuSectionRepository.getById(menuSectionTestObj5Id));

        List<MenuSection> actualMenuSections = menuSectionRepository.getByMenu(menuRepository.getById(3));

        TestUtils.assertEquals(expectedMenuSections.size(), actualMenuSections.size(), "expectedMenuSectionList, " +
                "realMenuSectionList; selectionParameter menu: \"Основное меню ресторана Extra lounge (id = 3)\";");

        assertEqualsMenuSection(expectedMenuSections.get(0), actualMenuSections.get(0));
        assertEqualsMenuSection(expectedMenuSections.get(1), actualMenuSections.get(1));
        menuSectionRepository.remove(menuSectionTestObj4Id);
        menuSectionRepository.remove(menuSectionTestObj5Id);
    }

    /**
     * Добавляем в базу данных тестовый объект. Запоминаем значения всех его полей.<br/>
     * Далее в этом объекте изменияем значение поле "name" и снова запоминаем значения всех полей этого объекта.<br/>
     * Сравниваем 2 этих объекта на соответствие по полям. Значения полей этих объектов, кроме того, которое изменено,
     * должны совпадать.
     */
    @Test
    public void menuSectionUpdateTest() {
        Integer menuItemTestObj2Id = menuSectionRepository.add(menuSectionTestObj2);
        menuSectionTestObj2.setId(menuItemTestObj2Id);

        logger.info("Saving the values of all fields of this object before updating.\n");
        Integer idBeforeUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getId();
        String nameBeforeUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getName();
        Integer menuIdBeforeUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getMenu().getId();
        String menuNameBeforeUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getMenu().getName();

        String updatedName = "Тест: Десерты";
        menuSectionRepository.update(menuSectionRepository.getById(menuItemTestObj2Id).setName(updatedName));
        logger.info("In this object changed the value of the field \"name\".\n.");

        logger.info("Saving the values of all fields of this object after updating.\n");
        Integer idAfterUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getId();
        String nameAfterUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getName();
        Integer menuIdAfterUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getMenu().getId();
        String menuNameAfterUpdate = menuSectionRepository.getById(menuItemTestObj2Id).getMenu().getName();

        assertEquals(idBeforeUpdate, idAfterUpdate, "idBeforeUpdate, idAfterUpdate");
        assertNotEquals(nameBeforeUpdate, nameAfterUpdate, "nameBeforeUpdate, nameAfterUpdate");
        assertEquals(menuIdBeforeUpdate, menuIdAfterUpdate, "menuIdBeforeUpdate, menuIdAfterUpdate");
        assertEquals(menuNameBeforeUpdate, menuNameAfterUpdate, "menuNameBeforeUpdate, menuNameAfterUpdate");
        assertEquals(updatedName, nameAfterUpdate, "updatedName, nameAfterUpdate");

        menuSectionRepository.remove(menuItemTestObj2Id);
    }

    private void assertEqualsMenuSection(MenuSection expectedMenuSection, MenuSection actualMenuSection) {
        Integer expectedMenuSectionId = expectedMenuSection.getId();
        Integer actualMenuSectionId = actualMenuSection.getId();
        logger.info("expectedMenuSection.id = " + expectedMenuSectionId + "," + "actualMenuSection.id = " + actualMenuSectionId);
        assertEquals(expectedMenuSection.getId(), actualMenuSection.getId(), "expectedMenuSection.id, " +
                "actualMenuSection.id");
        assertEquals(expectedMenuSection.getName(), actualMenuSection.getName(), "expectedMenuSection.name, " +
                "actualMenuSection.name");
        assertEquals(expectedMenuSection.getMenu().getId(), actualMenuSection.getMenu().getId(),
                "expectedMenuSection.menuId, actualMenuSection.menuId");
        assertEquals(expectedMenuSection.getMenu().getName(), actualMenuSection.getMenu().getName(),
                "expectedMenuSection.menuName, actualMenuSection.menuName");
    }
}
