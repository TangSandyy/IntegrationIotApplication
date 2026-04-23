package jku.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "scan_event")
public class ScanEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rfidId;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)  // speichert "GUT" statt 0,1,2... in der DB
    private CONDITION condition;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    private Long processInstanceKey;

    private String processDefinitionId;

    public Long getId() {
        return id;
    }

    public String getRfidId() {
        return rfidId;
    }

    public void setRfidId(String rfidId) {
        this.rfidId = rfidId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CONDITION getCondition() {
        return condition;
    }

    public void setCondition(CONDITION condition) {
        this.condition = condition;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(Long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
}