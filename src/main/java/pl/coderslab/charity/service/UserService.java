package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Address;
import pl.coderslab.charity.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByEmail(String name);
    Optional<User> findById(Long id);
    void saveUser(User user);
    void updateUser(User user);
    List<Address> getUserAddresses(String userEmail);
    Long saveAddress(String userEmail, Address address);
    List<User> findAll();
}