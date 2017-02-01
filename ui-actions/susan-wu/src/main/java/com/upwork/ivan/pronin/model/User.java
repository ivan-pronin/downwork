package com.upwork.ivan.pronin.model;

public class User
{
    private String login;
    private String pass;

    public User(String login, String pass)
    {
        super();
        this.login = login;
        this.pass = pass;
    }

    public String getLogin()
    {
        return login;
    }

    public String getPass()
    {
        return pass;
    }
}
