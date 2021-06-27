package pl.coderslab.charity.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.model.Address;
import pl.coderslab.charity.model.Role;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.repository.AddressRepository;
import pl.coderslab.charity.repository.DonationRepository;
import pl.coderslab.charity.repository.RoleRepository;
import pl.coderslab.charity.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final DonationRepository donationRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, AddressRepository addressRepository, DonationRepository donationRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<Address> getUserAddresses(String userEmail) {

        return addressRepository.findByUserEmail(userEmail);
    }

    @Override
    public Long saveAddress(String userEmail, Address address) {
        User user = userRepository.findByEmail(userEmail);
        if(user != null){
            address.setUser(user);
            addressRepository.save(address);
            return address.getId();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(String userId) {
        try{
            Long id = Long.parseLong(userId);
            var user = userRepository.findById(id);
            if(user.isEmpty()) return false;
            else if(user.get().getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) return false;
            userRepository.removeUser(id);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}