package applicationTest;

import application.dao.interfaces.MeasureDAO;
import application.model.Measure;
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
public class MeasureTest {
    private static final Logger logger = Logger.getLogger(MeasureTest.class);

    private final Measure measureTestObj1 = new Measure().setShortName("Тест: мл").setFullName("Тест: миллилитр");
    private final Measure measureTestObj2 = new Measure().setShortName("Тест: л").setFullName("Тест: литр");
    private final Measure measureTestObj3 = new Measure().setShortName("Тест: гр").setFullName("Тест: грамм");
    private final Measure measureTestObj4 = new Measure().setShortName("Тест: кг").setFullName("Тест: килограмм");

    @Autowired
    private MeasureDAO measureRepository;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void beforeTest() {
        logger.info("beforeTest------------------------------------");
        logger.info("Starting test: " + testName.getMethodName());
        logger.info("The database currently contains " + measureRepository.getAll().size() + " records.");
        logger.info("------------------------------------");
    }

    @After
    public void afterTest() {
        logger.info("------------------------------------");
        logger.info("The database currently contains " + measureRepository.getAll().size() + " records.");
        logger.info("afterTest------------------------------------\n\n");
    }

    /**
     * Определяем количество записей в таблице "measure" и полученное значение сравниваем с ожидаемым.<br/>
     * Ожидаемое значение равно 4. Они должны быть равны.
     */
    @Test
    public void measureGetAllTest() {
        assertThat(measureRepository.getAll().size(), is(4), "measureRepository.getAll().size(), is(4)");
    }

    /**
     * Определяем количество записей в таблице "measure", сохраняем это значение в переменной. <br/>
     * Добавляем в базу данных два тестовых объекта. Проверяем, что эти объекты не null. <br/>
     * Далее снова определяем количество записей в таблице "measure" и вычисляем разницу между текущим значение и
     * предыдущим.<br/>
     * Разница должна быть равна 2,так как в базу данных должны были добавиться два тестовых объекта.<br/>
     * После тестовые объекты из базы данных удаляем и проверяем, что они null.
     */
    @Test
    public void measureAddAndRemoveTest() {
        int sizeBeforeAddingObjects = measureRepository.getAll().size();

        Integer measureTestObj1Id = measureRepository.add(measureTestObj1);
        Integer measureTestObj2Id = measureRepository.add(measureTestObj2);
        measureTestObj1.setId(measureTestObj1Id);
        measureTestObj2.setId(measureTestObj2Id);
        assertNotNull(measureRepository.getById(measureTestObj1Id), "measureRepository.getById(measureTestObj1Id)");
        assertNotNull(measureRepository.getById(measureTestObj2Id), "measureRepository.getById(measureTestObj2Id)\n");

        int sizeAfterAddingObjects = measureRepository.getAll().size();
        int deviationSize = sizeAfterAddingObjects - sizeBeforeAddingObjects;
        assertThat(deviationSize, is(2), "deviationSize is(2)\n");

        measureRepository.remove(measureTestObj1Id);
        measureRepository.remove(measureTestObj2Id);
        assertNull(measureRepository.getById(measureTestObj1Id), "measureRepository.getById(measureTestObj1Id)");
        assertNull(measureRepository.getById(measureTestObj2Id), "measureRepository.getById(measureTestObj2Id)");
    }

    /**
     * Добавляем в базу данных 3 тестовых объекта. Запоминаем их id. <br/>
     * Далее id 2 объектов передаем в качестве входных параметров в метод: public Measure getById(Integer id){...}. <br/>
     * Получаем из базы данных 2 объекта и сравниваем их с тестовыми объектами, которые имеют соответствующий id.
     */
    @Test
    public void measureGetByIdTest() {
        Integer measureTestObj2Id = measureRepository.add(measureTestObj2);
        Integer measureTestObj3Id = measureRepository.add(measureTestObj3);
        Integer measureTestObj4Id = measureRepository.add(measureTestObj4);
        measureTestObj2.setId(measureTestObj2Id);
        measureTestObj3.setId(measureTestObj3Id);
        measureTestObj4.setId(measureTestObj4Id);

        Measure measureFromDB2 = measureRepository.getById(measureTestObj2Id);
        Measure measureFromDB3 = measureRepository.getById(measureTestObj3Id);
        assertEquals(measureTestObj2, measureFromDB2, "measureTestObj2, measureFromDB2");
        assertEquals(measureTestObj3, measureFromDB3, "measureTestObj3, measureFromDB3");

        measureRepository.remove(measureTestObj2Id);
        measureRepository.remove(measureTestObj3Id);
        measureRepository.remove(measureTestObj4Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "measure". Критерий отбора - значение поля "shortName".<br/>
     * С помощью метода: List<Measure> getByShortName(String shortName){...} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие.
     */
    @Test
    public void measureGetByShortNameTest() {
        Integer measureTestObj1Id = measureRepository.add(measureTestObj1);
        measureTestObj1.setId(measureTestObj1Id);

        List<Measure> expectedMeasures1 = new ArrayList<>();
        expectedMeasures1.add(measureRepository.getById(1));
        expectedMeasures1.add(measureTestObj1);

        List<Measure> actualMeasures1 = measureRepository.getByShortName("мл");

        assertArrayEquals(expectedMeasures1.toArray(), actualMeasures1.toArray(),
                "expectedMeasureArray1, realMeasureArray1; selectionParameter shortName: \"мл\";");

        List<Measure> expectedMeasureList2 = new ArrayList<>();
        expectedMeasureList2.add(measureRepository.getById(1));
        expectedMeasureList2.add(measureRepository.getById(2));
        expectedMeasureList2.add(measureTestObj1);

        List<Measure> realMeasureList2 = measureRepository.getByShortName("л");

        assertArrayEquals(expectedMeasureList2.toArray(), realMeasureList2.toArray(),
                "expectedMeasureArray2, realMeasureArray2; selectionParameter shortName: \"л\";");

        measureRepository.remove(measureTestObj1Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "measure". Критерий отбора - значение поля "fullName".<br/>
     * С помощью метода: List<Measure> getByFullName(String fullName){...} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие.
     */
    @Test
    public void measureGetByFullNameTest() {
        Integer measureTestObj3Id = measureRepository.add(measureTestObj3);
        Integer measureTestObj4Id = measureRepository.add(measureTestObj4);
        measureTestObj3.setId(measureTestObj3Id);
        measureTestObj4.setId(measureTestObj4Id);

        List<Measure> expectedMeasures1 = new ArrayList<>();
        expectedMeasures1.add(measureRepository.getById(3));
        expectedMeasures1.add(measureRepository.getById(4));
        expectedMeasures1.add(measureTestObj3);
        expectedMeasures1.add(measureTestObj4);

        List<Measure> actualMeasures1 = measureRepository.getByFullName("грамм");

        assertArrayEquals(expectedMeasures1.toArray(), actualMeasures1.toArray(),
                "expectedMeasureArray1, realMeasureArray1; selectionParameter fullName: \"грамм\";");

        List<Measure> expectedMeasures2 = new ArrayList<>();
        expectedMeasures2.add(measureRepository.getById(1));

        List<Measure> realMeasures2 = measureRepository.getByFullName("миллилитр");

        assertArrayEquals(expectedMeasures2.toArray(), realMeasures2.toArray(),
                "expectedMeasureArray2, realMeasureArray2; selectionParameter fullName: \"миллилитр\";");

        measureRepository.remove(measureTestObj3Id);
        measureRepository.remove(measureTestObj4Id);
    }

    /**
     * Добавляем в базу данных тестовый объект. Запоминаем значения всех его полей.<br/>
     * Далее в этом объекте изменияем значение поле "fullName" и снова запоминаем значения всех полей этого объекта.<br/>
     * Сравниваем 2 этих объекта на соответствие по полям. Значения полей этих объектов, кроме того, которое изменено,
     * должны совпадать.
     */
    @Test
    public void measureUpdateTest() {
        Integer measureTestObj1Id = measureRepository.add(measureTestObj1);
        measureTestObj1.setId(measureTestObj1Id);

        logger.info("Saving the values of all fields of this object before updating.\n");
        Integer idBeforeUpdate = measureRepository.getById(measureTestObj1Id).getId();
        String shortNameBeforeUpdate = measureRepository.getById(measureTestObj1Id).getShortName();
        String fullNameBeforeUpdate = measureRepository.getById(measureTestObj1Id).getFullName();

        String updatedFullName = "Тест: литр";
        measureRepository.update(measureRepository.getById(measureTestObj1Id).setFullName(updatedFullName));

        logger.info("In this object changed the value of the field \"name\".\n");

        logger.info("Saving the values of all fields of this object after updating.\n");
        Integer idAfterUpdate = measureRepository.getById(measureTestObj1Id).getId();
        String shortNameAfterUpdate = measureRepository.getById(measureTestObj1Id).getShortName();
        String fullNameAfterUpdate = measureRepository.getById(measureTestObj1Id).getFullName();

        assertEquals(idBeforeUpdate, idAfterUpdate, "idBeforeUpdate, idAfterUpdate");
        assertEquals(shortNameBeforeUpdate, shortNameAfterUpdate, "shortNameBeforeUpdate, shortNameAfterUpdate");
        assertNotEquals(fullNameBeforeUpdate, fullNameAfterUpdate, "fullNameBeforeUpdate, fullNameAfterUpdate");
        assertEquals(updatedFullName, fullNameAfterUpdate, "updatedFullName, fullNameAfterUpdate");

        measureRepository.remove(measureTestObj1Id);
    }
}
