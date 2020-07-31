package dbproject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDriver {

	public static void main(String[] args) throws NumberFormatException, IOException {

		// initialize
		String userID = "dbuser";
		String userPW = "dbpwd";
		String url = "jdbc:mysql://localhost:3306/dbprj?&serverTimezone=UTC";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		String sql = "";
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		// variables for project
		String name;
		float price, cost, priceChange;
		String mainIngredient;
		int soldCount, todaySold;
		String category;

		try {

			/*
			 * Class.forName("com.mysql.jdbc.Driver"); This is deprecated. The new driver
			 * class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered
			 * via the SPI and manual loading of the driver class is generally unnecessary.
			 * 
			 */

			// construct a connection to mysql server
			conn = DriverManager.getConnection(url, userID, userPW);
			stmt = conn.createStatement();

			// Prepared Statements for 1. Record Today's Sales
			PreparedStatement pStmt1getS = conn.prepareStatement("SELECT Name, SoldCount FROM Sandwich");
			PreparedStatement pStmt1getD = conn.prepareStatement("SELECT Name, SoldCount FROM Drink");
			PreparedStatement pStmt1updateS = conn.prepareStatement("UPDATE Sandwich SET SoldCount = ? WHERE Name = ?");
			PreparedStatement pStmt1updateD = conn.prepareStatement("UPDATE Drink SET SoldCount = ? WHERE Name = ?");

			// Prepared Statements for 2. Add New Menu(Sandwich)
			PreparedStatement pStmt2 = conn
					.prepareStatement("INSERT INTO Sandwich (Name, Price, MainIngredient, SoldCount) VALUES (?,?,?,0)");
			PreparedStatement pStmt2i = conn
					.prepareStatement("INSERT INTO Ingredient (Name, Price, Category) VALUES (?,?,'main')");

			// Prepared Statements for 3. Delete Menu(Sandwich)
			PreparedStatement pStmt3 = conn.prepareStatement("DELETE FROM Sandwich WHERE Name=?");

			// Prepared Statements for 4. Check Popularity of Sandwich
			PreparedStatement pStmt4 = conn
					.prepareStatement("SELECT Name FROM Sandwich ORDER BY SoldCount DESC LIMIT ?");

			// Prepared Statements for 5. Check Net Profit
			PreparedStatement pStmt5 = conn.prepareStatement(
					"SELECT Profit*SoldCount AS NetProfit FROM Profit natural join Sandwich WHERE Name=?");

			// Prepared Statements for 6. Check Revenue
			PreparedStatement pStmt6 = conn.prepareStatement(
					"SELECT SUM(Price * SoldCount)+DrinkRevenue AS Revenue FROM Sandwich, (SELECT SUM(Price * SoldCount) AS DrinkRevenue FROM Drink WHERE Name!=?)DrinkR WHERE Name!=?");

			// Prepared Statements for 7. Update Ingredient's Price
			PreparedStatement pStmt7check = conn.prepareStatement("SELECT Category FROM Ingredient WHERE Name=?");
			PreparedStatement pStmt7updateI = conn
					.prepareStatement("UPDATE Ingredient SET Price = Price + ? WHERE Name=?");
			PreparedStatement pStmt7updateS = conn
					.prepareStatement("UPDATE Sandwich SET Price = Price + ? WHERE MainIngredient=?");
			PreparedStatement pStmt7updateAllS = conn.prepareStatement("UPDATE Sandwich SET Price = Price + ?");

			if (conn != null) {
				System.out.println("Connected to database.\n");
				System.out.println(String.format("%66s", "").replace(' ', '='));
				System.out.println("Welcome to Ewha Sandwich Store's Menu(Database) Management System!");
				System.out.println(String.format("%66s", "").replace(' ', '='));
			}

			int menu = 0;
			while (menu != 9) {
				// show menu for user input
				System.out.println("\n\n" + String.format("%77s", "").replace(' ', '-'));
				System.out.println("1. Record Today's Sales  2. Add New Menu(Sandwich)  3. Delete Menu(Sandwich)");
				System.out.println("4. Check Popularity of Sandwich  5. Check Net Profit  6. Check Revenue");
				System.out.println("7. Update Ingredient's Price  8. Print Out Contents of All Tables  9. Exit ");
				System.out.println(String.format("%77s", "").replace(' ', '-'));

				System.out.print("Please enter the desired action(as a number) with your keyboard. Number: ");
				menu = Integer.parseInt(bf.readLine());

				switch (menu) {
				case 1: // 1. Record Today's Sales
					System.out.println("Please enter the quantity sold for each menu.");

					System.out.println("\n<Sandwich>");
					rset = pStmt1getS.executeQuery();

					while (rset.next()) {
						name = rset.getString("Name");
						soldCount = rset.getInt("SoldCount");
						System.out.print(name + ": ");
						todaySold = Integer.parseInt(bf.readLine());
						// add today's sales to total sold count recorded in database
						pStmt1updateS.setInt(1, soldCount + todaySold);
						pStmt1updateS.setString(2, name);
						pStmt1updateS.executeUpdate();
					}
					rset.close();

					System.out.println("\n<Drink>");
					rset = pStmt1getD.executeQuery();

					while (rset.next()) {
						name = rset.getString("Name");
						soldCount = rset.getInt("SoldCount");
						System.out.print(name + ": ");
						todaySold = Integer.parseInt(bf.readLine());
						// add today's sales to total sold count recorded in database
						pStmt1updateD.setInt(1, soldCount + todaySold);
						pStmt1updateD.setString(2, name);
						pStmt1updateD.executeUpdate();
					}
					rset.close();

					System.out.println("Finish recording today's sales.");
					break;

				case 2: // 2. Add New Menu(Sandwich)
					System.out.println("Please enter information about new menu.");
					System.out.println("What is the name of the new menu?");
					name = bf.readLine();
					pStmt2.setString(1, name);
					System.out.println("How much is the new menu?");
					price = Float.parseFloat(bf.readLine());
					pStmt2.setFloat(2, price);
					System.out.println("What is the main ingredient of the new menu?");
					mainIngredient = bf.readLine();
					pStmt2.setString(3, mainIngredient);
					pStmt2i.setString(1, mainIngredient);
					System.out.println("How much is the new menu's main ingredient?");
					price = Float.parseFloat(bf.readLine());
					pStmt2i.setFloat(2, price);
					// insert data into ingredient table first(because of referential integrity
					// constraint)
					pStmt2i.executeUpdate();
					pStmt2.executeUpdate();

					System.out.println("\nNew menu has been added.");
					break;

				case 3: // 3. Delete Menu(Sandwich)
					System.out.println("Which menu do you want to delete?");
					name = bf.readLine();
					pStmt3.setString(1, name);
					pStmt3.executeUpdate();
					System.out.println(name + " is deleted from the menu.");
					break;

				case 4: // 4. Check Popularity of Sandwich
					System.out.print("Check Top n Menu! Enter n: ");
					int n = Integer.parseInt(bf.readLine());
					pStmt4.setInt(1, n);
					rset = pStmt4.executeQuery();

					int i = 1;
					while (rset.next()) {
						name = rset.getString("Name");
						System.out.println("Top " + i++ + ". " + name);
					}
					rset.close();

					break;

				case 5: // 5. Check Net Profit
					System.out.println("Which menu(sandwich)'s net profit will you check?");
					name = bf.readLine();
					pStmt5.setString(1, name);
					rset = pStmt5.executeQuery();
					rset.next();
					float netprofit = rset.getFloat("NetProfit");
					System.out.println("Net profit of menu [" + name + "] : $" + netprofit);
					rset.close();
					break;

				case 6: // 6. Check Revenue
					System.out.println("Please select the menu to exclude from the revenue calculation.");
					name = bf.readLine();
					pStmt6.setString(1, name);
					pStmt6.setString(2, name);
					rset = pStmt6.executeQuery();
					rset.next();
					float revenue = rset.getFloat("Revenue");
					System.out.println("Revenue without " + name + " : $" + revenue);
					rset.close();
					break;

				case 7: // 7. Update Ingredient's Price
					// Price of sandwich change as price of ingredient(cost) change
					// Use transaction
					conn.setAutoCommit(false);
					System.out.println("Which ingredient would you like to update?");
					name = bf.readLine();

					// check category(main or sub)
					pStmt7check.setString(1, name);
					rset = pStmt7check.executeQuery();
					rset.next();
					category = rset.getString("Category");
					rset.close();

					System.out.println("How much has the price changed?");
					priceChange = Float.parseFloat(bf.readLine());

					if (category.equals("main")) {
						pStmt7updateI.setString(2, name);
						pStmt7updateS.setString(2, name);
						pStmt7updateI.setFloat(1, priceChange);
						pStmt7updateS.setFloat(1, priceChange);
						pStmt7updateI.executeUpdate();
						pStmt7updateS.executeUpdate();
					} else if (category.equals("sub")) {
						pStmt7updateI.setString(2, name);
						pStmt7updateI.setFloat(1, priceChange);
						pStmt7updateAllS.setFloat(1, priceChange);
						pStmt7updateI.executeUpdate();
						pStmt7updateAllS.executeUpdate();
					}

					conn.commit();
					conn.setAutoCommit(true);

					break;

				case 8: // 8. Print Out Contents of All Tables
					System.out.println("Printing out contents of all tables.");

					System.out.println("\n<Sandwich Table>");

					sql = "SELECT * FROM Sandwich";
					rset = stmt.executeQuery(sql);

					System.out.println(String.format("%64s", "").replace(' ', '-'));
					System.out.println(String.format("Name %15s | Price | Main Ingredient %4s | Sold Count", "", ""));
					System.out.println(String.format("%64s", "").replace(' ', '-'));

					while (rset.next()) {
						name = rset.getString("Name");
						price = rset.getFloat("Price");
						mainIngredient = rset.getString("MainIngredient");
						soldCount = rset.getInt("SoldCount");
						System.out.println(String.format(
								String.format("%20s | $%4s | %20s | %10s", name, price, mainIngredient, soldCount)));
					}
					rset.close();

					System.out.println("\n<Ingredient Table>");
					sql = "SELECT * FROM Ingredient";
					rset = stmt.executeQuery(sql);

					System.out.println(String.format("%40s", "").replace(' ', '-'));
					System.out.println(String.format("Name %15s | Price | Category", ""));
					System.out.println(String.format("%40s", "").replace(' ', '-'));

					while (rset.next()) {
						name = rset.getString("Name");
						price = rset.getFloat("Price");
						category = rset.getString("Category");
						System.out.println(String.format(String.format("%20s | $%4s | %9s", name, price, category)));
					}
					rset.close();

					System.out.println("\n<Drink Table>");
					sql = "SELECT * FROM Drink";
					rset = stmt.executeQuery(sql);

					System.out.println(String.format("%40s", "").replace(' ', '-'));
					System.out.println(String.format("Name %5s | Price | Cost  | Sold Count", ""));
					System.out.println(String.format("%40s", "").replace(' ', '-'));

					while (rset.next()) {
						name = rset.getString("Name");
						price = rset.getFloat("Price");
						cost = rset.getFloat("Cost");
						soldCount = rset.getInt("SoldCount");
						System.out.println(String
								.format(String.format("%10s | $%4s | $%4s | %10s", name, price, cost, soldCount)));
					}
					rset.close();
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (rset != null) {
				try {
					rset.close();
					System.out.println("Close ResultSet");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
					System.out.println("Close Statement");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
					System.out.println("Close Connection");
					System.out.println("Good bye!");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
