package com.microservice.training.bean;

import java.io.Serializable;

/**
 * Created by MACHUNAGENDRADURGP
 */

public class Transfer implements Serializable {

    private int fromAccountId;

    private int toAccountId;

    private float balance = 0;

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
