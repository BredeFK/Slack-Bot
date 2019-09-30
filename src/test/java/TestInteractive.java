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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Interactive.class)
@ContextConfiguration(classes = {Main.class})
public class TestInteractive {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPOSTInteractive() throws Exception {
        String xSlackSignature = "TEST-xSlackSignature";

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/interactive")                             // Post to correct page
                        // TODO : add test data and read from file or make this prettier/more compact
                        .content("")
                        .header("X-SLack-Signature", xSlackSignature)    // Add correct header
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)                // Establish content type
                        .accept(MediaType.TEXT_PLAIN))                          // Expect body in plain text
                .andDo(print())                                               // Uncomment for debugging
                .andExpect(status().isOk());
    }
}
