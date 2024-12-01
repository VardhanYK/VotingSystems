package com.voting.servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ResultsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h1>Election Results</h1>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Register the driver
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinevotings", "root", "");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM candidates")) {

                while (rs.next()) {
                    out.println("<p>" + rs.getString("name") + ": " + rs.getInt("votes") + " votes</p>");
                }
            }
        } catch (ClassNotFoundException e) {
            out.println("<h3>Driver not found: " + e.getMessage() + "</h3>");
        } catch (SQLException e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
