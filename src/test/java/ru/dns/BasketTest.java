package ru.dns;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.lang.Thread.sleep;

public class BasketTest {
    static WebDriver driver;
    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().browserVersion("112").setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 1000));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    /**
     * Тест на добавление товара в корзину
     */
    @Test
    public void testAddBasket() {
        WebElement element = null;

        //открыть карточку товара
        driver.get("https://www.dns-shop.ru/product/3201e84b9b47d721/elektrocajnik-polaris-pwk-1712cgld-wifi-iq-cernyj/");


        //добавить в корзину
        driver.findElement(By.cssSelector(".buy-btn:nth-child(3)")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='app-cart-modal']")));

        //перейти в корзину
        driver.findElement(By.cssSelector(".cart-modal__footer-buttons > .base-ui-button_brand_avQ")).click();

        //перевести тугл о получении товара со склада
        driver.findElement(By.cssSelector(".base-ui-toggle__icon")).click();

        try {
            element = driver.findElement(By.xpath("//nav[@id='header-search']/div/div[3]/div/div/a/span/span"));
        } catch (NoSuchElementException e) {
            Assertions.fail("Элемент не найден на странице");
        }

        //проверки
        String textElement = element.getText();
        String message = String.format("В корзине неверное количество товаров. Ожидалось: %s, Получили: %s", "1", textElement);
        Assertions.assertEquals("1", textElement, message);

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
// Пришлось применить Sleep, так как при добавлении ожидания через WebDriverWait - товар не удаляется и смена страницы не приосходит

        //очистка корзины
        driver.findElement(By.xpath("//div[@id='cart-page-new']/div/div[2]/div/div/div/div/div/div/div/div/div/div/div[3]/div/div[3]/div/div[2]/button[2]/p")).click();

    }

    /**
     * Тест на удаление товара из корзину
     */
    @Test
    public void testDelBasket() {
        WebElement element = null;

        //открыть карточку товара
        driver.get("https://www.dns-shop.ru/product/3201e84b9b47d721/elektrocajnik-polaris-pwk-1712cgld-wifi-iq-cernyj/");

        //добавить в корзину
        driver.findElement(By.cssSelector(".buy-btn:nth-child(3)")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='app-cart-modal']")));

        //очистить корзину
        driver.findElement(By.xpath("//div[@id='app-cart-modal']/div/div/div/div/span[2]")).click();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        WebDriverWait waits = new WebDriverWait(driver, Duration.ofSeconds(15));
//        waits.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='app-cart-modal']")));
//  с 75,76 строками кода товар остается почему то в корзине.

        //перейти в корзину
        driver.findElement(By.xpath("//nav[@id='header-search']/div/div[3]/div/div/a")).click();

        try {
            element = driver.findElement(By.cssSelector(".empty-message__title-empty-cart"));
        } catch (NoSuchElementException e) {
            Assertions.fail("Элемент не найден на странице");
        }

        //проверить наличие заголовка об отсутствии товаров
        String textElement = element.getText();
        Assertions.assertEquals("Корзина пуста", textElement);
    }

    @AfterAll
    public static void close() {
        driver.quit();
    }

}
