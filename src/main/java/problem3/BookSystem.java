package problem3;
/*
Name:Kajal Gaikwad
Date :8-11-2024
Problem 3: Book Recommendations
Problem Statement: You have a list of Book objects with the following fields: title, author, genre, and rating. Write a method to perform the following operations:
Filter: Select books that are of genre "Science Fiction" with a rating greater than 4.0.
Transform: Create a new list of BookRecommendation objects containing title and rating.
Sort: Sort these books by their rating in descending order.
Paginate: Return the top 10 books as a paginated result, assuming each page shows 5 books.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookSystem {

    private static final String mysqlurl = "jdbc:mysql://localhost:3306/demo1";
    private static final String user = "root";
    private static final String pwd = "Kajal@2001";


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(mysqlurl, user, pwd)) {
            List<BookRecommendation> recommendations = getTopBookRecommendations(connection, "Science Fiction", 4.0, 10);
            displayPaginatedResults(recommendations, 5); // 5 books per page
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static List<BookRecommendation> getTopBookRecommendations(Connection connection, String genre, double minRating, int limit) throws SQLException {
        String query = "SELECT title, rating FROM Book WHERE genre = ? AND rating > ? ORDER BY rating DESC LIMIT ?";
        List<BookRecommendation> recommendations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, genre);
            statement.setDouble(2, minRating);
            statement.setInt(3, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    double rating = rs.getDouble("rating");
                    recommendations.add(new BookRecommendation(title, rating));
                }
            }
        }
        return recommendations;
    }

    private static void displayPaginatedResults(List<BookRecommendation> recommendations, int pageSize) {
        int totalPages = (int) Math.ceil((double) recommendations.size() / pageSize);
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("Page " + page);
            recommendations.stream()
                    .skip((page - 1) * pageSize)
                    .limit(pageSize)
                    .forEach(System.out::println);
            System.out.println("---- End of Page " + page + " ----");
        }
    }

    static class BookRecommendation {
        private final String title;
        private final double rating;

        public BookRecommendation(String title, double rating) {
            this.title = title;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return "Title: " + title + ", Rating: " + rating;
        }
    }
}

