#create table as schema design - (1), (3), (4)

CREATE TABLE Drink(
	Name VARCHAR(10) NOT NULL,
	Price NUMERIC(2,1) NOT NULL,
	Cost NUMERIC(2,1),
	SoldCount INT,
	PRIMARY KEY (Name)
);

CREATE TABLE Ingredient(
	Name VARCHAR(20) NOT NULL,
	Price NUMERIC(2,1) NOT NULL,
	Category VARCHAR(10) NOT NULL,
	PRIMARY KEY (Name)
);

CREATE TABLE Sandwich(
	Name VARCHAR(20) NOT NULL,
	Price NUMERIC(2,1) NOT NULL,
	MainIngredient VARCHAR(20) NOT NULL,
	SoldCount INT,
	PRIMARY KEY (Name),
	FOREIGN KEY (MainIngredient) REFERENCES Ingredient(Name)
);

#insert data into tables - (2)

INSERT INTO Drink (Name, Price) values ('Coke', 2);
INSERT INTO Drink (Name, Price) values ('Sprite', 2);
INSERT INTO Drink (Name, Price) values ('Juice', 4);
INSERT INTO Drink (Name, Price) values ('Water', 1);
INSERT INTO Drink (Name, Price) values ('Coffee', 3);
INSERT INTO Drink (Name, Price) values ('Milk', 2);
UPDATE Drink SET SoldCount = 0;
UPDATE Drink SET Cost = Price * 0.5;

INSERT INTO Ingredient (Name, Price, Category) values ('egg', 1, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('beef', 3, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('chicken', 2, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('ham', 0.8, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('avocado', 1.5, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('turkey', 1, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('tuna', 0.7, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('bacon', 1, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('meatball', 1.2, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('shrimp', 1, 'main');
INSERT INTO Ingredient (Name, Price, Category) values ('bread', 0.3, 'sub');
INSERT INTO Ingredient (Name, Price, Category) values ('vegetable', 0.7, 'sub');
INSERT INTO Ingredient (Name, Price, Category) values ('cheese', 0.5, 'sub');
INSERT INTO Ingredient (Name, Price, Category) values ('sauce', 0.1, 'sub');

INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Egg', 5.5, 'egg');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Roasted Beef', 9, 'beef');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Roasted Chicken', 8, 'chicken');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Ham', 6, 'ham');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Avocado', 7, 'avocado');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Turkey', 6.5, 'turkey');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Tuna', 6.3, 'tuna');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Bacon', 6.8, 'bacon');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Meatball', 7.5, 'meatball');
INSERT INTO Sandwich (Name, Price, MainIngredient) values ('Shrimp', 7.5, 'shrimp');
UPDATE Sandwich SET SoldCount = 0;

#create index - (5)

CREATE INDEX SandwichSoldCountIndex ON Sandwich(SoldCount);
CREATE INDEX DrinkSoldCountIndex ON Drink(SoldCount);

#create view - (6)

CREATE VIEW DefaultCost AS
SELECT sum(Price) as Cost 
FROM Ingredient 
WHERE Category='sub';

CREATE VIEW Profit AS
SELECT (Sandwich.Name) AS Name, (Sandwich.Price - Ingredient.Price - DefaultCost.Cost) as Profit
FROM Sandwich, Ingredient, DefaultCost
WHERE Sandwich.MainIngredient = Ingredient.Name;
