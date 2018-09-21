package webFrontCommonUtils;

import java.io.IOException;

//import org.apache.log4j.Logger;

import utilidades.Environment;

//import com.ncr.webfront.core.utils.logging.WebFrontLogger;
//import com.ncr.webfront.core.utils.propertiesmapping.WebFrontMappingProperties;

public class GlobalServiceClientFactory {

	private POSServerRestConnector restConnector = new POSServerRestConnector();
	//private String posServerRestfulBaseURL = WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + "/global";
	private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL()*/"http://" + Environment.getEnv_ip() + ":12345/global";
	//private final Logger logger = WebFrontLogger.getLogger(GlobalServiceClientFactory.class);

	public GlobalServiceClientFactory() {
		if (posServerRestfulBaseURL.isEmpty())
			throw new RuntimeException(
					"The remote URL cannot be empty. Look at webfront.properties file, in the 'webfront.posserver.restful.base.url' property.");
	}

	public boolean pingPOSServer() throws IOException {

		try {

			restConnector.get(posServerRestfulBaseURL + "/ping");

			// In error case, it will exit with exception
			return true;

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to ping server. Details: " + e.getMessage());
			return false;
		}
	}
}
