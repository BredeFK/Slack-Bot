import alfred.Main;
import alfred.controllers.Interactive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Interactive.class)
@ContextConfiguration(classes = {Main.class})
public class TestInteractive {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPOSTInteractive_ReturnExpiredURL() throws Exception {
        String xSlackSignature = "TEST-xSlackSignature";

        String body = getInteractiveTXT();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/slack/interactive")                        // Post to correct page
                        .content(body)                                          // Get body from file
                        .header("X-SLack-Signature", xSlackSignature)    // Add correct header
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))    // Establish content type
                //.andDo(print())                                                // Uncomment for debugging
                .andExpect(status().isInternalServerError());                  // Expect error: expired_url
    }

    private String getInteractiveTXT() throws IOException {

        // Get file path
        Path filePath = Paths.get("interactive.txt");

        // Get file
        File file = new File(filePath.toString());

        // Check if file exists
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}
