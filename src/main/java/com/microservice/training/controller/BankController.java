package com.microservice.training.controller;

import com.microservice.training.bean.Account;
import com.microservice.training.bean.CustomerProfile;
import com.microservice.training.bean.Transfer;
import com.microservice.training.repository.AccountRepository;
import com.microservice.training.repository.CustomerRepository;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by MACHUNAGENDRADURGP
 */

@Controller
public class BankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankController.class);

    public static final String ACCOUNTS = "accounts";
    public static final String TRANSFER = "transfer";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/create")
    public String createAccount(Account account) {
        return "add-account";
    }

    @GetMapping("/searchAccount")
    public String searchAccount(Account account) {
        return "search";
    }

    @GetMapping("/display")
    public String displayAll() {
        return "display";
    }

    @GetMapping("/transfer")
    public String transfer(Transfer transfer) {
        return TRANSFER;
    }


    @PostMapping("/searchAccountBy")
    public String searchAccountBy(Account account, BindingResult result, Model model) {

        if(!accountRepository.findByAccountId(account.getAccountId()).isPresent()){
            throw  new IllegalArgumentException("Invalid Account No");
        }
        model.addAttribute(ACCOUNTS, accountRepository.findByAccountId(account.getAccountId()).get());
        return "search";
    }

    @PostMapping("/addaccount")
    public String addAccount(Account account, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors().stream());
            return "add-account";
        }
        customerRepository.save(account.getCustomerProfile());
        accountRepository.save(account);
        model.addAttribute(ACCOUNTS, accountRepository.findAll());
        return "index";
    }

    @PostMapping("/transferfunds")
    public String transferFunds(Transfer transfer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors().stream());
            return "transfer";
        }
        Optional<Account> fromAcc = accountRepository.findByAccountId(transfer.getFromAccountId());
        Optional<Account> toAcc = accountRepository.findByAccountId(transfer.getToAccountId());
        if(fromAcc.isPresent() && toAcc.isPresent()){
            if(fromAcc.get().getBalance() >= transfer.getBalance()){
                float existingTarget = toAcc.get().getBalance();
                float newBalance = existingTarget+transfer.getBalance();
                toAcc.get().setBalance(newBalance);
                float existingSource = fromAcc.get().getBalance();
                float newSourceBalance = existingSource-transfer.getBalance();
                fromAcc.get().setBalance(newSourceBalance);
                accountRepository.save(fromAcc.get());
                accountRepository.save(toAcc.get());
            }
            else {
                throw  new IllegalArgumentException("Insufficient funds");
            }
        }
        else{
            throw  new IllegalArgumentException("Account No does not exists");
        }
        model.addAttribute("transfer", transfer);
        return "transfer";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id:" + id));

        model.addAttribute("account", account);
        return "update-account";
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable("id") int id, @Valid Account account,
                                BindingResult result, Model model) {
        LOGGER.info("Id is : {},Name is : {}, Account Id is : {},Customer Id : {} " ,
                id,
                account.getCustomerProfile().getName(),
                account.getAccountId(),
                account.getCustomerProfile().getCustomerId());
        if (result.hasErrors()) {
            account.setId(id);
            return "update-account";
        }
        Optional<Account> accountEntity = accountRepository.findById(id);
        accountEntity.get().setAccountId(account.getAccountId());
        accountEntity.get().setBalance(account.getBalance());
        CustomerProfile customerProfile = customerRepository.findByCustomerId(accountEntity.get().getCustomerProfile().getCustomerId());
        customerProfile.setName(account.getCustomerProfile().getName());
        accountEntity.get().setCustomerProfile(customerProfile);
        accountRepository.save(accountEntity.get());
        model.addAttribute("accounts", accountRepository.findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") int id, Model model) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id:" + id));
        accountRepository.delete(account);
        model.addAttribute("accounts", accountRepository.findAll());
        return "index";
    }

    @GetMapping("/displayAll/{type}")
    public String displayAllAccounts(@PathVariable("type") String type, Model md) {
        if ("accounts".equalsIgnoreCase(type))
            md.addAttribute("accounts", accountRepository.findAll());
        else
            md.addAttribute("profiles", customerRepository.findAll());
        return "display";
    }
}