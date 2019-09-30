import alfred.Main;
import alfred.controllers.Index;
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


@RunWith(SpringRunner.class)
@WebMvcTest(Index.class)
@ContextConfiguration(classes = {Main.class})
public class TestIndex {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getIndex() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/")
                        .accept(MediaType.TEXT_HTML))
                // .andDo(print()) // Uncomment for debugging
                .andExpect(status().isAccepted());
    }

}
