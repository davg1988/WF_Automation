package utilidades;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
	
}
