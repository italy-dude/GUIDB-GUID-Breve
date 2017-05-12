/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidbreve.guidb.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author mainuser
 */
public class Logger
{

    private org.apache.log4j.Logger log;

    public Logger(Object caller)
    {
        log = org.apache.log4j.Logger.getLogger(caller.getClass());
        // Basic Log4J configuration.
        Properties properties = new Properties();
        properties.setProperty("log4j.rootLogger", "INFO,stdout,ROLLINGFILE");
        properties.setProperty("log4j.appender.stdout", "guidbreve.guidb.logger.ANSIConsoleAppender");
        properties.setProperty("log4j.appender.stdout.Threshold", "INFO");
        properties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
        properties.setProperty("log4j.appender.ROLLINGFILE", "org.apache.log4j.RollingFileAppender");
        properties.setProperty("log4j.appender.ROLLINGFILE.Threshold", "INFO");
        properties.setProperty("log4j.appender.ROLLINGFILE.File", "guidb_error.log");
        properties.setProperty("log4j.appender.ROLLINGFILE.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.ROLLINGFILE.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
        PropertyConfigurator.configure(properties);

        try
        {
            //below is to avoid the odf toolkit to spit out endless logs
            java.util.logging.LogManager.getLogManager().readConfiguration(new java.io.ByteArrayInputStream("org.odftoolkit.level=WARNING".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
    }

    public void info(String message)
    {
        log.info(message);
    }

    public void fatal(String message)
    {
        log.fatal(message);
    }

    public void warn(String message)
    {
        log.warn(message);
    }

    public void debug(String message)
    {
        log.debug(message);
    }

    public void error(String message)
    {
        log.error(message);
    }

    public void error(String message, Exception e)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        log.error(message + " " + stringWriter.toString());
    }
}
