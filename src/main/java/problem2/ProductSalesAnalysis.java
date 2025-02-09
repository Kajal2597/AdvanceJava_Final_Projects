package problem2;
/*
Name:Kajal Gaikwad
Date :7-11-2024
Problem 2: Product Sales Analysis
Problem Statement: You have a list of Sale objects with the following fields: productId, quantity, and price.
Write a method to perform the following operations:
Filter: Select sales where the quantity sold is greater than 10.
Transform: Create a new list of ProductSales objects containing productId and the total revenue (quantity * price) for each product.
Sort: Sort the products by total revenue in descending order.
Top N: Retrieve the top 5 products by total revenue.

 */
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProductSalesAnalysis {

    private static final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
    private static final String user = "root";
    private static final String pwd = "Kajal@2001";


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(mysqlurl, user, pwd)) {
            List<ProductSales> topProducts = getTopSales(connection, 5);
            topProducts.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<ProductSales> getTopSales(Connection connection, int topN) throws SQLException {
        String query = "SELECT productId, quantity, price FROM Sale WHERE quantity > 10";
        List<ProductSales> productSalesList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                int productId = rs.getInt("productId");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double AllRevenue = quantity * price;

                productSalesList.add(new ProductSales(productId, AllRevenue));
            }
        }

        // Sort for find top N products
        return productSalesList.stream()
                .sorted(Comparator.comparing(ProductSales::getAllRevenue).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    static class ProductSales {
        private final int productId;
        private final double AllRevenue;

        public ProductSales(int productId, double AllRevenue) {
            this.productId = productId;
            this.AllRevenue = AllRevenue;
        }

        public int getProductId() { return productId; }
        public double getAllRevenue() { return AllRevenue; }

        @Override
        public String toString() {
            return "Product ID: " + productId + ", Total Revenue: $" + AllRevenue;
        }
    }
}

