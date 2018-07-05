package webFrontCommonUtils;

import java.io.IOException;

public class Test {

	private static CServiceClientFactory cFactory = new CServiceClientFactory();
	private static UServiceClientFactory uFactory = new UServiceClientFactory();
	private static IServiceClientFactory iFactory = new IServiceClientFactory();
	private static RServiceClientFactory rFactory = new RServiceClientFactory();
	
	public static void main(String[] args) {
		final String operator = "9999";
		
		getLanFile();
		
		insertNewCashier(operator);
		modifyCashierName(operator);
		
		changePassword(operator, "1234");
		cleanPassword(operator);
		
		deleteCashier(operator);
		findCashier(operator);

	}

	private static void getLanFile() {
		
		try {
			rFactory.getLANFunction(99999);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	static void insertNewCashier(String operator) {
		
		CTLLine ctlLine = new CTLLine(operator);
		//ctlLine.setOperatorNo(operator);
		ctlLine.setName("Cajero Rest         xxxxxxxxxx");
		System.out.println("Modified line Cashier Name: " + ctlLine.getName());
		
		try {
			boolean result =uFactory.updateCTLLine(ctlLine);
			System.out.println("Update: " + result);
			System.out.println("Get Modified line Cashier Name: " + ctlLine.getName());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void modifyCashierName(String operator) {
		
		try {
			
			CTLLine ctlLine = cFactory.searchCTLByOperatorCode(operator);
			System.out.println("Remote line Cashier Name: " + ctlLine.getName());
			
			ctlLine.setLockIndicator("1");
			
			ctlLine.setName("POS Server Rest     xxxxxxxxxx");
			System.out.println("Modified line Cashier Name: " + ctlLine.getName());
			
			boolean result = uFactory.updateCTLLine(ctlLine);
			
			System.out.println("Update: " + result);
			
			ctlLine = cFactory.searchCTLByOperatorCode(operator);
			System.out.println("Get Modified line Cashier Name: " + ctlLine.getName());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void changePassword(String operator, String newPassword) {

		try {
			
			iFactory.changePassword(operator, newPassword);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void cleanPassword(String operator) {

		try {
			
			iFactory.cleanPassword(operator);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	static void deleteCashier(String operator) {
		
		try {
		
			iFactory.cleanPassword(operator);
			
			CTLLine ctlLine = new CTLLine(operator);
			System.out.println("Remote line Cashier Name: " + ctlLine.getName());
			
			boolean result = uFactory.updateCTLLine(ctlLine);
			
			System.out.println("Update: " + result);
			
			ctlLine = cFactory.searchCTLByOperatorCode(operator);
			System.out.println("Get Modified line Cashier Name: " + ctlLine.getName());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	static void findCashier(String operator) {
		
		try {
			
			CTL ctl = cFactory.searchCTLByPersonnelNumber("xxxx00000000");

			// Cajero
			ctl.getLines().get(0);
			
			//Supervisor
			ctl.getLines().get(0);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
}


