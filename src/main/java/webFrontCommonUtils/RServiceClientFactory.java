package webFrontCommonUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

//import org.apache.log4j.Logger;

import com.google.gson.Gson;
//import com.ncr.webfront.core.utils.logging.WebFrontLogger;
//import com.ncr.webfront.core.utils.propertiesmapping.WebFrontMappingProperties;

import utilidades.Environment;

public class RServiceClientFactory {

	private POSServerRestConnector restConnector = new POSServerRestConnector();
	private Gson gson = new Gson();
	//private String posServerRestfulBaseURL = WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + "/r_service";
	private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL()*/"http://" + Environment.getEnv_ip() + ":12345/r_service";
	//private final Logger logger = WebFrontLogger.getLogger(RServiceClientFactory.class);

	public RServiceClientFactory() {
		if (posServerRestfulBaseURL.isEmpty())
			throw new RuntimeException(
					"The remote URL cannot be empty. Look at webfront.properties file, in the 'webfront.posserver.restful.base.url' property.");
	}

	// Obtiene la funcion LAN
	public LAN getLANFunction() throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/lan");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				LAN lan = gson.fromJson(reader, LAN.class);
				return lan;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the LAN function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene una linea de la funcion LAN
	public LANLine getLANFunction(int rrn) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/lan/" + rrn);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				LAN lan = gson.fromJson(reader, LAN.class);
				return lan.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the rrn '" + rrn + "' of LAN function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene la funcion CTL
	public CTL getCTLFunction() throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/ctl");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				CTL ctl = gson.fromJson(reader, CTL.class);
				return ctl;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the CTL function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene una linea de la funcion CTL
	public CTLLine getCTLFunction(int rrn) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/ctl/" + rrn);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				CTL ctl = gson.fromJson(reader, CTL.class);
				return ctl.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the rrn '" + rrn + "' of CTL function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene la funcion REG
	public REG getREGFunction() throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/reg");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				REG reg = gson.fromJson(reader, REG.class);
				return reg;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the REG function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene una linea de la funcion REG
	public REGLine getREGFunction(int rrn) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/reg/" + rrn);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				REG reg = gson.fromJson(reader, REG.class);
				return reg.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the rrn '" + rrn + "' of REG function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene la funcion REG por Cashier
	public REG getREGCashier(int cashier) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/reg/0/" + cashier + "/0");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				REG reg = gson.fromJson(reader, REG.class);
				return reg;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the REG Cashier function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene la funcion REG por Terminal
	public REG getREGTerminal(int terminal) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/reg/" + terminal + "/0/0");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				REG reg = gson.fromJson(reader, REG.class);
				return reg;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the REG Cashier function. Details: " + e.getMessage());
			return null;
		}
	}
	
	// Obtiene una linea de la funcion REG - Cashier
	public REGLine getREGFunction(int terminal, int cashier, int rrn) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/reg/" + terminal + "/" + cashier + "/" + rrn);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				REG reg = gson.fromJson(reader, REG.class);
				return reg.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the rrn '" + rrn + "' of REG function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene la funcion DPT
	public DPT getDPTFunction() throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/dpt");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				DPT dpt = gson.fromJson(reader, DPT.class);
				return dpt;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the DPT function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene una linea de la funcion DPT
	public DPTLine getDPTFunction(int rrn) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/dpt/" + rrn);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				DPT dpt = gson.fromJson(reader, DPT.class);
				return dpt.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the rrn '" + rrn + "' of DPT function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene la funcion ACT
	public ACT getACTFunction() throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/act");

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				ACT act = gson.fromJson(reader, ACT.class);
				return act;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the ACT function. Details: " + e.getMessage());
			return null;
		}
	}

	// Obtiene una linea de la funcion ACT
	public ACTLine getACTFunction(int rrn) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/act/" + rrn);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				ACT act = gson.fromJson(reader, ACT.class);
				return act.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to get the rrn '" + rrn + "' of ACT function. Details: " + e.getMessage());
			return null;
		}
	}
}
