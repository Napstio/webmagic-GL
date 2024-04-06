package us.codecraft.webmagic.downloader;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午12:27 <br>
 */
public class SeleniumTest {


    @Test
    public void testChromeSelenium() {
        WebDriverManager.chromedriver().setup();
        Map<String, Object> contentSettings = new HashMap<String, Object>();
        contentSettings.put("images", 2);

        Map<String, Object> preferences = new HashMap<String, Object>();
        preferences.put("profile.default_content_settings", contentSettings);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--user-data-dir=" + System.getProperty("user.home") + "/temp/chrome");
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get("http://huaban.com/");
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        System.out.println(webElement.getAttribute("outerHTML"));
        webDriver.close();
    }


    @Test
    public void testFirefoxSelenium() {
        WebDriverManager.firefoxdriver().setup();
        Map<String, Object> contentSettings = new HashMap<String, Object>();
        contentSettings.put("images", 2);

        Map<String, Object> preferences = new HashMap<String, Object>();

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("profile.default_content_setting_values.images", 2);
        options.addArguments("--user-data-dir=" + System.getProperty("user.home") + "/temp/chrome");
        WebDriver webDriver = new FirefoxDriver(options);
        webDriver.get("http://huaban.com/");
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        System.out.println(webElement.getAttribute("outerHTML"));
        webDriver.close();
    }
}
