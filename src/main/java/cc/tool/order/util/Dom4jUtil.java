package cc.tool.order.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 16-10-10
 */
public class Dom4jUtil {

    /**
     * String --> Document
     *
     * @param text 被转换的String
     * @return Document
     */
    public static Document parseDocument(String text) {

        try {

            return DocumentHelper.parseText(text);

        } catch (DocumentException e) {

            e.printStackTrace();

            return null;

        }

    }

    /**
     * String --> Map
     *
     * @param text 被转换的String
     * @return Map
     */
    public synchronized static Map<String, Object> findMapByText(String text) {

        Map<String, Object> map = new HashMap<String, Object>();

        Document document = parseDocument(text);

        Element rootElement = document.getRootElement();

        recursiveGetMap(map, rootElement);

        return map;

    }

    /**
     * 递归所有节点封装到Map
     *
     * @param map         Map
     * @param rootElement rootElement
     */
    private static void recursiveGetMap(Map<String, Object> map, Element rootElement) {

        List elements = rootElement.elements();

        if (elements.size() == 0) {

            return;

        }

        for (Iterator iterator = elements.iterator(); iterator.hasNext(); ) {

            Element element = (Element) iterator.next();

            map.put(element.getName(), element.getText());

            recursiveGetMap(map, element);

        }

    }

    public static Map<String, Object> findMapByInputStream(InputStream inputStream) {

        String result;

        try {

            StringBuffer stringBuffer = new StringBuffer();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String temp;

            while ((temp = bufferedReader.readLine()) != null) {

                stringBuffer.append(temp);

            }

            result = stringBuffer.toString();

        } catch (IOException e) {

            e.printStackTrace();

            result = "";

        }

        return findMapByText(result);

    }

}
