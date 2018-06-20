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

public class ModificarContrasenhaWF {
	
	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		fl = new File("Parametros\\PoliticasDeSeguridad\\politicas.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ModificarContrasenhaWF");
		String url = sh.getCell(1,2).getContents();
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver, 20);
		driver.get(url);
	}
	
	@Test (priority=2)
	public void loginAdmin() {
		
		//Getting admin credentials from excel file
		String adminUser = sh.getCell(1,4).getContents();
		String adminPass = sh.getCell(2,4).getContents();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@title='Introduzca el Nombre de Usuario']"))).sendKeys(adminUser);
		driver.findElement(By.xpath("//*[@class='textlogin z-textbox' and @title='Tipee la Contraseña del Usuario']")).sendKeys(adminPass);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
	}	
	
	@Test (priority=3)
	public void navigation() throws InterruptedException {
		
		//Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
		
		//Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();
	}
	
	@Test (priority=4)
	public void userToModify() throws Exception {

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
	}
	
	@Test (priority=5)
	public void positiveCase() throws Exception {
		
		//Getting data from excel file
		String pass, testCase;
		pass = sh.getCell(2,8).getContents();	
		testCase = sh.getCell(5,8).getContents();
		
		int btnModificar = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']"),1)).size();
		driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']")).get(btnModificar-1).click();

		List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		fields.get(2).clear();
		fields.get(2).sendKeys(pass);
		
		//Confirm the input data
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			
		//Take screenshot of the Message
		ScreenShot.takeSnapShot(driver, "Evidencia\\PoliticasDeSeguridad\\"+testCase+".png");

	}
	
	@Test (priority=6)
	public void negativeCase() throws Exception {

		int btnModificar = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']"),1)).size();
		driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']")).get(btnModificar-1).click();
		
		for (int i = 9; i < sh.getRows(); i++) {
			
			//Getting data from excel file
			String user, testCase;
			user = sh.getCell(2,i).getContents();
			testCase = sh.getCell(5, i).getContents();
			
			if(i<10) {
				List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"),2));
				fields.get(2).clear();
				fields.get(2).sendKeys(user);
			} else {
				List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"),2));
				fields.get(3).clear();
				fields.get(3).sendKeys(user);
			}
	
			//Confirm the input data
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();			
				
			//Take screenshot of the Message
			ScreenShot.takeSnapShot(driver, "Evidencia\\PoliticasDeSeguridad\\"+testCase+".png");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"))).click();

		}
	}
	
	@Test (priority=7)
	public void eraseUserToModify() throws InterruptedException {

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']"))).click();
		try {
			int btnEliminar = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"), 1)).size();
			driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']")).get(btnEliminar-2).click();
		} catch (Exception e) {
			int btnEliminar = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"), 1)).size();
			driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']")).get(btnEliminar-2).click();
		}
		List<WebElement> btnYes = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"), 0));
		btnYes.get(0).click();
		
		UsefulMethodsWF.logoutWF(driver);
	}
}
