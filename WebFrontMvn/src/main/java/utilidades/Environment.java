package utilidades;

public class Environment {
	
	private static String env_ip;
	
	public Environment() {
	}
	
	public static String getEnv_ip() {
		return env_ip;
	}
	
	public static void setEnv_ip(String url) {
		int index = url.indexOf(":", 8);
		env_ip = url.substring(8,index);
	}
}
