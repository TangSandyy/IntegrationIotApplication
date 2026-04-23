package jku.service;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import jku.api.ScanRequest;
import jku.api.StartProcessResponse;
import jku.entity.CONDITION;
import jku.entity.ScanEvent;
import jku.repository.ScanEventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScanProcessService {

    //verwenden Möbelprozess_1.bpmn in ressource datei, id von dem
    // private static final String BPMN_PROCESS_ID = "Process_1utbv5j";
    // aktuell mit test bpmn und dessen id
    private static final String BPMN_PROCESS_ID = "RfidTestProzess";

    private final CamundaClient camundaClient;
    private final ScanEventRepository scanEventRepository;

    public ScanProcessService(CamundaClient camundaClient, ScanEventRepository scanEventRepository) {
        this.camundaClient = camundaClient;
        this.scanEventRepository = scanEventRepository;
    }

    public StartProcessResponse handleScan(ScanRequest request) {
        OffsetDateTime eventTime =
                (request.timestamp() == null || request.timestamp().isBlank())
                        ? OffsetDateTime.now()
                        : OffsetDateTime.parse(request.timestamp());

        Map<String, Object> variables = new HashMap<>();
        variables.put("rfidId", request.rfidId());
        variables.put("location", request.location());
        variables.put("condition", request.condition() == null ? CONDITION.UNKNOWN : request.condition());
        variables.put("timestamp", eventTime.toString());

        ProcessInstanceEvent event = camundaClient
                .newCreateInstanceCommand()
                .bpmnProcessId(BPMN_PROCESS_ID)
                .latestVersion()
                .variables(variables)
                .execute();

        ScanEvent scanEvent = new ScanEvent();
        scanEvent.setRfidId(request.rfidId());
        scanEvent.setLocation(request.location());
        scanEvent.setCondition(request.condition() == null ? CONDITION.UNKNOWN : CONDITION.valueOf(request.condition()));
        scanEvent.setTimestamp(eventTime);
        scanEvent.setProcessInstanceKey(event.getProcessInstanceKey());
        scanEvent.setProcessDefinitionId(event.getBpmnProcessId());

        scanEventRepository.save(scanEvent);

        return new StartProcessResponse(
                event.getProcessInstanceKey(),
                event.getBpmnProcessId(),
                "Process started and scan stored successfully"
        );
    }
}