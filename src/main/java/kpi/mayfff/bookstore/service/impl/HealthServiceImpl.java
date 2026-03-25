package kpi.mayfff.bookstore.service.impl;

import kpi.mayfff.bookstore.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService {
    private final DataSource dataSource;

    @Override
    public boolean isHealthy() {
        try (Connection connection = dataSource.getConnection()) {
            int timeout = 3;
            return connection.isValid(timeout);
        } catch (Exception e) {
            return false;
        }
    }
}
