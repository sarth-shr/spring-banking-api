package com.example.foneproject.scheduling;

import com.example.foneproject.entity.Account;
import com.example.foneproject.repository.AccountRepository;
import com.example.foneproject.service.TransactionService;
import com.example.foneproject.util.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InterestPaymentScheduling {
    private final EmailUtils emailUtils;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Scheduled(cron = "*/60 * * * * *")
    public void payInterest() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            return;
        }
        for (Account acc : accounts) {
            Float interest = acc.getInterest();
            float currentBalance = acc.getBalance();
            if (interest != null) {
                float interestPaid = currentBalance * interest / 100;
                acc.setBalance((int) (currentBalance + interestPaid));
                accountRepository.save(acc);
                transactionService.saveInterest(acc, (int) interestPaid);

                emailUtils.sendMail(
                        acc.getCustomer().getEmail(),
                        "Interest Received",
                        "Interest of amount: " + interestPaid + " has been credited to your account with ID: " + acc.getId()
                );
            }
        }
    }
}
