import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.util.List;

import static java.net.Inet4Address.*;


public class demo {
    public static void main(String args[]) {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);

//        InetAddress serverAddress = new InetAddress()
        proxy.start(8080);
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        String localIPAddress="";
        try {
            localIPAddress = getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy("192.168.0.105:"+proxy.getPort());
            seleniumProxy.setSslProxy("192.168.0.105:"+proxy.getPort());
            System.out.println(localIPAddress+proxy.getPort());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(proxy.isStarted());
//        InternetExplorerDriver ieDriver = new InternetExplorerDriver();
//        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\heet1\\AppData\\Roaming\\npm\\node_modules\\protractor\\node_modules\\webdriver-manager\\selenium\\chromedriver_2.34.exe");
//        options.setCapability(CapabilityType.PROXY, seleniumProxy);
        options.setProxy(seleniumProxy);
        options.setCapability("network.proxy.http", localIPAddress);
        options.setCapability("network.proxy.http_port", proxy.getPort());
//        ChromeDriverService service =
//                new ChromeDriverService.Builder().withWhitelistedIps("").withVerbose(true).build();
                proxy.newHar("zerohedge.har");
        ChromeDriver driver = new ChromeDriver( options);
        driver.get("https://www.google.com");
        Har har = proxy.getHar();
        List<HarEntry> entries = proxy.getHar().getLog().getEntries();
        System.out.println(entries.size());
        for(HarEntry entry: entries){
            System.out.println(entry.getRequest().getUrl());
        }
        File harFile = new File("D:\\code\\test_har.har");

        try {
            har.writeTo(harFile);
            proxy.stop();
            Thread.sleep(5000);
            driver.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }

}
