package articlesAbm;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

public class ProcesarArchivo {
	
	@Test (priority=1)
	public void checkFileAlta() throws IOException {
		
		Path p = Paths.get("Parametros\\Articulos\\Archivo\\ARSAPPLY.DAT_20180720190633_processed");
		List<String> reader = Files.readAllLines(p);
		
	}
}
