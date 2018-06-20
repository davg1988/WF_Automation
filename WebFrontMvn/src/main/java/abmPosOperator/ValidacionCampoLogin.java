package abmPosOperator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.ScreenShot;

public class ValidacionCampoLogin {

	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ValidarCampoLogin");
		String url = sh.getCell(1,0).getContents();
		System.setProperty("webdriver.chrome.driver", "Librerias\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(url);
	}
	
	@Test (priority=2)
	public void createTestUser () throws InterruptedException {
		
		//Login as an admin
		Thread.sleep(300);
		String user, pass;
		user = sh.getCell(1,1).getContents();
		pass = sh.getCell(1,2).getContents();
		driver.findElement(By.xpath("//*[@title='Introduzca el Nombre de Usuario']")).sendKeys(user);
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@title='Tipee la Contrase�a del Usuario']")).sendKeys(pass);
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Go To Gestion Login
		Thread.sleep(300);
		driver.findElements(By.xpath("//*[@class='z-window-embedded']//*[@class='z-toolbarbutton-cnt']")).get(1).click();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='titlebar nomargin_left z-hbox']")).click();
		
		//Insert User for Testing
		user = sh.getCell(1,3).getContents();
		pass = sh.getCell(1,4).getContents();
		String rol = sh.getCell(1,5).getContents();
		String funcionalidad = sh.getCell(1,6).getContents();
		String menuBehaviour = sh.getCell(1,7).getContents();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Insertar']")).click();
		Thread.sleep(300);
		driver.findElements(By.xpath("//*[@class='z-textbox']")).get(1).sendKeys(user);
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-textbox' and @type='password']")).sendKeys(pass);
		Thread.sleep(300);
		Select selectRol = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
		selectRol.selectByVisibleText(rol);
		Thread.sleep(300);
		Select selectFunctionality = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
		selectFunctionality.selectByVisibleText(funcionalidad);
		Thread.sleep(300);
		Select selectMenuBehaviour = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(2));
		selectMenuBehaviour.selectByVisibleText(menuBehaviour);
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		Thread.sleep(300);
		
		//Logout
		driver.findElement(By.xpath("//*[@title='Salir del Programa']")).click();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os'and text()='OK']")).click();
		Thread.sleep(300);
	}

	@Test (priority=3)
	public void userLogin() throws InterruptedException {
		String user, pass;
		user = sh.getCell(1,3).getContents();
		pass = sh.getCell(1,4).getContents();
		driver.findElement(By.xpath("//*[@title='Introduzca el Nombre de Usuario']")).sendKeys(user);
		Thread.sleep(200);
		driver.findElement(By.xpath("//*[@title='Tipee la Contrase�a del Usuario']")).sendKeys(pass);
		Thread.sleep(200);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		Thread.sleep(200);
	}

	@Test (priority=4)
	public void goToMantenimientoUsuarios() throws InterruptedException {
		driver.findElement(By.xpath("//*[@class='verticalmenu z-div']")).click();
		Thread.sleep(300);
		driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0).click();
		Thread.sleep(300);
	}
	
	@Test (priority=5)
	public void longOfLogin() throws Exception {
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Insertar']")).click();
		Thread.sleep(300);
		String rol = sh.getCell(1,8).getContents();
		Select select = new Select(driver.findElements(By.className("z-selectbox")).get(1));
		select.selectByVisibleText(rol);
		Thread.sleep(300);
		String name = sh.getCell(1,9).getContents();
		driver.findElement(By.xpath("//*[@class='z-textbox' and @maxlength='20']")).sendKeys(name);
		Thread.sleep(300);
		
		//Create operator with login larger than 8/12 digits
		driver.findElements(By.xpath("//*[@class='z-textbox']")).get(2).clear();
		Thread.sleep(300);
		String login = sh.getCell(1,10).getContents();
		driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus']")).sendKeys(login);
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		Thread.sleep(300);
		String tc = sh.getCell(0, 10).getContents(); 
		ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\ValidacionCampoLogin\\"+tc+".png" );
		Assert.assertEquals(true, driver.findElement(By.xpath("//*[@class='z-messagebox z-div' and text()='El usuario ha sido creado/actualizado correctamente']")).isDisplayed());
		driver.findElement(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os' and text()='OK']")).click();;
	}
}
