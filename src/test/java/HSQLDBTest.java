import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class HSQLDBTest {

    private static IDatabaseTester databaseTester;
    private static SalaryCalculation salCal;

    @BeforeClass
    public static void init() throws Exception {
        databaseTester = new JdbcDatabaseTester(org.hsqldb.jdbcDriver.class.getName(), "jdbc:hsqldb:mem:test", "sa", "");
        createTablesSinceDbUnitDoesNot(databaseTester.getConnection().getConnection());
        String inputXml = "<dataset>" + "    <SCHOOL EMPID=\"54601B\" "
                + "       SALARY=\"25000\""
                + "       BONUS=\"5000\""
                + "       INCREMENT=\"0\""
                + "       SUBJECT=\"mathametics\"/>"
                + "</dataset>";
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new StringReader(inputXml));
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();
        salCal = new SalaryCalculation();
        salCal.setConnection(databaseTester.getConnection().getConnection());
    }

    private static void createTablesSinceDbUnitDoesNot(Connection connection)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE SCHOOL"+
                "(EMPID VARCHAR(20),SALARY VARCHAR(10), BONUS VARCHAR(20),INCREMENT VARCHAR(10),"+
                "SUBJECT VARCHAR(30))");
        statement.execute();
        statement.close();
    }

    @Test
    public void testCalculator() throws SQLException{
        assertEquals(30000,salCal.calculator("54601B"));
    }


    @Test
    public void testCalculatorInvalidEmpID() throws SQLException{
        assertEquals(0,salCal.calculator("54501A"));
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        salCal=null;
        databaseTester.onTearDown();
        databaseTester = null;
    }
}
