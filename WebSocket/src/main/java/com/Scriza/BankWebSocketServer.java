package com.Scriza;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.Scriza.server.QrCodeServerEndpoint;

public class BankWebSocketServer {
    private static final String SCREENSHOT_PATH = "./Screenshots/QRCODE.png";
    private static final String HTML_FILE_PATH = "C:\\Users\\H3RI3R\\eclipse-workspace\\WebSocket\\webroot\\index.html";

    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/selenium WebDriver/chromedriver-win64/chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/");
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        File screenshotFile = new File(SCREENSHOT_PATH);

        openHtmlInBrowser();

        while (true) {
            try {
                WebElement qrElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class ='_akau']")));
                File screenshot = ((TakesScreenshot) qrElement).getScreenshotAs(OutputType.FILE);
                BufferedImage qrImage = ImageIO.read(screenshot);
                ImageIO.write(qrImage, "png", screenshotFile);
                System.out.println("Screenshot taken and saved.");
                QrCodeServerEndpoint.notifyClients();
            } catch (Exception e) {
            	 if (isElementPresent(driver, By.xpath("//span[@data-icon='refresh-large']"))) {
                     WebElement refreshElement = driver.findElement(By.xpath("//span[@data-icon='refresh-large']"));
                     refreshElement.click();
                System.out.println("An error occurred while taking the screenshot: " + e.getMessage());
            	 }
            }
            Thread.sleep(2000);
        }
    }
    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void openHtmlInBrowser() {
        try {
            File htmlFile = new File(HTML_FILE_PATH);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                System.err.println("Desktop is not supported. Cannot open the HTML file in the default browser.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
