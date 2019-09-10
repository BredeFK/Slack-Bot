package Handlers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/")
public class Index extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Index.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "GET request on /");
        resp.getWriter().write("<h1>Welcome to my index page</h1><h2>This is all</h2>");
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
    }
}
