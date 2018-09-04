package abmPosOperator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.Environment;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.RServiceClientFactory;

public class CrearModificarEliminarUsuario {
	
	WebDriver driver;
	Sheet sh;
	Workbook wb;
	File fl;
	WebDriverWait wait;
	int line = 0;
	List<Integer> registers = new ArrayList<>(10);
	String long_login = "";
	RServiceClientFactory factory;

	@BeforeTest
	//@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		
		// Configuring driver
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver,50);
		
		// Create WebFront user for execute the test
		UsefulMethodsWF.createWFTestUser(driver);	
	}
	
	@Test (priority=2)
	public void insertNewUser() throws Exception {
		
		long_login = UsefulMethodsWF.getLongLogin();
		
		factory = new RServiceClientFactory();
		
		//Login with WebFront test user
		UsefulMethodsWF.loginWFTestUser(driver);
		
		// Go to Mantenimiento de Usuarios
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
		
		// Configure objects for data provider
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("CrearEliminarModificar");
		
		// Loop to create the pos users
		for (int i = 11; i < sh.getRows(); i++) {
			
			// Obtain Parameters From Excel File
			String role, username, login;
			role = sh.getCell(1,i).getContents();
			username = sh.getCell(2,i).getContents();
			login = sh.getCell(3,i).getContents();
			
			// Determines the number of the line available to register a new operator
			// This is used to locate the created user on the verification of CTL file
			line = findFreeRegisterOnCtl(factory, role);
			registers.add(new Integer(line));
			
			//Insertion of new user
			createPosOperator(driver, role, username, login);
			
			//Verification of created user
			verificationCreatedUser(username, login, factory, driver, long_login, line);
		}		
	}
	
	@Test (priority=3)
	public void modifyUser() throws Exception {
		
		//variable that indicates the index of the register that is going to be checked
		int count = 1;
		for (int i = 11; i < sh.getRows(); i++) {
			
			String actual_name, modified_name, modified_login;
			actual_name = sh.getCell(2,i).getContents();
			modified_name = sh.getCell(6,i).getContents();
			modified_login = sh.getCell(7,i).getContents();
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()=\""+actual_name+"\"]"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).clear();
			driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus' and @maxlength='20']")).sendKeys(modified_name);
			driver.findElement(By.xpath("//*[@class='z-textbox' and @maxlength='"+UsefulMethodsWF.getVisibleDigits()+"']")).clear();
			driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus']")).sendKeys(modified_login);
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"))).click();
			
			int register = registers.get(count-1);
			
			//Verification of the modification on WF, on the DB and on the CTL
			verificationCreatedUser(modified_name, modified_login, factory, driver, long_login, register);
			
			//increase the index to check the next register
			count++;
		}		
	}
	
	@Test (priority=4)
	public void deleteUser() throws Exception {
		
		int count = 1;
		for (int i = 11; i < sh.getRows(); i++) {
			String name = sh.getCell(6,i).getContents();
			deletePosOperator(driver, name);
			int register = (int) registers.get(count-1);
			verificationDeletedUser(name, factory, driver, register);
			count++;
		}
		UsefulMethodsWF.logoutWF(driver);
		driver.close();
	}
	
	@AfterTest
	//@Test (priority=5)
	public void closeWF() throws BiffException, IOException {
		
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		UsefulMethodsWF.deleteWFTestUser(driver);
	}
	
	//********************* Methods **********************
	
	public static void deletePosOperator (WebDriver driver, String name) {
		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='z-modal-mask']")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()=\""+name+"\"]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm'and text()=' Baja']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='OK']"))).click();
	}
	
	public static void createPosOperator (WebDriver driver, String role, String name, String login) throws BiffException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 45);
		
		// Click on Insertar button
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("z-button-cm"), 2));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='z-modal-mask']")));
		driver.findElements(By.className("z-button-cm")).get(1).click();
		
		// Selecting the role of the operator
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
		Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
		select.selectByVisibleText(role);
		
		// Filling the name field
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).sendKeys(name);
		
		// Filling the login field
		driver.findElement(By.xpath("//*[@class='z-textbox' and @maxlength='"+UsefulMethodsWF.getVisibleDigits()+"']")).clear();
		driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus']")).sendKeys(login);
		
		// Click on Confirmar button
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		// Click on OK
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
	}
	
	public static int findFreeRegisterOnCtl(RServiceClientFactory factory, String role) throws IOException {
		int line = 0;
		if(role.equals("Cajero")) {
			for (int i = 1; i < 799; i++) {
				String name = "";
				name = factory.getCTLFunction(i).getName();
				if(name.equals("xxxxxxxxxx")) {
					line = i;
					i = 799;
				}
			}
		} else {
			for (int i = 801; i < 899; i++) {
				String name = "";
				name = factory.getCTLFunction(i).getName();
				if(name.equals("xxxxxxxxxx")) {
					line = i;
					i = 899;
				}
			}			
		}
		return line;
	}
	
	public static void verificationCreatedUser(String username, String login, RServiceClientFactory factory, WebDriver driver, String long_login, int line) throws SQLException, IOException {
		
		// Verification on WF
		Assert.assertEquals(true, driver.findElement(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()=\""+username+"\"]")).isDisplayed());
		
		// Verification on CTL File
		Assert.assertEquals(factory.getCTLFunction(line).getName().substring(0, username.length()), username);
		Assert.assertEquals(factory.getCTLFunction(line).getPersonnelNo(), StringUtils.leftPad(login, Integer.parseInt(long_login), '0'));
		
		// Verification on DB (checker table)
		String query = "SELECT name, personnel_number FROM mtxadmin.checker WHERE checker_number = "+ line +"";
		
		// Connection to the DB
		Connection db = DriverManager.getConnection("jdbc:postgresql://"+Environment.getEnv_ip()+":5432/webfront", "postgres", "ARS4ever");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		Assert.assertEquals(username, rs.getString("name").substring(0, username.length()));
		Assert.assertEquals(rs.getString("personnel_number"),login);
		db.close();
		
		// Verification on CTL that there is no displacement of the register
		if (long_login.equals("12")) {
			Assert.assertEquals("xxxxxx", factory.getCTLFunction(line).getName().substring(24, 30));
		} else {
			Assert.assertEquals("xxxxxxxxxx", factory.getCTLFunction(line).getName().substring(20, 30));
		}
	}

	public static void verificationDeletedUser (String name, RServiceClientFactory factory, WebDriver driver, int line) throws IOException, SQLException {
		
		// Verify that does not appear in WF
		Assert.assertEquals(true, driver.findElements(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name+"']")).size()==0);
		
		// Verify that does not appear in CTL file
		Assert.assertEquals(factory.getCTLFunction(line).getName(),"xxxxxxxxxx");
		Assert.assertEquals(factory.getCTLFunction(line).getLockIndicator(), "1");
		Assert.assertEquals(factory.getCTLFunction(line).getPassword(), "000000000000000000000000");
		Assert.assertEquals(factory.getCTLFunction(line).getWrongEntries(), "00");
		
		// Verify that does not appear in the DB
		String query = "SELECT name,personnel_number FROM mtxadmin.checker WHERE checker_number = "+ line +"";
		Connection db = DriverManager.getConnection("jdbc:postgresql://"+Environment.getEnv_ip()+":5432/webfront", "postgres", "ARS4ever");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		String empty_name_db = String.format("%"+name.length()+"s", "");
		Assert.assertEquals(rs.getString("name").substring(0, name.length()),empty_name_db);
		Assert.assertEquals(rs.getString("personnel_number"),"0");
		db.close();
	}
}
