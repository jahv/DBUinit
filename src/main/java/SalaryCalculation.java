import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SalaryCalculation {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int calculator(String EmpID) throws SQLException{
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery("select * from SCHOOL where empid='"+EmpID+"'");
        int salary = 0;
        int bonus = 0;
        int increment = 0;
        while (rs.next()) {
            salary = rs.getInt("Salary");
            bonus = rs.getInt("Bonus");
            increment = rs.getInt("Increment");
        }
        return (salary+bonus+increment);
    }
}
