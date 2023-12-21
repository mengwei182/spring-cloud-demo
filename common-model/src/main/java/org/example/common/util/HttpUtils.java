package org.example.common.util;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class HttpUtils {
    private static final CloseableHttpClient HTTP_CLIENT;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setConnectTimeout(Timeout.ofMinutes(5)).build());
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(Timeout.ofMinutes(5)).setResponseTimeout(Timeout.ofMinutes(50)).build();
        HTTP_CLIENT = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
    }

    private HttpUtils() {
    }

    private static HttpGet buildHttpGet(String url) {
        return new HttpGet(url);
    }

    private static HttpPost buildHttpPost(String url) {
        return new HttpPost(url);
    }

    private static HttpPost buildHttpPost(String url, String body) {
        HttpPost httpPost = buildHttpPost(url);
        httpPost.setEntity(new StringEntity(body));
        return httpPost;
    }

    private static HttpPost buildHttpPost(String url, Map<String, Object> body) {
        HttpPost httpPost = buildHttpPost(url);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                entityBuilder.addTextBody(key, (String) value);
            }
            if (value instanceof File) {
                entityBuilder.addBinaryBody(key, (File) value);
            }
        }
        httpPost.setEntity(entityBuilder.build());
        return httpPost;
    }

    public static String get(String url) throws Exception {
        HttpGet httpGet = buildHttpGet(url);
        return HTTP_CLIENT.execute(httpGet, new BasicHttpClientResponseHandler());
    }

    public static ClassicHttpResponse getHttpResponse(String url) throws Exception {
        HttpGet httpGet = buildHttpGet(url);
        CommonHttpClientResponseHandler responseHandler = new CommonHttpClientResponseHandler();
        ClassicHttpResponse response = HTTP_CLIENT.execute(httpGet, responseHandler);
        response.setEntity(new ByteArrayEntity(responseHandler.getContent(), ContentType.WILDCARD));
        return response;
    }

    public static String post(String url) throws Exception {
        HttpPost httpPost = buildHttpPost(url);
        return HTTP_CLIENT.execute(httpPost, new BasicHttpClientResponseHandler());
    }

    public static String post(String url, String body) throws Exception {
        HttpPost httpPost = buildHttpPost(url, body);
        return HTTP_CLIENT.execute(httpPost, new BasicHttpClientResponseHandler());
    }

    public static String post(String url, Map<String, Object> body) throws Exception {
        HttpPost httpPost = buildHttpPost(url, body);
        return HTTP_CLIENT.execute(httpPost, new BasicHttpClientResponseHandler());
    }

    public static ClassicHttpResponse postHttpResponse(String url) throws Exception {
        HttpPost httpPost = buildHttpPost(url);
        return HTTP_CLIENT.execute(httpPost, new CommonHttpClientResponseHandler());
    }

    public static ClassicHttpResponse postHttpResponse(String url, String body) throws Exception {
        HttpPost httpPost = buildHttpPost(url, body);
        return HTTP_CLIENT.execute(httpPost, new CommonHttpClientResponseHandler());
    }

    public static ClassicHttpResponse postHttpResponse(String url, Map<String, Object> body) throws Exception {
        HttpPost httpPost = buildHttpPost(url, body);
        CommonHttpClientResponseHandler responseHandler = new CommonHttpClientResponseHandler();
        ClassicHttpResponse response = HTTP_CLIENT.execute(httpPost, responseHandler);
        response.setEntity(new ByteArrayEntity(responseHandler.getContent(), ContentType.WILDCARD));
        return response;
    }

    public static void download(String url, String filepath) throws Exception {
        HttpGet httpGet = buildHttpGet(url);
        HTTP_CLIENT.execute(httpGet, new FileHttpClientResponseHandler(filepath));
    }

    @Getter
    static class CommonHttpClientResponseHandler implements HttpClientResponseHandler<ClassicHttpResponse> {
        private byte[] content;

        @Override
        public ClassicHttpResponse handleResponse(ClassicHttpResponse response) throws IOException {
            this.content = response.getEntity().getContent().readAllBytes();
            return response;
        }
    }

    static class FileHttpClientResponseHandler implements HttpClientResponseHandler<Void> {
        private final String filepath;

        public FileHttpClientResponseHandler(String filepath) {
            this.filepath = filepath;
        }

        @Override
        public Void handleResponse(ClassicHttpResponse classicHttpResponse) throws IOException {
            FileOutputStream fos = new FileOutputStream(this.filepath);
            InputStream is = classicHttpResponse.getEntity().getContent();
            IoUtil.copy(is, fos);
            IoUtil.close(fos);
            IoUtil.close(is);
            return null;
        }
    }
}