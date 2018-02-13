package com.idealista.db.factory;

import org.assertj.core.util.Arrays;

import com.idealista.db.model.Seller;

public class SellerFactory
{

    public static Seller create(String agent, String agentPhone, String agentEmail)
    {
        String[] nameParts = agent.split(" ");
        if (!Arrays.isNullOrEmpty(nameParts))
        {
            String name = nameParts[0];
            String lastName = null;
            if (nameParts.length > 1)
            {
                lastName = nameParts[1];
            }
            return new Seller(name, lastName, agentPhone, agentEmail);
        }
        else
        {
            return null;
        }
    }

}
