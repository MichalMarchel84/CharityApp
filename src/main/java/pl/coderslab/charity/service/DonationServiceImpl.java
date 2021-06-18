package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.model.Address;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.repository.AddressRepository;
import pl.coderslab.charity.repository.DonationRepository;

import java.util.Optional;

@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final AddressRepository addressRepository;

    public DonationServiceImpl(DonationRepository donationRepository, AddressRepository addressRepository) {
        this.donationRepository = donationRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public int getTotalBags() {
        Optional<Integer> bags = donationRepository.getTotalBags();
        return bags.orElse(0);
    }

    @Override
    public int getTotalDonations() {
        return (int) donationRepository.count();
    }

    @Override
    public void saveDonation(Donation donation) {
        Address address = donation.getAddress();
        address.setUser(null);
        address.setId(null);
        addressRepository.save(address);
        donationRepository.save(donation);
    }

    @Override
    public boolean saveDonation(Long addressId, Donation donation) {
        Optional<Address> adr = addressRepository.findById(addressId);
        if (adr.isEmpty()) return false;
        else {
            Address address = adr.get();
            if (address.getStreet().equals(donation.getAddress().getStreet())
                    && address.getCity().equals(donation.getAddress().getCity())
                    && address.getPostCode().equals(donation.getAddress().getPostCode())
                    && address.getPhone().equals(donation.getAddress().getPhone())
                    && address.getUser().getId().equals(donation.getUser().getId())
            ){
                donation.getAddress().setId(addressId);
            }else {
                donation.getAddress().setId(null);
                donation.getAddress().setUser(null);
                addressRepository.save(donation.getAddress());
            }
            donationRepository.save(donation);
        }
        return true;
    }
}
