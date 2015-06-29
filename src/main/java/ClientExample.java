import com.ardoq.ArdoqClient;

public class ClientExample {

    public static void main(String[] args) {
        ArdoqClient client = new ArdoqClient("http://192.168.59.103", "676615202ef64142becd775d42eec8a2")
                .setOrganization("ardoq");
        

    }
}