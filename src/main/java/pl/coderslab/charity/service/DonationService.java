package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Donation;

import java.util.List;

public interface DonationService {

    int getTotalBags();
    int getTotalDonations();
    void saveDonation(Donation donation);
    boolean saveDonation(Long addressId, Donation donation);
    List<Donation> findByUserEmail(String email);
    List<Donation> findAll();
}
