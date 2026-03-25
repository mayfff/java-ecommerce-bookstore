package kpi.mayfff.bookstore.controller;

import kpi.mayfff.bookstore.service.HealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        boolean healthy = healthService.isHealthy();

        return healthy ? ResponseEntity.ok().build() : ResponseEntity.status(503).build();
    }
}
