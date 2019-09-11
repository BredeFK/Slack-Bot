package Classes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class GeneralFunctions {

    public GeneralFunctions() {

    }

    public String getBody(HttpServletRequest req) throws IOException {

        StringBuilder jb = new StringBuilder();
        String line = null;
        BufferedReader reader = req.getReader();


        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }

        return jb.toString();
    }
}
