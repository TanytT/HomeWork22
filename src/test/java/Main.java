import org.junit.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Main {
    public static WebDriver driver;
    static String mobPhone;
    static String Email;

    WebDriverWait wait;
    @BeforeClass
    public  static void PrecondMain(){
        String curTimeStr = String.valueOf(System.currentTimeMillis());
        curTimeStr = curTimeStr.substring(6,13);
        mobPhone = "38050" +curTimeStr;
        Email = "box"+curTimeStr+"@test.ua";
        System.setProperty("webdriver.gecko.driver","E:\\Study\\Driver\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }
    @Before
    public void Precond(){
        wait = new WebDriverWait(driver,2000);
        driver.get("https://user-data.hillel.it/html/registration.html");
    }

    @Test
    public void Registration(){
        System.out.println("Registration of user");
        driver.findElement(By.cssSelector("[value=\"Registration\"]")).click();
        driver.findElement(By.id("first_name")).sendKeys("Name");
        driver.findElement(By.id("last_name")).sendKeys("LastName");
        driver.findElement(By.id("field_work_phone")).sendKeys("123456");
        driver.findElement(By.id("field_phone")).sendKeys(mobPhone);
        driver.findElement(By.id("field_email")).sendKeys(Email);
        driver.findElement(By.id("field_password")).sendKeys("Rfhfv,f123!");
        driver.findElement(By.id("female")).click();
        driver.findElement(By.cssSelector("[value=\"manager\"]")).click();
        driver.findElement(By.id("button_account")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alertOK = driver.switchTo().alert();
        String ExpMes ="Successful registration";
        Assert.assertEquals("Successful registration failed.",ExpMes,alertOK.getText());
        alertOK.accept();
    }

    @Test
    public void Authorization_Search(){
        System.out.println("Authorization of user");
        driver.findElement(By.name("email")).sendKeys(Email);
        driver.findElement(By.name("password")).sendKeys("Rfhfv,f123!");
        driver.findElement(By.cssSelector("[value=\"Login\"]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id=\"dashboard\"]")));
        String title = driver.getTitle();
        Assert.assertEquals("Successful login failed","Main", title);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[3]"))).click();

        String mail="";
        int i=1;
        String str;
        str="//tr[1]/td[3]";
        while (driver.findElement(By.xpath(str)) != null)
        {
            str="//tr["+Integer.toString(i)+"]/td[3]";
            mail = driver.findElement(By.xpath(str)).getText();
            if(mail.equals(Email)) break;
            i++;
        };
        Assert.assertEquals("User in table failed",Email, mail);

        driver.findElement(By.xpath("//input[@id=\"mobile_phone\"]")).sendKeys(mobPhone);
        driver.findElement(By.xpath("//text[@id=\"search\"]")).click();
        if (driver.findElement(By.xpath("//tr[1]/td[4]")) != null){
            Assert.assertEquals("User search failed",mobPhone, driver.findElement(By.xpath("//tr[1]/td[4]")).getText());
        } else {
            Assert.assertFalse("User search failed (0-elements found)", true);
        }
    }
    @AfterClass
    public static void PostCondMain(){
        System.out.println("Close driver");
        driver.close();
    }
}
