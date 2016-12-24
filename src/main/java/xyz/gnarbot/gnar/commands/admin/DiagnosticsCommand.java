package xyz.gnarbot.gnar.commands.admin;

import com.sun.management.OperatingSystemMXBean;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

@Command(
        aliases = {"diag", "diagnostics", "memory"},
        description = "Show current memory usage of GN4R.",
        showInHelp = false
)
public class DiagnosticsCommand extends CommandExecutor
{
    @Override
    public void execute(Note msg, String label, String[] args)
    {
        //msg.replyRaw("```xl\n" + new SystemInfo().osInfo() + "```\n```xl\n" + new SystemInfo().memInfo() + "```");
    
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
        long prevUpTime = runtimeMXBean.getUptime();
        long prevProcessCpuTime = operatingSystemMXBean.getProcessCpuTime();
        double cpuUsage;
        try
        {
            Thread.sleep(500);
        }
        catch (Exception ignored) { }
    
        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long upTime = runtimeMXBean.getUptime();
        long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
        long elapsedCpu = processCpuTime - prevProcessCpuTime;
        long elapsedTime = upTime - prevUpTime;
    
        cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
        System.out.println("Java CPU: " + cpuUsage);
    }
}