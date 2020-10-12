import java.sql. *;

public class Model {
    private final Connection con; // connection to db
    private final Statement stmt; // operator

    // constructor
    public Model(String DBName, String ip, int port)
            throws Exception {

        String url = "jdbc:mysql://" + ip + ":" + port + "/" +
                DBName + "?serverTimezone=Europe/Kiev&useSSL=FALSE";
        con = DriverManager.getConnection(url, "admin", "Password_1");
        stmt = con.createStatement();
    }

    // models list
    public void showModels() {
        String sql = "SELECT ID, Name, ManufacturerID, ColorID, Year, EngineCapacity, Count FROM Model";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Cars list:");
            System.out.println("ID - Name - ManufacturerID - ColorID - Year - EngineCapacity - Count");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                int man_id = rs.getInt("ManufacturerID");
                int col_id = rs.getInt("ColorID");
                int year = rs.getInt("Year");
                int eng_cap = rs.getInt("EngineCapacity");
                int count = rs.getInt("Count");
                System.out.println(">>" + id + " - " + name + " - " + man_id + " - " + col_id + " - " + year +
                        " - " + eng_cap + " - " + count);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "ERROR while getting auto's list");
            System.out.println(" >> " + e.getMessage());
        }
    }

    // models by manufacturer ID
    public void findModelsByManID(int manufacturer_id) {
        String sql = "SELECT ID, Name, ManufacturerID, ColorID, Year, EngineCapacity, Count FROM Model " +
                "WHERE ManufacturerID = " + manufacturer_id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Cars list:");
            System.out.println("ID - Name - ManufacturerID - ColorID - Year - EngineCapacity - Count");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                int man_id = rs.getInt("ManufacturerID");
                int col_id = rs.getInt("ColorID");
                int year = rs.getInt("Year");
                int eng_cap = rs.getInt("EngineCapacity");
                int count = rs.getInt("Count");
                System.out.println(">>" + id + " - " + name + " - " + man_id + " - " + col_id + " - " + year +
                        " - " + eng_cap + " - " + count);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "ERROR while getting auto's list");
            System.out.println(" >> " + e.getMessage());
        }
    }

    // stop work
    public void stop() throws SQLException {
        con.close();
    }

    // add model
    public boolean addModel(String name, int man_id, int col_id, int year, int eng_cap, int count) {
        String sql = "INSERT INTO Model (Name, ManufacturerID, ColorID, Year, EngineCapacity, Count) " +
                "VALUES ('" + name + "', " + man_id + ", " + col_id + ", "
                + year  + ", " + eng_cap  + ", " + count + ")";
        try {
            stmt.executeUpdate(sql);
            System.out.println("Model " + name + " added successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR! Model " + name + " not added!");
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    // update model
    public boolean updateModel(int id, String name, int man_id, int col_id, int year, int eng_cap, int count) {
        String sql = "UPDATE Model SET ID = " + id;
        if (!name.equals("")) {
            sql += ", Name = '" + name + "'";
        }

        if (man_id != 0) {
            sql += ", ManufacturerID = " + man_id;
        }

        if (col_id != 0) {
            sql += ", ColorID = " + col_id;
        }

        if (year > 0) {
            sql += ", Year = " + year;
        }

        if (eng_cap > 0) {
            sql += ", EngineCapacity = " + eng_cap;
        }

        if (count >= 0) {
            sql += ", Count = " + count;
        }
        sql += " WHERE ID = " + id;

        try {
            stmt.executeUpdate(sql);
            System.out.println("Model " + name + " added successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR! Model " + name + " not added!");
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    // delete model
    public boolean deleteModel(int id) {
        String sql = "DELETE FROM Model WHERE ID =" + id;
        try {
            int c = stmt.executeUpdate(sql);
            if (c > 0) {
                System.out.println("Model with id " + id + " deleted successfully!");
                return true;
            } else {
                System.out.println("Model with id " + id + " not found!");

                return false;
            }
        } catch (SQLException e) {
            System.out.println("ERROR while deleting model with id " + id);
                    System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
    }
}