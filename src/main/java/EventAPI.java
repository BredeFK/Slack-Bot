import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/event")
public class EventAPI extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EventAPI.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("GET method is not supported for this URL");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get content
        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error: " + e);
        }

        // Convert json string to object
        if (req.getContentType().toLowerCase().contains("json")) {
            SlackEvent event = new GsonBuilder().create().fromJson(jb.toString(), SlackEvent.class);

            System.out.println(event.toString());
        }
    }

}
