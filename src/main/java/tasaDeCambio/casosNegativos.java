package tasaDeCambio;

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

public class casosNegativos {

	WebDriver driver;
	WebDriverWait wait;
	File fl;
	Workbook wb;
	Sheet sh;
	
	@Test (priority = 1)
	public void negativeCases() throws BiffException, IOException {
		
		//  Setting Driver configuration
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver, 45);
		
		// Creation of test user to execute the test case
		//UsefulMethodsWF.createWFTestUser(driver); // -----> Uncomment this to run the class only
		
		// Login with test user
		UsefulMethodsWF.loginWFTestUser(driver);
		
		// Navigate to Tasa de Cambio on the menu
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();
		
		// Click on Modificar button on the dollar currency
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(0).click();
		
		// Get rate displayed
		String rate = "";
		rate = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).getAttribute("value");
		
		
		// Initialize objects to handle the Excel file 
		fl = new File("Parametros\\TasaDeCambio\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("NegativeCases");
		
		// Loop where the negative values are going to be entered
		for (int i = 9; i < sh.getRows(); i++) {
			
			// Clear field
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).clear();
			
			// Get negative parameter
			String valor_negativo = sh.getCell(1, i).getContents();
			
			// Input negative parameter
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("z-textbox"))).sendKeys(valor_negativo);
			
			// Clic on Guardar
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Guardar']")).click();

			// Verify that the error mesagge is displayed
			boolean message_appears = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-window-highlighted-header z-window-highlighted-header-move' and text()='Confirma Modificación']"))).isDisplayed();
			Assert.assertEquals(message_appears, true);
			
			// Clic on OK
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']"
					+ "//*[@class='z-messagebox-btn z-button-os']"))).click();
		}
		
		// Clic on Cancelar
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']")).click();
		
		// Go to home page 
		driver.findElement(By.xpath("//a[@title='Volver a la Página Principal']")).click();

		// Go again to Tasa de Cambio
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();
		
		// Click on Modificar button on the dollar currency to get the rate
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(0).click();
		
		// Verify that the rate on the dollar currency has not been changed
		Assert.assertEquals(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).getAttribute("value"), rate);
		
		// Clic on Cancelar
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']")).click();
		
		// Logout of WebFront
		UsefulMethodsWF.logoutWF(driver);
		
		// Close browser
		driver.close(); // -----> Comment this sentece to run the class only
		
		// Delete test user
		//UsefulMethodsWF.deleteWFTestUser(driver); // -----> Uncomment this to run the class only
	}
}
