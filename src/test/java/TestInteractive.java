import alfred.Main;
import alfred.controllers.GeneralFunctions;
import alfred.controllers.Interactive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(Interactive.class)
@ContextConfiguration(classes = {Main.class})
public class TestInteractive {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPOSTInteractive_ReturnExpiredURL() throws Exception {
        String xSlackSignature = "TEST-xSlackSignature";

        String body = new GeneralFunctions().getFileAsString("files/interactive.txt");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/slack/interactive")                        // Post to correct page
                        .content(body)                                          // Get body from file
                        .header("X-SLack-Signature", xSlackSignature)    // Add correct header
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))    // Establish content type
                //.andDo(print())                                                // Uncomment for debugging
                .andExpect(status().isInternalServerError());                  // Expect error: expired_url
    }

}
