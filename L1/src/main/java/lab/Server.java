import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) throws Exception {
        try {
            try {
                server = new ServerSocket(8080);

                System.out.println("Server is running!");

                while (true) {
                    clientSocket = server.accept();

                    try {
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        String word = in.readLine();
                        if (word.equals("exit")) {
                            break;
                        }

                        out.write(processClientMessage(word) + "\n");
                        out.flush();
                    } finally {
                        clientSocket.close();
                        in.close();
                        out.close();
                    }
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static String processClientMessage(String mes) throws Exception {
        Operation type;
        int index = 0;
        if (mes.indexOf("Add") == 0) {
            type = Operation.Add;
            index += "Add".length();
        } else if (mes.indexOf("Delete") == 0) {
            type = Operation.Delete;
            index += "Delete".length();
        } else if (mes.indexOf("Update") == 0) {
            type = Operation.Update;
            index += "Update".length();
        } else if (mes.indexOf("Calculate") == 0) {
            type = Operation.Calculate;
            index += "Calculate".length();
        } else if (mes.indexOf("Show") == 0) {
            type = Operation.Show;
            index += "Show".length();
        } else {
            type = Operation.Unknown;
        }
        ++ index; // space after operation type in client msg

        Object obj = Object.Unknown;
        ShowType show_type = ShowType.Unknown;
        switch (type) {
            case Add:
            case Update:
            case Delete:
                if (mes.indexOf("Model", index) == index) {
                    obj = Object.Model;
                    index += "Model".length();
                } else if (mes.indexOf("Manufacturer", index) == index) {
                    obj = Object.Manufacturer;
                    index += "Manufacturer".length();
                }
                ++ index; // space after object type in client msg
                break;
            case Calculate:
                break;
            case Show:
                if (mes.indexOf("ManufacturerList", index) == index) {
                    show_type = ShowType.ManufacturerList;
                    index += "ManufacturerList".length();
                } else if (mes.indexOf("ModelsWithManufacturer", index) == index) {
                    show_type = ShowType.ModelsWithManufacturer;
                    index += "ModelsWithManufacturer".length();
                } else if (mes.indexOf("ModelsByManufacturer", index) == index) {
                    show_type = ShowType.ModelsByManufacturer;
                    index += "ModelsByManufacturer".length();
                }
                ++ index; // space after show type
                break;
            case Unknown:
                return "Wrong operation type provided. Try again.";
        }

        if (obj == Object.Unknown && show_type == ShowType.Unknown) {
            return "Wrong client message. Try again";
        }

        return makeQuery(type, obj, show_type, mes.substring(index));
    }

    public static String makeQuery(Operation type, Object obj, ShowType show_type, String query) throws Exception {
        Model m = new Model("CarShowroom", "localhost", 3306);
        switch (obj) {
            case Model:
                switch (type) {
                    case Add:
                    {
                        // String name, int man_id, int col_id, int year, int eng_cap, int count
                        int index = 0;
                        String name = "";
                        // man_id, col_id, year, eng_cap, count;
                        Vector<Integer> vals = new Vector<>();

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                name = query.substring(0, ind);
                                index += ind;
                                ++ index;
                            }

                            try {
                                for (int i = 0; i < 5; ++i) {
                                    ind = query.indexOf(" ", index);
                                    if (ind != -1) {
                                        vals.add(Integer.parseInt(query.substring(index, ind)));
                                        index += (ind + 1);
                                    }
                                }
                            } catch (Exception e) {
                                return "Error while processing query";
                            }
                        }

                        boolean is_ok = m.addModel(name, vals.elementAt(0), vals.elementAt(1),
                                vals.elementAt(2), vals.elementAt(3), vals.elementAt(4));

                        if (is_ok) {
                            return "Model added successfully";
                        } else {
                            return "Error while adding model";
                        }
                    }
                    case Update:
                    {
                        // String name, int man_id, int col_id, int year, int eng_cap, int count
                        int index = 0;
                        String name = "";
                        // man_id, col_id, year, eng_cap, count;
                        Vector<Integer> vals = new Vector<>();

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                vals.add(Integer.parseInt(query.substring(0, ind)));
                                index += (ind + 1);
                            }

                            ind = query.indexOf(" ");
                            if (ind != -1) {
                                name = query.substring(0, ind);
                                index += (ind + 1);
                            }

                            try {
                                for (int i = 0; i < 5; ++i) {
                                    ind = query.indexOf(" ", index);
                                    if (ind != -1) {
                                        vals.add(Integer.parseInt(query.substring(index, ind)));
                                        index += (ind + 1);
                                    }
                                }
                            } catch (Exception e) {
                                return "Error while processing query";
                            }
                        }

                        boolean is_ok = m.updateModel(vals.elementAt(0), name, vals.elementAt(1), vals.elementAt(2),
                                vals.elementAt(3), vals.elementAt(4), vals.elementAt(5));

                        if (is_ok) {
                            return "Model updated successfully";
                        } else {
                            return "Error while updating model";
                        }
                    }
                    case Delete: {
                        int index = 0;
                        int id = 0;

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                id = Integer.parseInt(query.substring(0, ind));
                            }
                        }

                        boolean is_ok = m.deleteModel(id);

                        if (is_ok) {
                            return "Model deleted succsessfully";
                        } else {
                            return "Error while deleting model";
                        }
                    }
                }
            case Manufacturer:
            case Unknown:
                switch (type) {
                    case Show:
                        switch (show_type) {
                            case ManufacturerList:
                            case ModelsByManufacturer:
                            case ModelsWithManufacturer:
                                break;
                        }
                    case Calculate:
                        return m.countModelsByManufacturer();
                }
        }

        return new String();
    }

    public static enum Operation {
        Add,
        Delete,
        Update,
        Calculate,
        Show,
        Unknown
    };

    public static enum Object {
        Model,
        Manufacturer,
        Unknown
    };

    public static enum ShowType {
        ManufacturerList,
        ModelsWithManufacturer,
        ModelsByManufacturer,
        Unknown
    }
}