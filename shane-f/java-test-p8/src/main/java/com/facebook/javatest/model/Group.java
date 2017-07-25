package com.facebook.javatest.model;

public class Group
{
    private long id;
    private String name;
    private long members;
    private String buttonId;

    public Group(long id, String name, long members, String buttonId)
    {
        this.id = id;
        this.name = name;
        this.members = members;
        this.buttonId = buttonId;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public long getMembers()
    {
        return members;
    }

    public String getButtonId()
    {
        return buttonId;
    }

    @Override
    public String toString()
    {
        return "Group [id=" + id + ", name=" + name + ", members=" + members + ", buttonId=" + buttonId + "]";
    }
}
