package pl.coderslab.charity.service;

import pl.coderslab.charity.model.User;

public interface UserService {
    User findByEmail(String name);
    void saveUser(User user);
}