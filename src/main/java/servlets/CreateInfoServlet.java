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
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(urlPatterns = "/addInfoServlet")
public class CreateInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;
    PreparedStatement statement;

    public void init(ServletConfig config) {
        try {
            ServletContext context = config.getServletContext();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    context.getInitParameter("dbUrl"),
                    context.getInitParameter("dbUser"),
                    context.getInitParameter("dbPassword"));
            statement = connection.prepareStatement("insert into info(position,company,contact_id) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String position = request.getParameter("position");
        String company = request.getParameter("company");
        int contactId = Integer.parseInt(request.getParameter("contact_id"));
        try {
            statement.setString(1, position);
            statement.setString(2, company);
            statement.setInt(3, contactId);
            statement.executeUpdate();
            out.print("Info created");
        } catch (SQLException e) {
            out.println("Error Creating the Info");
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
