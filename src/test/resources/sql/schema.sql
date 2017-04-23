DROP ALL OBJECTS;

  CREATE TABLE
  IF NOT EXISTS
  ingredient (
    ingredientId INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(1000) NOT NULL
  );

  CREATE TABLE
  IF NOT EXISTS
  measure (
    measureId INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    shortName VARCHAR(512) NOT NULL,
    fullName VARCHAR (1000) NOT NULL
  );

  CREATE TABLE
  IF NOT EXISTS
  organization (
    organizationId INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(512) NOT NULL,
    inn  INT NOT NULL,
    legalAddress VARCHAR(2000)
  );

  CREATE TABLE
  IF NOT EXISTS
  menu (
    menuId INT UNSIGNED NOT NULL PRIMARY KEY  AUTO_INCREMENT,
    name VARCHAR(512) NOT NULL,
    organizationId INT NOT NULL,
    FOREIGN KEY (organizationId) REFERENCES organization(organizationId),
  );

  CREATE TABLE
  IF NOT EXISTS
  menuSection (
    menuSectionId INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(1000) NOT NULL,
    menuId INT NOT NULL,
    FOREIGN KEY (menuId) REFERENCES menu(menuId)
  );

  CREATE TABLE
  IF NOT EXISTS
  menuItem(
    menuItemId INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(1000) NOT NULL,
    portionVolume INT NOT NULL,
    measureId INT NOT NULL,
    price INT NOT NULL,
    menuSectionId INT NOT NULL,
    FOREIGN KEY (measureId) REFERENCES measure(measureId),
    FOREIGN KEY (menuSectionId) REFERENCES menuSection(menuSectionId)
  );

   CREATE TABLE
   IF NOT EXISTS
   menuItem_ingredient (
     menuItemId INT UNSIGNED NOT NULL,
     ingredientId INT UNSIGNED NOT NULL,
     FOREIGN KEY (menuItemId) REFERENCES menuItem (menuItemId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
     FOREIGN KEY (ingredientId) REFERENCES ingredient (ingredientId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
   );





