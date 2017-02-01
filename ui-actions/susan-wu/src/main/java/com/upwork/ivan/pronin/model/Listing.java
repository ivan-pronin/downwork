package com.upwork.ivan.pronin.model;

public class Listing
{
    private String storeName;
    private String accountNumber;
    private String pin;
    private String costPrice;
    private String price;

    public Listing(String storeName, String accountNumber, String pin, String costPrice, String price)
    {
        this.storeName = storeName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.costPrice = costPrice;
        this.price = price;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getPin()
    {
        return pin;
    }

    public String getCostPrice()
    {
        return costPrice;
    }

    public String getPrice()
    {
        return price;
    }
}
