package com.idealista.scraper.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.model.search.FotocasaSearchAttributes;
import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.VibboSearchAttributes;

import org.springframework.stereotype.Component;

@Component
public final class CollectionUtils
{
    private Set<CategoryChoosingAttribute> mergedCombinations = new HashSet<>();

    public Set<CategoryChoosingAttribute> getAllItemCombinations(Set<Set<String>> allAttributesSets)
    {
        allAttributesSets.removeIf(Set::isEmpty);
        String[][] stringArray = allAttributesSets.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
        mergeCombinations(stringArray, 0, new String[0]);
        return mergedCombinations;
    }

    public Set<String> wrapWithIdentificationFlag(Set<String> inputData, IGenericSearchAttributes attributeType)
    {
        return inputData.stream().map(e -> e + attributeType.getIdentificationFlag()).collect(Collectors.toSet());
    }

    private CategoryChoosingAttribute createSearchAttribute(String[] data)
    {
        CategoryChoosingAttribute att = new CategoryChoosingAttribute();
        for (String value : data)
        {
            String typology = VibboSearchAttributes.TYPOLOGY.getIdentificationFlag();
            if (value.contains(typology))
            {
                att.setTypology(value.replace(typology, ""));
            }
            String property = VibboSearchAttributes.PROPERTY_TYPE.getIdentificationFlag();
            if (value.contains(property))
            {
                att.setPropertyType(value.replace(property, ""));
            }
            String operation = VibboSearchAttributes.OPERATION.getIdentificationFlag();
            if (value.contains(operation))
            {
                att.setOperation(value.replace(operation, ""));
            }
            String advertiser = VibboSearchAttributes.ADVERTISER.getIdentificationFlag();
            if (value.contains(advertiser))
            {
                att.setAdvertiser(value.replace(advertiser, ""));
            }
            String district = FotocasaSearchAttributes.DISTRICT.getIdentificationFlag();
            if (value.contains(district))
            {
                att.setDistrict(value.replace(district, ""));
            }
        }
        return att;
    }

    private void mergeCombinations(String[][] sets, int n, String[] prefix)
    {
        if (n >= sets.length)
        {
            CategoryChoosingAttribute newAtt = createSearchAttribute(prefix);
            System.out.println("Created new: " + newAtt);
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
}
