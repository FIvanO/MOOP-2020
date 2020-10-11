import java.sql. *;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Manufacturer {
    private final Connection con; // з'єднання з БД
    private final Statement stmt; // оператор
    // Конструктор
    public Manufacturer(String DBName, String ip, int port)
            throws Exception {

        String url = "jdbc:mysql://" + ip + ":" + port + "/" +
                DBName + "?serverTimezone=Europe/Kiev&useSSL=FALSE&allowPublicKeyRetrieval=true";
        con = DriverManager.getConnection(url, "admin", "Password_1");
        stmt = con.createStatement();
    }

    // manufacturers list
    public void showManufacturers() {
        String sql = "SELECT ID, Name, FoundationDate FROM Manufacturer";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Manufacturers list:");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                Date foundation = rs.getDate("FoundationDate");
                System.out.println(">>" + id + " - " + name + " - " + foundation.toString());
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "ERROR while getting Manufacturer`s list");
            System.out.println(" >> " + e.getMessage());
        }
    }

    public String getManufacturerFoundationDate(int id) {
        String sql = "SELECT FoundationDate FROM Manufacturer WHERE ID = " + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Date foundation = rs.getDate("FoundationDate");
                return foundation.toString();
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(
                    "ERROR while getting Manufacturer foundation date");
            System.out.println(" >> " + e.getMessage());
        }

        return "1970-01-01";
    }

    // stop work
    public void stop() throws SQLException {
        con.close();
    }

    // add manufacturer
    public boolean addManufacturer(String name, Calendar foundation_date) {
        int month = foundation_date.get(Calendar.MONTH);
        String month_q;
        if (month < 10) {
            month_q = "0" + String.valueOf(month);
        } else {
            month_q = String.valueOf(month);
        }

        int day = foundation_date.get(Calendar.DAY_OF_MONTH);
        String day_q;
        if (day < 10) {
            day_q = "0" + String.valueOf(day);
        } else {
            day_q = String.valueOf(day);
        }

        String sql = "INSERT INTO Manufacturer (Name, FoundationDate) " +
                "VALUES ('" + name + "', '" + foundation_date.get(Calendar.YEAR) + '-' +
                month_q + '-' + day_q + "')";
        try {
            stmt.executeUpdate(sql);
            System.out.println("Manufacturer " + name + " added successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR! Manufacturer " + name + " not added!");
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public boolean updateManufacturer(int id, String name, String foundation_date) {
        String sql;
        if (name.equals("")) {
            sql = "UPDATE Manufacturer SET FoundationDate = '" + foundation_date + "' WHERE ID = " + id;
        } else {
            sql = "UPDATE Manufacturer SET Name = '" + name + "', FoundationDate = '" +
                    foundation_date + "' WHERE ID = " + id;
        }
        try {
            stmt.executeUpdate(sql);
            System.out.println("Manufacturer " + name + " updated successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR! Manufacturer " + name + " not updated!");
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    // delete Manufacturer
    public boolean deleteManufacturer(int id) {
        String sql = "DELETE FROM Manufacturer WHERE ID =" + id;
        try {
            int c = stmt.executeUpdate(sql);
            if (c > 0) {
                System.out.println("Manufacturer with id " + id + " deleted successfully!");
                return true;
            } else {
                System.out.println("Manufacturer with id " + id + " not found!");

                return false;
            }
        } catch (SQLException e) {
            System.out.println("ERROR while deleting Manufacturer with id " + id);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    // test scenario
    public static void main(String[] args) throws Exception {
        Manufacturer m = new Manufacturer("CarShowroom", "localhost", 3306);
        m.showManufacturers();
        System.out.println("");

        Scanner in = new Scanner(System.in);

        // add manufacturers
        {
            System.out.println("Do You want to add manufacturers (y/n)?");
            String add = in.nextLine();
            if (add.equals("y")) {
                Calendar calendar = new GregorianCalendar(1920, 1, 30);
                m.addManufacturer("Mazda", calendar);
                calendar = new GregorianCalendar(1997, 3, 18);
                m.addManufacturer("Cherry", calendar);

                m.showManufacturers();
            }
        }

        // update manufacturers
        {
            System.out.println("Do You want to update manufacturers (y/n)?");
            String s = in.nextLine();
            if (s.equals("y")) {
                System.out.println("How much manufacturers you want to update?");
                int count_to_update = in.nextInt();

                for (int i = 0; i < count_to_update; ++i) {
                    System.out.println("Enter manufacturer id: ");
                    int id = in.nextInt();

                    System.out.println("Enter manufacturer new name (0 - for do not change name): ");
                    String name = in.next();

                    if (name.equals("0")) {
                        name = new String();
                    }

                    String date;
                    int year, month, day;
                    System.out.println("Do You want to set new foundation date (y/n): ");
                    String q = in.next();
                    if (q.equals("y")) {
                        System.out.println("Enter new foundation year: ");
                        year = in.nextInt();

                        System.out.println("Enter new foundation month: ");
                        month = in.nextInt();

                        System.out.println("Enter new foundation day: ");
                        day = in.nextInt();

                        String month_q;
                        if (month < 10) {
                            month_q = "0" + String.valueOf(month);
                        } else {
                            month_q = String.valueOf(month);
                        }

                        String day_q;
                        if (day < 10) {
                            day_q = "0" + String.valueOf(day);
                        } else {
                            day_q = String.valueOf(day);
                        }

                        date = String.valueOf(year) + '-' + month_q + '-' + day_q;
                    } else {
                        date = m.getManufacturerFoundationDate(id);
                    }

                    m.updateManufacturer(id, name, date);
                }

                m.showManufacturers();
            }
        }

        // delete manufacturers
        {
            System.out.println("How much manufacturers you want to delete?");
            int count_to_delete = in.nextInt();

            if (count_to_delete != 0) {
                System.out.println("Which " + count_to_delete + " manufacturers you want to delete (id)?");
            }

            for (int i = 0; i < count_to_delete; ++i) {
                int man_id = in.nextInt();
                m.deleteManufacturer(man_id);
            }

            m.showManufacturers();
        }

        m.stop();
    }
}