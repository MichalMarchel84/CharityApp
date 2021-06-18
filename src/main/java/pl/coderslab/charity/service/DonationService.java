package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Donation;

public interface DonationService {

    int getTotalBags();
    int getTotalDonations();
    void saveDonation(Donation donation);
    boolean saveDonation(Long addressId, Donation donation);
}
