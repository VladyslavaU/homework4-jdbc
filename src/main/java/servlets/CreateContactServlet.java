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

@WebServlet(urlPatterns = "/addContactServlet")
public class CreateContactServlet extends HttpServlet {
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
            statement = connection.prepareStatement("insert into contact(first_name, phone, address) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String firstName = request.getParameter("firstName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        try {
            statement.setString(1, firstName);
            statement.setString(2, phone);
            statement.setString(3, address);
            statement.executeUpdate();
            out.print("Contact created");
        } catch (SQLException e) {
            out.println("Error Creating the Contact");
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
