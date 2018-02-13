package com.idealista.db.model;

public class Seller
{

    private String name;
    private String lastName;
    private String phone;
    private String email;

    public Seller(String name, String lastName, String phone, String email)
    {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "Seller [name=" + name + ", lastName=" + lastName + ", phone=" + phone + ", email=" + email + "]";
    }

}
