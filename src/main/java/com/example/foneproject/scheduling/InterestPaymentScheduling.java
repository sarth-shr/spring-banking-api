package com.example.foneproject.scheduling;

import com.example.foneproject.entity.Account;
import com.example.foneproject.repository.AccountRepository;
import com.example.foneproject.service.TransactionService;
import com.example.foneproject.util.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterestPaymentScheduling {
    private final EmailUtils emailUtils;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Scheduled(cron = "0 */30 * * * *")
    public void payInterest() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            log.info("No Accounts liable for interest");
            return;
        }
        for (Account acc : accounts) {
            Float interest = acc.getInterest();
            float currentBalance = acc.getBalance();
            if (interest > 0) {
                float interestPaid = currentBalance * interest / 100;
                acc.setBalance((int) (currentBalance + interestPaid));
                accountRepository.save(acc);
                transactionService.saveInterest(acc, (int) interestPaid);

                log.info("Interest of amount: {} has been credited to account ID: {}", interestPaid, acc.getId());

//                emailUtils.sendMail(
//                        acc.getCustomer().getEmail(),
//                        "Interest Received",
//                        "Interest of amount: " + interestPaid + " has been credited to your account with ID: " + acc.getId()
//                );
            }
        }
    }
}
