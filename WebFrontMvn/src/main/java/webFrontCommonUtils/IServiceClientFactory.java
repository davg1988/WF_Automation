package webFrontCommonUtils;

import java.io.IOException;

import utilidades.Environment;

//import org.apache.log4j.Logger;

//import com.ncr.webfront.core.utils.logging.WebFrontLogger;
//import com.ncr.webfront.core.utils.propertiesmapping.WebFrontMappingProperties;

public class IServiceClientFactory {

	private POSServerRestConnector restConnector = new POSServerRestConnector();
	//private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + */"http://153.72.48.136:12345/i_service";
	private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + */"http://"+Environment.getEnv_ip()+":12345/i_service";
	//private final Logger logger = WebFrontLogger.getLogger(IServiceClientFactory.class);

	public IServiceClientFactory() {
		if (posServerRestfulBaseURL.isEmpty())
			throw new RuntimeException(
					"The remote URL cannot be empty. Look at webfront.properties file, in the 'webfront.posserver.restful.base.url' property.");
	}

	public boolean changePassword(String operatorCode, String newPassword) throws IOException {

		try {
			restConnector.put(posServerRestfulBaseURL + "/password/change/" + operatorCode + "/" + newPassword, null);

			// In error case, it will exit with exception
			return true;

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to change password (Operator Code: " + operatorCode + ", Password: " + newPassword + "). Details: "
					//+ e.getMessage());
			return false;
		}
	}

	public boolean cleanPassword(String operatorCode) throws IOException {

		try {
			restConnector.put(posServerRestfulBaseURL + "/password/clean/" + operatorCode, null);

			// In error case, it will exit with exception
			return true;

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to clean password (Operator Code: " + operatorCode + "). Details: " + e.getMessage());
			return false;
		}
	}
}
