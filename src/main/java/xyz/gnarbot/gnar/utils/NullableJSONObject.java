package xyz.gnarbot.gnar.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * If value is null/not found, instead of throwing an exception,
 * just return null instead.
 */
public class NullableJSONObject extends JSONObject
{
    public NullableJSONObject()
    {
        super();
    }
    
    public NullableJSONObject(String content)
    {
        super(content);
    }
    
    @Override
    public Object get(String key)
    {
        try
        {
            return super.get(key);
        }
        catch (JSONException e)
        {
            return null;
        }
    }
}
