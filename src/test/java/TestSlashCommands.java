import alfred.Main;
import alfred.controllers.SlashCommands;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;

@Ignore // TODO : Remove this and finish tests
@RunWith(SpringRunner.class)
@WebMvcTest(SlashCommands.class)
@ContextConfiguration(classes = {Main.class})
public class TestSlashCommands {

    @Test
    public void TestPOSTQuote() {

    }

    @Test
    public void TestPOSTGithub() {

    }

    @Test
    public void TestPOSTMannen() {

    }

    @Test
    public void TestPOSTDovre() {

    }

}
