package com.glambook.dao;

import com.glambook.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes users in users.txt.
 * Update and delete load the whole file into a list, change the list and write it back.
 */
public class UserDAO {

    private static final String FILE_NAME = "users.txt";

    // returns every valid user; broken lines are skipped
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        for (String line : FileHandler.readLines(FILE_NAME)) {
            User user = User.fromFileString(line);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    public User findById(String userId) {
        for (User user : getAll()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    // emails are compared without case so "Nimali@Gmail.com" matches "nimali@gmail.com"
    public User findByEmail(String email) {
        for (User user : getAll()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (User user : getAll()) {
            ids.add(user.getUserId());
        }
        return ids;
    }

    // CREATE: add the new user as a new line at the end of the file
    public boolean add(User user) {
        return FileHandler.appendLine(FILE_NAME, user.toFileString());
    }

    // UPDATE: replace the matching user in the list and rewrite the file
    public boolean update(User updatedUser) {
        List<User> users = getAll();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }
        return found && saveAll(users);
    }

    // DELETE: remove the matching user from the list and rewrite the file
    public boolean delete(String userId) {
        List<User> users = getAll();
        boolean removed = users.removeIf(user -> user.getUserId().equals(userId));
        return removed && saveAll(users);
    }

    private boolean saveAll(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            lines.add(user.toFileString());
        }
        return FileHandler.writeLines(FILE_NAME, lines);
    }
}
