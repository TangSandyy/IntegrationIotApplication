package jku.controller;

import jakarta.validation.Valid;
import jku.api.ScanRequest;
import jku.api.StartProcessResponse;
import jku.entity.ScanEvent;
import jku.repository.ScanEventRepository;
import jku.service.ScanProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    private final ScanProcessService scanProcessService;
    private final ScanEventRepository scanEventRepository;

    public ScanController(ScanProcessService scanProcessService, ScanEventRepository scanEventRepository) {
        this.scanProcessService = scanProcessService;
        this.scanEventRepository = scanEventRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StartProcessResponse createScan(@Valid @RequestBody ScanRequest request) {
        return scanProcessService.handleScan(request);
    }

    @GetMapping
    public List<ScanEvent> getAllScans() {
        return scanEventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ScanEvent getScanById(@PathVariable Long id) {
        return scanEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}