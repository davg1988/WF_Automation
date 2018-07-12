package abmPosOperator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.Environment;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.CTLLine;
import webFrontCommonUtils.UServiceClientFactory;

public class SinCodigoLibre {
	
	//Objects to handle parameters
	File fl;
	Workbook wb;
	Sheet sh;
	
	//Objects to handle the browser
	WebDriver driver;
	WebDriverWait wait;
	String url = "";
	
	UServiceClientFactory uFactory;
	
	@Test (priority=1)
	public void fillCTL() throws BiffException, IOException {
		
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("SinCodigoLibre");
		
		url = sh.getCell(1, 2).getContents();
		Environment.setEnv_ip(url);
		
		String long_login = sh.getCell(1, 8).getContents();
		
		uFactory = new UServiceClientFactory();
		
		//Loop to fill the cashier par of CTL
		for (int i = 3; i < 800; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");;
			String personnel_number = StringUtils.leftPad(String.valueOf(i), 8, "0");;
			String name = "";
			
			if(long_login.equals("8")) {
				name = StringUtils.rightPad("Luis D'Elía", 20, " ") + "xxxxxxxxxx";
			} else {
				name = StringUtils.rightPad("Luis D'Elía", 20, " ") + "0000xxxxxx";
			}
			
			CTLLine line = new CTLLine(operator_code);
			line.setName(name);
			line.setPersonnelNo(personnel_number);
			line.setProfile("001");
			
			uFactory.updateCTLLine(line);
		}
		
		//Loop to fill the supervisor par of CTL
		for (int i = 802; i < 900; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");;
			String personnel_number = StringUtils.leftPad(String.valueOf(i), 8, "0");;
			String name = "";
			
			if(long_login.equals("8")) {
				name = StringUtils.rightPad("José Núñez", 20, " ") + "xxxxxxxxxx";
			} else {
				name = StringUtils.rightPad("José Núñez", 20, " ") + "0000xxxxxx";
			}
			
			CTLLine line = new CTLLine(operator_code);
			line.setName(name);
			line.setPersonnelNo(personnel_number);
			line.setProfile("002");
			
			uFactory.updateCTLLine(line);
		}
	}
	
	@Test (priority=2)
	public void launchBrowser() {
		
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver, 20);
		driver.get(url);
	}

	public void createWFUser() {
		
		String adminUser = sh.getCell(1, 5).getContents();
		String adminPass = sh.getCell(2, 5).getContents();
		
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
	}
	
	@Test (priority=10)
	public void emptyCTL() throws IOException {
		
		//Loop to delete cashiers from CTL
		for (int i = 3; i < 800; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");
			
			CTLLine line = new CTLLine(operator_code);
			line.setLockIndicator("1");
			uFactory.updateCTLLine(line);
		}
		
		//Loop to delete supervisors from CTL
		for (int i = 802; i < 900; i++) {
			
			String operator_code = StringUtils.leftPad(String.valueOf(i), 4, "0");
			
			CTLLine line = new CTLLine(operator_code);
			uFactory.updateCTLLine(line);
		}
	}
}
