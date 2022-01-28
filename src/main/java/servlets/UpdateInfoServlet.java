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

@WebServlet("/updateInfoServlet")
public class UpdateInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;
    private PreparedStatement statement;

    public void init(ServletConfig config) {
        try {
            ServletContext context = config.getServletContext();

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    context.getInitParameter("dbUrl"),
                    context.getInitParameter("dbUser"),
                    context.getInitParameter("dbPassword"));
            statement = connection.prepareStatement("update info set position=?, company=? where contact_id=?");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("contact_id"));
        String position = request.getParameter("position");
        String company = request.getParameter("company");
        PrintWriter out = response.getWriter();
        try {
            statement.setString(1, position);
            statement.setString(2, company);
            statement.setInt(3, id);
            statement.executeUpdate();
            out.print("Info Updated");
        } catch (SQLException e) {
            out.print("Error updating the info");
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
