import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.net.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class AutoFTP {
	
	static String Servidor;
	static int Porta;
	static String Usuario;
	static String Senha;
	static String Diretorio;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//getting current date and time using Date class
		DateFormat dfor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date obj = new Date();
		String DataHora = dfor.format(obj);
		System.out.println(DataHora);
		
		/*getting current date time using calendar class 
		*An Alternative of above*/
		//Calendar cobj = Calendar.getInstance();
		//System.out.println(dfor.format(cobj.getTime()));
		
		FTPClient ftpClient = new FTPClient();
		
		Servidor = "192.168.0.170";
		Porta = 21;
		Usuario = "pi";
		Senha = "raspberry";
		Diretorio = "/home/pi";
		
		ftpClient.connect(Servidor, Porta);
		ftpClient.login(Usuario, Senha);
		//ftpClient.changeWorkingDirectory(Diretorio);
		
	       
        // use local passive mode to pass firewall
        ftpClient.enterLocalPassiveMode();
		
		System.out.println ("Estado da Conexão: " + ftpClient.getStatus());
		
		// get details of a file or directory
        String remoteFilePath = "/home/pi/Desktop/Programas";
 
        FTPFile ftpFile = ftpClient.mlistFile(remoteFilePath);
        if (ftpFile != null) {
            String name = ftpFile.getName();
            long size = ftpFile.getSize();
            //String timestamp = ftpFile.getTimestamp().getTime().toString();
            String type = ftpFile.isDirectory() ? "Directory" : "File";

            System.out.println("Name: " + name);
            System.out.println("Size: " + size);
            System.out.println("Type: " + type);
            //System.out.println("Timestamp: " + timestamp);
        } else {
            System.out.println("The specified file/directory may not exist!");
        }
	    
		ftpClient.quit();
        ftpClient.disconnect();

	}

}
