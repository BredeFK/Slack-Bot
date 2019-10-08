package alfred.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ErrorHandler implements ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    private static final String PATH = "/error";


    @RequestMapping(PATH)
    public void error(HttpServletRequest httpServletRequest) {
        logger.log(Level.WARNING, "request on {0}", httpServletRequest.getRequestURL());
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
