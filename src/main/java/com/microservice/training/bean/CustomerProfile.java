package com.microservice.training.bean;
import javax.persistence.*;
import java.util.Set;

@Entity
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false)
    private int customerId;

    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "customerProfile", cascade = CascadeType.ALL)
    private Set<Account> accounts;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getAccounts() {
        return accounts;
    }

    public void setAccounts(Set accounts) {
        this.accounts = accounts;
    }
}
