package xyz.gnarbot.gnar.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Utils
{
    public static JSONObject information;
    
//    static
//    {
//        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
//        {
//            public X509Certificate[] getAcceptedIssuers()
//            {
//                return null;
//            }
//
//            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
//
//            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
//        }};
//
//        try
//        {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        }
//        catch (Exception ignore) {}
//    }
    
    
    
    public static void setLeagueInfo()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File("_DATA/league/League.txt")));
            
            String info = "";
            
            String line;
            while ((line = br.readLine()) != null)
            {
                info += line;
            }
            
            information = new JSONObject(info);
        }
        catch (Exception ignore) { }
    }
    
}
