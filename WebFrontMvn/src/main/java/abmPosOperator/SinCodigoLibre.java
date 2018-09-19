package abmPosOperator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import junit.framework.Assert;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.CTLLine;
import webFrontCommonUtils.UServiceClientFactory;

public class SinCodigoLibre {
	
	//Objects to handle parameters
	File fl;
	Workbook wb;
	Sheet sh;
	
	//Objects to handle the browser
	WebDriver driver;
	//WebDriver driver;
	WebDriverWait wait;
	String url = "";
	
	// Environment parameters
	String long_login = "";
	
	UServiceClientFactory uFactory;

	@Test (priority=2)
	public void createCashierSupervisor() throws IOException, BiffException {
		
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver, 45);
		
		// Create WebFront test user
		//UsefulMethodsWF.createWFTestUser(driver); // Uncomment this line when executing this class alone
		
		// Getting long of the personnel number
		long_login = UsefulMethodsWF.getLongLogin();
		
		uFactory = new UServiceClientFactory();
		
		// Loop to fill the cashier par of CTL
		for (int i = 1; i < 800; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");;
			String personnel_number = StringUtils.leftPad(String.valueOf(i), 8, "0");;
			String name = "";
			
			if(long_login.equals("8")) {
				name = StringUtils.rightPad("Luis D'Elía", 20, " ") + "xxxxxxxxxx";
			} else {
				name = StringUtils.rightPad("Luis D'Elía", 20, " ") + "0000xxxxxx";
			}
			
			CTLLine line = new CTLLine(operator_code);
			line.setName(name);
			line.setPersonnelNo(personnel_number);
			line.setProfile("001");
			
			uFactory.updateCTLLine(line);
		}
		
		// Loop to fill the supervisor par of CTL
		for (int i = 801; i < 900; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");;
			String personnel_number = StringUtils.leftPad(String.valueOf(i), 8, "0");;
			String name = "";
			
			if(long_login.equals("8")) {
				name = StringUtils.rightPad("José Núñez", 20, " ") + "xxxxxxxxxx";
			} else {
				name = StringUtils.rightPad("José Núñez", 20, " ") + "0000xxxxxx";
			}
			
			CTLLine line = new CTLLine(operator_code);
			line.setName(name);
			line.setPersonnelNo(personnel_number);
			line.setProfile("002");
			
			uFactory.updateCTLLine(line);
		}
		
		// Login as a user
		UsefulMethodsWF.loginWFTestUser(driver);;
		
		// Navigate to Mantenimiento de Usuarios
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
		
		//Click on Insertar button
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("z-button-cm"), 2));
		driver.findElements(By.className("z-button-cm")).get(1).click();
		
		//Selecting the cashier role
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
		Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
		select.selectByVisibleText("Cajero");
		
		//Check if the error message is displayed
		Assert.assertEquals(true, wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//span[@class='z-label' and text()='No hay códigos libres']"))).isDisplayed());
		
		//Click on OK
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
		
		//Selecting the supervisor role
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
		Select select2 = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
		select2.selectByVisibleText("Supervisor");
		
		//Check if the error message is displayed
		Assert.assertEquals(true, wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//span[@class='z-label' and text()='No hay códigos libres']"))).isDisplayed());
		
		//Click on OK
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
		
		//Click on Cancelar
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("z-modal-mask")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']"))).click();
		
		//Logout of WF
		UsefulMethodsWF.logoutWF(driver);
		
		//Loop to delete cashiers from CTL
		for (int i = 1; i < 800; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");
			
			CTLLine line = new CTLLine(operator_code);
			line.setLockIndicator("1");
			uFactory.updateCTLLine(line);
		}
		
		//Loop to delete supervisors from CTL
		for (int i = 801; i < 900; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");
			
			CTLLine line = new CTLLine(operator_code);
			uFactory.updateCTLLine(line);
		}
		
		//UsefulMethodsWF.deleteWFTestUser(driver); // Uncomment this line when executing this class alone
		driver.close(); // Comment this line when executing this line alone
	}
}
