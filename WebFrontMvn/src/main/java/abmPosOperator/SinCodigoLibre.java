package abmPosOperator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SinCodigoLibre {
	
	//Objects to get parameters
	Workbook wb;
	Sheet sh;
	File fl;

	//Fill CTL with 799 cashiers and 99 supervisors
	@Test (priority=1)
	public void fillCTL() throws BiffException, IOException {
		
		//Initializing objects
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("SinCodigoLibre");	
		
		//Long of the personnel number to be used
		String long_login = sh.getCell(1,8).getContents();
		
		//Data of the cashiers to be created
		String name_cashier = "";
		String personnel_num = "";
		String operator_code = "";
		
		for(int i = 0; i<800; i++) {
			operator_code = StringUtils.leftPad(String.valueOf(i), 3, '0');
			
			if(long_login.equals("8")) {
				personnel_num = StringUtils.leftPad(String.valueOf(i), 8, '0');
				name_cashier = StringUtils.rightPad(sh.getCell(1,12).getContents(), 20, ' ') + "xxxxxxxxxx";
			} else {
				personnel_num = StringUtils.leftPad(String.valueOf(i), 12, '0');
			}
		}
	}
}
