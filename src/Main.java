import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 100; i++) {
			System.out.println(getQueryList());
			System.out.println("Enter query name to execute or say 'Add new' to create a new one. Press 'Quit' to exit.");
			String response = sc.nextLine();
			if (response.equalsIgnoreCase("Add new")) {
				System.out.println("Enter Query Name: ");
				String inputQueryName = sc.nextLine();
				System.out.println("Enter Query: ");
				String inputQuery = sc.nextLine();
				inputQuery(inputQueryName, inputQuery);
				System.out.println("Inserted Succesfully!");
			} 
			else if(response.equalsIgnoreCase("Quit")){
				System.out.println("Thank you.");
				break;
			}
			else {
				String query = getQuery(response);
				System.out.println("Do you want to save this to a text file?(Yes/No)");
				String answer = sc.nextLine();
				if (answer.equalsIgnoreCase("YES")) {
					PrintWriter writer;
					try {
						writer = new PrintWriter("CustomerData.txt", "UTF-8");
						writer.println(executeString(query));
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					System.out.println("Your data is stored in CustomerData.txt");
				} else {
					System.out.println(executeString(query));
				}
			}
		}
		sc.close();
	}

	public static String executeString(String sql) {
		String returnString = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int i = 1; i < columnsNumber + 1; i++) {
				returnString += rsmd.getColumnName(i) + "||";
			}
			returnString += "\n";
			while (rs.next()) {
				for (int i = 1; i < columnsNumber + 1; i++) {
					returnString += rs.getString(i) + "||";
				}
				returnString += "\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}

	public static String getQueryList() {
		String returnString = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM QueryList");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int i = 1; i < columnsNumber + 1; i++) {
				returnString += rsmd.getColumnName(i) + "||";
			}
			returnString += "\n";
			while (rs.next()) {
				for (int i = 1; i < columnsNumber + 1; i++) {
					returnString += rs.getString(i) + "||";
				}
				returnString += "\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}

	public static void inputQuery(String QueryName, String Query) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
			stmt = con.createStatement();
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO QueryList (QueryName,Query) VALUES (?,?)");
			pstmt.setString(1, QueryName);
			pstmt.setString(2, Query);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getQuery(String QueryName) {
		String returnString = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
			stmt = con.createStatement();
			PreparedStatement pstmt = con.prepareStatement("SELECT Query FROM QueryList where QueryName = ?");
			pstmt.setString(1, QueryName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				returnString += rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}
}
