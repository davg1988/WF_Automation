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

public class CrearModificarEliminarUsuario {
	
	public WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	int line = 0;
	List<Integer> registers = new ArrayList<>(10);
	String long_login = "";
	RServiceClientFactory factory;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("CrearEliminarModificar");
		long_login = sh.getCell(1,8).getContents();
		
		//Setting launch of the browser
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver,20);
		String url = sh.getCell(1,2).getContents();
		driver.get(url);
		
		//Setting the IP for the clients of Rest Service
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
	
	@Test (priority=6)
	public void insertNewUser() throws Exception {
		
		int count = 1;
		for (int i = 11; i < sh.getRows(); i++) {
			
			// Obtain Parameters From Excel File
			String role, username, login, tc;
			role = sh.getCell(1,i).getContents();
			username = sh.getCell(2,i).getContents();
			login = sh.getCell(3,i).getContents();
			tc = sh.getCell(5,i).getContents();
			
			// Determines the number of the line available to register a new operator
			// This is used to locate the created user on the verification of CTL file
			line = findFreeRegisterOnCtl(factory, role);
			registers.add(new Integer(line));
			
			//Insertion of new user
			createPosOperator(driver, role, username, login);
			ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\CrearModificarEliminarUsuario\\"+ tc + count +".png");
			
			//Verification of created user
			verificationCreatedUser(username, login, factory, driver, long_login, line);
			count++;
		}		
	}
	
	@Test (priority=7)
	public void modifyUser() throws Exception {
		
		int count = 1;
		for (int i = 11; i < sh.getRows(); i++) {
			
			String actual_name, modified_name, modified_login, tc;
			actual_name = sh.getCell(2,i).getContents();
			modified_name = sh.getCell(6,i).getContents();
			modified_login = sh.getCell(7,i).getContents();
			tc = sh.getCell(8,i).getContents();	
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+actual_name+"']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm' and text()=' Modificar']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).clear();
			driver.findElement(By.xpath("//*[@class='z-textbox z-textbox-focus' and @maxlength='20']")).sendKeys(modified_name);
			driver.findElement(By.xpath("//*[@class='z-longbox']")).clear();
			driver.findElement(By.xpath("//*[@class='z-longbox z-longbox-focus']")).sendKeys(modified_login);
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"))).click();
			ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\CrearModificarEliminarUsuario\\"+ tc + count +".png");
			
			int register = registers.get(count-1);
			verificationCreatedUser(modified_name, modified_login, factory, driver, long_login, register);
			count++;
		}		
	}
	
	@Test (priority=8)
	public void deleteUser() throws Exception {
		
		int count = 1;
		for (int i = 11; i < sh.getRows(); i++) {
			String name = sh.getCell(6,i).getContents();
			deletePosOperator(driver, name);
			ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\CrearModificarEliminarUsuario\\EliminarUsuario"+ count +".png");
			int register = (int) registers.get(count-1);
			verificationDeletedUser(name, factory, driver, register);
			count++;
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
	
	//********************* Methods **********************
	
	public static void deletePosOperator (WebDriver driver, String name) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name+"']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-button-cm'and text()=' Baja']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='OK']"))).click();
	}
	
	public static void createPosOperator (WebDriver driver, String role, String name, String login) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		//Click on Insertar button
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("z-button-cm"), 2));
		driver.findElements(By.className("z-button-cm")).get(1).click();
		
		//Selecting the role of the operator
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
		Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
		select.selectByVisibleText(role);
		
		//Filling the name field
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).sendKeys(name);
		
		//Filling the login field
		driver.findElement(By.xpath("//*[@class='z-longbox']")).clear();
		driver.findElement(By.xpath("//*[@class='z-longbox z-longbox-focus']")).sendKeys(login);
		
		//Click on Confirmar button
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Click on OK
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
		
		//Verification on WF
		Assert.assertEquals(true, driver.findElement(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+username+"']")).isDisplayed());
		
		//Verification on CTL File
		Assert.assertEquals(factory.getCTLFunction(line).getName().substring(0, username.length()), username);
		Assert.assertEquals(factory.getCTLFunction(line).getPersonnelNo(), login);
		
		//Verification on DB (checker table)
		String query = "SELECT name, personnel_number FROM mtxadmin.checker WHERE checker_number = "+ line +"";
		
		//Connection to the DB
		Connection db = DriverManager.getConnection("jdbc:postgresql://"+Environment.getEnv_ip()+":5432/webfront", "postgres", "ARS4ever");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		Assert.assertEquals(username, rs.getString("name").substring(0, username.length()));
		Assert.assertEquals(rs.getString("personnel_number"),login);
		
		//Verification on CTL that there is no displacement of the register
		if (long_login.equals("12")) {
			Assert.assertEquals("xxxxxx", factory.getCTLFunction(line).getName().substring(24, 30));
		} else {
			Assert.assertEquals("xxxxxxxxxx", factory.getCTLFunction(line).getName().substring(20, 30));
		}
	}

	public static void verificationDeletedUser (String name, RServiceClientFactory factory, WebDriver driver, int line) throws IOException, SQLException {
		
		//Verify that does not appear in WF
		Assert.assertEquals(true, driver.findElements(By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+name+"']")).size()==0);
		
		//Verify that does not appear in CTL file
		Assert.assertEquals(factory.getCTLFunction(line).getName(),"xxxxxxxxxx");
		Assert.assertEquals(factory.getCTLFunction(line).getLockIndicator(), "1");
		Assert.assertEquals(factory.getCTLFunction(line).getPassword(), "000000000000000000000000");
		Assert.assertEquals(factory.getCTLFunction(line).getWrongEntries(), "00");
		
		//Verify that does not appear in the DB
		String query = "SELECT name,personnel_number FROM mtxadmin.checker WHERE checker_number = "+ line +"";
		Connection db = DriverManager.getConnection("jdbc:postgresql://"+Environment.getEnv_ip()+":5432/webfront", "postgres", "ARS4ever");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		String empty_name_db = String.format("%"+name.length()+"s", "");
		Assert.assertEquals(rs.getString("name").substring(0, name.length()),empty_name_db);
		Assert.assertEquals(rs.getString("personnel_number"),"0");
	}
}
