package com.voting.servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class AdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        int id = 0;

        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Register the driver
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineVotings", "root", "")) {
                
                if ("add".equalsIgnoreCase(action)) {
                    // Add candidate
                    try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO candidates (name) VALUES (?)")) {
                        stmt.setString(1, name);
                        stmt.executeUpdate();
                        out.println("<h3>Candidate added successfully!</h3>");
                    }
                } else if ("delete".equalsIgnoreCase(action)) {
                    // Delete candidate
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM candidates WHERE id=?")) {
                        stmt.setInt(1, id);
                        stmt.executeUpdate();
                        out.println("<h3>Candidate deleted successfully!</h3>");
                    }
                } else if ("view".equalsIgnoreCase(action)) {
                    // View candidates
                    try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM candidates");
                         ResultSet rs = stmt.executeQuery()) {

                        out.println("<h3>All Candidates:</h3>");
                        while (rs.next()) {
                            out.println("<p>ID: " + rs.getInt("id") + " - Name: " + rs.getString("name") + " - Votes: " + rs.getInt("votes") + "</p>");
                        }
                    }
                } else {
                    out.println("<h3>Invalid action!</h3>");
                }
            }
        } catch (ClassNotFoundException e) {
            out.println("<h3>Driver not found: " + e.getMessage() + "</h3>");
        } catch (SQLException e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // Handle GET requests the same way as POST
    }
}
