package webFrontCommonUtils;

import java.io.IOException;

//import org.apache.log4j.Logger;

import com.google.gson.Gson;
//import com.ncr.webfront.core.utils.logging.WebFrontLogger;
//import com.ncr.webfront.core.utils.propertiesmapping.WebFrontMappingProperties;

import okhttp3.RequestBody;
import utilidades.Environment;

public class UServiceClientFactory {

	private POSServerRestConnector restConnector = new POSServerRestConnector();
	private Gson gson = new Gson();
	//private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + */"http://153.72.48.136:12345/u_service";
	private String posServerRestfulBaseURL = /*WebFrontMappingProperties.getInstance().getPOSServerRestfulBaseURL() + */"http://"+Environment.getEnv_ip()+":12345/u_service";
	//private final Logger logger = WebFrontLogger.getLogger(UServiceClientFactory.class);

	public UServiceClientFactory() {
		if (posServerRestfulBaseURL.isEmpty())
			throw new RuntimeException(
					"The remote URL cannot be empty. Look at webfront.properties file, in the 'webfront.posserver.restful.base.url' property.");
	}

	public boolean updateCTLLine(CTLLine line) throws IOException {

		try {

			RequestBody body = RequestBody.create(null, "{\"lines\":[" + gson.toJson(line) + "]}");

			restConnector.put(posServerRestfulBaseURL + "/ctl/update", body);

			// In error case, it will exit with exception
			return true;

		} catch (NotFoundException | InternalServerErrorException e) {
			//logger.error("An error occurred while trying to update CTL line. Details: " + e.getMessage());
			return false;
		}
	}
}
