package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.repository.DonationRepository;

import java.util.Optional;

@Service
public class DonationServiceImpl implements DonationService{

    private final DonationRepository donationRepository;

    public DonationServiceImpl(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
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
}
