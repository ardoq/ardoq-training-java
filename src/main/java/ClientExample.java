import com.ardoq.ArdoqClient;
import com.ardoq.model.Model;

public class ClientExample {

    public static void main(String[] args) {
        ArdoqClient client = new ArdoqClient("https://app.ardoq.com", "")
                .setOrganization("");

        Model template = client.model().getTemplateByName("Application Service");

    }
}