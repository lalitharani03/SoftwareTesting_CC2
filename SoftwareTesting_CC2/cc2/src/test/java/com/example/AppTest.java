package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class AppTest 
{
     ExtentReports reports;
     Actions actions;
     XSSFWorkbook workbook;
     ExtentTest test;
     String input;
    WebDriverWait wait;
     WebDriver driver;
     JavascriptExecutor js;
     Logger logger=Logger.getLogger(AppTest.class);
     @BeforeTest
     public void beforeTest() throws IOException{
       String path = "E:\\software testing\\CC2\\cc2\\Excelsheet\\data.xlsx";
       FileInputStream fs = new FileInputStream(path);
       workbook  =  new XSSFWorkbook(fs);
       XSSFSheet sheet = workbook.getSheetAt(0);
        input = sheet.getRow(1).getCell(0).getStringCellValue();
        
        
        reports = new ExtentReports();
        String paths = "E:\\software testing\\CC2\\cc2\\Reports\\report.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(paths);
        reports.attachReporter(spark);
        spark.config().setDocumentTitle("Banersandnoble");
        spark.config().setTheme(Theme.DARK);
        test = reports.createTest("Testcase");
        test.log(Status.PASS, "Well Done");
        
        PropertyConfigurator.configure("E:\\software testing\\CC2\\cc2\\src\\test\\java\\com\\example\\resources\\log4j.properties");
      }
      @BeforeMethod
      public void beforeMethod(){
        driver = new ChromeDriver();
        driver.get("https://www.barnesandnoble.com");
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        actions =  new Actions(driver);
         js = (JavascriptExecutor)driver;
      }
      @Test(priority = 1)
   public void  testSearch(){
        WebElement l = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("All")));
        l.click();
        

        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Books")));
        e.click();
        
        driver.findElement(By.xpath("//*[@id='rhf_header_element']/nav/div/div[3]/form/div/div[2]/div/input[1]")).sendKeys(input);

        driver.findElement(By.xpath("//*[@id='rhf_header_element']/nav/div/div[3]/form/div/span/button")).click();

        WebElement text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/div[2]/div[1]/div[3]/div[2]/div/div/section[1]/section[1]/div/div[1]/div[1]/h1/span")));
        if(text.getText().equals("Chetan Bhagat")){
          System.out.println("Chetan Bhagat is present here");
          test.log(Status.PASS, "Chetan Bhagat is present here");
          logger.info("Chetan Bhagat is present here");
        }
        else{
          test.log(Status.FAIL, "Chetan Bhagat is not present");
          System.out.println("Chetan Bhagat is not present");
          logger.info("Chetan Bhagat is not present");
        }
      }
      @Test(priority = 2)
      public void testAudioBooks() throws InterruptedException{
       
        actions.moveToElement(driver.findElement(By.xpath("//*[@id='rhfCategoryFlyout_Audiobooks']"))).perform();
        Thread.sleep(4000);

        driver.findElement(By.xpath("//*[@id='navbarSupportedContent']/div/ul/li[5]/div/div/div[1]/div/div[2]/div[1]/dd/a[1]")).click();
        Thread.sleep(2000);

        driver.findElement(By.linkText("Funny Story")).click();
        Thread.sleep(2000);

        driver.findElement(By.xpath("//*[@id='otherAvailFormats']/div/div/div[3]")).click();
        Thread.sleep(2000);
        
        driver.findElement(By.xpath("//*[@id=\'addToBagForm_2940159543998\'']/input[11]")).submit();
        String find = driver.switchTo().alert().getText();
        if (find.contains("Item Successfully Added To Your Cart")) {
            System.out.println("Successfully inserted into the cart");
        } else {
            System.out.println("Item not inserted into the cart");
        }


      
      }
      @Test(priority = 3)
      public void testBandN() throws IOException, InterruptedException{

        driver.navigate().to("https://www.barnesandnoble.com/");
        driver.findElement(By.xpath("//*[@id=\"footer\"]/div/dd/div/div/div[1]/div/a[5]")).click();
        Thread.sleep(2000);

        driver.findElement(By.xpath("//*[@id=\"rewards-modal-link\"]")).click();
        Thread.sleep(2000);


        File ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String path = "E:\\software testing\\CC2\\cc2\\screenshot.png";
        FileUtils.copyFile(ss, new File(path));
      }

      @AfterMethod
      public void afterMethod() throws InterruptedException{
        Thread.sleep(5000);
        driver.quit();
      }

      @AfterTest
      public void afterTest(){
        reports.flush();
      }
}
