package abmPosOperator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import junit.framework.Assert;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.Environment;
import utilidades.ScreenShot;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.RServiceClientFactory;

public class ValidacionCampoNombre {

	public WebDriver driver;
	public WebDriverWait wait;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public RServiceClientFactory factory;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		
		//Extracting parameters from Excel file
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ValidarCampoNombre");
		String url = sh.getCell(1,2).getContents();
		
		//Setting options to launch Chrome web browser
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver,20);
		driver.get(url);
		
		//Setting the IP for the clients of Rest Service
		Environment.setEnv_ip(url);
		factory = new RServiceClientFactory();
	}
		
	@Test (priority=2)
	public void createTestUser () {
			
		//Getting admin credentials from excel file
		String adminUser = sh.getCell(1,5).getContents();
		String adminPass = sh.getCell(2,5).getContents();
			
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@title='Introduzca el Nombre de Usuario']"))).sendKeys(adminUser);
		driver.findElement(By.xpath("//*[@class='textlogin z-textbox' and @title='Tipee la Contraseña del Usuario']")).sendKeys(adminPass);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			
		//Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
			
		//Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();
			
		//Getting data of WebFront User to create for the execution of test
		String user, pass, role, functionality, menuBehaviour;
		user = sh.getCell(1,6).getContents();
		pass = sh.getCell(2,6).getContents();
		role = sh.getCell(3,6).getContents();
		functionality = sh.getCell(4,6).getContents();
		menuBehaviour = sh.getCell(5,6).getContents();

		List<WebElement> txtFields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		txtFields.get(1).sendKeys(user);
		txtFields.get(2).sendKeys(pass);
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
	
	@Test (priority=3)
	public void goToMantenimientoUsuarios() {
		
		// Login as a user
		String user, pass;
		user = sh.getCell(1,6).getContents();
		pass = sh.getCell(2,6).getContents();
		UsefulMethodsWF.loginWF(driver, user, pass);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
	}
	
	@Test (priority=4)
	public void validateNameField() throws Exception {

		int count = 1;
		for (int i = 10; i < sh.getRows(); i++) {
		
			// Obtain Parameters From Excel File
			String role, username, login, tc;
			role = sh.getCell(1,i).getContents();
			username = sh.getCell(2,i).getContents();
			login = sh.getCell(3,i).getContents();
			tc = sh.getCell(0,i).getContents();
			
			//Insertion of new user
			//Click on Insertar button
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("z-button-cm"), 2));
			driver.findElements(By.className("z-button-cm")).get(1).click();
			
			//Selecting the role of the operator
			wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
			Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
			select.selectByVisibleText(role);
			
			//Filling the name field
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).sendKeys(username);
			
			//Filling the login field
			driver.findElement(By.xpath("//*[@class='z-longbox']")).clear();
			driver.findElement(By.xpath("//*[@class='z-longbox z-longbox-focus']")).sendKeys(login);
			
			//Click on Confirmar button
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			String window_message = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath
					("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']"
							+ "//*[@class='z-window-highlighted-header z-window-highlighted-header-move']"))).getText();
			Assert.assertEquals("Error", window_message);
			
			//Click on OK
			ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\ValidacionCampoNombre\\"+ tc + count +".png");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
			
			//Click on Cancelar
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("z-modal-mask")));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']"))).click();
			count++;
			
			//Check that the fields for filling the information of the pos operator disappears
			//before inserting a new one
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']")));
		}	
		UsefulMethodsWF.logoutWF(driver);
	}
	
	@Test (priority=9)
	public void deleteTestUser() {
		
		//Login as an admin
		String user, pass;
		user = sh.getCell(1,5).getContents();
		pass = sh.getCell(2,5).getContents();
		UsefulMethodsWF.loginWF(driver, user, pass);
		
		//Go To Gestion Login
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-window-embedded']//*[@class='z-toolbarbutton-cnt']"))).get(1).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("z-modal-mask")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='titlebar nomargin_left z-hbox']"))).click();
		
		//Find test user
		List<WebElement> white_rows = driver.findElements(By.xpath("//*[@class='z-row']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		List<WebElement> gray_rows = driver.findElements(By.xpath("//*[@class='z-row z-grid-odd']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		String name = sh.getCell(1,6).getContents();
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
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"))).get(located_index).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		UsefulMethodsWF.logoutWF(driver);
		driver.close();
	}

}
