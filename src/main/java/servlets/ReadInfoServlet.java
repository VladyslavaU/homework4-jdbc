package servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/readInfo")
public class ReadInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PreparedStatement statement;
    private Connection connection;

    public void init(ServletConfig config) {
        try {
            ServletContext context = config.getServletContext();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    context.getInitParameter("dbUrl"),
                    context.getInitParameter("dbUser"),
                    context.getInitParameter("dbPassword"));
            statement = connection.prepareStatement("select * from info");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ResultSet resultSet = statement.executeQuery("select * from info");
            PrintWriter out = response.getWriter();
            out.print("<table>");
            out.print("<tr>");
            out.print("<th>");
            out.println("Id");
            out.print("</th>");
            out.print("<th>");
            out.println("Position");
            out.print("</th>");
            out.print("<th>");
            out.println("Company");
            out.print("</th>");
            out.print("<th>");
            out.println("Contact id");
            out.print("</th>");
            out.print("</tr>");
            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>");
                out.print(resultSet.getString(1));
                out.println("</td>");
                out.println("<td>");
                out.print(resultSet.getString(2));
                out.println("</td>");
                out.println("<td>");
                out.print(resultSet.getString(3));
                out.println("</td>");
                out.println("<td>");
                out.print(resultSet.getString(4));
                out.println("</td>");
                out.println("</tr>");
            }
            out.print("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
