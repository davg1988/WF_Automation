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

public class casoPositivo {

	WebDriver driver;
	WebDriverWait wait;
	File fl;
	Workbook wb;
	Sheet sh;

	@Test (priority=2)
	public void modifyExchangeRates() throws BiffException, IOException {

		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver, 40);
		//UsefulMethodsWF.createWFTestUser(driver);

		// Getting parameters
		fl = new File("Parametros\\TasaDeCambio\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("PositiveCases");

		// Login with WebFront test user
		UsefulMethodsWF.loginWFTestUser(driver);

		// Navigates to Tasa de Cambio 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();

		// Loop to modify the exchange rates
		for (int j = 0; j < 3; j++) {
			
			for (int i = 9; i < sh.getRows(); i++) {

				// Clic on Modificar of exchange rate
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='z-modal-mask']")));
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(j).click();
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).clear();

				// Get input
				String input = sh.getCell(1, i).getContents();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className("z-textbox"))).sendKeys(input);

				// Clic on Guardar
				driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Guardar'   ]")).click();

				// Clic on OK
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']"
						+ "//*[@class='z-messagebox-btn z-button-os']"))).click();

				// Go to home page
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='z-modal-mask']")));
				driver.findElement(By.xpath("//a[@title='Volver a la Página Principal']")).click();

				// Interlock
				if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-label' and text()='Bienvenido a WebFront']"))).isDisplayed()) {

					// Go again to Tasa de Cambio
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
					wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();			
				};

				// Clic on Modificar of exchange rate
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(j).click();

				// Get rate displayed
				String rate = "";
				rate = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).getAttribute("value");

				// Clic on Cancelar
				driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']")).click();

				// Verification
				Assert.assertEquals(rate, sh.getCell(2, i).getContents());

				// Go to home page 
				driver.findElement(By.xpath("//a[@title='Volver a la Página Principal']")).click();

				// Go again to Tasa de Cambio
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();
			}
		}

		UsefulMethodsWF.logoutWF(driver);
		//UsefulMethodsWF.deleteWFTestUser(driver);
		driver.close();
	}
}
