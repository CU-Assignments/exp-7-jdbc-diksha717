import java.sql.*;
import java.util.Scanner;

public class medium{
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";
    static Connection conn;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);
            Scanner sc = new Scanner(System.in);
            int choice;
            do {
                System.out.println("\n1. Add Product\n2. View Products\n3. Update Product\n4. Delete Product\n5. Exit");
                choice = sc.nextInt();
                switch (choice) {
                    case 1 -> addProduct(sc);
                    case 2 -> viewProducts();
                    case 3 -> updateProduct(sc);
                    case 4 -> deleteProduct(sc);
                    case 5 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice.");
                }
            } while (choice != 5);
            sc.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addProduct(Scanner sc) throws SQLException {
        System.out.println("Enter ProductID, ProductName, Price, Quantity:");
        int id = sc.nextInt();
        String name = sc.next();
        double price = sc.nextDouble();
        int quantity = sc.nextInt();

        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Product added successfully.");
        }
    }

    static void viewProducts() throws SQLException {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ProductID\tName\tPrice\tQuantity");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + "\t" + rs.getString("ProductName") + "\t" + rs.getDouble("Price") + "\t" + rs.getInt("Quantity"));
            }
        }
    }

    static void updateProduct(Scanner sc) throws SQLException {
        System.out.println("Enter ProductID to update, new Price and new Quantity:");
        int id = sc.nextInt();
        double price = sc.nextDouble();
        int quantity = sc.nextInt();

        String sql = "UPDATE Product SET Price=?, Quantity=? WHERE ProductID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, price);
            ps.setInt(2, quantity);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            conn.commit();
            System.out.println(rows > 0 ? "Product updated." : "Product not found.");
        }
    }

    static void deleteProduct(Scanner sc) throws SQLException {
        System.out.println("Enter ProductID to delete:");
        int id = sc.nextInt();

        String sql = "DELETE FROM Product WHERE ProductID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            conn.commit();
            System.out.println(rows > 0 ? "Product deleted." : "Product not found.");
        }
    }
}
