package applicationTest;

import application.dao.interfaces.MenuDAO;
import application.dao.interfaces.OrganizationDAO;
import application.model.Menu;
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
public class MenuTest {
    private static final Logger logger = Logger.getLogger(MenuTest.class);

    private Menu menuTestObj1;
    private Menu menuTestObj2;

    @Autowired
    private MenuDAO menuRepository;

    @Autowired
    private OrganizationDAO organizationRepository;

    @Rule
    public TestName testName = new TestName();

    @PostConstruct
    private void init() {
        menuTestObj1 = new Menu().setName("Тест: Детское меню").setOrganization(organizationRepository.getById(2));
        menuTestObj2 = new Menu().setName("Тест: Сезонное меню").setOrganization(organizationRepository.getById(2));
    }

    @Before
    public void beforeTest() {
        logger.info("beforeTest ------------------------------------");
        logger.info("Starting test: " + testName.getMethodName());
        logger.info("The database currently contains " + menuRepository.getAll().size() + " records.");
        logger.info("-----------------------------------------------");
    }

    @After
    public void afterTest() {
        logger.info("------------------------------------");
        logger.info("The database currently contains " + menuRepository.getAll().size() + " records.");
        logger.info("afterTest------------------------------------\n\n");
    }

    /**
     * Определяем количество записей в таблице "menu" и полученное значение сравниваем с ожидаемым.<br/>
     * Ожидаемое значение равно 4. Они должны быть равны.
     */
    @Test
    public void menuGetAllTest() {
        assertThat(menuRepository.getAll().size(), is(4), "menuRepository.getAll().size() is(4)");
    }

    /**
     * Определяем количество записей в таблице "menu", сохраняем это значение в переменной. <br/>
     * Добавляем в базу данных два тестовых объекта. Проверяем, что эти объекты не null. <br/>
     * Далее снова определяем количество записей в таблице "menu" и вычисляем разницу между текущим значение и
     * предыдущим.<br/>
     * Разница должна быть равна 2,так как в базу данных должны были добавиться два тестовых объекта.<br/>
     * После тестовые объекты из базы данных удаляем и проверяем, что они null.
     */
    @Test
    public void menuAddAndRemoveTest() {
        int sizeBeforeAddingObjects = menuRepository.getAll().size();

        Integer menuTestObj1Id = menuRepository.add(menuTestObj1);
        Integer menuTestObj2Id = menuRepository.add(menuTestObj2);
        menuTestObj1.setId(menuTestObj1Id);
        menuTestObj2.setId(menuTestObj2Id);
        assertNotNull(menuRepository.getById(menuTestObj1Id), "menuRepository.getById(menuTestObj1Id)");
        assertNotNull(menuRepository.getById(menuTestObj2Id), "menuRepository.getById(menuTestObj2Id)\n");

        int sizeAfterAddingObjects = menuRepository.getAll().size();
        int deviationSize = sizeAfterAddingObjects - sizeBeforeAddingObjects;
        assertThat(deviationSize, is(2), "deviationSize is(2)\n");

        menuRepository.remove(menuTestObj1Id);
        menuRepository.remove(menuTestObj2Id);
        assertNull(menuRepository.getById(menuTestObj1Id), "menuRepository.getById(menuTestObj1Id)");
        assertNull(menuRepository.getById(menuTestObj2Id), "menuRepository.getById(menuTestObj1Id)");
    }

    /**
     * Добавляем в базу данных 2 тестовых объекта. Запоминаем их id. <br/>
     * Далее id 2 объектов передаем в качестве входных параметров в метод: public Menu getById(Integer id){...}. <br/>
     * Получаем из базы данных 2 объекта и сравниваем их с тестовыми объектами, которые имеют соответствующий id. <br/>
     * Сравнение объектов проводится в разрезе полей.
     */
    @Test
    public void menuGetByIdTest() {
        Integer menuTestObj1Id = menuRepository.add(menuTestObj1);
        Integer menuTestObj2Id = menuRepository.add(menuTestObj2);
        menuTestObj1.setId(menuTestObj1Id);
        menuTestObj2.setId(menuTestObj2Id);

        Menu menuFromDB1 = menuRepository.getById(menuTestObj1Id);
        Menu menuFromDB2 = menuRepository.getById(menuTestObj2Id);
        assertEqualsMenu(menuTestObj1, menuFromDB1);
        assertEqualsMenu(menuTestObj2, menuFromDB2);

        menuRepository.remove(menuTestObj1Id);
        menuRepository.remove(menuTestObj2Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "menu". Критерий отбора - значение поля "organization".<br/>
     * Из таблицы "organization" по определенному id получаем организацию. Ссылку на эту организацию возвращаем в переменную <br/>
     * Передаем ссылку на эту организацию в метод: public Menu getByOrganization(Organization organization){...}, <br/>
     * который возвращает массив актуальных значений. <br/>
     * Далее сравниваем эти два массива на соответствие.
     */
    @Test
    public void menuGetByOrganizationTest() {
        List<Menu> expectedMenus = new ArrayList<>();
        expectedMenus.add(menuRepository.getById(1));
        expectedMenus.add(menuRepository.getById(2));

        List<Menu> actualMenus = menuRepository.getByOrganization(organizationRepository.getById(1));

        assertEquals(expectedMenus.size(), actualMenus.size(), "expectedMenus.size(), actualMenus.size()");

        assertEqualsMenu(expectedMenus.get(0),actualMenus.get(0));
        assertEqualsMenu(expectedMenus.get(1),actualMenus.get(1));
    }

    /**
     * Добавляем в базу данных тестовый объект. Запоминаем значения всех его полей.<br/>
     * Далее в этом объекте изменияем значение поле "name" и снова запоминаем значения всех полей этого объекта.<br/>
     * Сравниваем 2 этих объекта на соответствие по полям. Значения полей этих объектов, кроме того, которое изменено,
     * должны совпадать.
     */
    @Test
    public void menuUpdateTest() {
        Integer menuTestObj2Id = menuRepository.add(menuTestObj2);
        menuTestObj2.setId(menuTestObj2Id);

        logger.info("Saving the values of all fields of this object before updating.\n");
        Integer idBeforeUpdate = menuRepository.getById(menuTestObj2Id).getId();
        String nameBeforeUpdate = menuRepository.getById(menuTestObj2Id).getName();
        Integer orgIdBeforeUpdate = menuRepository.getById(menuTestObj2Id).getOrganization().getId();
        String orgNameBeforeUpdate = menuRepository.getById(menuTestObj2Id).getOrganization().getName();

        String updatedName = "Тест: Новинки меню ресторана Приют холостяка";
        menuRepository.update(menuRepository.getById(menuTestObj2Id).setName(updatedName));
        logger.info("In this object changed the value of the field \"name\".\n.");

        logger.info("Saving the values of all fields of this object after updating.\n");
        Integer idAfterUpdate = menuRepository.getById(menuTestObj2Id).getId();
        String nameAfterUpdate = menuRepository.getById(menuTestObj2Id).getName();
        Integer orgIdAfterUpdate = menuRepository.getById(menuTestObj2Id).getOrganization().getId();
        String orgNameAfterUpdate = menuRepository.getById(menuTestObj2Id).getOrganization().getName();

        assertEquals(idBeforeUpdate, idAfterUpdate, "idBeforeUpdate, idAfterUpdate");
        assertNotEquals(nameBeforeUpdate, nameAfterUpdate, "nameBeforeUpdate, nameAfterUpdate");
        assertEquals(orgIdBeforeUpdate, orgIdAfterUpdate, "orgIdBeforeUpdate, orgIdAfterUpdate");
        assertEquals(orgNameBeforeUpdate, orgNameAfterUpdate, "orgNameBeforeUpdate, orgNameAfterUpdate");
        assertEquals(updatedName, nameAfterUpdate, "updatedName, nameAfterUpdate");

        menuRepository.remove(menuTestObj2Id);
    }

    private void assertEqualsMenu(Menu expectedMenu, Menu actualMenu) {
        Integer expectedMenuId = expectedMenu.getId();
        Integer actualMenuId = actualMenu.getId();
        logger.info("expectedMenu.id = " + expectedMenuId + "," + "actualMenu.id = " + actualMenuId);
        assertEquals(expectedMenu.getId(), actualMenu.getId(), "expectedMenu.id, actualMenu.id");
        assertEquals(expectedMenu.getName(), actualMenu.getName(), "expectedMenu.name, actualMenu.name");
        assertEquals(expectedMenu.getOrganization().getId(), actualMenu.getOrganization().getId(),
                "expectedMenu.orgId, actualMenu.orgId");
        assertEquals(expectedMenu.getOrganization().getName(), actualMenu.getOrganization().getName(),
                "expectedMenu.orgName, actualMenu.orgName");
    }
}

