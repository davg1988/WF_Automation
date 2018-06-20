package articlesAbm;

import javax.ws.rs.WebApplicationException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class createArticles {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@Test (priority=1)
	public void launchWF() {
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver,20);
	}
}
