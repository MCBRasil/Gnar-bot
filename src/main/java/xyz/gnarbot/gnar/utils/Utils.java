package xyz.gnarbot.gnar.utils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

public class Utils
{
    static
    {
        TrustManager[] trustAllCerts = new TrustManager[]
        {
            new X509TrustManager()
            {
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
                
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };
        
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception ignore) {}
    }
    
    private static String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1)
        {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public static JSONObject jsonFromUrl(String url) throws JSONException
    {
        URLConnection conn;
        try
        {
            conn = new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Gnar");
            conn.connect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        try (InputStream is = conn.getInputStream())
        {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        
//        try
//        {
//            HttpResponse<String> response = Unirest.get(url)
//                    .header("User-Agent", "Discord Bot")
//                    .asString();
//
//            System.out.println(response.toString());
//
//            return new JSONObject(response.getBody().toString());
//        }
//        catch (Exception e)
//        {
//            return null;
//        }
    }
}
