package com.idealista.scraper.debug;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CollectionsTests
{
    private Set<SearchAttribute> mergedCombinations = new HashSet<>();

    // @Test
    public void testName() throws Exception
    {

        String sArray[] = new String[] {"A", "A", "B", "C"};
        // convert array to list
        List<String> list1 = Arrays.asList(sArray);
        List<String> list2 = Arrays.asList(sArray);
        List<String> list3 = Arrays.asList(sArray);
        List<String> list4 = Arrays.asList(sArray);

        LinkedList<List<String>> lists = new LinkedList<List<String>>();

        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        lists.add(list4);

        Set<String> combinations = new TreeSet<String>();
        Set<String> newCombinations;

        for (String s : lists.removeFirst())
            combinations.add(s);

        while (!lists.isEmpty())
        {
            List<String> next = lists.removeFirst();
            newCombinations = new TreeSet<String>();
            for (String s1 : combinations)
                for (String s2 : next)
                    newCombinations.add(s1 + s2);

            combinations = newCombinations;
        }
        for (String s : combinations)
            System.out.print(s + " ");
        System.out.println();
        System.out.println("Size: " + combinations.size());
    }

    @Test
    public void testMergeSets() throws Exception
    {
        Set<String> userTypologies = new HashSet<>(Arrays.asList("typ1", "typ2"));
        Set<String> userOperations = new HashSet<>(Arrays.asList("op_1", "op_2", "op_3"));
        Set<String> userPropertyTypes = new HashSet<>(Arrays.asList("prop1", "prop2"));
        Set<String> advertiser = new HashSet<>(Arrays.asList("adv1"));
        String[] typs = new String[0];
        typs = userTypologies.toArray(typs);
        String[] ops = new String[0];
        ops = userOperations.toArray(ops);
        String[] props = new String[0];
        props =  userPropertyTypes.toArray(props);
        String[] adv = new String[0];
        adv =  advertiser.toArray(adv);
        String[][] sets = {typs, props, adv, ops};
        mergeCombinations(sets, 0, new String[0]);
        System.out.println("Size: " + mergedCombinations.size());
        System.out.println(mergedCombinations);
    }

    private void mergeCombinations(String[][] sets, int n, String[] prefix)
    {
        if (n >= sets.length)
        {
            String outp = "{";
            SearchAttribute newAtt = createSearchAttribute(prefix);
            System.out.println("Created new searchATt object: " + newAtt);
            mergedCombinations.add(newAtt);
            return;
        }
        for (String o : sets[n])
        {
            String[] newPrefix = Arrays.copyOfRange(prefix, 0, prefix.length + 1);
            newPrefix[newPrefix.length - 1] = o;
            mergeCombinations(sets, n + 1, newPrefix);
        }
    }

    private SearchAttribute createSearchAttribute(String[] data)
    {
        SearchAttribute att = new SearchAttribute();
        for (String value : data)
        {
            if (value.contains("typ"))
            {
                att.setTypology(value);
            }
            if (value.contains("prop"))
            {
                att.setPropertyType(value);
            }
            if (value.contains("op_"))
            {
                att.setOperation(value);
            }
            if (value.contains("adv"))
            {
                att.setAdvertiser(value);
            }
        }
        return att;
    }

    // @Test
    public void testMrgeSetsRecursion() throws Exception
    {
        Integer[] set1 = {1, 2};
        Float[] set2 = {2.0F, 1.3F, 2.8F};
        String[] set3 = {"$", "%", "£", "!"};
        Object[][] sets = {set1, set2, set3};

        printCombinations(sets, 0, new Object[0]);
    }

    private void printCombinations(Object[][] sets, int n, Object[] prefix)
    {
        if (n >= sets.length)
        {
            String outp = "{";
            for (Object o : prefix)
            {
                outp = outp + o.toString() + ",";
            }
            System.out.println(outp.substring(0, outp.length() - 1) + "}");
            return;
        }
        for (Object o : sets[n])
        {
            Object[] newPrefix = Arrays.copyOfRange(prefix, 0, prefix.length + 1);
            newPrefix[newPrefix.length - 1] = o;
            printCombinations(sets, n + 1, newPrefix);
        }
    }

    private class SearchAttribute
    {
        private String typology;
        private String operation;
        private String propertyType;
        private String advertiser;

        public String getTypology()
        {
            return typology;
        }

        public void setTypology(String typology)
        {
            this.typology = typology;
        }

        public String getOperation()
        {
            return operation;
        }

        public void setOperation(String operation)
        {
            this.operation = operation;
        }

        public String getPropertyType()
        {
            return propertyType;
        }

        public void setPropertyType(String propertyType)
        {
            this.propertyType = propertyType;
        }

        public String getAdvertiser()
        {
            return advertiser;
        }

        public void setAdvertiser(String advertiser)
        {
            this.advertiser = advertiser;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((advertiser == null) ? 0 : advertiser.hashCode());
            result = prime * result + ((operation == null) ? 0 : operation.hashCode());
            result = prime * result + ((propertyType == null) ? 0 : propertyType.hashCode());
            result = prime * result + ((typology == null) ? 0 : typology.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SearchAttribute other = (SearchAttribute) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (advertiser == null)
            {
                if (other.advertiser != null)
                    return false;
            }
            else if (!advertiser.equals(other.advertiser))
                return false;
            if (operation == null)
            {
                if (other.operation != null)
                    return false;
            }
            else if (!operation.equals(other.operation))
                return false;
            if (propertyType == null)
            {
                if (other.propertyType != null)
                    return false;
            }
            else if (!propertyType.equals(other.propertyType))
                return false;
            if (typology == null)
            {
                if (other.typology != null)
                    return false;
            }
            else if (!typology.equals(other.typology))
                return false;
            return true;
        }

        private CollectionsTests getOuterType()
        {
            return CollectionsTests.this;
        }

        @Override
        public String toString()
        {
            return "SearchAttribute [typology=" + typology + ", operation=" + operation + ", propertyType="
                    + propertyType + ", advertiser=" + advertiser + "]";
        }
    }
}
