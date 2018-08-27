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

public class ModificarUsuarioWF {

	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	
	//@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver(); 
		wait = new WebDriverWait(driver, 45);
	}

	@Test (priority=2)
	public void modificarCasoPositivo() throws Exception {
		
		driver = UsefulMethodsWF.getDriver(); 
		wait = new WebDriverWait(driver, 45);
		
		// Login as an admin
		UsefulMethodsWF.loginAdmin(driver);
		
		//Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
		
		//Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();
		
		// Setting data provider
		fl = new File("Parametros\\PoliticasDeSeguridad\\politicas.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ModificarUsuarioWF");

		String user = sh.getCell(1,7).getContents();
		String pass = sh.getCell(2,7).getContents();
		String role = sh.getCell(3,7).getContents();
		String functionality = sh.getCell(4,7).getContents();

		List<WebElement> txtFields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		txtFields.get(1).sendKeys(user);
		txtFields.get(2).sendKeys(pass);
		Select roleList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
		roleList.selectByVisibleText(role);
		Select functionalityList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
		functionalityList.selectByVisibleText(functionality);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Getting data from excel file
		String user_positive = sh.getCell(1,8).getContents();	
		
		int btnModificar = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']"),1)).size();
		driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']")).get(btnModificar-1).click();

		List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		fields.get(1).clear();
		fields.get(1).sendKeys(user_positive);
		
		//Confirm the input data
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		// Wait for the created user to appear
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='z-label' and text()='"+ user_positive +"']"))).isDisplayed();
	}
	
	@Test (priority=3)
	public void modificarCasoNegativo() throws Exception {

		int btnModificar = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']"),1)).size();
		driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']")).get(btnModificar-1).click();
		
		for (int i = 9; i < sh.getRows(); i++) {
			
			//Getting data from excel file
			String user = sh.getCell(1,i).getContents();
			
			if(i<10) {
				List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"),2));
				fields.get(1).clear();
				fields.get(1).sendKeys(user);
			} else {
				List<WebElement> fields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"),2));
				fields.get(2).clear();
				fields.get(2).sendKeys(user);
			}
	
			//Confirm the input data
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();			
				
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"))).click();	
		}
		
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
		driver.close();
	}
}
