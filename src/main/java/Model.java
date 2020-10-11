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

    // test scenario
    public static void main(String[] args) throws Exception {
        Model m = new Model("CarShowroom", "localhost", 3306);
        m.showModels();
        System.out.println("");

        Scanner in = new Scanner(System.in);

        // add models
        {
            System.out.println("Do You want to add models (y/n)?");
            String add = in.nextLine();

            if (add.equals("y")) {
                m.addModel("EC8", 14, 4, 2020, 18, 1);
                m.addModel("i50", 3, 1, 2020, 12, 3);

                m.showModels();
                System.out.println("");

            }
        }

        // update models
        {
            System.out.println("Do You want to update models (y/n)?");
            String s = in.nextLine();
            if (s.equals("y")) {
                System.out.println("How many models you want to update?");
                int count_to_update = in.nextInt();

                for (int i = 0; i < count_to_update; ++i) {
                    System.out.println("Enter model id: ");
                    int id = in.nextInt();

                    System.out.println("Enter model`s new name (0 - for do not change name): ");
                    String name = in.next();

                    if (name.equals("0")) {
                        name = new String();
                    }

                    int manufacturer_id = 0, color_id = 0, year = 0, engine_capacity = 0, count = 0;

                    System.out.println("Enter new ManufacturerID (0 - for do not change): ");
                    manufacturer_id = in.nextInt();

                    System.out.println("Enter new ColorID (0 - for do not change): ");
                    color_id = in.nextInt();

                    System.out.println("Enter new Year (0 - for do not change): ");
                    year = in.nextInt();

                    System.out.println("Enter new EngineCapacity (0 - for do not change): ");
                    engine_capacity = in.nextInt();

                    System.out.println("Enter new Count ('-1' - for do not change): ");
                    count = in.nextInt();

                    m.updateModel(id, name, manufacturer_id, color_id, year, engine_capacity, count);
                }

                m.showModels();
                System.out.println("");
            }
        }

        // delete models
        {
            System.out.println("How many models you want to delete?");
            int count_to_delete = in.nextInt();

            if (count_to_delete != 0) {
                System.out.println("Which " + count_to_delete + " models you want to delete (id)?");
            }

            for (int i = 0; i < count_to_delete; ++i) {
                int model_id = in.nextInt();
                m.deleteModel(model_id);
            }
        }

        m.showModels();
        m.stop();
    }
}