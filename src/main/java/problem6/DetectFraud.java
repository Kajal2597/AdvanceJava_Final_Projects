package problem6;
import java.sql.*;

public class DetectFraud {

        public static void main(String[] args) {
            final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
            final String user = "root";
            final String pwd = "Kajal@2001";

            String query = "SELECT policyNumber, COUNT(transactionId) AS fraudTransactionCount, SUM(amount) AS totalFraudAmount "
                    + "FROM Transactions "
                    + "WHERE isFraudulent = TRUE AND amount > 10000 "
                    + "GROUP BY policyNumber "
                    + "HAVING COUNT(transactionId) > 5 OR SUM(amount) > 50000;";

            try (Connection conn = DriverManager.getConnection(mysqlurl, user, pwd);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Generate alerts for policies meeting the fraud criteria
                while (rs.next()) {
                    String policyNumber = rs.getString("policyNumber");
                    int fraudTransactionCount = rs.getInt("fraudTransactionCount");
                    double totalFraudAmount = rs.getDouble("totalFraudAmount");

                    System.out.println("ALERT: Policy Number: " + policyNumber);
                    System.out.println("Fraudulent Transaction Count: " + fraudTransactionCount);
                    System.out.println("Total Fraud Amount: $" + totalFraudAmount);
                    System.out.println("-------------------------------");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


