package xyz.gnarbot.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class PropertiesManager
{
    private final String path;
    private final String fileName;
    private final Object anchor;
    private final boolean readOnly;
    private Properties prop;
    private Properties defaultProp;
    private File file;
    
    public PropertiesManager()
    {
        this(null, null, null, true);
    }
    
    public PropertiesManager(Object anchor, String path, String fileName)
    {
        this(anchor, path, fileName, false);
    }
    
    private PropertiesManager(Object anchor, String path, String fileName, boolean readOnly)
    {
        this.anchor = anchor;
        this.path = path;
        this.fileName = fileName;
        this.readOnly = readOnly;
    }
    
    public void loadToFile()
    {
        loadToFile(this.file, anchor.getClass().getResourceAsStream(this.path));
    }
    
    //create a file in working directory, write the default file to file, and load from said file
    private void loadToFile(File file, InputStream is)
    {
        try
        {
            if (file == null)
            {
                file = new File(fileName);
                
                if (!file.exists())
                {
                    if (file.mkdirs())
                    {
                        System.out.println(String.format("Making directory for file '%s'.", fileName));
                    }
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    
                    System.out.println(String.format("Copying file '%s' from jar to disk.", fileName));
                }
                this.file = file;
            }
            
            load(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    //load Property from InputStream
    public PropertiesManager load(InputStream is)
    {
        try
        {
            prop = new Properties();
            prop.load(is);
            is.close();
            return this;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    //load Property from working directory
    public PropertiesManager load(File file)
    {
        try
        {
            return load(new FileInputStream(file));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setDefault(InputStream is)
    {
        try
        {
            defaultProp = new Properties();
            defaultProp.load(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public String get(String key, Object... obj)
    {
        if (prop == null) throw new IllegalStateException("Property is null.");
        String s = prop.getProperty(key);
        
        if (s == null)
        {
            if (defaultProp != null)
            {
                String def = defaultProp.getProperty(key);
                if (def != null)
                {
                    return def;
                }
            }
            return null;
        }
        
        if (obj.length > 0) s = String.format(s, obj);
        return s;
    }
    
    public String getDefault(String key)
    {
        return defaultProp.getProperty(key);
    }
    
    public void set(String key, String value)
    {
        if (readOnly) throw new IllegalStateException("PropertyManager is in read only mode.");
        prop.setProperty(key, value);
    }
    
    public void save()
    {
        if (readOnly) throw new IllegalStateException("PropertyManager is in read only mode.");
        if (file == null)
        {
            throw new IllegalStateException("File is null.");
        }
        try (OutputStream out = new FileOutputStream(file))
        {
            prop.store(out, null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
}
