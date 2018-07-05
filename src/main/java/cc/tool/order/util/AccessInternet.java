package cc.tool.order.util;

import cc.admore.infant.dao.entity.Image;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by lxj on 16-9-30
 */
public class AccessInternet {

    public static Logger logger = LoggerFactory.getLogger(AccessInternet.class);

    public static String httpAccess(String url) {

        logger.debug("http get url ------" + url);

        HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));

        GetMethod getMethod = new GetMethod(url);

        return access(httpClient, getMethod);
    }

    public static byte[] httpAccess(String url, Image image) {

        logger.debug("http url ------" + url);

        HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));

        GetMethod getMethod = new GetMethod(url);

        return access(httpClient, getMethod, image);

    }

    public static byte[] access(HttpClient httpClient, HttpMethodBase httpMethodBase, Image image) {

        byte[] bytes = null;

        try {

            httpClient.executeMethod(httpMethodBase);

            logger.debug("服务器返回状态 : " + String.valueOf(httpMethodBase.getStatusLine()));

            bytes = httpMethodBase.getResponseBody();

            image.setContentType(httpMethodBase.getResponseHeader("Content-Type").getValue());

            logger.debug("Content-Type" + httpMethodBase.getResponseHeader("Content-Type").getValue());

            image.setFileName(httpMethodBase.getResponseHeader("Content-disposition").getValue());

            logger.debug("Content-disposition" + httpMethodBase.getResponseHeader("Content-disposition").getValue());

            image.setLength(httpMethodBase.getResponseContentLength());

            logger.debug("Content-Length" + String.valueOf(httpMethodBase.getResponseContentLength()));

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            httpMethodBase.releaseConnection();

        }

        return bytes;

    }

    public static String httpAccessXml(String url, String xml) {

        logger.debug("http post xml url ------" + url);

        HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));

        PostMethod postMethod = new PostMethod(url);

        postMethod.setRequestHeader("Content-type", "text/xml;charset=utf-8");
        postMethod.setRequestBody(xml);

        return access(httpClient, postMethod);
    }

    public static String access(HttpClient httpClient, HttpMethodBase httpMethodBase) {

        String result = null;

        try {

            httpClient.executeMethod(httpMethodBase);

            logger.debug("服务器返回状态 : " + String.valueOf(httpMethodBase.getStatusLine()));

            result = httpMethodBase.getResponseBodyAsString();

            logger.debug("返回结果 : " + result);


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            httpMethodBase.releaseConnection();

        }

        return result;

    }

}
