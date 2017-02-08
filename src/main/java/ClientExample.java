import com.ardoq.ArdoqClient;
import com.ardoq.model.*;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ClientExample {
    public static final String API_TOKEN = "";
    public static final String ORGANIZATION_LABEL = "";
    public static final String UNIQUE_TEMPLATE_NAME = "";
    public static final String LOREM = "LOREM ipsum dolor sit amet, consectetur adipiscing elit. Suo genere perveniant ad extremum; Propter nos enim illam, non propter eam nosmet ipsos diligimus. Inde igitur, inquit, ordiendum est. Duo Reges: constructio interrete. Aliter enim explicari, quod quaeritur, non potest. Non laboro, inquit, de nomine. Nondum autem explanatum satis, erat, quid maxime natura vellet. Quos quidem tibi studiose et diligenter tractandos magnopere censeo. Quid autem habent admirationis, cum prope accesseris? Quod si ita sit, cur opera philosophiae sit danda nescio.";
    public static final int PARENTS = 9;
    public static final int CHILDREN = 10;
    public static final int REFERENCES = PARENTS * CHILDREN * 15;
    public static final MetricRegistry metrics = new MetricRegistry();

    public static final Timer componentTimer = metrics.timer("createComponentTimer");
    public static final Timer referenceTimer = metrics.timer("createReferenceTimer");

    private static Reference createReference(ArdoqClient client, Workspace workspace, Model model, Component source, Component targe) {
        final Timer.Context context = referenceTimer.time();
        try {
            return client.reference().createReference(new Reference(workspace.getId(), LOREM, source.getId(),
                    targe.getId(), model.getReferenceTypeByName("Synchronous")));
        } finally {
            context.stop();
        }
    }

    private static Component createComponent(ArdoqClient client, Component service) {
        final Timer.Context context = componentTimer.time();
        try {
            return client.component().createComponent(service);
        } finally {
            context.stop();
        }
    }

    public static void main(String[] args) {
        System.out.print("Import stated: " + new Date().toString());
        startImport();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.report();
        System.out.print("\nImport finished: " + new Date().toString());

    }

    private static void startImport() {
        ArdoqClient client = new ArdoqClient("https://app.ardoq.com", API_TOKEN)
                .setOrganization(ORGANIZATION_LABEL);

        Model template = client.model().getTemplateByName(UNIQUE_TEMPLATE_NAME);
        Workspace workspace = client.workspace().createWorkspace(new Workspace("Api Demo", template.getId(), "My workspace"));
        Model model = client.model().getModelById(workspace.getComponentModel());
        ArrayList<Component> parents = new ArrayList<Component>();
        ArrayList<Component> children = new ArrayList<Component>();

        for (int i = 0; i < PARENTS; i++) {
            Component component = createComponent(client, new Component("Parent " + i, workspace.getId(), LOREM));
            parents.add(component);
        }

        for (Component parent : parents) {
            for (int i = 0; i < CHILDREN; i++) {
                Component child = createComponent(client, new Component("Child " + i, workspace.getId(),
                        LOREM, model.getComponentTypeByName("Service"), parent.getId()));
                children.add(child);
            }
        }

        parents.addAll(children);
        Random r = new Random();

        for (int i = 0; i < REFERENCES; i++) {
            Component source = parents.get(r.nextInt(parents.size()));
            Component target = parents.get(r.nextInt(parents.size()));
            createReference(client, workspace, model, source, target);
        }
    }
}