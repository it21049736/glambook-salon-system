package com.glambook.service;

import com.glambook.dao.ServiceDAO;
import com.glambook.model.Service;
import com.glambook.util.IdGenerator;
import com.glambook.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic for salon services (add, search, edit, delete).
 * Named ServiceManager so it is not confused with the Service model class.
 */
public class ServiceManager {

    private static final int MAX_DURATION_MINUTES = 480;

    private final ServiceDAO serviceDAO = new ServiceDAO();

    // ADD a new service; returns an error message or null on success
    public String addService(String category, String name, String price, String duration,
                             String description, String extra) {
        String error = validate(name, price, duration);
        if (error != null) {
            return error;
        }
        String newId = IdGenerator.generateId(IdGenerator.SERVICE, serviceDAO.getAllIds());
        Service service = Service.create(category, newId, name, Double.parseDouble(price),
                Integer.parseInt(duration), description, extra);
        if (service == null) {
            return "Please choose a valid category.";
        }
        return serviceDAO.add(service) ? null : "Could not save the service.";
    }

    // UPDATE name, price, duration, description and the extra detail (category stays the same)
    public String updateService(String serviceId, String name, String price, String duration,
                                String description, String extra) {
        Service service = serviceDAO.findById(serviceId);
        if (service == null) {
            return "Service not found.";
        }
        String error = validate(name, price, duration);
        if (error != null) {
            return error;
        }
        service.setName(name);
        service.setBasePrice(Double.parseDouble(price));
        service.setDurationMinutes(Integer.parseInt(duration));
        service.setDescription(description);
        service.setExtraDetail(extra);
        return serviceDAO.update(service) ? null : "Could not update the service.";
    }

    public boolean deleteService(String serviceId) {
        return serviceDAO.delete(serviceId);
    }

    public Service getServiceById(String serviceId) {
        return serviceDAO.findById(serviceId);
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAll();
    }

    // SEARCH by keyword (name or description) and FILTER by category
    public List<Service> searchServices(String keyword, String category) {
        List<Service> result = new ArrayList<>();
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        for (Service service : serviceDAO.getAll()) {
            boolean matchesCategory = Validator.isEmpty(category) || service.getCategory().equals(category);
            boolean matchesKeyword = key.isEmpty()
                    || service.getName().toLowerCase().contains(key)
                    || service.getDescription().toLowerCase().contains(key);
            if (matchesCategory && matchesKeyword) {
                result.add(service);
            }
        }
        return result;
    }

    // id -> name map, used by other pages to show service names instead of ids
    public Map<String, String> getServiceNames() {
        Map<String, String> names = new HashMap<>();
        for (Service service : serviceDAO.getAll()) {
            names.put(service.getServiceId(), service.getName());
        }
        return names;
    }

    private String validate(String name, String price, String duration) {
        if (Validator.isEmpty(name) || Validator.isEmpty(price) || Validator.isEmpty(duration)) {
            return "Name, price and duration are required.";
        }
        if (!Validator.isPositiveNumber(price)) {
            return "Price must be a number greater than 0.";
        }
        if (!Validator.isPositiveInteger(duration) || Integer.parseInt(duration) > MAX_DURATION_MINUTES) {
            return "Duration must be between 1 and " + MAX_DURATION_MINUTES + " minutes.";
        }
        return null;
    }
}
