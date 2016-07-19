package uk.co.shenghua.spark;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

public class Client {

    /**
     * Use -Djavax.net.ssl.trustStore=/projectPath/truststore to set the location of the trust store
     *
     * @param args
     */
    public static void main(String[] args) {

        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("https://localhost:4567");

        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = httpClient.executeMethod(getMethod);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            } else {
                String s = getMethod.getResponseBodyAsString();
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
