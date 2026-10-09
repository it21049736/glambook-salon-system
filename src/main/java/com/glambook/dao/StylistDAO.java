package com.glambook.dao;

import com.glambook.model.Stylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes stylists in stylists.txt.
 */
public class StylistDAO {

    private static final String FILE_NAME = "stylists.txt";

    public List<Stylist> getAll() {
        List<Stylist> stylists = new ArrayList<>();
        for (String line : FileHandler.readLines(FILE_NAME)) {
            Stylist stylist = Stylist.fromFileString(line);
            if (stylist != null) {
                stylists.add(stylist);
            }
        }
        return stylists;
    }

    public Stylist findById(String stylistId) {
        for (Stylist stylist : getAll()) {
            if (stylist.getStylistId().equals(stylistId)) {
                return stylist;
            }
        }
        return null;
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (Stylist stylist : getAll()) {
            ids.add(stylist.getStylistId());
        }
        return ids;
    }

    // CREATE
    public boolean add(Stylist stylist) {
        return FileHandler.appendLine(FILE_NAME, stylist.toFileString());
    }

    // UPDATE: replace in the list, then rewrite the file
    public boolean update(Stylist updated) {
        List<Stylist> stylists = getAll();
        for (int i = 0; i < stylists.size(); i++) {
            if (stylists.get(i).getStylistId().equals(updated.getStylistId())) {
                stylists.set(i, updated);
                return saveAll(stylists);
            }
        }
        return false;
    }

    // DELETE: remove from the list, then rewrite the file
    public boolean delete(String stylistId) {
        List<Stylist> stylists = getAll();
        boolean removed = stylists.removeIf(s -> s.getStylistId().equals(stylistId));
        return removed && saveAll(stylists);
    }

    private boolean saveAll(List<Stylist> stylists) {
        List<String> lines = new ArrayList<>();
        for (Stylist stylist : stylists) {
            lines.add(stylist.toFileString());
        }
        return FileHandler.writeLines(FILE_NAME, lines);
    }
}
