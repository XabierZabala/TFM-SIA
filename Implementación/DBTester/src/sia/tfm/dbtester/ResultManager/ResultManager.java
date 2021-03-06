package sia.tfm.dbtester.ResultManager;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import sia.tfm.dbtester.Classes.FileWrite;
import sia.tfm.dbtester.FileManager.FileManager;

public class ResultManager {
	
	/**
	 * Esta clase engloba los métodos necesarios para gestionar
	 * los resultados de las pruebas realizadas.
	 * @version: 09/07/2016
	 * @author Xabier Zabala
	 */
	
	private ResultManager(){};
	
	/**
	 * Método que genera un array con directorios donde se almacenan los resultados.
	 * @param hm Mapa de pares parametro/valor obtenidos del fichero de configuración.
	 * @return String[] Arreglo de directorios en formato String
	 */
	
	public static String[] directoryArray(HashMap<String, String> hm){
		
		String[] result = new String[2];
		
		result[0] = hm.get("mysqlResults");
		result[1] = hm.get("cassandraResults");
		
		return result;
	
	}
	
	/**
	 * Método que genera un array con operaciones descritas en la configuración.
	 * @param hm Mapa de pares parametro/valor obtenidos del fichero de configuración.
	 * @return String[] Arreglo de operaciones en formato String
	 */
	
	public static String[] operationArray(HashMap<String, String> hm){
		
		String[] result = new String[4];
		
		result[0] = hm.get("query0");
		result[1] = hm.get("query1");
		result[2] = hm.get("query2");
		result[3] = hm.get("query3");
		
		return result;
	
	}
	
	/**
	 * Método que comprueba la existencia de los directorios donde se
	 * almacenan los resultados de los test y en caso no existir los crea.
	 * @param paths Listado de rutas donde se almacenan los resultados de los test.
	 * @param ops Listado de operaciones que se van a realizar durante las pruebas
	 */
	
	public static void configureDirectories(String[] paths, String[] ops){
		
		for(String path: paths){
			
			for(String operation: ops){
				
				String filename = path + "/" + operation; 
				
				File dir = new File(filename);
				if(!dir.exists() || !dir.isDirectory()){
					dir.mkdirs();
				}
			}	
		}	
	}
	
	/**
	 * Método que crea el fichero de resultados correspondiente a la BD
	 * seleccionada
	 * @param hm Mapa de pares parametro/valor obtenidos del fichero de configuración.
	 * @param dirs Arreglo de directorios en formato String
	 * @param ops Arreglo de operaciones en formato String
	 * @return File El fichero de resultados.
	 */
	
	public static File createResultFile(HashMap<String, String> hm, String[] dirs,String[] ops){
		
		int selectedDB = Integer.parseInt(hm.get("selectedDB"));
		int selectedOp = Integer.parseInt(hm.get("selectedOp"));
		Long now = System.currentTimeMillis();
		
		try{
			
			String filePath = dirs[selectedDB] + '/' + ops[selectedOp] + "/" + "result_" + now + ".txt";
			File f = new File(filePath);
			f.createNewFile();
			
			return f;
			
		}catch(Exception e){
			System.out.println("La base de datos seleccionada no existe");
			System.exit(0);
			return null;
		}	
	}
	
	/**
	 * Método que escribe los resultados obtenidos por la operación seleccionada en el fichero
	 * correspondiente a la base de datos sobre el cual ha ejecutado.
	 * @param file Fichero donde se han de escribir los resultados
	 * @param bd Identificador de la base de datos seleccionada.
	 * @param op Identificador de la operación seleccionada.
	 */
	
	public static void resultWriter(File file, ArrayList<String> opElapsedTime){
		
		FileWrite fw = FileManager.accessFileWrite(file);
		PrintWriter pw = fw.getPw();
		int iter = 0;
		
		if(opElapsedTime.size() > 1){
			
			for(String et: opElapsedTime){
				iter++;
				pw.println("Tiempo transcurrido en importar el fichero " 
				+ iter + ": " + et);
			}
			
		}else{
			
			pw.println("Tiempo transcurrido en finalizar el" +
			"proceso: " + opElapsedTime.get(0));
			
		}
		
		FileManager.closeFileWrite(fw);
		
	}

}
