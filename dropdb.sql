#DROP indices

ALTER TABLE Sandwich DROP INDEX SandwichSoldCountIndex;
ALTER TABLE Drink DROP INDEX DrinkSoldCountIndex;

#DROP tables

DROP TABLE Drink;
DROP TABLE Sandwich;
DROP TABLE Ingredient;

#DROP views

DROP VIEW DefaultCost; 
DROP VIEW Profit;

