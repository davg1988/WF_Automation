package abmPosOperator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.Environment;
import utilidades.ScreenShot;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.RServiceClientFactory;

public class HabilitarDeshabilitar {

	WebDriver driver;
	WebDriverWait wait;
	File fl;
	Workbook wb;
	Sheet sh;
	RServiceClientFactory factory;
	
	//Variables to handle verification of operations
	int line = 0;
	List<Integer> registers = new ArrayList<>(10);
	String long_login = "";
	
	@Test (priority=1)
	public void launchWFAndSetParameters () throws BiffException, IOException {
		
		//Getting parameters to launch browser
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("HabilitarDeshabilitar");
		String url = sh.getCell(1,2).getContents();
		
		//Configure and launch Web Browser
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		driver.get(url);
		
		//Setting IP for rest service
		Environment.setEnv_ip(url);
		factory = new RServiceClientFactory();
	}
	
	@Test (priority=3)
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

	@Test (priority=5)
	public void goToMantenimientoUsuarios() {
		
		// Login as a user
		String user, pass;
		user = sh.getCell(1,6).getContents();
		pass = sh.getCell(2,6).getContents();
		UsefulMethodsWF.loginWF(driver, user, pass);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
	}

	@Test (priority=7)
	public void insertNewUser() throws Exception {
		
		long_login = sh.getCell(1,8).getContents();
		
		for (int i = 11; i < sh.getRows(); i++) {
			
			// Obtain Parameters From Excel File
			String role, username, login;
			role = sh.getCell(1,i).getContents();
			username = sh.getCell(2,i).getContents();
			login = sh.getCell(3,i).getContents();
			
			// Determines the number of the line available to register a new operator
			// This is used to locate the created user on the verification of CTL file
			line = CrearModificarEliminarUsuario.findFreeRegisterOnCtl(factory, role);
			registers.add(new Integer(line));
			
			//Insertion of new user
			CrearModificarEliminarUsuario.createPosOperator(driver, role, username, login);
			
			//Verification of created user
			CrearModificarEliminarUsuario.verificationCreatedUser(username, login, factory, driver, long_login, line);
		}		
	}

	@Test (priority = 9)
	public void disablePosOperator() throws IOException {
		
		int cont_icons = 2;
		int cont_registers = 0;
		
		for(int i = 11; i < sh.getRows(); i++) {
			String name = sh.getCell(2,i).getContents();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name+"']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm'and text()=' Deshabilitar']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='OK']"))).click();
			
			//Verification of lock field, must change to 1
			Assert.assertEquals(factory.getCTLFunction(registers.get(cont_registers)).getLockIndicator(), "1");
			
			//Verification of disabled icon
			Assert.assertEquals(driver.findElements(By.xpath("//img[@src='/WebFrontBase/resources/webfront/common/disabled.png']")).size(), cont_icons);
			
			cont_icons++;
			cont_registers++;
		}
	}
	
	@Test (priority = 11)
	public void enablePosOperator() throws IOException {
		
		int cont_icons = 2;
		int cont_registers = 0;
		
		for(int i = 11; i < sh.getRows(); i++) {
			String name = sh.getCell(2,i).getContents();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name+"']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm'and text()=' Habilitar']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='OK']"))).click();
			
			//Verification of lock field, must change to 1
			Assert.assertEquals(factory.getCTLFunction(registers.get(cont_registers)).getLockIndicator(), "0");
			
			//Verification of disabled icon
			Assert.assertEquals(driver.findElements(By.xpath("//img[@src='/WebFrontBase/resources/webfront/common/enabled.png']")).size(), cont_icons);
			
			cont_icons++;
			cont_registers++;
		}
	}
	
	@Test (priority=13)
	public void deleteUser() throws Exception {
		
		int count = 1;
		for (int i = 11; i < sh.getRows(); i++) {
			String name = sh.getCell(2,i).getContents();
			CrearModificarEliminarUsuario.deletePosOperator(driver, name);
			ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\CrearModificarEliminarUsuario\\EliminarUsuario"+ count +".png");
			count++;
		}
		UsefulMethodsWF.logoutWF(driver);
	}
	
	@Test (priority=15)
	public void deleteTestUser() {
		
		//Login as an admin
		String user, pass;
		user = sh.getCell(1,5).getContents();
		pass = sh.getCell(2,5).getContents();
		UsefulMethodsWF.loginWF(driver, user, pass);
		
		//Go To Gestion Login
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-window-embedded']//*[@class='z-toolbarbutton-cnt']"))).get(1).click();
		driver.findElement(By.xpath("//*[@class='titlebar nomargin_left z-hbox']")).click();
		
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
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"))).get(located_index).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		UsefulMethodsWF.logoutWF(driver);
		driver.close();
	}
	
}
