/**
 * 
 */
package wozuul;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Wagner F O Jr
 *	Classe para carregar dados de um arquivo.
 *	Utiliza API java Properties
 */
public class LoadData {

	public static Properties getProp () throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/dados.properties");
		props.load(file);
		
		return props;		
	}
}
