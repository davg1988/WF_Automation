package tasaDeCambio;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.UsefulMethodsWF;

public class ModificarCancelar {

	WebDriver driver;
	WebDriverWait wait;
	File fl;
	Workbook wb;
	Sheet sh;
	
	@Test (priority = 1)
	public void modifyCancel() throws BiffException, IOException {
		
		//Setting Chrome browser
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver, 40);
		
		//Getting parameters
		fl = new File("Parametros\\TasaDeCambio\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ModifyCancel");
		String url = sh.getCell(1, 2).getContents();
		
		driver.get(url);
		
		String adminUser = sh.getCell(1, 5).getContents();
		String adminPass = sh.getCell(2, 5).getContents();
		String testUser = sh.getCell(1, 6).getContents();
		String testPass = sh.getCell(2, 6).getContents();
		String role = sh.getCell(3, 6).getContents();
		String functionality = sh.getCell(4, 6).getContents();
		String menuBehaviour = sh.getCell(5, 6).getContents();
		
		UsefulMethodsWF.createWFTestUser(adminUser, adminPass, testUser, testPass, role, functionality, menuBehaviour, driver);
		UsefulMethodsWF.loginWF(driver, testUser, testPass);
		
		//Navigates to Tasa de Cambio 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();
		
		//Loop to modify the exchange rates
		for (int i = 10; i < sh.getRows(); i++) {
			//Clic on Modificar of exchange rate
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(i-10).click();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).clear();
			
			//Get input
			String input = sh.getCell(1, i).getContents();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("z-textbox"))).sendKeys(input);
			
			//Clic on Guardar
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Guardar'   ]")).click();
			
			//Clic on OK
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']"
					+ "//*[@class='z-messagebox-btn z-button-os']"))).click();
		}
		
		//Go to home page 
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
		driver.findElement(By.xpath("//a[@title='Volver a la Página Principal']")).click();
		
		//Interlock
		if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-label' and text()='Bienvenido a WebFront']"))).isDisplayed()) {
			
			//Go again to Tasa de Cambio
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();
			
		};
	
		//Loop to modify and cancel the exchange rates
		for (int i = 10; i < sh.getRows(); i++) {
			//Clic on Modificar of exchange rate
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(i-10).click();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).clear();
			
			//Get input
			String input = sh.getCell(2, i).getContents();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("z-textbox"))).sendKeys(input);
			
			//Clic on Cancelar
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']")).click();

			
			//Go to home page 
			driver.findElement(By.xpath("//a[@title='Volver a la Página Principal']")).click();
			
			//Go again to Tasa de Cambio
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();

		}
		
		//Loop to verify that the exchange rates were not modified
		for (int i = 10; i < sh.getRows(); i++) {
			//Clic on Modificar of exchange rate
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(i-10).click();
			
			//Get rate displayed
			String rate = "";
			rate = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).getAttribute("value");
			
			//Clic on Cancelar
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']")).click();
			
			//Verification
			Assert.assertEquals(rate, sh.getCell(1, i).getContents());
			
			//Go to home page 
			driver.findElement(By.xpath("//a[@title='Volver a la Página Principal']")).click();
			
			//Go again to Tasa de Cambio
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();

		}
		
		UsefulMethodsWF.logoutWF(driver);
		UsefulMethodsWF.deleteWFTestUser(adminUser, adminPass, testUser, driver);
	}
}
