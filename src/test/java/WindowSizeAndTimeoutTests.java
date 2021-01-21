import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class WindowSizeAndTimeoutTests {
    Logger logger = LogManager.getLogger(WindowSizeAndTimeoutTests.class);
    protected static WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        logger.info("Драйвер успешно запущен");
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер успешно остановлен");
        }
    }

    @Test
    public void checkYandexTitle() {
        driver.manage().window().maximize();
        driver.get("https://yandex.ru");
        logger.info("Открыта страница yandex.ru");

        String expectedTitle = "Яндекс";
        String actualTitle = driver.getTitle();

        Assert.assertEquals(expectedTitle, actualTitle);
        logger.info("Title страницы проверен");
    }

    @Test
    public void checkTelephoneSearchOnTele2Page() {
        driver.get("https://msk.tele2.ru/shop/number");
        logger.info("Открыта страница msk.tele2.ru/shop/number");

        WebDriverWait wait = new WebDriverWait(driver, 7);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#searchNumber")));
        WebElement inputEl = driver.findElement(By.cssSelector("input#searchNumber"));
        inputEl.sendKeys("97");
        logger.info("Введен текст '97' в поле для поиска");

        // Ждем исчезновения спиннера загрузки
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.preloader-icon")));

        WebElement firstTelephone = driver.findElement(By.cssSelector("div.product-group div.bundles-column:first-child div.phone-number-block:first-child span.phone-number > span"));
        String actualTelephone = firstTelephone.getText();
        Assert.assertTrue("Первый телефон содержит цифры 9 и 7",
                actualTelephone.contains("9") && actualTelephone.contains("7"));
        logger.info("Проверен телефон " + actualTelephone + " на наличие цифр 9 и 7");
    }
}
