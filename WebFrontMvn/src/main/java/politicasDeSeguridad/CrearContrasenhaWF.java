package politicasDeSeguridad;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.ScreenShot;
import utilidades.UsefulMethodsWF;

public class CrearContrasenhaWF {
	
	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		fl = new File("Parametros\\PoliticasDeSeguridad\\politicas.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("CrearContrasenhaWF");
		String url = sh.getCell(1,2).getContents();
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver, 30);
		driver.get(url);
	}
	
	@Test (priority=2)
	public void loginAdmin() {
		
		//Getting admin credentials from excel file
		String adminUser = sh.getCell(1,4).getContents();
		String adminPass = sh.getCell(2,4).getContents();
		
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
	}	
	
	@Test (priority=3)
	public void navigation() {
		
		//Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
		
		//Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();
	}
	
	@Test (priority=4)
	public void PositiveCase() throws Exception {
		
		//Getting parameters from the excel file
		String user, pass, role, functionality, testCase;
		user = sh.getCell(1,7).getContents();
		pass = sh.getCell(2,7).getContents();
		role = sh.getCell(3, 7).getContents();
		functionality = sh.getCell(4, 7).getContents();
		testCase = sh.getCell(5, 7).getContents();
		
		List<WebElement> txtFields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		txtFields.get(1).sendKeys(user);
		txtFields.get(2).sendKeys(pass);
		Select roleList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
		roleList.selectByVisibleText(role);
		Select functionalityList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
		functionalityList.selectByVisibleText(functionality);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		ScreenShot.takeSnapShot(driver, "Evidencia\\PoliticasDeSeguridad\\"+testCase+".png");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")));
		int btnSize = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"), 1)).size();
		driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']")).get(btnSize-2).click();
		List<WebElement> btnOK = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"), 0));
		btnOK.get(0).click();
	}
	
	@Test (priority=5)
	public void NegativeCase() throws Exception {

		//Click on Insertar button
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")));
			driver.findElement(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")).click();
		} catch (Exception e) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")));
			driver.findElement(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")).click();
		}
		
		//Loop for Negative Test Cases
		for (int i = 8; i < 13; i++) {
			
			//Getting data from excel file
			String user, pass, testCase;
			user = sh.getCell(1,i).getContents();
			pass = sh.getCell(2,i).getContents();	
			testCase = sh.getCell(5, i).getContents();
			
			List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 1));
			driver.findElements(By.xpath("//*[@class='z-textbox']")).clear();
			if (i<9) {
				fields.get(1).sendKeys(user);
			} else {
				fields.get(2).clear();
				fields.get(2).sendKeys(user);
			}
			driver.findElement(By.xpath("//*[@class='z-textbox' and @type='password']")).sendKeys(pass);
			Select listRole = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
			listRole.selectByVisibleText("User");
			Select listFunctionality = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
			listFunctionality.selectByVisibleText("General");
		
			//Confirm the input data
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			ScreenShot.takeSnapShot(driver, "Evidencia\\PoliticasDeSeguridad\\"+testCase+".png");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"))).click();

		} // End of the loop for negative test cases
		
		UsefulMethodsWF.logoutWF(driver);
	}
}
