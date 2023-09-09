//package org.example;
//
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.edge.EdgeOptions;
//
//
//// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
//// then press Enter. You can now see whitespace characters in your code.
//public class Main {
//@Test
//    public void ChromeDriver() throws InterruptedException {
//    ChromeOptions chromeOptions = new ChromeOptions();
//    chromeOptions.addArguments("--remote-allow-origins=*");
//    ChromeDriver driver = new ChromeDriver(chromeOptions);
//    driver.get("http://77.50.236.203:4881/");
//    Thread.sleep(1000);
//    WebElement element = driver.findElement(By.xpath("//input[@name = 'email']"));
//    element.click();
//    System.out.println();
//    driver.close();
//    }
//}