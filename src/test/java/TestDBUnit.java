
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBUnit extends DatabaseTestCase {

    public static final String TABLE_LOGIN = "message_store";
    private FlatXmlDataSet loadedDataSet;

    // Provide a connection to the database
    protected IDatabaseConnection getConnection() throws Exception{
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
//        return new DatabaseConnection(jdbcConnection);

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection jdbcConnection = DriverManager.getConnection("jdbc:sqlserver://int-mssql01.int.skyad.io:1433;databaseName=SKYHOOK", "dog", "fluffy");
        return new DatabaseConnection(jdbcConnection);
    }

    // Load the data which will be inserted for the test
    protected IDataSet getDataSet() throws Exception{
        loadedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResourceAsStream("input.xml"));
        return loadedDataSet;
    }

    // Check that the data has been loaded.
    public void testCheckLoginDataLoaded() throws Exception{
        assertNotNull(loadedDataSet);
        int rowCount = loadedDataSet.getTable(TABLE_LOGIN).getRowCount();
        assertEquals(2, rowCount);
    }
}
