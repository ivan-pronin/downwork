package com.codigospostales.scrap;

import java.net.URL;
import java.util.Objects;
import java.util.Set;

public class PostalCodeCategory
{
    private String province;
    private Set<URL> postCodeUrls;

    @Override
    public int hashCode()
    {
        return Objects.hash(province);
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
        PostalCodeCategory other = (PostalCodeCategory) obj;
        return Objects.equals(province, other.province);
    }

    public String getProvince()
    {
        return province;
    }

    public Set<URL> getPostCodeUrls()
    {
        return postCodeUrls;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setPostCodeUrls(Set<URL> postCodeUrls)
    {
        this.postCodeUrls = postCodeUrls;
    }
}
