package com.cactusglobal.whiteboard.action;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentSearchActions
{
    private static final Logger LOGGER = LogManager.getLogger(DocumentSearchActions.class);

    private Document document;

    public DocumentSearchActions(Document document)
    {
        this.document = document;
    }

    public Element getElementById(String id)
    {
        Element element = document.getElementById(id);
        if (!isNullElement(element))
        {
            return element;
        }
        return null;
    }

    public List<Element> getElementsByClassContainingValue(List<Element> rootElement, String value)
    {
        if (rootElement.isEmpty())
        {
            return Collections.emptyList();
        }
        return rootElement.get(0).getElementsByAttributeValueContaining("class", value);
    }

    public List<Element> getElementsByClassName(Element rootElement, String className)
    {
        if (isNullElement(rootElement))
        {
            return Collections.emptyList();
        }
        return rootElement.getElementsByClass(className);
    }

    public List<Element> getElementsByTag(List<Element> rootElement, String tagName)
    {
        if (rootElement.isEmpty())
        {
            return Collections.emptyList();
        }
        return rootElement.get(0).getElementsByTag(tagName);
    }

    public String getElementText(List<Element> elements, int index)
    {
        if (elements.isEmpty() || index > elements.size() - 1)
        {
            LOGGER.error("Empty elements OR invalid index requested {} for elements with size {}", index,
                    elements.size());
            return StringUtils.EMPTY;
        }
        return elements.get(index).text();
    }

    private boolean isNullElement(Element element)
    {
        if (element == null)
        {
            LOGGER.error("Root element is null");
            return true;
        }
        return false;
    }
}
