package com.voting.servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Register the driver
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinevotings", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();
                out.println("<h3>Registration successful!</h3>");
                out.println("<a href='login.html'>Login here</a>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<h3>Driver not found: " + e.getMessage() + "</h3>");
        } catch (SQLException e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
