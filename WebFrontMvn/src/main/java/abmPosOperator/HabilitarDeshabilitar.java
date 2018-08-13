package abmPosOperator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		
		//Setting the driver
		driver = UsefulMethodsWF.setUpWf();
		wait = new WebDriverWait(driver,40);
		
		//Getting the long of the personnel number configured in UserWatcher.properties
		long_login = UsefulMethodsWF.getLongLogin();
		
		//Instance of RServiceClientFactory
		factory = new RServiceClientFactory();
		
		//Getting parameters to launch browser
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("HabilitarDeshabilitar");
		
		//Getting admin credentials from excel file
		String adminUser = sh.getCell(1,5).getContents();
		String adminPass = sh.getCell(2,5).getContents();
		
		//Getting data of WebFront User to create for the execution of test
		String user = sh.getCell(1,6).getContents();
		String pass = sh.getCell(2,6).getContents();
		String role = sh.getCell(3,6).getContents();
		String functionality = sh.getCell(4,6).getContents();
		String menuBehaviour = sh.getCell(5,6).getContents();

		//Create WF test user
		UsefulMethodsWF.createWFTestUser(adminUser, adminPass, user, pass, role, functionality, menuBehaviour, driver);	
	}

	@Test (priority = 9)
	public void disablePosOperator() throws Exception {
		
		//Login to WF and go to Mantenimiento de Usuarios
		String user = sh.getCell(1,6).getContents();
		String pass = sh.getCell(2,6).getContents();
		UsefulMethodsWF.loginWF(driver, user, pass);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
		
		long_login = sh.getCell(1,8).getContents();
		
		//Loop to create the pos operators to disable
		for (int i = 12; i < sh.getRows(); i++) {
			
			// Obtain Parameters From Excel File
			String role, username, login;
			role = sh.getCell(0,i).getContents();
			username = sh.getCell(1,i).getContents();
			login = sh.getCell(2,i).getContents();
			
			// Determines the number of the line available to register a new operator
			// This is used to locate the created user on the verification of CTL file
			line = CrearModificarEliminarUsuario.findFreeRegisterOnCtl(factory, role);
			registers.add(new Integer(line));
			
			//Insertion of new user
			CrearModificarEliminarUsuario.createPosOperator(driver, role, username, login);
		}
		
		int cont_icons = 2;
		int cont_registers = 0;
		
		for(int i = 12; i < sh.getRows(); i++) {
			String name = sh.getCell(1,i).getContents();
			disableOperator(driver, name);
			
			//Verification of lock field, must change to 1
			Assert.assertEquals(factory.getCTLFunction(registers.get(cont_registers)).getLockIndicator(), "1");
			
			//Verification of disabled icon
			Assert.assertEquals(driver.findElements(By.xpath("//img[@src='/WebFrontBase/resources/webfront/common/disabled.png']")).size(), cont_icons);
			
			cont_icons++;
			cont_registers++;
		}
	}
	
	@Test (priority = 11)
	public void enablePosOperator() throws Exception {
		
		int cont_icons = 2;
		int cont_registers = 0;
		
		//Loops to enable the pos operators
		for(int i = 12; i < sh.getRows(); i++) {
			String name = sh.getCell(1,i).getContents();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()=\""+name+"\"]"))).click();
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
		
		//Loop to delete the pos operators used during the test
		for (int i = 12; i < sh.getRows(); i++) {
			String name = sh.getCell(1,i).getContents();
			CrearModificarEliminarUsuario.deletePosOperator(driver, name);
		}
		UsefulMethodsWF.logoutWF(driver);
	}
	
	@Test (priority=15)
	public void closeWF() {
		
		//Login as an admin
		String user, pass;
		user = sh.getCell(1,5).getContents();
		pass = sh.getCell(2,5).getContents();
		//Name of the WF user used to do the test
		String name = sh.getCell(1,5).getContents();
		
		UsefulMethodsWF.deleteWFTestUser(user, pass, name, driver);
	}
	
	// **************************** METHODS USED IN THE CLASS *************************************
	
	public static void disableOperator(WebDriver driver, String name) {
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()=\""+name+"\"]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm'and text()=' Deshabilitar']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='OK']"))).click();
	}
	
}
