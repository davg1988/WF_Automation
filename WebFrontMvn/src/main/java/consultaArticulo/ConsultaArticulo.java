package consultaArticulo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class ConsultaArticulo {

	WebDriver driver;
	WebDriverWait wait;
	File fl;
	Workbook wb;
	Sheet sh;
	
	
	@Test (priority=1)
	public void articuloExistente() throws BiffException, IOException {
		
		// Set driver and wait
		UsefulMethodsWF.setDriver();
		driver = UsefulMethodsWF.getDriver();
		wait = new WebDriverWait(driver,90);
		
		// Create movil user
		//UsefulMethodsWF.createWFTestUserMovil(driver);
		
		// Login with movil user
		UsefulMethodsWF.loginWFTestUserMovil(driver);
		
		// Go to Inicio
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-window-overlapped z-window-overlapped-shadow']//*[@class='z-toolbarbutton-cnt']"))).click();	
		
		// Click on busqueda de artículo
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ricerca_articolo_big z-div']"))).click();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println( "Clic en Búsqueda: "+sdf.format(cal.getTime()) );
		
		// Get Parameter from Excel file
		fl = new File("Parametros\\ConsultaArticulo\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("TestCases");
		
		// Enter plu number of existing article
		String plu = sh.getCell(1,6).getContents();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='h_input_fields_big z-div']"))).isDisplayed();
		Calendar cal2 = Calendar.getInstance();
		System.out.println( "Pagina carga: "+sdf.format(cal2.getTime()) );
		
		try {
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='h_input_fields_big z-div' and @title='Búsqueda Artículos']//input[@class='input_big z-textbox' and @type='text']"))).sendKeys(plu);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-window-embedded-cnt-noborder']//div[@class='h_input_fields_big z-div']//input[@class='input_big z-textbox']"))).sendKeys(plu);
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='input_big z-textbox' and @type='text']"))).sendKeys(plu);
			Calendar cal3 = Calendar.getInstance();
			System.out.println( "Caja de texto es encontrada: "+sdf.format(cal3.getTime()) );
		} catch (Exception e) {
			Calendar cal4 = Calendar.getInstance();
			System.out.println( "Caja de texto NO es encontrada: "+sdf.format(cal4.getTime()) );
		}
		Calendar cal5 = Calendar.getInstance();
		System.out.println( "Fin del programa: "+sdf.format(cal5.getTime()) );
		
		// Click on search icon
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@title=' Búsqueda Artículos']"))).click();
		
		// Verification name of the product appears
		/*String expected_name = sh.getCell(2,6).getContents();
		System.out.println(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='article_info_big z-div']"))).isDisplayed());
		String actual_name = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='article_info_big z-div']//td[@align='center']//*[@class='label_big z-label']"))).get(1).getText();		
		Assert.assertEquals(actual_name, expected_name);*/

		}
}
