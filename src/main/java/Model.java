import java.sql. *;
import java.util.Scanner;

public class Model {
    private final Connection con; // з'єднання з БД
    private final Statement stmt; // оператор
    // Конструктор
    public Model(String DBName, String ip, int port)
            throws Exception {

        String url = "jdbc:mysql://" + ip + ":" + port + "/" +
                DBName + "?serverTimezone=Europe/Kiev&useSSL=FALSE";
        con = DriverManager.getConnection(url, "admin", "Password_1");
        stmt = con.createStatement();
    }

    // cars list
    public void showModels() {
        String sql = "SELECT ID, Name, ManufacturerID, ColorID, Year, EngineCapacity, Count FROM Model";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Cars list:");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                System.out.println(">>" + id + "-" + name);
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

    // test scenario
    public static void main(String[] args) throws Exception {
        Model m = new Model("CarShowroom", "localhost", 3306);
        m.showModels();
        m.addModel("EC8", 14, 4, 2020, 18, 1);
        m.addModel("i50", 3, 1, 2020, 12, 3);
        m.showModels();

        System.out.println("Which two models you want to delete?");

        Scanner in = new Scanner(System.in);

        int model_id = in.nextInt();
        m.deleteModel(model_id);

        model_id = in.nextInt();
        m.deleteModel(model_id);
        m.showModels();
        m.stop();
    }
}