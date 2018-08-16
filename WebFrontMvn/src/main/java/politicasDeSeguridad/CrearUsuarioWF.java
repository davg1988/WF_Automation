package politicasDeSeguridad;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.UsefulMethodsWF;

public class CrearUsuarioWF {

	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		
		driver = UsefulMethodsWF.setUpWf(); 
		wait = new WebDriverWait(driver, 45);
	}
	
	@Test (priority=4)
	public void PositiveCase() throws Exception {
		
		// Login as an admin
		UsefulMethodsWF.loginAdmin(driver);
		
		// Setting the data provider
		fl = new File("Parametros\\PoliticasDeSeguridad\\politicas.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("CrearUsuarioWF");
		
		//Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
		
		//Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();
		
		//Getting parameters from the excel file
		String user, pass, role, functionality;
		user = sh.getCell(1,7).getContents();
		pass = sh.getCell(2,7).getContents();
		role = sh.getCell(3, 7).getContents();
		functionality = sh.getCell(4, 7).getContents();
		
		//Filling text fields
		List<WebElement> txtFields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		txtFields.get(1).sendKeys(user);
		txtFields.get(2).sendKeys(pass);
		Select roleList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
		roleList.selectByVisibleText(role);
		Select functionalityList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
		functionalityList.selectByVisibleText(functionality);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();

		//Deleting the user created before
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
			WebElement btnInsertar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")));
			btnInsertar.click();
		} catch (Exception e) {
			WebElement btnInsertar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']")));
			btnInsertar.click();
		}

		//Input data for Negative Test Cases
		String user, pass, role, functionality;
		for (int i = 8; i < sh.getRows(); i++) {
			
			//Filling data for negative TC's
			List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 1));
			driver.findElements(By.xpath("//*[@class='z-textbox']")).clear();
			user = sh.getCell(1,i).getContents();
			pass = sh.getCell(2,i).getContents();
			role = sh.getCell(3, i).getContents();
			functionality = sh.getCell(4, i).getContents();
			if (i<=8) {
				fields.get(1).sendKeys(user);
			} else {
				fields.get(2).clear();
				fields.get(2).sendKeys(user);
			}
			driver.findElement(By.xpath("//*[@class='z-textbox' and @type='password']")).sendKeys(pass);
			Select roleList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
			roleList.selectByVisibleText(role);
			Select functionalityList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
			functionalityList.selectByVisibleText(functionality);
		
			//Confirm the input data
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"))).click();			
		}
		
		UsefulMethodsWF.logoutWF(driver);
		driver.close();
	}
}

