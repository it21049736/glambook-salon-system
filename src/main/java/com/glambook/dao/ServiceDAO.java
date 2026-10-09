package com.glambook.dao;

import com.glambook.model.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes salon services in services.txt.
 */
public class ServiceDAO {

    private static final String FILE_NAME = "services.txt";

    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        for (String line : FileHandler.readLines(FILE_NAME)) {
            Service service = Service.fromFileString(line);
            if (service != null) {
                services.add(service);
            }
        }
        return services;
    }

    public Service findById(String serviceId) {
        for (Service service : getAll()) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (Service service : getAll()) {
            ids.add(service.getServiceId());
        }
        return ids;
    }

    // CREATE
    public boolean add(Service service) {
        return FileHandler.appendLine(FILE_NAME, service.toFileString());
    }

    // UPDATE: replace in the list, then rewrite the file
    public boolean update(Service updated) {
        List<Service> services = getAll();
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getServiceId().equals(updated.getServiceId())) {
                services.set(i, updated);
                return saveAll(services);
            }
        }
        return false;
    }

    // DELETE: remove from the list, then rewrite the file
    public boolean delete(String serviceId) {
        List<Service> services = getAll();
        boolean removed = services.removeIf(s -> s.getServiceId().equals(serviceId));
        return removed && saveAll(services);
    }

    private boolean saveAll(List<Service> services) {
        List<String> lines = new ArrayList<>();
        for (Service service : services) {
            lines.add(service.toFileString());
        }
        return FileHandler.writeLines(FILE_NAME, lines);
    }
}
