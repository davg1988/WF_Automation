package webFrontCommonUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class POSServerRestConnector {

	private OkHttpClient client = new OkHttpClient.Builder().connectTimeout(0, TimeUnit.SECONDS).writeTimeout(0, TimeUnit.SECONDS)
			.readTimeout(0, TimeUnit.SECONDS).build();

	public String get(String url) throws IOException, NotFoundException, InternalServerErrorException {

		Request request = new Request.Builder().url(url).get().build();

		try (Response response = client.newCall(request).execute()) {

			if (response.code() == 404) // Not Found
				throw new NotFoundException("The remote server return 404 - Not Found");

			if (response.code() == 500) // Server Internal Error
				throw new InternalServerErrorException("The remote server return 500 - Internal Server Error");

			if (!response.isSuccessful()) // Another error
				throw new IOException("Unexpected code (" + response.code() + ") " + response.body().toString());

			return response.body().string();
		}
	}

	public String put(String url, RequestBody body) throws IOException, NotFoundException, InternalServerErrorException {

		if (body == null)
			body = RequestBody.create(null, "");

		Request request = new Request.Builder().url(url).put(body).build();

		try (Response response = client.newCall(request).execute()) {

			if (response.code() == 404) // Not Found
				throw new NotFoundException("The remote server return 404 - Not Found");

			if (response.code() == 500) // Server Internal Error
				throw new InternalServerErrorException("The remote server return 500 - Internal Server Error");

			if (!response.isSuccessful()) // Another error
				throw new IOException("Unexpected code (" + response.code() + ") " + response.body().toString());

			return response.body().string();
		}
	}
}