package alfred.Handlers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class Index {
    private static final Logger logger = Logger.getLogger(Index.class.getName());

    public Index() {

    }

    @GetMapping(value = "/test")
    public String indexGET(@RequestParam(value = "id", defaultValue = "1337") int id) {
        logger.log(Level.INFO, "GET request on /");
        return String.format("Hello, the Id is %d", id);
        /*
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("<h1>Welcome to my index page</h1><h2>This is all</h2>");
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);

         */
    }
}
