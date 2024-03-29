import alfred.Main;
import alfred.controllers.SlashCommands;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@WebMvcTest(SlashCommands.class)
@ContextConfiguration(classes = {Main.class})
public class TestSlashCommands {

    @Autowired
    private MockMvc mockMvc;

    @Ignore
    @Test
    public void TestPOSTQuote() {

    }

    @Ignore
    @Test
    public void TestPOSTGithub() {

    }

    @Ignore
    @Test
    public void TestPOSTMannen() throws Exception {

        String xSlackSignature = "TEST-xSlackSignature";
/*
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/slack/slashcommands")                             // Post to correct page
                        .header("X-SLack-Signature", xSlackSignature)    // Add correct header
                        .param("channel_id", "")
                        .param("command", "/mannen")
                        .param("text", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)     // Establish content type
                        .accept(MediaType.TEXT_PLAIN))                          // Expect body in plain text
                .andDo(print())                                               // Uncomment for debugging
                .andExpect(status().isOk());

 */
    }

    @Ignore
    @Test
    public void TestPOSTDovre() {

    }

}
