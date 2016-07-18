package sia.tfm.dbtester.DB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class MySQL {
	
	/**
	 * Clase que contiene las llamadas relativas a MySQL
	 * @version: 11/07/2016
	 * @author Xabier Zabala
	 */
	
	private MySQL(){}
	
	/**
	 * M�todo genera el esquema y tabla sobre el cual se realizar�n
	 * las pruebas despues en MySQL
	 */
	
	public static void createStructure(){
		
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/nyt2015";
        String user = "root";
        String password = "gurereala";

        try {
            
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from trips");

            if (rs.next()) {
                
                System.out.println(rs.getString(1) + " "
                + rs.getString(2));
            }

        } catch (SQLException ex) {
        
            System.exit(0);

        } finally {
            
            try {
                
                if (rs != null) {
                    rs.close();
                }
                
                if (st != null) {
                    st.close();
                }
                
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                
                System.exit(0);
            }
        }	
	}
	
	/**
	 * M�todo que resuelve la operaci�n de la petici�n realizada desde 
	 * la l�gica de la aplicaci�n
	 * @param op Identificador de la operaci�n seleccionada.
	 * @param dp Ruta del directorio donde se encuentran los datos.
	 * @return Array<String> Arreglo de String que representan el tiempo
	 * necesitado para realizar cada accion que compone la operaci�n.
	 */
	
	public static ArrayList<String> operationResolver(String op, String dp){
		
		if(op.equals("0")){
			// Insertar datos en MySQL
			insertarDatos(dp);
			
		}else if(op.equals("1")){
			
		}else if(op.equals("2")){
			
		}
		
		return null; // IMPLEMENTAR
		
	}
	
	/**
	 * M�todo privado que cuantifica el tiempo transcurrido en 
	 * insertar los datos correspondientes a cada fichero en la DB.
	 * @param path Ruta del directorio en donde se encuentran 
	 * los ficheros.
	 * @return Array<String> Arreglo de String que representan el tiempo
	 * necesitado para realizar cada accion que compone la operaci�n.
	 */
	// ArrayList<String>
	private static void insertarDatos(String path){
		
		File f = new File(path);
		
		if(f.exists() && !f.isDirectory()){
			System.out.println("La especificada no corresponde a un directorio");
			System.exit(0);
		}else{
			
			ArrayList<String> results = new ArrayList<String>();
			
			File[] listOfFiles = f.listFiles(); 
			for(File file: listOfFiles){
				String name = file.getName();
				if (name.endsWith(".csv") || name.endsWith(".CSV")){
                    System.out.println(name);
                }
			}
		}	
	}
	
	/**
	 * M�todo privado que genera el conector a la base de datos 
	 * partiendo de los datos obtenidos del fichero de configuracion
	 * @param path Ruta del directorio en donde se encuentran 
	 * los ficheros.
	 * @return Array<String> Arreglo de String que representan el tiempo
	 * necesitado para realizar cada accion que compone la operaci�n.
	 */
	

}
