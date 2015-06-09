import com.ardoq.ArdoqClient;
import com.ardoq.model.*;
import com.ardoq.util.SyncUtil;
import com.google.common.collect.Lists;

import java.util.Map;

public class ClientExample {

    public static void main(String[] args) {
        ArdoqClient client = new ArdoqClient("http://192.168.59.103", "603c58741e9d4193b4d4ddf3a41a69e0")
                .setOrganization("statoil");

    }
}