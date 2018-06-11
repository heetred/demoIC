import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static java.net.InetAddress.getLocalHost;

public class demo1 {
    public static ChromeDriver driver;
    public static BrowserMobProxyServer server;

    @BeforeClass
    public void setup() throws Exception{
        server = new BrowserMobProxyServer();
        server.start();
        Proxy proxy = ClientUtil.createSeleniumProxy(server);
        int port = server.getPort();
        proxy.setHttpProxy(getLocalHost().getHostAddress()+":"+server.getPort());
        proxy.setSslProxy(getLocalHost().getHostAddress()+":"+server.getPort());
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.PROXY, proxy);
        options.setCapability("network.proxy.http", getLocalHost().getHostAddress());
        options.setCapability("network.proxy.http_port", server.getPort());
        System.setProperty("webdriver.chrome.driver", "/usr/local/share/chromedriver");
        driver = new ChromeDriver(options);
        System.out.println("Port started:"+ getLocalHost().getHostAddress());
    }

    @Test
    public void test_run_1() throws InterruptedException {
        server.newHar("testRun1.har");
        driver.get("https://www.zerohedge.com/");
        driver.manage().window().maximize();
        Thread.sleep(11500);
    }

    @AfterClass
    public void teardown() throws Exception{
        Har har = server.getHar();
        File harFile = new File("/home/heet/test_run_1.har");
        har.writeTo(harFile);
        driver.quit();
        server.stop();
    }

}
