package articlesAbm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ProcesarArchivo {
	
	//Objects to handle the parameters from Excel file
	File fl;
	Workbook wb;
	Sheet sh;
	
	@Test (priority=1)
	public void checkFileAlta() throws IOException, BiffException {
		
		fl = new File("Parametros\\Articulos\\Parametros\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("FileStructure");
		
		Path p = Paths.get("Parametros\\Articulos\\Archivo\\ARSAPPLY.DAT_20180723092000_processed");
		List<String> reader = Files.readAllLines(p);
		
		//Expected results on Native register
		String header = sh.getCell(1, 1).getContents();
		String filler2 = sh.getCell(1, 2).getContents();
		String article_number = StringUtils.rightPad(sh.getCell(1, 3).getContents(), 14, '0');
		String department_number = sh.getCell(1, 4).getContents();
		String code1 = sh.getCell(1, 5).getContents();
		String code2 = sh.getCell(1, 6).getContents();
		String discount_code = sh.getCell(1, 7).getContents();
		String vat = sh.getCell(1, 8).getContents();
		String code3 = sh.getCell(1, 9).getContents();
		String pack_type = StringUtils.rightPad(sh.getCell(1, 10).getContents(), 2, ' ');
		String packing_unit = StringUtils.leftPad(String.valueOf((int)(Float.parseFloat(sh.getCell(1, 11).getContents()) * 10)), 4, '0');
		String deposit_link = sh.getCell(1, 12).getContents();
		String description = StringUtils.rightPad(sh.getCell(1, 13).getContents(), 20, ' ');
		String filler10 = sh.getCell(1, 14).getContents();
		String code5 = sh.getCell(1, 15).getContents();
		String code6 = sh.getCell(1, 16).getContents();
		String age_restriction = StringUtils.leftPad(sh.getCell(1, 17).getContents(), 1, '0');
		String chain_discount_code = sh.getCell(1, 18).getContents();
		String price = StringUtils.leftPad(String.valueOf((int)(Float.parseFloat(sh.getCell(1, 19).getContents()) * 100)), 8, '0');
		
		//Actual results on Native register
		String act_header = reader.get(0).substring(0, 10);
		String act_filler2 = reader.get(0).substring(10, 12);
		String act_article_number = reader.get(0).substring(12, 26);
		String act_department_number = reader.get(0).substring(26, 30);
		String act_code1 = reader.get(0).substring(30, 31);
		String act_code2 = reader.get(0).substring(31, 32);
		String act_discount_code = reader.get(0).substring(32, 33);
		String act_vat = reader.get(0).substring(33, 34);
		String act_code3 = reader.get(0).substring(34, 36);
		String act_pack_type = reader.get(0).substring(36, 38);
		String act_packing_unit = reader.get(0).substring(38, 42);
		String act_deposit_link = reader.get(0).substring(42, 46);
		String act_description = reader.get(0).substring(46, 66);
		String act_filler10 = reader.get(0).substring(66, 76);
		String act_code5 = reader.get(0).substring(76, 77);
		String act_code6 = reader.get(0).substring(77, 78);
		String act_age_restriction = reader.get(0).substring(78, 79);
		String act_chain_discount_code = reader.get(0).substring(79, 80);
		String act_price = reader.get(0).substring(80);
		
		//Assertions of native register
		Assert.assertEquals(act_header, header);
		Assert.assertEquals(act_filler2, filler2);
		Assert.assertEquals(act_article_number, article_number);
		Assert.assertEquals(act_department_number, department_number);
		Assert.assertEquals(act_code1, code1);
		Assert.assertEquals(act_code2, code2);
		Assert.assertEquals(act_discount_code, discount_code);
		Assert.assertEquals(act_vat, vat);
		Assert.assertEquals(act_code3, code3);
		Assert.assertEquals(act_pack_type, pack_type);
		Assert.assertEquals(act_packing_unit, packing_unit);
		Assert.assertEquals(act_deposit_link, deposit_link);
		Assert.assertEquals(act_description, description);
		Assert.assertEquals(act_filler10, filler10);
		Assert.assertEquals(act_code5, code5);
		Assert.assertEquals(act_code6, code6);
		Assert.assertEquals(act_age_restriction, age_restriction);
		Assert.assertEquals(act_chain_discount_code, chain_discount_code);
		Assert.assertEquals(act_price, price);
		
		//Expected results on register E
		String filler_E = sh.getCell(1, 23).getContents(); 
		String package_size = sh.getCell(1, 25).getContents();
		String price_reference = sh.getCell(1, 26).getContents();
		String unit_of_measure = sh.getCell(1, 27).getContents();
		String family_code = sh.getCell(1, 28).getContents();
		String filler_E2 = sh.getCell(1, 29).getContents();
		String promotional_price = sh.getCell(1, 30).getContents();
		
		//Actual results on register E
		String act_header_E = reader.get(1).substring(0, 10);
		String act_filler_E = reader.get(1).substring(10, 12); 
		String act_article_number_E = reader.get(1).substring(12, 26);
		String act_package_size = reader.get(1).substring(26, 30);
		String act_price_reference = reader.get(1).substring(30, 34);
		String act_unit_of_measure = reader.get(1).substring(34, 36);
		String act_family_code = reader.get(1).substring(36, 39);
		String act_filler_E2 = reader.get(1).substring(39, 80);
		String act_promotional_price = reader.get(1).substring(80);
		
		//Assertions of register E
		Assert.assertEquals(act_header_E, header);
		Assert.assertEquals(act_filler_E, filler_E);
		Assert.assertEquals(act_article_number_E, article_number);
		Assert.assertEquals(act_package_size, package_size);
		Assert.assertEquals(act_price_reference, price_reference);
		Assert.assertEquals(act_unit_of_measure, unit_of_measure);
		Assert.assertEquals(act_family_code, family_code);
		Assert.assertEquals(act_filler_E2, filler_E2);
		Assert.assertEquals(act_promotional_price, promotional_price);
		
		//Expected results register g
		String filler_g = sh.getCell(1, 34).getContents();
		String imp_int = StringUtils.leftPad(String.valueOf((int)(Float.parseFloat(sh.getCell(1, 36).getContents()) * 100)), 8, '0');
		String structure_code = StringUtils.rightPad(sh.getCell(1, 37).getContents(), 20, '0');
		String cod_me = sh.getCell(1, 38).getContents();
		String cod_imp1 = sh.getCell(1, 39).getContents();
		String cod_imp2 = sh.getCell(1, 40).getContents();
		String cod_imp3 = sh.getCell(1, 41).getContents();
		String code3_g = sh.getCell(1, 42).getContents();
		String sku_number = sh.getCell(1, 43).getContents();
		String deposit_type = sh.getCell(1, 44).getContents();
		String content = sh.getCell(1, 45).getContents();
		String measure_unit = sh.getCell(1, 46).getContents();
		String code4 = sh.getCell(1, 47).getContents();
		String code5_g = sh.getCell(1, 48).getContents();
		String code6_g = sh.getCell(1, 49).getContents();
		String pack_quantity = sh.getCell(1, 50).getContents();
		
		//Actual results register g
		String act_header_g = reader.get(2).substring(0, 10);
		String act_filler_g = reader.get(2).substring(10, 12);
		String act_article_number_g = reader.get(2).substring(12, 26);
		String act_imp_int = reader.get(2).substring(26, 34);
		String act_structure_code = reader.get(2).substring(34, 54);
		String act_cod_me = StringUtils.leftPad(reader.get(2).substring(54, 56), 2, '0');
		String act_cod_imp1 = StringUtils.leftPad(reader.get(2).substring(56, 58), 2, '0');
		String act_cod_imp2 = StringUtils.leftPad(reader.get(2).substring(58, 60), 2, '0');
		String act_cod_imp3 = StringUtils.leftPad(reader.get(2).substring(60, 62), 2, '0');
		String act_code3_g = reader.get(2).substring(62, 63);
		String act_sku_number = StringUtils.leftPad(reader.get(2).substring(63, 74), 11, '0');
		String act_deposit_type = StringUtils.leftPad(reader.get(2).substring(74, 75), 1, '0');
		String act_content = StringUtils.leftPad(reader.get(2).substring(75, 79), 4, '0');
		String act_measure_unit = StringUtils.leftPad(reader.get(2).substring(79, 81), 2, ' ');
		String act_code4 = reader.get(2).substring(81, 82);
		String act_code5_g = reader.get(2).substring(82, 83);
		String act_code6_g = reader.get(2).substring(83, 84);
		String act_pack_quantity = StringUtils.leftPad(reader.get(2).substring(84), 4, '0');
		
		//Assertions of register g
		Assert.assertEquals(act_header_g, header);
		Assert.assertEquals(act_filler_g, filler_g);
		Assert.assertEquals(act_article_number_g, article_number);
		Assert.assertEquals(act_imp_int, imp_int);
		Assert.assertEquals(act_structure_code, structure_code);
		Assert.assertEquals(act_cod_me, cod_me);
		Assert.assertEquals(act_cod_imp1, cod_imp1);
		Assert.assertEquals(act_cod_imp2, cod_imp2);
		Assert.assertEquals(act_cod_imp3, cod_imp3);
		Assert.assertEquals(act_code3_g, code3_g);
		Assert.assertEquals(act_sku_number, sku_number);
		Assert.assertEquals(act_deposit_type, deposit_type);
		Assert.assertEquals(act_content, content);
		Assert.assertEquals(act_measure_unit, measure_unit);
		Assert.assertEquals(act_code4, code4);
		Assert.assertEquals(act_code5_g, code5_g);
		Assert.assertEquals(act_code6_g, code6_g);
		Assert.assertEquals(act_pack_quantity, pack_quantity);
		
	}
}
