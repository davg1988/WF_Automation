package webFrontCommonUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

//import org.apache.log4j.Logger;

import com.google.gson.Gson;

import utilidades.Environment;
//import com.ncr.webfront.core.utils.logging.WebFrontLogger;
//import com.ncr.webfront.core.utils.propertiesmapping.WebFrontMappingProperties;

public class CServiceClientFactory {

	private POSServerRestConnector restConnector = new POSServerRestConnector();
	private Gson gson = new Gson();
	//private String posServerRestfulBaseURL = WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + "/c_service/cashier";
	private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL()*/"http://" + Environment.getEnv_ip() + ":12345/c_service/cashier";
	//private final Logger logger = WebFrontLogger.getLogger(CServiceClientFactory.class);

	public CServiceClientFactory() {
		if (posServerRestfulBaseURL.isEmpty())
			throw new RuntimeException(
					"The remote URL cannot be empty. Look at webfront.properties file, in the 'webfront.posserver.restful.base.url' property.");
	}

	public CTLLine searchCTLByOperatorCode(String operatorCode) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/by_operator_code/" + operatorCode);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				CTL ctl = gson.fromJson(reader, CTL.class);
				return ctl.getLines().get(0);
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying find in CTL by code " + operatorCode + ". Details: " + e.getMessage());
			return null;
		}
	}

	public CTL searchCTLByPersonnelNumber(String personnelNumber) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/by_personnel_number/" + personnelNumber);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				CTL ctl = gson.fromJson(reader, CTL.class);
				return ctl;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to find in CTL by personnel number " + personnelNumber + ". Details: " + e.getMessage());
			return null;
		}
	}

	public CTL searchCTLByName(String name) throws IOException {

		String response;
		try {

			response = restConnector.get(posServerRestfulBaseURL + "/by_name/" + name);

			try (Reader reader = new StringReader(response)) {

				// Convert JSON to Java Object
				CTL ctl = gson.fromJson(reader, CTL.class);
				return ctl;
			}

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to find in CTL by name '" + name + "'. Details: " + e.getMessage());
			return null;
		}
	}
}
