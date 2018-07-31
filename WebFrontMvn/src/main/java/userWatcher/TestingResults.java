package userWatcher;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.Environment;
import webFrontCommonUtils.RServiceClientFactory;

public class TestingResults {

	WebDriver driver;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public WebDriverWait wait;
	RServiceClientFactory factory;
	
	@Test (priority=1)
	public void settingParameters() throws BiffException, IOException {
		fl = new File("Parametros\\UserWatcher\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("PosOperatorsData");
		String url = sh.getCell(1,2).getContents();
		
		//Setting the IP for the clients of Rest Service
		Environment.setEnv_ip(url);
		factory = new RServiceClientFactory();
	}
	
	@Test (priority=4)
	public void checkDbCtl() throws SQLException, IOException {
		
		//Checking long of personnel number
		String long_login = sh.getCell(3, 10).getContents();
	
		//Loop for checking data of every operator
		for (int i = 15; i < sh.getRows(); i++) {
			if(long_login.equals("12")) {
				String name = sh.getCell(1,i).getContents();
				int line = Integer.parseInt(sh.getCell(0,i).getContents());
				//Check which operation is performed over the POS user, in order to determine the verification to be executed
				if(sh.getCell(5,i).getContents().equals("1")) {
					String login = sh.getCell(2,i).getContents();
					verificationCreatedUser(name, login, factory, long_login, line);
				} else {
					verificationDeletedUser(name, factory, line);
				}
			} else {
				String name = sh.getCell(3,i).getContents();
				//System.out.println("Nombre: " + name);
				int line = Integer.parseInt(sh.getCell(0,i).getContents());
				//Check which operation is performed over the POS user, in order to determine the verification to be executed
				if(sh.getCell(5,i).getContents().equals("1")) {
					String login = sh.getCell(4,i).getContents();
					verificationCreatedUser(name, login, factory, long_login, line);
				} else {
					verificationDeletedUser(name, factory, line);
				}
			}
		}
	}
	
//***************** METHODS USED TO VERIFICATION **********************
	
	public static void verificationCreatedUser(String username, String login, RServiceClientFactory factory,
			String long_login, int line) throws SQLException, IOException {
	
		//Verification on CTL File
		
		//Checking Name
		Assert.assertEquals(factory.getCTLFunction(line).getName().substring(0, username.length()), username);
		System.out.println("Verificado: " + factory.getCTLFunction(line).getName());
		
		//Checking Personnel Number
		if(long_login.equals("12")) {
			Assert.assertEquals(factory.getCTLFunction(line).getName().substring(20,24) + factory.getCTLFunction(line).getPersonnelNo(), login);
		} else {
			Assert.assertEquals(factory.getCTLFunction(line).getPersonnelNo(), login);
		}
		
		
		//Verification on DB (checker table)
		String query = "SELECT name, personnel_number FROM mtxadmin.checker WHERE checker_number = "+ line +"";
		
		//Connection to the DB
		Connection db = DriverManager.getConnection("jdbc:postgresql://"+Environment.getEnv_ip()+":5432/webfront", "postgres", "ARS4ever");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		Assert.assertEquals(username, rs.getString("name").substring(0, username.length()));
		String login_for_DB = StringUtils.leftPad(rs.getString("personnel_number"),Integer.parseInt(long_login),'0');
		Assert.assertEquals(login_for_DB,login);
		
		//Verification on CTL that there is no displacement of the register
		if (long_login.equals("12")) {
			Assert.assertEquals("xxxxxx", factory.getCTLFunction(line).getName().substring(24, 30));
		} else {
			Assert.assertEquals("xxxxxxxxxx", factory.getCTLFunction(line).getName().substring(20, 30));
		}
	}
	
	public static void verificationDeletedUser (String name, RServiceClientFactory factory, int line) throws IOException, SQLException {
		
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
