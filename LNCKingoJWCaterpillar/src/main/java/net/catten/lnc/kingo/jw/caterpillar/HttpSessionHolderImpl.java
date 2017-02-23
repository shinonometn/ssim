package net.catten.lnc.kingo.jw.caterpillar;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by catten on 2/23/17.
 */
public class HttpSessionHolderImpl implements HttpSessionHolder {

    private String userAgent = "HttpSessionHolder/Apache HttpClient/JRE";
    private String charset = "UTF-8";

    private HttpClient httpClient;

    private HttpContext websiteContext;
    private CookieStore cookieStore;

    public HttpSessionHolderImpl(){
        cookieStore = new BasicCookieStore();
        websiteContext = new BasicHttpContext();
        websiteContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        httpClient = HttpClients.createDefault();
    }

    @Override
    public Document post(String target, String refer, UrlEncodedFormEntity formEntity) throws IOException {
        HttpPost request = new HttpPost(target);
        request.setHeader("Referer", refer);
        request.setHeader("User-Agent", userAgent);
        request.setEntity(formEntity);
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request, websiteContext);
        Document document = Jsoup.parse(response.getEntity().getContent(),charset,target);
        response.close();
        return document;
    }

    @Override
    public Document get(String target, String refer) throws IOException {
        HttpGet request = new HttpGet(target);
        request.setHeader("Referer", refer);
        request.setHeader("User-Agent", userAgent);
        CloseableHttpResponse response = (CloseableHttpResponse)httpClient.execute(request, websiteContext);
        Document document = Jsoup.parse(response.getEntity().getContent(),charset,target);
        response.close();
        return document;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }
}
