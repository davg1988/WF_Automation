package abmPosOperator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.ScreenShot;

public class ValidacionCampoNombre {

	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ValidarCampoNombre");
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
	public void longOfName() throws Exception {
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Insertar']")).click();
		Thread.sleep(300);
		String rol = sh.getCell(1,8).getContents();
		Select select = new Select(driver.findElements(By.className("z-selectbox")).get(1));
		select.selectByVisibleText(rol);
		Thread.sleep(300);
		
		//Create operator with more than 20 characters
		String name = sh.getCell(1,9).getContents();
		driver.findElement(By.xpath("//*[@class='z-textbox' and @maxlength='20']")).sendKeys(name);
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		Thread.sleep(300);
		String tc = sh.getCell(0, 9).getContents(); 
		ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\ValidacionCampoNombre\\"+tc+".png" );
		driver.findElement(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os' and text()='OK']")).click();;
				
		//Check that the created operator has 20 characters on the name field
		Assert.assertEquals(true, driver.findElement(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name.substring(0,20)+"']")).isDisplayed());
	}
	
	@Test (priority=6)
	public void blankSpacesValidation() throws Exception {
		//White spaces
		String name = sh.getCell(1,9).getContents();
		Thread.sleep(800);
		driver.findElement(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name.substring(0,20)+"']")).click();
		Thread.sleep(800);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']")).click();
		Thread.sleep(800);
		String espacios = sh.getCell(1,10).getContents();
		driver.findElement(By.xpath("//*[@class='z-textbox' and @maxlength='20']")).clear();
		Thread.sleep(800);
		driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus' and @maxlength='20']")).sendKeys(espacios);
		Thread.sleep(800);
		String tc = sh.getCell(0, 10).getContents(); 
		ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\ValidacionCampoNombre\\"+tc+".png" );
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Validates if the warning message appears
		Assert.assertEquals("El campo no puede estar en blanco", driver.findElement(By.xpath("//*[@class='z-errbox-center']")).getText());
	}
	
	@Test (priority=7)
	public void emptyField() throws Exception {
		//Empty field
		driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-text-invalid' and @maxlength='20']")).clear();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		Thread.sleep(300);
		String tc = sh.getCell(0, 11).getContents(); 
		ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\ValidacionCampoNombre\\"+tc+".png" );
		Thread.sleep(300);
		
		//Validates if the warning message appears
		Assert.assertEquals("El campo no puede estar en blanco", driver.findElement(By.xpath("//*[@class='z-errbox-center']")).getText());
		
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']")).click();
	}
	
	@Test (priority=8)
	public void deleteUser() throws InterruptedException {
		String nombre = sh.getCell(1,9).getContents().substring(0,20);
		driver.findElement(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+nombre+"']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@class='z-button-cm'and text()=' baja']")).click();
		Thread.sleep(800);
		driver.findElement(By.xpath("//*[@class='z-messagebox-btn z-button-os' and text()='Yes']")).click();
		Thread.sleep(800);
		driver.findElement(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='OK']")).click();
		Thread.sleep(500);
		
		//Logout
		driver.findElement(By.xpath("//*[@title='Salir del Programa']")).click();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os'and text()='OK']")).click();
		Thread.sleep(300);
	}
	
	@Test (priority=9)
	public void deleteTestUser() throws InterruptedException {
		
		//Login as an admin
		Thread.sleep(1000);
		String user, pass;
		user = sh.getCell(1,1).getContents();
		pass = sh.getCell(1,2).getContents();
		driver.findElement(By.xpath("//*[@title='Introduzca el Nombre de Usuario']")).sendKeys(user);
		Thread.sleep(400);
		driver.findElement(By.xpath("//*[@title='Tipee la Contrase�a del Usuario']")).sendKeys(pass);
		Thread.sleep(800);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Go To Gestion Login
		Thread.sleep(500);
		driver.findElements(By.xpath("//*[@class='z-window-embedded']//*[@class='z-toolbarbutton-cnt']")).get(1).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//*[@class='titlebar nomargin_left z-hbox']")).click();
		Thread.sleep(500);
		
		//Find test user
		List<WebElement> white_rows = driver.findElements(By.xpath("//*[@class='z-row']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		List<WebElement> gray_rows = driver.findElements(By.xpath("//*[@class='z-row z-grid-odd']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		String name = sh.getCell(1,5).getContents();
		int located_index = 0;
		int i = 0;
		while(i<white_rows.size()) {
			if(white_rows.get(i).getText().equals(name)) {
				located_index = i*2;
				i = white_rows.size();
			}				
		}
		int j = 0;
		while(j<gray_rows.size()) {
			if(gray_rows.get(i).getText().equals(name)) {
				located_index = j+j+1;
				j = gray_rows.size();
			}
		}
		driver.findElements(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']")).get(located_index).click();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='Yes']")).click();
		Thread.sleep(300);
		
		//Logout
		driver.findElement(By.xpath("//*[@title='Salir del Programa']")).click();
		Thread.sleep(300);
		driver.findElement(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os'and text()='OK']")).click();
		Thread.sleep(300);
		driver.close();
	}
}
