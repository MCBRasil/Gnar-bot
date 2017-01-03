package xyz.gnarbot.gnar.commands.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Command(aliases = {"eyes", "googleyeyes"}, usage = "(image url)", description = "Put weird eyes on people!")
public class GoogleyEyesCommand extends CommandExecutor
{
    private static BufferedImage resize(BufferedImage img, int newW, int newH)
    {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return dimg;
    }
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0)
        {
            note.replyError("Please provide an image link.");
            return;
        }
        
        try
        {
            String urlStr = args[0];
            String encodedStr = URLEncoder.encode(urlStr, StandardCharsets.UTF_8.displayName());
            
            HttpResponse<JsonNode> response = Unirest.get("https://apicloud-facerect.p.mashape.com/process-url" +
                    ".json?features=true&url=" + encodedStr).header("X-Mashape-Key", Bot.INSTANCE.getAuthTokens()
                    .getProperty("mashape")).header("Accept", "application/json").asJson();
            
            
            JSONObject j = new JSONObject(response.getBody().toString());
            
            List<JSONObject> eyesJSON = new ArrayList<>();
            
            JSONArray j2 = (JSONArray) j.get("faces");
            
            try
            {
                for (int in = 0; in < j2.length(); in++)
                {
                    JSONObject j3 = (JSONObject) j2.get(in);
                    JSONObject j4 = (JSONObject) j3.get("features");
                    JSONArray j5 = (JSONArray) j4.get("eyes");
                    
                    for (int i = 0; i < j5.length(); i++)
                    {
                        eyesJSON.add(new JSONObject(j5.get(i).toString()));
                    }
                }
            }
            catch (Exception e)
            {
                if (eyesJSON.isEmpty())
                {
                    note.replyError("The API did not detect any eyes/facial features.");
                    return;
                }
            }
            
            //set connection header so access wont get forbidden'ed
            URLConnection uCon = new URL(urlStr).openConnection();
            uCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, " +
                    "like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            uCon.connect();
            
            BufferedImage targetImg = ImageIO.read(uCon.getInputStream());
            BufferedImage eye, resizedEye;
            Graphics graphics = targetImg.getGraphics();
            for (JSONObject json : eyesJSON)
            {
                eye = ImageIO.read(new File("_DATA/resources/eye" + new Random().nextInt(2) + ".png"));
                resizedEye = resize(eye, (int) json.get("width"), (int) json.get("height"));
                graphics.drawImage(resizedEye, (int) json.get("x"), (int) json.get("y"), null);
            }
            
            //writing the file
            File outputFile = new File("_DATA/output_image.png");
            try
            {
                if (ImageIO.write(targetImg, "jpg", outputFile))
                {
                    //send if success
                    note.getChannel().sendFile(outputFile, null);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                //noinspection ResultOfMethodCallIgnored
                outputFile.delete();
            }
            
            //event.getChannel().sendMessage(mb.build());
        }
        catch (Exception e)
        {
            note.reply("An unexpected error occurred, did you provide a proper link?");
            e.printStackTrace();
        }
    }
}
