package abmPosOperator;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.RServiceClientFactory;

public class ValidacionCampos {

	WebDriver driver;
	WebDriverWait wait;
	Sheet sh;
	Workbook wb;
	File fl;
	RServiceClientFactory factory;
	
	@Test (priority=2)
	public void validateFields() throws Exception {
		
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver,45);
		
		factory = new RServiceClientFactory();
		
		// Create WebFront test user
		//UsefulMethodsWF.createWFTestUser(driver); // -----> Uncomment this line when executing this class alone
		
		UsefulMethodsWF.loginWFTestUser(driver);
		
		//Navigate to mantenimiento de usuarios
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
		
		//Extracting parameters from Excel file
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ValidarCampos");
		
		for (int i = 10; i < sh.getRows(); i++) {
		
			// Obtain Parameters From Excel File
			String role = sh.getCell(1,i).getContents();
			String username = sh.getCell(2,i).getContents();
			String login = sh.getCell(3,i).getContents();
			
			//Insertion of new user
			//Click on Insertar button
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("z-button-cm"), 2));
			driver.findElements(By.className("z-button-cm")).get(1).click();
			
			//Selecting the role of the operator
			wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
			Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
			select.selectByVisibleText(role);
			
			//Filling the name field
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).sendKeys(username);
			
			//Filling the login field
			driver.findElement(By.xpath("//*[@class='z-textbox' and @maxlength='"+UsefulMethodsWF.getVisibleDigits()+"']")).clear();
			driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus']")).sendKeys(login);
			
			if(role.equals("Supervisor")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-intbox' and @maxlength='3']"))).clear();
				String level = sh.getCell(4, i).getContents();
				driver.findElement(By.xpath("//*[@class='z-intbox z-intbox-focus']")).sendKeys(level);
			}
			
			//Click on Confirmar button
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']"))).click();
			String window_message = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath
					("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']"
							+ "//*[@class='z-window-highlighted-header z-window-highlighted-header-move']"))).getText();
			Assert.assertEquals("Error", window_message);
			
			//Click on OK
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
			
			//Click on Cancelar
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("z-modal-mask")));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']"))).click();
			
			//Check that the fields for filling the information of the pos operator disappears
			//before inserting a new one
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']")));
		}	
		UsefulMethodsWF.logoutWF(driver);
		//UsefulMethodsWF.deleteWFTestUser(driver); // -----> Uncomment this line when executing this class alone
		driver.close(); // -----> Comment this line when executing this class alone
	}
}
