package problem7;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RenewalReminder {
    private int policyId;
    private int policyHolderId;
    private int remainingDays;

    public RenewalReminder(int policyId, int policyHolderId, int remainingDays) {
        this.policyId = policyId;
        this.policyHolderId = policyHolderId;
        this.remainingDays = remainingDays;
    }

    @Override
    public String toString() {
        return "Policy ID: " + policyId + ", Remaining Days: " + remainingDays;
    }

    public int getPolicyHolderId() {
        return policyHolderId;
    }
}

 class PolicyRenewalAnalysis {
    public static void main(String[] args) {
        final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
        final String user = "root";
        final String pwd = "Kajal@2001";

        String query = "SELECT policyId, policyHolderId, DATEDIFF(expiryDate, CURDATE()) AS remainingDays "
                + "FROM Policy "
                + "WHERE status = 'Active' "
                + "AND expiryDate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) "
                + "ORDER BY remainingDays ASC;";

        Map<Integer, List<RenewalReminder>> renewalReminders = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(mysqlurl, user, pwd);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Process the result set
            while (rs.next()) {
                int policyId = rs.getInt("policyId");
                int policyHolderId = rs.getInt("policyHolderId");
                int remainingDays = rs.getInt("remainingDays");

                RenewalReminder reminder = new RenewalReminder(policyId, policyHolderId, remainingDays);

                // Group by policyHolderId
                renewalReminders.computeIfAbsent(policyHolderId, k -> new ArrayList<>()).add(reminder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the grouped reminders
        for (Map.Entry<Integer, List<RenewalReminder>> entry : renewalReminders.entrySet()) {
            System.out.println("Policy Holder ID: " + entry.getKey());
            for (RenewalReminder reminder : entry.getValue()) {
                System.out.println(reminder);
            }
            System.out.println("-------------------------------");
        }
    }
}
