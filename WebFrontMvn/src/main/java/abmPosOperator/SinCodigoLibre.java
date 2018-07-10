package abmPosOperator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import webFrontCommonUtils.CTLLine;
import webFrontCommonUtils.UServiceClientFactory;

public class SinCodigoLibre {
	
	//Objects to get parameters
	Workbook wb;
	Sheet sh;
	File fl;
	String long_login = "";
	
	UServiceClientFactory uFactory;

	//Fill CTL with 799 cashiers and 99 supervisors
	@Test (priority=1)
	public void fillCTL() throws BiffException, IOException {
		
		//Initializing objects
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("SinCodigoLibre");	
		
		//Long of the personnel number to be used
		long_login = sh.getCell(1,8).getContents();
		
		//Data of the cashiers to be created
		String name_cashier = "";
		String personnel_num = "";
		
		//Loop to create cashiers
		for(int i = 3; i<800; i++) {
			
			if(long_login.equals("8")) {
				personnel_num = StringUtils.leftPad(String.valueOf(i), 8, '0');
				name_cashier = StringUtils.rightPad("Luis D'Elía", 20, ' ') + "xxxxxxxxxx";
			} else {
				personnel_num = StringUtils.leftPad(String.valueOf(i), 12, '0');
				name_cashier = StringUtils.rightPad("Luis D'Elía", 20, ' ') + personnel_num.substring(0,4) + "xxxxxx";
			}
			
			CTLLine line = new CTLLine(String.valueOf(i));
			line.setName(name_cashier);
			line.setPersonnelNo(personnel_num);
			line.setProfile("001");
			line.setLockIndicator("0");
			line.setWrongEntries("00");
			
			uFactory.updateCTLLine(line);
			
		}
		
		//Loop to create Supervisors
		for(int i=802; i<900; i++) {
			
			if(long_login.equals("8")) {
				personnel_num = StringUtils.leftPad(String.valueOf(i), 8, '0');
				name_cashier = StringUtils.rightPad("José Núñez", 20, ' ') + "xxxxxxxxxx";
			} else {
				personnel_num = StringUtils.leftPad(String.valueOf(i), 12, '0');
				name_cashier = StringUtils.rightPad("José Núñez", 20, ' ') + personnel_num.substring(0,4) + "xxxxxx";
			}
			
			CTLLine line = new CTLLine(String.valueOf(i));
			line.setName(name_cashier);
			line.setPersonnelNo(personnel_num);
			line.setAuthorization("002");
			line.setProfile("002");
			line.setLockIndicator("0");
			line.setWrongEntries("00");
			
			uFactory.updateCTLLine(line);
			
		}
	}

	@Test (priority=2)
	public void emptyCTL() {
		
		//Delete all cashiers
		for (int i = 3; i < 800; i++) {
			
			CTLLine line = new CTLLine(String.valueOf(i));
			
			line.setName(StringUtils.leftPad("", 20," ") + "xxxxxxxxxx");
		}
	}
}
