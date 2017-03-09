package com.idealista.scraper.webdriver.proxy;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.springframework.stereotype.Component;

@Component
public class ProxyAdapter
{
    private Proxy seleniumProxy;
    private String host;
    private int port;

    public ProxyAdapter()
    {
        this(new Proxy().setProxyType(ProxyType.DIRECT), "host_not_set", 0);
    }

    public ProxyAdapter(Proxy seleniumProxy, String host, int port)
    {
        this.seleniumProxy = seleniumProxy;
        this.host = host;
        this.port = port;
    }

    public Proxy getSeleniumProxy()
    {
        return seleniumProxy;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    @Override
    public String toString()
    {
        return "ProxyAdapter [" + host + ":" + port + "]";
    }
}
