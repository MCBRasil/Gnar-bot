package xyz.gnarbot.gnar.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * If value is null/not found, instead of throwing an exception,
 * just return null instead. If it's a primitive type, return
 * the default primitive value then.
 */
public class NullableJSON extends JSONObject
{
    public NullableJSON()
    {
        super();
    }
    
    public NullableJSON(String content)
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
    
    @Override
    public int getInt(String key)
    {
        try
        {
            return super.getInt(key);
        }
        catch (JSONException e)
        {
            return 0;
        }
    }
    
    @Override
    public double getDouble(String key)
    {
        try
        {
            return super.getInt(key);
        }
        catch (JSONException e)
        {
            return Double.NaN;
        }
    }
    
    @Override
    public boolean getBoolean(String key)
    {
        try
        {
            return super.getBoolean(key);
        }
        catch (JSONException e)
        {
            return false;
        }
    }
    
    @Override
    public long getLong(String key)
    {
        try
        {
            return super.getLong(key);
        }
        catch (JSONException e)
        {
            return 0;
        }
    }
    
    @Override
    public String getString(String key)
    {
        try
        {
            return super.getString(key);
        }
        catch (JSONException e)
        {
            return null;
        }
    }
}
