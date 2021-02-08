package org.example;

import java.sql.*;
import java.util.Scanner;
public class App 
{
    static final String DB_CONNECTION="jdbc:mysql://localhost:3306/myfirstdb1?serverTimezone=Europe/Kiev";
    static final String DB_USER="root";
    static final String DB_PASSWORD="ghbdtn_max_12";

    static Connection conn;

    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();
                while (true) {
                    System.out.println("1: add flat");
                    System.out.println("2: delete flat");
                    System.out.println("3: change flat");
                    System.out.println("4: view flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlat(sc);
                            break;
                        case "2":
                            deleteFlat(sc);
                            break;
                        case "3":
                            changeFlat(sc);
                            break;
                        case "4":
                            System.out.println("Введите параметр с которым хотитие сделать выборку: ");
                            System.out.print("-> ");
                            String param=sc.nextLine();
                            if (param.equals("id") || param.equals("district") || param.equals("area") ||
                                    param.equals("address") ||param.equals("rooms") || param.equals("price") || param.equals("*")){
                            viewClients(param);
                            break;
                            } else {
                                System.out.println("Такого параметра нет");
                                break;
                            }
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Flats");
            st.execute("CREATE TABLE Flats (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, district VARCHAR(20) NOT NULL ,address VARCHAR(20) " +
                    "DEFAULT NULL, area DOUBLE NOT NULL, rooms INT NOT NULL,price INT NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void addFlat(Scanner sc) throws SQLException {
        System.out.print("Enter district: ");
        String district = sc.nextLine();

        System.out.print("Enter area: ");
        String sArea = sc.nextLine();
        double area = Double.parseDouble(sArea);

        System.out.print("Enter rooms: ");
        String sRooms = sc.nextLine();
        int rooms = Integer.parseInt(sRooms);

        System.out.print("Enter price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Flats (district,area,rooms,price) VALUES(?, ?,?,?)");
        try {
            ps.setString(1, district);
            ps.setDouble(2, area);
            ps.setInt(3, rooms);
            ps.setInt(4, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void deleteFlat(Scanner sc) throws SQLException {
        System.out.print("Enter id: ");
        String sId = sc.nextLine();
        int id=Integer.parseInt(sId);

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Flats WHERE id = ?");
        try {
            ps.setInt(1, id);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void changeFlat(Scanner sc) throws SQLException {
        System.out.print("Enter client id: ");
        String sId = sc.nextLine();
        int id=Integer.parseInt(sId);
        System.out.print("Enter new price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE Flatts SET price = ? " +
                        "WHERE id = ?");
        try {
            ps.setInt(1, price);
            ps.setInt(2, id);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void viewClients(String param) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT " + param + " FROM Flats");
        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
}

