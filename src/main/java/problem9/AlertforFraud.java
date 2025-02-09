package problem9;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertforFraud {


        private int policyId;
        private int policyHolderId;
        private int highValueTransactionCount;
        private double totalHighValueAmount;

        public AlertforFraud(int policyId, int policyHolderId, int highValueTransactionCount, double totalHighValueAmount) {
            this.policyId = policyId;
            this.policyHolderId = policyHolderId;
            this.highValueTransactionCount = highValueTransactionCount;
            this.totalHighValueAmount = totalHighValueAmount;
        }

        @Override
        public String toString() {
            return "Policy ID: " + policyId + ", Policy Holder ID: " + policyHolderId
                    + ", High-Value Transaction Count: " + highValueTransactionCount
                    + ", Total High-Value Amount: $" + totalHighValueAmount;
        }
    }

     class FraudDetection {
        public static void main(String[] args) {
            final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
            final String user = "root";
            final String pwd = "Kajal@2001";

            String query = "SELECT t.customerId, p.policyHolderId, COUNT(t.customerId) AS highValueTransactionCount, "
                    + "SUM(t.transactionAmount) AS totalHighValueAmount "
                    + "FROM CustomerTransaction t "
                    + "JOIN Policy p ON t.customerId = p.policyId "
                    + "WHERE t.transactionAmount > 100 "
                    + "AND t.transactionDate >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) "
                    + "GROUP BY t.customerId, p.policyHolderId "
                    + "HAVING highValueTransactionCount > 3 AND totalHighValueAmount > 50000;";

            List<AlertforFraud> fraudAlerts = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(mysqlurl, user, pwd);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Process the result set
                while (rs.next()) {
                    int policyId = rs.getInt("policyId");
                    int policyHolderId = rs.getInt("policyHolderId");
                    int highValueTransactionCount = rs.getInt("highValueTransactionCount");
                    double totalHighValueAmount = rs.getDouble("totalHighValueAmount");

                    AlertforFraud alert = new AlertforFraud(policyId, policyHolderId, highValueTransactionCount, totalHighValueAmount);
                    fraudAlerts.add(alert);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Print flagged policies for potential fraud
            for (AlertforFraud alert : fraudAlerts) {
                System.out.println(alert);
            }
        }
    }


