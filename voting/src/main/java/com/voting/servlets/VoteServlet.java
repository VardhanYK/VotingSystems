package com.voting.servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class VoteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinevotings", "root", "");

            // Fetch candidates
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM candidates");
            ResultSet rs = stmt.executeQuery();

            // Generate voting form
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Vote</title></head><body>");
            out.println("<h1>Vote for a Candidate</h1>");
            out.println("<form action='VoteServlet' method='post'>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                out.println("<input type='radio' name='candidateId' value='" + id + "' required> " + name + "<br>");
            }

            out.println("<button type='submit'>Submit Vote</button>");
            out.println("</form>");
            out.println("</body></html>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        String candidateId = request.getParameter("candidateId");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (username == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinevotings", "root", "");

            // Check if the user has already voted
            PreparedStatement checkStmt = conn.prepareStatement("SELECT voted FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getBoolean("voted")) {
                out.println("<h3>You have already voted!</h3>");
                return;
            }

            // Record the vote
            PreparedStatement voteStmt = conn.prepareStatement("UPDATE candidates SET votes = votes + 1 WHERE id = ?");
            voteStmt.setInt(1, Integer.parseInt(candidateId));
            voteStmt.executeUpdate();

            // Update the user's voted status
            PreparedStatement updateUserStmt = conn.prepareStatement("UPDATE users SET voted = TRUE WHERE username = ?");
            updateUserStmt.setString(1, username);
            updateUserStmt.executeUpdate();

            out.println("<h3>Thank you for voting!</h3>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
