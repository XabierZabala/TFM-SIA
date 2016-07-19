package sia.tfm.dbtester.DB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import sia.tfm.dbtester.Classes.FileRead;
import sia.tfm.dbtester.FileManager.FileManager;



public class MySQL {
	
	/**
	 * Clase que contiene las llamadas relativas a MySQL
	 * @version: 11/07/2016
	 * @author Xabier Zabala
	 */
	
	private MySQL(){}
	
	/**
	 * M�todo que resuelve la operaci�n de la petici�n realizada desde 
	 * la l�gica de la aplicaci�n
	 * @param hm Mapa de pares parametro/valor obtenidos del fichero de configuraci�n.
	 * @return Array<String> Arreglo de String que representan el tiempo
	 * necesitado para realizar cada accion que compone la operaci�n.
	 */
	
	public static ArrayList<String> operationResolver(HashMap<String,String> hm){
		
		/*
		 * 0: Insertar datos
		 * 1:
		 * 2:
		 * 3:
		 * */
		
		String op = hm.get("selectedOp");
		
		if(op.equals("0")){
			
			return insertarDatos(hm);
			
		}else if(op.equals("1")){
			
		}else if(op.equals("2")){
			
		}else if(op.equals("3")){
			
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
	
	private static ArrayList<String> insertarDatos(HashMap<String, String> hm){
		
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<String> results = new ArrayList<String>();
        
        try {
        	
        	String host = "jdbc:mysql://"+hm.get("ipMySQL")+":3306/nyt2015";
            
        	// Conexi�n a la base de datos
        	con = DriverManager.getConnection(host,
        			hm.get("userMysql"), hm.get("passMysql"));
        	st = con.createStatement();
        	
        	System.out.println("Conexi�n establecida con la DB");
        	
        	////////////// Ficheros
        	
        	File f = new File(hm.get("dataPath"));
    		
    		if(f.exists() && !f.isDirectory()){
    			
    			System.out.println("La especificada no corresponde a un directorio");
    			releaseMemory(rs, st, con);	
    			System.exit(0);
    			return null;
    			
    		}else{
    			
    			// Arreglo de ficheros que contienen los datos a insertar
    			File[] listOfFiles = f.listFiles(); 
    			for(File file: listOfFiles){
    				String name = file.getName();
    				if (name.endsWith(".csv") || name.endsWith(".CSV")){
                        
    					// Acceder a cada uno de los ficheros
    					FileRead fr = FileManager.accessFileRead(file);
    					Long start_time = System.currentTimeMillis();
    					
    					String line = null;
    					int count = 0;
    			        while((line = fr.getBr().readLine())!=null){
    			        	
    			        	// Para ignorar el header de los ficheros CSV
    			        	if(count > 0){
    			        		
    			        		// Parsear linea e insertarlo en la DB
        			        	String[] data = line.replace(",.", ",0.").split(",");
        			        	
        			        	String query = " INSERT INTO trips (vendor_id, tpep_pickup_datetime,"
        			        			+ "tpep_dropoff_datetime, passanger_count, trip_distance, pickup_latitude,"
        			        			+ "pickup_longitude, rate_code_id, store_fwd, dropoff_latitude,"
        			        			+ "dropoff_longitude, payment_type, fare_amount, extra, mta_tax,"
        			        			+ "improvement_surcharge, tip_amount, tolls_amount, total_amount) "
        			        			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        			        	
		        			        	PreparedStatement preparedStmt = con.prepareStatement(query);
		        			            preparedStmt.setInt(1, new Integer(data[0]));
		        			            preparedStmt.setTimestamp(2, Timestamp.valueOf(data[1])); 
		        			            preparedStmt.setTimestamp(3, Timestamp.valueOf(data[2]));
		        			            preparedStmt.setInt(4, new Integer(data[3]));
		        			            preparedStmt.setFloat(5, new Float(data[4]));
		        			            preparedStmt.setFloat(6, new Float(data[5]));
		        			            preparedStmt.setFloat(7, new Float(data[6]));
		        			            preparedStmt.setInt(8, new Integer(data[7]));
		        			            preparedStmt.setString(9, data[8]);
		        			            preparedStmt.setFloat(10, new Float(data[9]));
		        			            preparedStmt.setFloat(11, new Float(data[10]));
		        			            preparedStmt.setInt(12, new Integer(data[11]));
		        			            preparedStmt.setFloat(13, new Float(data[12]));
		        			            preparedStmt.setFloat(14, new Float(data[13]));
		        			            preparedStmt.setFloat(15, new Float(data[14]));
		        			            preparedStmt.setFloat(15, new Float(data[14]));
		        			            preparedStmt.setFloat(16, new Float(data[15]));
		        			            preparedStmt.setFloat(17, new Float(data[16]));
		        			            preparedStmt.setFloat(18, new Float(data[17]));
		        			            preparedStmt.setFloat(19, new Float(data[18]));
		        			            
		        			            preparedStmt.execute();
        			        	
    			        	} 
    			        	
    			        	count++;
    			        	System.out.println(count);
    			        }
    			         
    			         Long end_time = System.currentTimeMillis();
    			         FileManager.closeFileRead(fr);
    			         
    			         results.add(name + " -> " + (end_time - start_time)/1000 + "seg");
    					
                    }
    			}

    			releaseMemory(rs, st, con);
    			return results;
    		}
        	
        	/////////////////////////
    		
    		/*
    		 * rs = st.executeQuery("SELECT * from trips");

            if (rs.next()) {
                
                System.out.println(rs.getString(1) + " "
                + rs.getString(2));
            }
    		 * */
        	

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            releaseMemory(rs, st, con);
            System.exit(0);
            return null;
        }
		
		
		
	}
	
	/**
	 * M�todo privado que cierra la conexi�n a la base de datos 
	 * liberando la memoria cautiva.
	 * @param rs Buffer donde se guardan los resultados obtenidos
	 * en la consulta.
	 * @param st Buffer que guarda la consulta realizada a la BD.
	 * en la consulta.
	 * @param con Objeto que almacena informaci�n sobre la conexi�n a
	 * la base de datos.
	 */
	
	private static void releaseMemory(ResultSet rs, Statement st, Connection con){
		
		try{
            
            if(rs != null){
               rs.close();
            }
            
            if(st != null){
               st.close();
            }
            
            if(con != null){
               con.close();
            }
            
            System.out.println("Conexi�n cerrada con la DB");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
		
	}
}
