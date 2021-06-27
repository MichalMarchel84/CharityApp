package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Address;
import pl.coderslab.charity.model.User;

import java.util.List;

public interface UserService {
    User findByEmail(String name);
    void saveUser(User user);
    void updateUser(User user);
    List<Address> getUserAddresses(String userEmail);
    Long saveAddress(String userEmail, Address address);
    List<User> findAll();
}