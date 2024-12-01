package com.voting.servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Register the driver
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinevotings", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {

                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    response.sendRedirect("vote.html");
                } else {
                    out.println("<h3>Invalid credentials.</h3>");
                    out.println("<a href='login.html'>Try Again</a>");
                }
            }
        } catch (ClassNotFoundException e) {
            out.println("<h3>Driver not found: " + e.getMessage() + "</h3>");
        } catch (SQLException e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
