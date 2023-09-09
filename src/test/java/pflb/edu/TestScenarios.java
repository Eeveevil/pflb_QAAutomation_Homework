package pflb.edu;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pflb.edu.config.ApplicationConfig;
import pflb.edu.page.LoginPage;
import pflb.edu.page.SortUsersPage;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestScenarios {

    private RemoteWebDriver driver;
    private ApplicationConfig config;
    private WebDriverWait wait;

    @BeforeAll
    public void configInit() {
        config = new ApplicationConfig();
    }


    @BeforeEach
    public void init() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

//    @Test
//    public void loginTest() throws InterruptedException {
//        driver.get(config.baseUrl);
//        WebElement loginInput = driver.findElement(By.xpath("//input[@name = 'email']"));
//        WebElement passwordInput = driver.findElement(By.xpath("//input[@name = 'password']"));
//        WebElement subButton = driver.findElement(By.xpath("//button[@class='Nav-btn btn btn-primary']"));
//
//        loginInput.sendKeys(config.username);
//        passwordInput.sendKeys(config.userPassword);
//        subButton.click();
//
////        String alertText = "";
//        boolean exitFlag = false;
//
//        String alertText = wait.until(driver -> {
//            Alert alert = driver.switchTo().alert();
//            String text = alert.getText();
//            alert.dismiss();
//            return text;
//        });

        @Test
        public void RestAssureLoginTest() throws InterruptedException {
            RestAssured.baseURI = "http://77.50.236.203:4879/";

            String username = config.username;
            String password = config.userPassword;
            String requestBody = String.format("{\"password\":\"%s\",\"username\":\"%s\"}", password, username);

            String response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("/login")
                    .then()
                    .extract()
                    .response()
                    .asString();

            System.out.println(response);

            Assert.assertTrue(response.contains("access_token"));

            driver.get(config.baseUrl);
            WebElement loginInput = driver.findElement(By.xpath("//input[@name = 'email']"));
            WebElement passwordInput = driver.findElement(By.xpath("//input[@name = 'password']"));
            WebElement subButton = driver.findElement(By.xpath("//button[@class='Nav-btn btn btn-primary']"));
            loginInput.sendKeys(config.username);
            passwordInput.sendKeys(config.userPassword);
            subButton.click();

            System.out.println("Success");


    }

    private LoginPage login(){
        driver.get(config.baseUrl);
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.fillLoginInput(config.username);
        loginPage.fillPasswordInput(config.userPassword);
        loginPage.submitForm();
        loginPage.dismissAlert();
        return new LoginPage(driver, wait);
    }


//    @Test
//    public void loginTestUsingPageObject() {
//        driver.get(config.baseUrl);
//        LoginPage loginPage = new LoginPage(driver, wait);
//        loginPage.fillLoginInput(config.username);
//        loginPage.fillPasswordInput(config.userPassword);
//        loginPage.submitForm();
//        String alertText = loginPage.getAlertText();
//        Assertions.assertTrue(alertText.contains("Successful"), "Test doesn't contains about successful auth");
//        loginPage.dismissAlert();
//    }

    @Test
    public void usersSortTest() {
        var loginPage = this.login();
//        loginPage.setUsersButton();
//        loginPage.setReadAllButton();
        loginPage.openOption("Users", "Read all");
        var userPage = new SortUsersPage(driver, wait);
        userPage.SortByIdButton(true);
        Assertions.assertTrue(userPage.checkSortedUsers(true), "Сортировка по возрастанию");
        userPage.cancelSorting("ID");
        userPage.SortByIdButton(false);
        Assertions.assertTrue(userPage.checkSortedUsers(false), "Сортировка по убыванию");
//        driver.get(config.usersUrl);
//        SortUsersPage sortUsersPage = new SortUsersPage(driver, wait);
//        sortUsersPage.SortByAgeButton();
//        driver.get(String.valueOf(By.xpath("//table[@class='table table-striped table-bordered table-hover']//tbody//tr//td")));
//        System.out.println("Sort by age Success");
//        sortUsersPage.SortByIdButton(true);
//        sortUsersPage.SortBySexButton();
//        sortUsersPage.SortByLastNameButton();
//        sortUsersPage.SortByMoneyButton();

    }

}
