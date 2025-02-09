package problem8;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CustomerValueClass {


        private int customerId;
        private double totalTransactionAmount;
        private double averageTransactionAmount;

        public CustomerValueClass(int customerId, double totalTransactionAmount, double averageTransactionAmount) {
            this.customerId = customerId;
            this.totalTransactionAmount = totalTransactionAmount;
            this.averageTransactionAmount = averageTransactionAmount;
        }

        @Override
        public String toString() {
            return "Customer ID: " + customerId + ", Total Transaction Amount: $" + totalTransactionAmount
                    + ", Average Transaction Amount: $" + averageTransactionAmount;
        }
    }

     class CustomerLifetimeValueCalculator {
        public static void main(String[] args) {
            final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
            final String user = "root";
            final String pwd = "Kajal@2001";

            String query = "SELECT customerId, SUM(transactionAmount) AS totalTransactionAmount, "
                    + "AVG(transactionAmount) AS averageTransactionAmount "
                    + "FROM CustomerTransaction "
                    + "WHERE transactionDate >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) "
                    + "GROUP BY customerId "
                    + "ORDER BY totalTransactionAmount DESC "
                    + "LIMIT 10;";

            List<CustomerValueClass> topCustomers = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(mysqlurl, user, pwd);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Process the result set
                while (rs.next()) {
                    int customerId = rs.getInt("customerId");
                    double totalTransactionAmount = rs.getDouble("totalTransactionAmount");
                    double averageTransactionAmount = rs.getDouble("averageTransactionAmount");

                    CustomerValueClass clv = new CustomerValueClass(customerId, totalTransactionAmount, averageTransactionAmount);
                    topCustomers.add(clv);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Print the top 10 customers by lifetime value
            for (CustomerValueClass clv : topCustomers) {
                System.out.println(clv);
            }
        }
    }


