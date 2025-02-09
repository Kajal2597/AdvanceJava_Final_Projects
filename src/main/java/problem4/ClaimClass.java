package problem4;
import java.sql.*;
/*
Problem 4: Claims Analysis
Problem Statement: You have a list of Claim objects with the following fields: claimId, policyNumber, claimAmount, claimDate, and status. Write a method to perform the following operations:
Filter: Select claims that are in the "Approved" status and have a claimAmount greater than $5,000.
Group: Group these claims by policyNumber.
Aggregate: For each policy, calculate the total claimAmount and the average claimAmount.
Top N: Identify the top 3 policies with the highest total claim amounts.
 */

public class ClaimClass {

        public static void main(String[] args) {
            final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
         final String user = "root";
            final String pwd = "Kajal@2001";

            String query = "SELECT policyNumber, SUM(claimAmount) AS totalClaim, AVG(claimAmount) AS averageClaim "
                    + "FROM Claims "
                    + "WHERE status = 'Approved' "
                    + "AND claimAmount > 5000 "
                    + "group BY policyNumber "
                    + "order by totalClaim DESC "
                    + "LIMIT 3;";

            try (Connection conn = DriverManager.getConnection(mysqlurl, user, pwd);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String policyNumber = rs.getString("policyNumber");
                    double totalClaim = rs.getDouble("totalClaim");
                    double averageClaim = rs.getDouble("averageClaim");

                    System.out.println("Policy Number: " + policyNumber);
                    System.out.println("Total Claim Amount: $" + totalClaim);
                    System.out.println("Average Claim Amount: $" + averageClaim);
                    System.out.println("----------******---------------------");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

