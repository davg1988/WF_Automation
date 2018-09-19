package claveDia;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.UsefulMethodsWF;

public class ClaveInvalida {

	WebDriver driver;
	WebDriverWait wait;
	Workbook wb;
	Sheet sh;
	File fl;

	@Test (priority=2)
	public void insertInvalidKey() throws BiffException, IOException {
		
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver, 45);
		
		// Create WebFront Test user
		//UsefulMethodsWF.createWFTestUser(driver); // Uncomment this line when executing the class alone
		
		// Login with webfront test user
		UsefulMethodsWF.loginWFTestUser(driver);
		
		// Go to Clave Día
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-embedded-cnt-noborder']"
				+ "//*[@class='z-groupbox-3d-header']//*[@class='z-caption-l' and text()='Fin de Día']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[@class='z-toolbarbutton-cnt'])[12]"))).click();
		
		//Setting data provider
		fl = new File("Parametros\\ClaveDia\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ClaveInvalida");
		
		//Insert invalid input
		for (int j = 9; j < sh.getRows(); j++) {
			String invalid_input = sh.getCell(1, j).getContents();
			if (j==9) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox']"))).clear();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox z-textbox-focus']"))).sendKeys(invalid_input);
			} else {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox']"))).clear();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox']"))).sendKeys(invalid_input);
			}
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-button-cm']"))).get(0).click();
			Assert.assertEquals(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-label']"))).getText(), "El valor ingresado no cumple la regla de validación regex: [0-9]{14}");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
		}
		
		//Logout
		UsefulMethodsWF.logoutWF(driver);
		//UsefulMethodsWF.deleteWFTestUser(driver); // Uncomment this line when executing this class alone
		driver.close(); // Comment this line when executing this class alone
	}
}
