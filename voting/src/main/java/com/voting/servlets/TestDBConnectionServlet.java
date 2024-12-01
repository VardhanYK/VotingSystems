package com.voting.servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class TestDBConnectionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (
        		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinevotings", "root", "")) {
            response.getWriter().println("Database connection successful!");
        } catch (SQLException e) {
            response.getWriter().println("Database connection failed: " + e.getMessage());
        }
    }
}
