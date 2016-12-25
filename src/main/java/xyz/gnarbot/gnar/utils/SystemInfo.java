package xyz.gnarbot.gnar.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;

public class SystemInfo
{
    private final Runtime runtime = Runtime.getRuntime();
    
    public String osName()
    {
        return System.getProperty("os.name");
    }
    
    public String osVersion()
    {
        return System.getProperty("os.version");
    }
    
    public String osArch()
    {
        return System.getProperty("os.arch");
    }
    
    public long totalMem()
    {
        return Runtime.getRuntime().totalMemory();
    }
    
    public long usedMem()
    {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
    public String memInfo()
    {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("[Free] ").append(format.format(freeMemory / 1024));
        sb.append("\n");
        sb.append("[Allocated] ").append(format.format(allocatedMemory / 1024));
        sb.append("\n");
        sb.append("[Max] ").append(format.format(maxMemory / 1024));
        sb.append("\n");
        sb.append("[Total] ").append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        sb.append("\n");
        return sb.toString();
        
    }
    
    public String osInfo()
    {
        return
                "[OS] " + this.osName() + "\n" +
                "[Version] " + this.osVersion() + "\n" +
                "[Arch] " + this.osArch() + "\n" +
                "[Cores] " + runtime.availableProcessors() + "\n";
    }
    
    public void printUsage()
    {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods())
        {
            method.setAccessible(true);
            if (method.getName().startsWith("get")
                    && Modifier.isPublic(method.getModifiers()))
            {
                Object value;
                try
                {
                    value = method.invoke(operatingSystemMXBean);
                }
                catch (Exception e)
                {
                    value = e;
                } // try
                System.out.println(method.getName() + " = " + value);
            } // if
        } // for
    }
}