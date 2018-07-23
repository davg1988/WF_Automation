package utilidades;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsefulMethodsWF {

	public static void logoutWF(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement btnSalir = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@title='Salir del Programa']")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
		btnSalir.click();
		List<WebElement> btnOK = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"), 0));
		btnOK.get(0).click();
	}
	
	public static void loginWF (WebDriver driver, String user, String pass) {
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@title='Introduzca el Nombre de Usuario']"))).sendKeys(user);
		driver.findElement(By.xpath("//*[@class='textlogin z-textbox' and @title='Tipee la Contraseña del Usuario']")).sendKeys(pass);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
	}
	
	public static void createWFTestUser(String adminUser, String adminPass, String testUser, String testPass, String role, String functionality, String menuBehaviour, WebDriver driver) {
		
		WebDriverWait wait = new WebDriverWait(driver,20);
		
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
		
		//Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
		
		//Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();

		List<WebElement> txtFields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		txtFields.get(1).sendKeys(testUser);
		txtFields.get(2).sendKeys(testPass);
		Select roleList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
		roleList.selectByVisibleText(role);
		Select functionalityList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
		functionalityList.selectByVisibleText(functionality);
		Select selectMenuBehaviour = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(2));
		selectMenuBehaviour.selectByVisibleText(menuBehaviour);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Logout
		UsefulMethodsWF.logoutWF(driver);
	}

	public static void deleteWFTestUser(String adminUser, String adminPass, String testUser, WebDriver driver) {

		WebDriverWait wait = new WebDriverWait(driver,20);
		
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
		
		//Go To Gestion Login
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-window-embedded']//*[@class='z-toolbarbutton-cnt']"))).get(1).click();
		driver.findElement(By.xpath("//*[@class='titlebar nomargin_left z-hbox']")).click();
		
		//Find test user
		List<WebElement> white_rows = driver.findElements(By.xpath("//*[@class='z-row']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		List<WebElement> gray_rows = driver.findElements(By.xpath("//*[@class='z-row z-grid-odd']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		int located_index = 0;
		int i = 0;
		while(i<white_rows.size()) {
			if(white_rows.get(i).getText().equals(testUser)) {
				located_index = i*2;
				i = white_rows.size();
			}				
		}
		int j = 0;
		while(j<gray_rows.size()) {
			if(gray_rows.get(i).getText().equals(testUser)) {
				located_index = j+j+1;
				j = gray_rows.size();
			}
		}
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"))).get(located_index).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		UsefulMethodsWF.logoutWF(driver);
		driver.close();
	}
}
