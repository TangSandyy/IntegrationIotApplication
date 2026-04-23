package jku;

import io.camunda.client.CamundaClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BpmnDeployer implements ApplicationRunner {

    private final CamundaClient camundaClient;

    public BpmnDeployer(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            System.out.println(">>> Deploying BPMN...");
            camundaClient.newDeployResourceCommand()
//                    .addResourceFromClasspath("moebelprozess.bpmn") noch nd lauffähig weil bpmn fehler
                    .addResourceFromClasspath("RfidTestProzess.bpmn")
                    .send()
                    .join();
            System.out.println(">>> BPMN deployed successfully!");
        } catch (Exception e) {
            System.out.println(">>> BPMN deployment failed: " + e.getMessage());
            System.out.println(">>> App continues without deployment.");
        }
    }
}