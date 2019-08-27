import org.json.JSONException;
import org.json.JSONObject;

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

    public static void main(String[] args) {
        System.out.println("Test");
    }

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
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error: " + ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Try to get body in jsonobject
        JSONObject json = null;
        try {
            json = new JSONObject(jb.toString());
        } catch (JSONException ex) {
            logger.log(Level.WARNING, "Error: " + ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Check if challenge exists
        if (!json.get("challenge").equals("")) {
            // Respond with challenge and status ok
            System.out.println(req.getHeader("X-Slack-Signature"));
            System.out.println(req.getHeader("X-Slack-Request-Timestamp"));
            resp.setContentType("test/plain");
            resp.getWriter().write(json.get("challenge").toString());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
