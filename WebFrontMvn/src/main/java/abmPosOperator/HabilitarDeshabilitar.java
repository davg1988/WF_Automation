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
	
	//@Test (priority=1)
	public void launchWF () throws BiffException, IOException {
		
		//Setting the driver
		UsefulMethodsWF.setDriver();
		wait = new WebDriverWait(driver,45);

		//Create WF test user
		//UsefulMethodsWF.createWFTestUser(driver);	//----------> Uncomment this line to execute this class alone
	}

	@Test (priority = 2)
	public void disablePosOperator() throws Exception {
		
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver,45);
		
		//Getting the long of the personnel number configured in UserWatcher.properties
		long_login = UsefulMethodsWF.getLongLogin();
		
		//Instance of RServiceClientFactory
		factory = new RServiceClientFactory();
		
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("HabilitarDeshabilitar");
		
		//Login to WF and go to Mantenimiento de Usuarios
		UsefulMethodsWF.loginWFTestUser(driver);
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
	
	@Test (priority = 3)
	public void enablePosOperator() throws Exception {
		
		int cont_icons = 2;
		int cont_registers = 0;
		
		//Loops to enable the pos operators
		for(int i = 12; i < sh.getRows(); i++) {
			String name = sh.getCell(1,i).getContents();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='z-modal-mask']")));
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
		//UsefulMethodsWF.deleteWFTestUser(driver); // --------> Uncomment this line when executing this class alone
		driver.close(); // --------> Comment this sentence when executing this class alone
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
