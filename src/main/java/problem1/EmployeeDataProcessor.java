package problem1;
/*
Name:Kajal Gaikwad
Date :7-11-2024
 Problem 1: Employee Data Processing
Problem Statement: You have a list of Employee objects with the following fields: id, name, department, and salary. Write a method to perform the following operations:
Filter: Select employees who belong to the "Engineering" department and have a salary greater than $80,000.
Sort: Sort these employees by their salary in descending order.
Group: Group the resulting employees by their department.
Aggregate: For each department, calculate the average salary of employees
package problem1;
 */
import java.sql.*;
import java.util.*;

public class EmployeeDataProcessor {

    private static final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
    private static final String user = "root";
    private static final String pwd = "Kajal@2001";


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(mysqlurl, user, pwd)) {
            List<Employee> employees = getEmployees(connection);

            Map<String, List<Employee>> groupedEmployees = groupEmployeesByDepartment(employees);
            calculateAvgSalary(groupedEmployees);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> getEmployees(Connection connection) throws SQLException {
        String query = "SELECT * FROM Employee WHERE department = 'Engineering' AND salary > 80000 ORDER BY salary DESC";
        List<Employee> employees = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");

                employees.add(new Employee(id, name, department, salary));
            }
        }
        return employees;
    }
    private static Map<String, List<Employee>> groupEmployeesByDepartment(List<Employee> employees) {
        Map<String, List<Employee>> groupedByDepartment = new HashMap<>();
        for (Employee employee : employees) {
            groupedByDepartment
                    .computeIfAbsent(employee.getDepartment(), k -> new ArrayList<>())
                    .add(employee);
        }
        return groupedByDepartment;
    }

    private static void calculateAvgSalary(Map<String, List<Employee>> groupedEmployees) {
        for (Map.Entry<String, List<Employee>> entry : groupedEmployees.entrySet()) {
            String department = entry.getKey();
            List<Employee> employees = entry.getValue();

            double averageSalary = employees.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0.0);

            System.out.println("Department: " + department + ", Average Salary: $" + averageSalary);
        }
    }

    static class Employee {
        private final int id;
        private final String name;
        private final String department;
        private final double salary;

        public Employee(int id, String name, String department, double salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }
    }
}
