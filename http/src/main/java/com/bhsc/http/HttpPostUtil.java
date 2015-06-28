package com.bhsc.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpPostUtil {

  private static CloseableHttpClient DEFAULT_CLIENT = HttpClients.createDefault();

  private static ResponseHandler<String> STRING_HANDLER = new ResponseHandler<String>() {
    @Override
    public String handleResponse(final HttpResponse response) throws IOException {
      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      if (statusLine.getStatusCode() >= 300) {
        throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
      }
      if (entity == null) {
        throw new ClientProtocolException("Response contains no content");
      }
      return EntityUtils.toString(entity);
    }
  };
  
  /**
   * send post reqeust
   * 
   * @param url
   * @param params
   * @param httpclient
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static String requestString(String url, Map<String, String> params,
      CloseableHttpClient httpclient)
      throws ClientProtocolException,
      IOException {
    return requestHandler(url, params, httpclient, STRING_HANDLER);
  }

  public static <T> T requestHandler(String url, Map<String, String> params,
      ResponseHandler<T> handler)
      throws ClientProtocolException, IOException {
    return requestHandler(url, params, null, handler);
  }

  public static <T> T requestHandler(String url, Map<String, String> params,
      CloseableHttpClient httpclient, ResponseHandler<T> handler)
      throws ClientProtocolException, IOException {
    if (handler == null) {
      throw new RuntimeException("handler can't be null");
    }
    if (httpclient == null) {
      httpclient = DEFAULT_CLIENT;
    }
    HttpPost httppost = new HttpPost(url);
    if (params != null && params.size() > 0) {
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      for (Map.Entry<String, String> entry : params.entrySet()) {
        formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
      }
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
      httppost.setEntity(entity);
    }
    return httpclient.execute(httppost, handler);
  }


}
