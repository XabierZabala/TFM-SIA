package sia.tfm.dbtester.Launcher;

import java.io.File;
import java.util.HashMap;

import sia.tfm.dbtester.ConfigManager.ConfigFile;
import sia.tfm.dbtester.FileManager.FileManager;
import sia.tfm.dbtester.ResultManager.ResultManager;

public class Launcher {

	public static void main(String[] args) {
		
		// Ruta del ficherode configuración
		final String config = "../DBTester/config/config.txt";
		
		// Comprobar la existencia y el formato del fichero de configuración
		if(!FileManager.fileExists(config)){
			errorMessage("El fichero de configuración no existe");
		}else if(!ConfigFile.configFileValidFormat(config)){
			errorMessage("Formato incorrecto del fichero de configuración");
		}else{
			
			HashMap<String, String> hm = ConfigFile.pairFromConfig(config);
			String selectedDB = hm.get("selectedDB");
			
			// Generar array con directorios donde se almacenan los resultados
			String []paths = ResultManager.directoryArray(hm.get("mysqlResults"),
					hm.get("cassandraResults"), hm.get("mysqlClusterResults"));
			
			// Crear directorios para almacenar los resultados
			ResultManager.configureDirectories(paths);
			
			// Obtener el fichero de resultados segun la DB seleccionada
			File resultFile = ResultManager.createResultFile(selectedDB, paths);
			
			
			
		}
		
	}
	
	/**
	 * Método que imprime en consola el mensaje de error
	 * proporcionado como parámetro y termina el proceso.
	 * @param String Mensaje de error.
	 */
	
	private static void errorMessage(String message){
		System.out.println(message);
		System.exit(0);
	}

}
