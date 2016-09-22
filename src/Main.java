import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Query");
		String query = sc.nextLine();
		System.out.println("Do you want to save this to a text file?(Yes/No)");
		String answer = sc.nextLine();
		if(answer.equalsIgnoreCase("YES")){
			PrintWriter writer;
			try {
				writer = new PrintWriter("CustomerData.txt", "UTF-8");
				writer.println(executeString(query));
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Your data is stored in CustomerData.txt");
		}
		else{
			System.out.println(executeString(query));
		}
		sc.close();
	}
	public static String executeString(String sql) {
		String returnString = "";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			returnString += "FIRSTNAME LASTNAME\n";
			returnString += "--------- --------\n";
			while (rs.next()) {
				returnString += rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME") + "\n";
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
