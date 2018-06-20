package webFrontCommonUtils;

import java.io.IOException;

import utilidades.Environment;

//import org.apache.log4j.Logger;

//import com.ncr.webfront.core.utils.logging.WebFrontLogger;
//import com.ncr.webfront.core.utils.propertiesmapping.WebFrontMappingProperties;

public class EServiceClientFactory {

	private POSServerRestConnector restConnector = new POSServerRestConnector();
	//private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + */"http://153.72.48.136:12345/e_service";
	private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + */"http://"+Environment.getEnv_ip()+":12345/e_service";
	//private final Logger logger = WebFrontLogger.getLogger(EServiceClientFactory.class);

	public EServiceClientFactory() {
		if (posServerRestfulBaseURL.isEmpty())
			throw new RuntimeException(
					"The remote URL cannot be empty. Look at webfront.properties file, in the 'webfront.posserver.restful.base.url' property.");
	}

	public boolean declareDefective(String terminal) throws IOException {

		try {
			restConnector.put(posServerRestfulBaseURL + "/eod/terminal/defective/" + terminal, null);

			// In error case, it will exit with exception
			return true;

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying declare Terimnal " + terminal +  " as defective. Details: " + e.getMessage());
			return false;
		}
	}

	public boolean startTerminalEOD(String terminal) throws IOException {

		try {
			restConnector.put(posServerRestfulBaseURL + "/eod/terminal/start/" + terminal, null);

			// In error case, it will exit with exception
			return true;

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to start EOD on Terminal " + terminal + ". Details: " + e.getMessage());
			return false;
		}
	}
}
