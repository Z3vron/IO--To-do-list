import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class Tests
{
    private WebDriver driver;
    private final String source = "http://localhost:8000/";
    private final String studentEmail = "adam.kowalski@example.com";
    private final String studentPassword = "Kowalski123!";
    private final String cpEmail = "michal.wojciechowski@example.com";
    private final String cpPassword = "Wojciechowski!123";
    private List<String> sem1Subjects = Arrays.asList("Matematyka 1", "Matematyka dyskretna", "Algebra wyższa", "Repetytorium z fizyki", "Podstawy informatyki", "Podstawy systemów operacyjnych/UNIX", "Wychowanie fizyczne 1");
    private List<String> sem1Sub1tasks = Arrays.asList("Kolokwium 1", "Kolokwium 2");

    @Before
    public void setUp()
    {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void Login_Redirect()
    {
        driver.get(source + "login.html");
        driver.findElement(By.linkText("Stwórz konto")).click();
        assertEquals("Zarejestruj się", driver.findElement(By.cssSelector(".display-2")).getText());
    }

    @Test
    public void Login_WrongPass()
    {
        driver.get(source + "login.html");
        driver.findElement(By.id("email")).sendKeys("qwerty");
        driver.findElement(By.id("password")).sendKeys("qwerty");
        driver.findElement(By.id("login_button")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        assertEquals("Coś poszło nie tak...", alert.getText());
        alert.accept();
    }

    @Test
    public void Login_LoginAsS()
    {
        driver.get(source + "login.html");
        driver.findElement(By.id("email")).sendKeys(studentEmail);
        driver.findElement(By.id("password")).sendKeys(studentPassword);
        driver.findElement(By.id("login_button")).click();

        assertEquals("Panel studenta", driver.findElement(By.cssSelector(".display-2")).getText());
    }

    @Test
    public void Login_LoginAsCP()
    {
        driver.get(source + "login.html");
        driver.findElement(By.id("email")).sendKeys(cpEmail);
        driver.findElement(By.id("password")).sendKeys(cpPassword);
        driver.findElement(By.id("login_button")).click();

        assertEquals("Panel starosty", driver.findElement(By.cssSelector(".display-2")).getText());
    }

    @Test
    public void Register_Duplicate()
    {
        driver.get(source + "regestration.html");
        driver.findElement(By.id("first_name")).sendKeys("Adam");
        driver.findElement(By.id("last_name")).sendKeys("Kowalski");
        driver.findElement(By.id("student_id")).sendKeys("423013");
        driver.findElement(By.id("email")).sendKeys(studentEmail);
        driver.findElement(By.id("password")).sendKeys("Passw0rd");
        driver.findElement(By.id("register_button")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        assertEquals("Coś poszło nie tak...", alert.getText());
        alert.accept();
    }

    @Test
    public void Register_Success()
    {
        driver.get(source + "regestration.html");
        driver.findElement(By.id("first_name")).sendKeys("Adam");
        driver.findElement(By.id("last_name")).sendKeys("Kowalski");
        int index = (int) ((Math.random() * (499999 - 400000)) + 400000);
        driver.findElement(By.id("student_id")).sendKeys(String.valueOf(index));
        driver.findElement(By.id("email")).sendKeys(String.valueOf(index) + "@example.com");
        driver.findElement(By.id("password")).sendKeys("Passw0rd");
        driver.findElement(By.id("register_button")).click();

        assertEquals("Panel studenta", driver.findElement(By.cssSelector(".display-2")).getText());
    }

    private void loginAsS()
    {
        driver.get(source + "login.html");
        driver.findElement(By.id("email")).sendKeys(studentEmail);
        driver.findElement(By.id("password")).sendKeys(studentPassword);
        driver.findElement(By.id("login_button")).click();
    }

    @Test
    public void StudentPanel_LoadingSubjects()
    {
        loginAsS();
        List<String> subjects = new ArrayList<>();

        boolean stop = true;
        int i = 1;
        do
        {
            try
            {
                subjects.add(driver.findElement(By.xpath("//tr[@id='" + String.valueOf(i) + "']/td")).getText());
                i++;
            }
            catch (Exception e)
            {
                stop = false;
            }
        }
        while (stop);

        assertEquals(sem1Subjects, subjects);
    }

    @Test
    public void StudentPanel_CheckingTasks()
    {
        loginAsS();

        List<String> tasks = new ArrayList<>();

        assertEquals("0%", driver.findElement(By.xpath("//tr[@id='1']/td[2]/div/div")).getText());

        boolean stop = true;
        int i = 1;
        do
        {
            try
            {
                driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/button")).click();
                driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[" + String.valueOf(i) + "]/div/input")).click();
                tasks.add(driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[" + String.valueOf(i) + "]/div/label")).getText());
                driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[4]/div/button")).click();
                String curr = driver.findElement(By.xpath("//tr[@id='1']/td[2]/div/div")).getText();
                new WebDriverWait(driver, 5L).until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(By.xpath("//tr[@id='1']/td[2]/div/div"), curr)));
                i++;
            }
            catch (Exception e)
            {
                stop = false;
            }
        }
        while (stop);

        driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/button")).click();
        for (int j = 1; j < i; j++)
        {
            driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[" + String.valueOf(j) + "]/div/input")).click();
        }
        driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[4]/div/button")).click();
        new WebDriverWait(driver, 5L).until(ExpectedConditions.textToBe(By.xpath("//tr[@id='1']/td[2]/div/div"), "0%"));
        assertEquals("0%", driver.findElement(By.xpath("//tr[@id='1']/td[2]/div/div")).getText());

        for (String task : tasks)
        {
            assertTrue(sem1Sub1tasks.contains(task));
        }
    }

    private void loginAsCP()
    {
        driver.get(source + "login.html");
        driver.findElement(By.id("email")).sendKeys(cpEmail);
        driver.findElement(By.id("password")).sendKeys(cpPassword);
        driver.findElement(By.id("login_button")).click();
    }

    @Test
    public void CPPanel_AddingAndRemovingTasks()
    {
        loginAsCP();

        List<String> tasks1 = new ArrayList<>();
        List<String> tasks2 = new ArrayList<>();
        List<String> tasks3 = new ArrayList<>();

        driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/button")).click();
        boolean stop = true;
        int i = 1;
        do
        {
            try
            {
                tasks1.add(driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[" + String.valueOf(i) + "]/div/label")).getText());
                i++;
            }
            catch (Exception e)
            {
                stop = false;
            }
        }
        while (stop);

        System.out.println(tasks1);

        driver.findElement(By.xpath("//td[4]/button")).click();
        driver.findElement(By.id("task_name_input")).click();
        driver.findElement(By.id("task_name_input")).sendKeys("Kolokwium 3");
        driver.findElement(By.id("task_description_input")).click();
        driver.findElement(By.id("task_description_input")).sendKeys("test");
        driver.findElement(By.id("save_task_btn")).click();

        driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/button")).click();
        stop = true;
        i = 1;
        do
        {
            try
            {
                tasks2.add(driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[" + String.valueOf(i) + "]/div/label")).getText());
                i++;
            }
            catch (Exception e)
            {
                stop = false;
            }
        }
        while (stop);

        System.out.println(tasks2);
        driver.findElement(By.xpath("//td[5]/button")).click();
        driver.findElement(By.xpath("//div[@id='task_data_input']/div/div/div[2]/ul/li[last()]/div/input")).click();
        driver.findElement(By.id("remove_tasks_btn")).click();

        driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/button")).click();
        stop = true;
        i = 1;
        do
        {
            try
            {
                tasks3.add(driver.findElement(By.xpath("//tr[@id='1']/td[3]/div/ul/li[" + String.valueOf(i) + "]/div/label")).getText());
                i++;
            }
            catch (Exception e)
            {
                stop = false;
            }
        }
        while (stop);

        System.out.println(tasks3);

        assertEquals(tasks1.size() + 1, tasks2.size());
        assertEquals(tasks3.size() + 1, tasks2.size());
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }
}
