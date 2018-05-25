package com.example.mqdemo.utils;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by a1 on 16/6/6.
 */
public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static final int TIMEOUT = 1000;

    public static String doPostJson(String url, String json) {
        CloseableHttpClient httpClient = null;
        HttpPost method = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();

            //设置请求和传输超时时间

            method = new HttpPost(url);

            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");
            method.setEntity(new StringEntity(json, UTF8));

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(500).setConnectTimeout(500).build();
            method.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(method);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, UTF8);
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (method != null) {
                    method.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOG.error("error is {} ", e);
            }
        }
        return result;
    }


    public static String doPost(String url, Map<String, String> map) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, UTF8);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, UTF8);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static <V> V doGet(String url, Class<V> clazz) {
        String response = doGet(url);

        return getResponse(response, clazz);
    }


    public static String doGet(String url) {
        try {
            URI uri = new URI(url);
            return doGet(uri, false);
        } catch (URISyntaxException e) {
            LOG.error("error is {} ", e);
        }
        return null;
    }

    public static String doGetByAgent(String url, Map<String, String> params, boolean agent) {
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addParameter(key, value);
            }
            URI uri = builder.build();
            return doGet(uri, agent);
        } catch (URISyntaxException e) {
            LOG.error("error is {} ", e);
        }
        return null;

    }

    public static <V> V doGet(String url, Map<String, String> params, Class<V> clazz) {
        String response = doGet(url, params);

        return getResponse(response, clazz);
    }

    public static String doGet(String url, Map<String, String> params) {
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addParameter(key, value);
            }
            URI uri = builder.build();
            return doGet(uri, false);
        } catch (URISyntaxException e) {
            LOG.error("error is {} ", e);
        }
        return null;

    }

    public static String doGet(URI url, boolean agent) {
        String getBodyStr = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            if (agent) {
                HttpHost proxy = new HttpHost("proxy1.yidian.com", 3128, "http");
                RequestConfig config = RequestConfig.custom().setSocketTimeout(TIMEOUT)
                        .setConnectTimeout(TIMEOUT).setProxy(proxy).build();
                httpGet.setConfig(config);
            } else {
                RequestConfig config = RequestConfig.custom().setSocketTimeout(TIMEOUT)
                        .setConnectTimeout(TIMEOUT).build();
                httpGet.setConfig(config);
            }

            HttpResponse response = httpClient.execute(httpGet);
            getBodyStr = getResponseEntity(response);
        } catch (IOException e) {
            LOG.error("error is {} ", e);
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOG.error("error is {} ", e);
            }
        }
        return getBodyStr;
    }

    //带Header的请求

    public static String doGetAddHeader(String url, Map<String, String> headers) {
        try {
            URI uri = new URI(url);
            return doGetAddHeader(uri, headers);
        } catch (URISyntaxException e) {
            LOG.error("error is {} ", e);
        }
        return null;
    }

    public static String doGetAddHeader(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addParameter(key, value);
            }
            URI uri = builder.build();
            return doGetAddHeader(uri, headers);
        } catch (URISyntaxException e) {
            LOG.error("error is {} ", e);
        }
        return null;

    }

    public static String doGetAddHeader(URI url, Map<String, String> headers) {

        String getBodyStr = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                LOG.info("key = {}", key);
                String value = entry.getValue();
                LOG.info("value = {}", value);
                httpGet.setHeader(key, value);
            }
            HttpResponse response = httpClient.execute(httpGet);
            getBodyStr = getResponseEntity(response);
        } catch (IOException e) {
            LOG.error("error is {} ", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOG.error("error is {} ", e);
            }
        }
        return getBodyStr;
    }


    //通用方法,将InputStream转为String
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            LOG.error("error is {} ", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("error is {} ", e);
            }
        }
        return sb.toString();
    }

    //通用方法,获取response中data,并转为字符串
    public static String getResponseEntity(HttpResponse response) throws IOException {

        String responseStr = "";

        HttpEntity postEntity = response.getEntity();

        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() && postEntity != null) {
            InputStream inStreams = postEntity.getContent();
            responseStr = convertStreamToString(inStreams);
            LOG.debug("http status: {} , content is {}", response.getStatusLine(), responseStr);
            // Do not need the rest
            // httpPost.abort();
        }
        return responseStr;
    }


    public static String doPost(String url, Map<String, String> headers, String body) {
        HttpPost httpPost = new HttpPost(url);

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }

        StringEntity stringEntity = new StringEntity(body, "utf-8");
        httpPost.setEntity(stringEntity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String ret = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);

            ret = EntityUtils.toString(response.getEntity(), "utf-8");

            httpClient.close();
        } catch (IOException e) {
            LOG.error("http do post error", e);
        }

        return ret;
    }

    public static <T, V> V httpPost(String url, T param, Class<V> clazz) {
        String response = httpPost(url, param);

        return getResponse(response, clazz);
    }


    public static <T, V> List<V> httpPostReceiveList(String url, T param, Class<V> clazz) {
        String response = httpPost(url, param);

        if (null != response) {
            return JSON.parseArray(response, clazz);
        } else {
            return null;
        }
    }

    private static <T> String httpPost(String url, T param) {
        HttpPost httpPost = new HttpPost(url);
        String response = null;

        try {
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.addHeader("accept", "application/json");
            String message;

            if (param instanceof String) {
                message = param.toString();
            } else {
                message = JSON.toJSONString(param);
            }

            StringEntity content = new StringEntity(message, ContentType.APPLICATION_JSON);
            httpPost.setEntity(content);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                response = null;

                LOG.error("get response error: " + httpResponse.getStatusLine().toString());
            } else {
                response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            LOG.error("io exception: ", e);
            response = null;
        } finally {
            httpPost.releaseConnection();
        }

        return response;
    }


    private static <V> V getResponse(String response, Class<V> clazz) {
        if (null != response) {
            if (clazz.equals(String.class)) {
                return (V) response;
            } else {
                return JSON.parseObject(response, clazz);
            }
        } else {
            return null;
        }

    }


}
