package applicationTest;

import application.dao.interfaces.OrganizationDAO;
import application.model.Organization;
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

import java.util.ArrayList;
import java.util.List;

import static applicationTest.TestUtils.*;
import static org.hamcrest.core.Is.is;

@ContextConfiguration(classes = ConfigTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrganizationTest {
    private static final Logger logger = Logger.getLogger(OrganizationTest.class);

    private final Organization orgTestObj1 = new Organization().setName("Тест: Evoo").setInn(1655059173).
            setLegalAddress("Тест: г.Казань, ул.Николая Ершова, д.1, к.А, 420061");
    private final Organization orgTestObj2 = new Organization().setName("Тест: Родео Стейк Хаус").setInn(1655237849).
            setLegalAddress("Тест: г. Казань, ул. Чистопольская д.40, 420110");
    private final Organization orgTestObj3 = new Organization().setName("Тест: Ромэйн").setInn(1655237849).
            setLegalAddress("Тест: г. Казань, ул. Чистопольская д.40, 420110");
    private final Organization orgTestObj4 = new Organization().setName("Тест: Европа").setInn(1655271998).
            setLegalAddress("Тест: г. Казань, ул. Петербургская д.14, 420107");
    private final Organization orgTestObj5 = new Organization().setName("Тест: Бурбон").setInn(1653237950).
            setLegalAddress("Тест: г. Казань, ул. Кремлевская д.10, 420110");

    @Autowired
    private OrganizationDAO organizationRepository;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void beforeTest() {
        logger.info("beforeTest------------------------------------");
        logger.info("Starting test: " + testName.getMethodName());
        logger.info("The database currently contains " + organizationRepository.getAll().size() + " records.");
        logger.info("------------------------------------");
    }

    @After
    public void afterTest() {
        logger.info("------------------------------------");
        logger.info("The database currently contains " + organizationRepository.getAll().size() + " records.");
        logger.info("afterTest------------------------------------\n\n");
    }

    /**
     * Определяем количество записей в таблице "organization" и полученное значение сравниваем с ожидаемым.<br/>
     * Ожидаемое значение равно 3. Они должны быть равны.
     */
    @Test
    public void organizationGetAllTest() {
        assertThat(organizationRepository.getAll().size(), is(3), "organizationRepository.getAll().size()");
    }

    /**
     * Определяем количество записей в таблице "organization", сохраняем это значение в переменной. <br/>
     * Добавляем в базу данных два тестовых объекта. Проверяем, что эти объекты не null. <br/>
     * Далее снова определяем количество записей в таблице "organization" и вычисляем разницу между текущим значение и
     * предыдущим.<br/>
     * Разница должна быть равна 2,так как в базу данных должны были добавиться два тестовых объекта.<br/>
     * После тестовые объекты из базы данных удаляем и проверяем, что они null.
     */
    @Test
    public void organizationAddAndRemoveTest() {
        int sizeBeforeAddingObjects = organizationRepository.getAll().size();

        Integer orgTestObj1Id = organizationRepository.add(orgTestObj1);
        Integer orgTestObj2Id = organizationRepository.add(orgTestObj2);
        orgTestObj1.setId(orgTestObj1Id);
        orgTestObj2.setId(orgTestObj2Id);

        assertNotNull(organizationRepository.getById(orgTestObj1Id), "organizationRepository.getById(orgTestObj1Id)");
        assertNotNull(organizationRepository.getById(orgTestObj2Id), "organizationRepository.getById(orgTestObj2Id\n");

        int sizeAfterAddingObjects = organizationRepository.getAll().size();
        int deviationSize = sizeAfterAddingObjects - sizeBeforeAddingObjects;
        assertThat(deviationSize, is(2), "deviationSize is(2)\n");

        organizationRepository.remove(orgTestObj1Id);
        organizationRepository.remove(orgTestObj2Id);
        assertNull(organizationRepository.getById(orgTestObj1Id), "organizationRepository.getById(orgTestObj1Id)");
        assertNull(organizationRepository.getById(orgTestObj2Id), "organizationRepository.getById(orgTestObj2Id)");
    }

    /**
     * Добавляем в базу данных 3 тестовых объекта. Запоминаем их id. <br/>
     * Далее id 2 объектов передаем в качестве входных параметров в метод: public Organization getById(Integer id){...}. <br/>
     * Получаем из базы данных 2 объекта и сравниваем их с тестовыми объектами, которые имеют соответствующий id.
     */
    @Test
    public void organizationGetByIdTest() {
        Integer orgTestObj3Id = organizationRepository.add(orgTestObj3);
        Integer orgTestObj4Id = organizationRepository.add(orgTestObj4);
        Integer orgTestObj5Id = organizationRepository.add(orgTestObj5);
        orgTestObj3.setId(orgTestObj3Id);
        orgTestObj4.setId(orgTestObj4Id);
        orgTestObj5.setId(orgTestObj5Id);

        Organization orgFromDB3 = organizationRepository.getById(orgTestObj3Id);
        Organization orgFromDB4 = organizationRepository.getById(orgTestObj4Id);
        assertEquals(orgTestObj3, orgFromDB3, "orgTestObj3, orgFromDB3");
        assertEquals(orgTestObj4, orgFromDB4, "orgTestObj4, orgFromDB4");

        organizationRepository.remove(orgTestObj3Id);
        organizationRepository.remove(orgTestObj4Id);
        organizationRepository.remove(orgTestObj5Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "organization". Критерий отбора - значение поля "тame".<br/>
     * С помощью метода: List<Organization> getByName(String name){...} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие.
     */
    @Test
    public void organizationGetByNameTest() {
        Integer orgTestObj1Id = organizationRepository.add(orgTestObj1);
        Integer orgTestObj2Id = organizationRepository.add(orgTestObj2);
        Integer orgTestObj3Id = organizationRepository.add(orgTestObj3);
        orgTestObj1.setId(orgTestObj1Id);
        orgTestObj2.setId(orgTestObj2Id);
        orgTestObj3.setId(orgTestObj3Id);

        List<Organization> expectedOrgs1 = new ArrayList<>();
        expectedOrgs1.add(orgTestObj2);
        expectedOrgs1.add(orgTestObj3);

        List<Organization> realOrgs1 = organizationRepository.getByName("Ро");

        assertArrayEquals(expectedOrgs1.toArray(), realOrgs1.toArray(), "expectedOrgs, realOrgs; " +
                "selectionParameter name: \"Ро\";\n");

        List<Organization> expectedOrgs2 = new ArrayList<>();
        expectedOrgs2.add(orgTestObj1);

        List<Organization> realOrgs2 = organizationRepository.getByName("evoo");

        assertArrayEquals(expectedOrgs2.toArray(), realOrgs2.toArray(), "expectedOrgs, realOrgs; " +
                "selectionParameter name: \"evoo\";\n");

        organizationRepository.remove(orgTestObj1Id);
        organizationRepository.remove(orgTestObj2Id);
        organizationRepository.remove(orgTestObj3Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "organization". Критерий отбора - значение поля "inn".<br/>
     * С помощью метода: List<Organization> getByINN(String inn){...} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие.
     */
    @Test
    public void organizationGetByINNTest() {
        Integer orgTestObj1Id = organizationRepository.add(orgTestObj1);
        Integer orgTestObj2Id = organizationRepository.add(orgTestObj2);
        orgTestObj1.setId(orgTestObj1Id);
        orgTestObj2.setId(orgTestObj2Id);

        List<Organization> expectedOrgs1 = new ArrayList<>();
        expectedOrgs1.add(organizationRepository.getById(2));
        expectedOrgs1.add(orgTestObj1);

        List<Organization> realOrgs1 = organizationRepository.getByINN("1655059173");

        TestUtils.assertArrayEquals(expectedOrgs1.toArray(), realOrgs1.toArray(), "expectedOrgs, realOrgs; " +
                "selectionParameter inn: \"1655059173\";\n");

        List<Organization> expectedOrgs2 = new ArrayList<>();
        expectedOrgs2.add(orgTestObj2);

        List<Organization> realOrgs2 = organizationRepository.getByINN("5237");

        TestUtils.assertArrayEquals(expectedOrgs2.toArray(), realOrgs2.toArray(), "expectedOrgs, realOrgs; " +
                "selectionParameter inn: \"5237\";\n");

        organizationRepository.remove(orgTestObj1Id);
        organizationRepository.remove(orgTestObj2Id);
    }

    /**
     * Добавляем в базу данных тестовый объект. Запоминаем значения всех его полей.<br/>
     * Далее в этом объекте изменияем значение поле "name" и снова запоминаем значения всех полей этого объекта.<br/>
     * Сравниваем 2 этих объекта на соответствие по полям. Значения полей этих объектов, кроме того, которое изменено,
     * должны совпадать.
     */
    @Test
    public void organizationUpdateTest() {
        Integer orgTestObj1Id = organizationRepository.add(orgTestObj1);
        orgTestObj1.setId(orgTestObj1Id);

        logger.info("Saving the values of all fields of this object before updating.\n");
        Integer idBeforeUpdate = organizationRepository.getById(orgTestObj1Id).getId();
        String nameBeforeUpdate = organizationRepository.getById(orgTestObj1Id).getName();
        Integer innBeforeUpdate = organizationRepository.getById(orgTestObj1Id).getInn();
        String legallAddressBeforeUpdate = organizationRepository.getById(orgTestObj1Id).getLegalAddress();

        String updatedName = "Тест: Extra lounge";
        organizationRepository.update(organizationRepository.getById(orgTestObj1Id).setName(updatedName));
        logger.info("In this object changed the value of the field \"name\".\n.");

        logger.info("Saving the values of all fields of this object after updating.\n");
        Integer idAfterUpdate = organizationRepository.getById(orgTestObj1Id).getId();
        String nameAfterUpdate = organizationRepository.getById(orgTestObj1Id).getName();
        Integer innAfterUpdate = organizationRepository.getById(orgTestObj1Id).getInn();
        String legallAddressAfterUpdate = organizationRepository.getById(orgTestObj1Id).getLegalAddress();

        assertEquals(idBeforeUpdate, idAfterUpdate, "idBeforeUpdate, idAfterUpdate");
        assertNotEquals(nameBeforeUpdate, nameAfterUpdate, "nameBeforeUpdate, nameAfterUpdate");
        assertEquals(innBeforeUpdate, innAfterUpdate, "innBeforeUpdate, innAfterUpdate");
        assertEquals(legallAddressBeforeUpdate, legallAddressAfterUpdate, "legallAddressBeforeUpdate, " +
                "legallAddressAfterUpdate");
        assertEquals(updatedName, nameAfterUpdate, "updatedName, nameAfterUpdate");
        organizationRepository.remove(orgTestObj1Id);
    }
}
