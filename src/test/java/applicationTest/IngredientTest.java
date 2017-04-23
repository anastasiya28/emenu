package applicationTest;

import application.dao.interfaces.IngredientDAO;
import application.model.Ingredient;
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
public class IngredientTest {
    private static final Logger logger = Logger.getLogger(IngredientTest.class);

    private final Ingredient ingrTestObj1 = new Ingredient().setName("Тест: болгарский перец");
    private final Ingredient ingrTestObj2 = new Ingredient().setName("Тест: куриное филе");
    private final Ingredient ingrTestObj3 = new Ingredient().setName("Тест: тигровые креветки");

    @Autowired
    private IngredientDAO ingredientRepository;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void beforeTest() {
        logger.info("beforeTest------------------------------------");
        logger.info("Starting test: " + testName.getMethodName());
        logger.info("The database currently contains " + ingredientRepository.getAll().size() + " records.");
        logger.info("------------------------------------");
    }

    @After
    public void afterTest() {
        logger.info("------------------------------------");
        logger.info("The database currently contains " + ingredientRepository.getAll().size() + " records.");
        logger.info("afterTest------------------------------------\n\n");
    }

    /**
     * Определяем количество записей в таблице "ingredient" и полученное значение сравниваем с ожидаемым.<br/>
     * Ожидаемое значение равно 58. Они должны быть равны.
     */
    @Test
    public void ingredientGetAllTest() {
        assertThat(ingredientRepository.getAll().size(), is(58), "ingredientRepository.getAll().size(), is(58)");
    }

    /**
     * Определяем количество записей в таблице "ingredient", сохраняем это значение в переменной. <br/>
     * Добавляем в базу данных два тестовых объекта. Проверяем, что эти объекты не null. <br/>
     * Далее снова определяем количество записей в таблице "ingredient" и вычисляем разницу между текущим значение и
     * предыдущим.<br/>
     * Разница должна быть равна 2,так как в базу данных должны были добавиться два тестовых объекта.<br/>
     * После тестовые объекты из базы данных удаляем и проверяем, что они null.
     */
    @Test
    public void ingredientAddAndRemoveTest() {
        int sizeBeforeAddingObjects = ingredientRepository.getAll().size();

        Integer ingrTestObj1Id = ingredientRepository.add(ingrTestObj1);
        Integer ingrTestObj2Id = ingredientRepository.add(ingrTestObj2);
        ingrTestObj1.setId(ingrTestObj1Id);
        ingrTestObj2.setId(ingrTestObj2Id);
        assertNotNull(ingredientRepository.getById(ingrTestObj1Id), "ingredientRepository.getById(ingrTestObj1Id)");
        assertNotNull(ingredientRepository.getById(ingrTestObj2Id), "ingredientRepository.getById(ingrTestObj2Id)\n");

        int sizeAfterAddingObjects = ingredientRepository.getAll().size();
        int deviationSize = sizeAfterAddingObjects - sizeBeforeAddingObjects;
        assertThat(deviationSize, is(2), "deviationSize is(2)\n");

        ingredientRepository.remove(ingrTestObj1Id);
        ingredientRepository.remove(ingrTestObj2Id);
        assertNull(ingredientRepository.getById(ingrTestObj1Id), "ingredientRepository.getById(ingrTestObj1Id)");
        assertNull(ingredientRepository.getById(ingrTestObj2Id), "ingredientRepository.getById(ingrTestObj2Id)");
    }

    /**
     * Добавляем в базу данных 3 тестовых объекта. Запоминаем их id. <br/>
     * Далее id 2 объектов передаем в качестве входных параметров в метод: public Ingredient getById(Integer id){...}. <br/>
     * Получаем из базы данных 2 объекта и сравниваем их с тестовыми объектами, которые имеют соответствующий id.
     */
    @Test
    public void ingredientGetByIdTest() {
        Integer ingrTestObj1Id = ingredientRepository.add(ingrTestObj1);
        Integer ingrTestObj2Id = ingredientRepository.add(ingrTestObj2);
        Integer ingrTestObj3Id = ingredientRepository.add(ingrTestObj3);
        ingrTestObj1.setId(ingrTestObj1Id);
        ingrTestObj2.setId(ingrTestObj2Id);
        ingrTestObj3.setId(ingrTestObj3Id);

        Ingredient ingrFromDB2 = ingredientRepository.getById(ingrTestObj2Id);
        Ingredient ingrFromDB3 = ingredientRepository.getById(ingrTestObj3Id);

        TestUtils.assertEquals(ingrTestObj2, ingrFromDB2, "ingrTestObj2, ingrFromDB2");
        TestUtils.assertEquals(ingrTestObj3, ingrFromDB3, "ingrTestObj3, ingrFromDB3");

        ingredientRepository.remove(ingrTestObj1Id);
        ingredientRepository.remove(ingrTestObj2Id);
        ingredientRepository.remove(ingrTestObj3Id);
    }

    /**
     * Формируем массив ожидаемых значений из таблицы "ingredient". Критерий отбора - значение поля "name".<br/>
     * С помощью метода: List<Ingredient> getByName(String name){..} получаем массив актуальных значений.<br/>
     * И сравниваем эти два массива на соответствие.
     */
    @Test
    public void ingredientGetByNameTest() {
        Integer ingrTestObj1Id = ingredientRepository.add(ingrTestObj1);
        Integer ingrTestObj2Id = ingredientRepository.add(ingrTestObj2);
        ingrTestObj1.setId(ingrTestObj1Id);
        ingrTestObj2.setId(ingrTestObj2Id);

        List<Ingredient> expectedIngredients1 = new ArrayList<>();
        expectedIngredients1.add(ingredientRepository.getById(27));
        expectedIngredients1.add(ingredientRepository.getById(56));
        expectedIngredients1.add(ingrTestObj1);

        List<Ingredient> actualIngredients1 = ingredientRepository.getByName("Перец");
        assertArrayEquals(expectedIngredients1.toArray(), actualIngredients1.toArray(), "expectedIngredients," +
                " actualIngredients; selectionParameter name: \"Перец\";");

        List<Ingredient> expectedIngredients2 = new ArrayList<>();
        expectedIngredients2.add(ingredientRepository.getById(28));
        expectedIngredients2.add(ingredientRepository.getById(34));
        expectedIngredients2.add(ingrTestObj2);

        List<Ingredient> actualIngredients2 = ingredientRepository.getByName("Филе");
        assertArrayEquals(expectedIngredients2.toArray(), actualIngredients2.toArray(), "expectedIngredients," +
                " actualIngredients; selectionParameter name: \"Филе\";");

        ingredientRepository.remove(ingrTestObj1Id);
        ingredientRepository.remove(ingrTestObj2Id);
    }

    /**
     * Добавляем в базу данных тестовый объект. Запоминаем значения всех его полей.<br/>
     * Далее в этом объекте изменияем значение поле "name" и снова запоминаем значения всех полей этого объекта.<br/>
     * Сравниваем 2 этих объекта на соответствие по полям. Значения полей этих объектов, кроме того, которое изменено,
     * должны совпадать.
     */
    @Test
    public void ingredientUpdateTest() {
        Integer ingrTestObj3Id = ingredientRepository.add(ingrTestObj3);
        ingrTestObj3.setId(ingrTestObj3Id);

        logger.info("Saving the values of all fields of this object before updating.\n");
        Integer idBeforeUpdate = ingredientRepository.getById(ingrTestObj3Id).getId();
        String nameBeforeUpdate = ingredientRepository.getById(ingrTestObj3Id).getName();

        String updatedName = "Тест: морской окунь";
        ingredientRepository.update(ingredientRepository.getById(ingrTestObj3Id).setName(updatedName));
        logger.info("In this object changed the value of the field \"name\".\n.");

        logger.info("Saving the values of all fields of this object after updating.\n");
        Integer idAfterUpdate = ingredientRepository.getById(ingrTestObj3Id).getId();
        String nameAfterUpdate = ingredientRepository.getById(ingrTestObj3Id).getName();

        assertEquals(idBeforeUpdate, idAfterUpdate, "idBeforeUpdate, idAfterUpdate");
        assertNotEquals(nameBeforeUpdate, nameAfterUpdate, "nameBeforeUpdate, nameAfterUpdate");
        assertEquals(updatedName, nameAfterUpdate, "updatedName, nameAfterUpdate");
        ingredientRepository.remove(ingrTestObj3Id);
    }
}
