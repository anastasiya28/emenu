INSERT INTO ingredient (name)VALUES('помидоры');
INSERT INTO ingredient (name)VALUES('сыр Моцарелла');
INSERT INTO ingredient (name)VALUES('базилик');
INSERT INTO ingredient (name)VALUES('масло оливковое');
INSERT INTO ingredient (name)VALUES('крем базилик');
INSERT INTO ingredient (name)VALUES('соус Песто');
INSERT INTO ingredient (name)VALUES('вяленые помидоры');
INSERT INTO ingredient (name)VALUES('микс салат');
INSERT INTO ingredient (name)VALUES('рукола');
INSERT INTO ingredient (name)VALUES('миндаль');
INSERT INTO ingredient (name)VALUES('кешью');
INSERT INTO ingredient (name)VALUES('соус "Базилик" или соус медовый');
INSERT INTO ingredient (name)VALUES('семга слабосоленая');
INSERT INTO ingredient (name)VALUES('лук репчатый');
INSERT INTO ingredient (name)VALUES('сыр Пармезан');
INSERT INTO ingredient (name)VALUES('лимонный фреш');
INSERT INTO ingredient (name)VALUES('мидии в панцире');
INSERT INTO ingredient (name)VALUES('сливки');
INSERT INTO ingredient (name)VALUES('вино');
INSERT INTO ingredient (name)VALUES('чеснок');
INSERT INTO ingredient (name)VALUES('виноградные улитки');
INSERT INTO ingredient (name)VALUES('говяжья вырезка');
INSERT INTO ingredient (name)VALUES('помидоры черри');
INSERT INTO ingredient (name)VALUES('огурцы');
INSERT INTO ingredient (name)VALUES('запеченный картофель');
INSERT INTO ingredient (name)VALUES('цукини');
INSERT INTO ingredient (name)VALUES('перец чили');
INSERT INTO ingredient (name)VALUES('утиное филе');
INSERT INTO ingredient (name)VALUES('мед');
INSERT INTO ingredient (name)VALUES('апельсиново - карамельный соус');
INSERT INTO ingredient (name)VALUES('манго');
INSERT INTO ingredient (name)VALUES('запеченное яблоко');
INSERT INTO ingredient (name)VALUES('перловка');
INSERT INTO ingredient (name)VALUES('филе семги');
INSERT INTO ingredient (name)VALUES('зеленый горошек');
INSERT INTO ingredient (name)VALUES('маслины');
INSERT INTO ingredient (name)VALUES('лайм');
INSERT INTO ingredient (name)VALUES('зелень');
INSERT INTO ingredient (name)VALUES('лимонный или устрично - креветочный соус');
INSERT INTO ingredient (name)VALUES('грецкие орехи');
INSERT INTO ingredient (name)VALUES('фундук');
INSERT INTO ingredient (name)VALUES('свекла');
INSERT INTO ingredient (name)VALUES('капуста');
INSERT INTO ingredient (name)VALUES('картофель');
INSERT INTO ingredient (name)VALUES('сметана');
INSERT INTO ingredient (name)VALUES('зелень');
INSERT INTO ingredient (name)VALUES('белые грибы');
INSERT INTO ingredient (name)VALUES('гренки');
INSERT INTO ingredient (name)VALUES('сырно - чесночный соус или винный соус');
INSERT INTO ingredient (name)VALUES('рис');
INSERT INTO ingredient (name)VALUES('тигровые креветки');
INSERT INTO ingredient (name)VALUES('морковь');
INSERT INTO ingredient (name)VALUES('сельдерей');
INSERT INTO ingredient (name)VALUES('тальятелле');
INSERT INTO ingredient (name)VALUES('телятина');
INSERT INTO ingredient (name)VALUES('болгарский перец');
INSERT INTO ingredient (name)VALUES('баклажан');
INSERT INTO ingredient (name)VALUES('шампиньоны');

INSERT INTO measure (shortName,fullName)VALUES('мл', 'миллилитр');
INSERT INTO measure (shortName, fullName)VALUES('л', 'литр');
INSERT INTO measure (shortName, fullName)VALUES('гр', 'грамм');
INSERT INTO measure (shortName, fullName)VALUES('кг', 'килограмм');

INSERT INTO organization(name, inn, legalAddress) VALUES('Приют холостяка', 1655227847,
'г.Казань, ул.Чернышевского, д.27А, 420111');
INSERT INTO organization (name, inn, legalAddress) VALUES('Extra lounge', 1655059173,
'г.Казань, ул.Николая Ершова, д.1, к.А, 420061');
INSERT INTO organization (name, inn, legalAddress) VALUES('Купеческое собрание', 1659091428,
'г. Казань, ул. Братьев Касимовых д.38, 420110');

INSERT INTO menu (name, organizationId) VALUES('Основное меню ресторана Приют холостяка', 1);
INSERT INTO menu (name, organizationId) VALUES('Сезонное меню ресторана Приют холостяка', 1);
INSERT INTO menu (name, organizationId) VALUES('Основное меню ресторана Extra lounge', 2);
INSERT INTO menu (name, organizationId) VALUES('Основное меню ресторана Купеческое собрание', 3);

INSERT INTO menuSection (name, menuId) VALUES ('Закуски холодные', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Салаты', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Горячие закуски', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Супы', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Основные блюда', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Ризотто', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Паста', 1);
INSERT INTO menuSection (name, menuId) VALUES ('Гарниры', 1);

INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Капрезе', 310, 3, 430, 1);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Салат тёплый с мясом и вялеными помидорами', 250, 3, 440, 2);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Салат из семги и помидоров', 240, 3, 370, 2);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Мидии в сливках и вине', 680, 3, 480, 3);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Виноградные улитки', 140, 3, 410, 3);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Борщ по - домашнему', 430, 1, 290, 4);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Итальянский грибной суп', 420, 1, 320, 4);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Филе - миньон', 190, 3, 660, 5);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Утиная грудка в медовом соусе', 170, 3, 530, 5);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Стейк семги с лимонным соусом', 210, 3, 630, 5);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Ризотто с креветками и базиликом', 350, 3, 420, 6);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Тальяте́лле с телятиной и белыми грибами', 400, 3, 490, 7);
INSERT INTO menuItem (name, portionVolume, measureId, price, menuSectionId) VALUES
('Овощи гриль', 230, 3, 270, 8);

INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (1, 1);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (1, 2);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (1, 3);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (1, 4);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (1, 5);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (1, 6);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 22);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 7);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 8);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 9);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 10);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 11);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (2, 12);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 13);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 1);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 14);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 15);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 8);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 16);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (3, 4);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (4, 17);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (4, 18);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (4, 19);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (4, 20);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (5, 21);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (5, 15);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (5, 20);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (5, 40);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (5, 41);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (6, 22);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (6, 42);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (6, 43);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (6, 44);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (6, 45);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (6, 46);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (7, 47);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (7, 14);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (7, 18);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (7, 48);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 22);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 22);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 23);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 24);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 25);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 26);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 27);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (8, 49);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (9, 28);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (9, 29);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (9, 30);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (9, 31);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (9, 32);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (9, 33);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (10, 34);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (10, 35);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (10, 36);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (10, 37);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (10, 38);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (10, 39);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 50);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 51);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 52);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 14);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 53);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 3);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (11, 15);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (12, 54);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (12, 55);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (12, 18);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (12, 47);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (12, 14);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (12, 15);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (13, 1);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (13, 56);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (13, 26);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (13, 57);
INSERT INTO menuItem_ingredient (menuItemId, ingredientId) VALUES (13, 58);








