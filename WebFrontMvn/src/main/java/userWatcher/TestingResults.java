package userWatcher;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.RServiceClientFactory;

public class TestingResults {

	WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	RServiceClientFactory factory;
	
	@Test (priority=1)
	public void launchWF() throws BiffException, IOException {
		fl = new File("Parametros\\UserWatcher\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("12 digits");
		
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
		System.out.println(factory.getCTLFunction(1).getName());
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
	public void checkDbCtlAndWF() throws SQLException, IOException {
		String long_login = sh.getCell(3, 10).getContents();
		for (int i = 15; i < sh.getRows(); i++) {
			
			System.out.println("Loop: " + i);
			String name = sh.getCell(1,i).getContents();
			int line = Integer.parseInt(sh.getCell(0,i).getContents());
			System.out.println("Nombre: " + name);
			System.out.println("Linea: " + line);
			
			//Check which operation is performed over the POS user, in order to determine the verification to be executed
			if(sh.getCell(3,i).getContents().equals("1")) {
				String login = sh.getCell(2,i).getContents();
				verificationCreatedUser(name, login, factory, driver, long_login, line, wait);
			} else {
				verificationDeletedUser(name, factory, driver, line);
			}
		}
	}
	
//***************** METHODS USED TO VERIFICATION **********************
	
	public static void verificationCreatedUser(String username, String login, RServiceClientFactory factory, WebDriver driver,
			String long_login, int line, WebDriverWait wait) throws SQLException, IOException {
		
		//Verification on WebFront
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("z-paging-text")));
		int number_pages = Integer.parseInt(driver.findElement(By.className("z-paging-text")).getText().substring(2));
		System.out.println("Numero de paginas: " + number_pages);
		if (number_pages > 1) {
			for (int i = 1; i <= number_pages ; i++) {
				if(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath
						("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+username+"' ]"))).isDisplayed()) {
					String operator_no = String.format("%04d", line);
					Assert.assertEquals(driver.findElement(
							By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+operator_no+"']"  )).isDisplayed(), true);
					Assert.assertEquals(driver.findElement(
							By.xpath("//*[@class='z-listcell-cnt z-overflow-hidden' and text()='"+login+"']")), true);
				}
			}
			
		} else {

		}
		
		//Verification on CTL File
		Assert.assertEquals(factory.getCTLFunction(line).getName().substring(0, username.length()), username);
		Assert.assertEquals(factory.getCTLFunction(line).getName().substring(20,24) + factory.getCTLFunction(line).getPersonnelNo(), login);
		
		//Verification on DB (checker table)
		String query = "SELECT name, personnel_number FROM mtxadmin.checker WHERE checker_number = "+ line +"";
		
		//Connection to the DB
		Connection db = DriverManager.getConnection("jdbc:postgresql://"+Environment.getEnv_ip()+":5432/webfront", "postgres", "ARS4ever");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		Assert.assertEquals(username, rs.getString("name").substring(0, username.length()));
		String login_for_DB = StringUtils.leftPad(rs.getString("personnel_number"),12,'0');
		Assert.assertEquals(login_for_DB,login);
		
		//Verification on CTL that there is no displacement of the register
		if (long_login.equals("12")) {
			Assert.assertEquals("xxxxxx", factory.getCTLFunction(line).getName().substring(24, 30));
		} else {
			Assert.assertEquals("xxxxxxxxxx", factory.getCTLFunction(line).getName().substring(20, 30));
		}
	}
	
	public static void verificationDeletedUser (String name, RServiceClientFactory factory, WebDriver driver, int line) throws IOException, SQLException {
		
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
