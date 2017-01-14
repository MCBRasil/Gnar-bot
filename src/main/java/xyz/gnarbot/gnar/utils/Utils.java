package xyz.gnarbot.gnar.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Utils
{
    public static JSONObject information;
    
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
