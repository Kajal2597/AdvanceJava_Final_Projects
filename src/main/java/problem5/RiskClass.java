package problem5;
/*
Problem 5: Policy Holder Risk Assessment
Problem Statement: You have a list of PolicyHolder objects with the following fields: holderId, name, age, policyType, and premiumAmount. Write a method to perform the following operations:
Filter: Select policyholders with a policyType of "Life" and age greater than 60.
Transform: Create a new list of RiskAssessment objects containing holderId, name, and a risk score calculated as premiumAmount / age.
Sort: Sort these risk assessments by the risk score in descending order.
Categorize: Group these policy holders into risk categories: "High Risk" for risk scores above 0.5 and "Low Risk" otherwise.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiskClass {
    private int holderId;
        private String name;
        private double riskScore;
        private String riskCategory;

        public RiskClass(int holderId, String name, double riskScore, String riskCategory) {
            this.holderId = holderId;
            this.name = name;
            this.riskScore = riskScore;
            this.riskCategory = riskCategory;
        }

        @Override
        public String toString() {
            return "Holder ID: " + holderId + ", Name: " + name + ", Risk Score: " + riskScore + ", Risk Category: " + riskCategory;
        }
    }

     class PolicyHolderRiskAssessment {
        public static void main(String[] args) {
            final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
           final String user = "root";
           final String pwd = "Kajal@2001";

            String query = "SELECT holderId, name, premiumAmount / age AS riskScore, "
                    + "CASE WHEN (premiumAmount / age) > 0.5 THEN 'High Risk' ELSE 'Low Risk' END AS riskCategory "
                    + "FROM PolicyHolder "
                    + "WHERE policyType = 'Life' AND age > 30 "
                    + "ORDER BY riskScore DESC;";

            List<RiskClass> riskAssessments = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(mysqlurl, user, pwd);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int holderId = rs.getInt("holderId");
                    String name = rs.getString("name");
                    double riskScore = rs.getDouble("riskScore");
                    String riskCategory = rs.getString("riskCategory");

                    RiskClass assessment = new RiskClass(holderId, name, riskScore, riskCategory);
                    riskAssessments.add(assessment);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (RiskClass assessment : riskAssessments) {
                System.out.println(assessment);
            }
        }
    }


