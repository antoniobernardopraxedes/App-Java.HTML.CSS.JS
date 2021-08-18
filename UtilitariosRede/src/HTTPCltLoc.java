import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;

//****************************************************************************************************************************
//                                                                                                                           *
// Nome da Classe: HTTPClt01                                                                                                 *
//                                                                                                                           *
// Funcao: rotinas de cliente HTTP para enviar dados para o Servidor HTTP PraxServer em mensagens binárias ou XML            *
//                                                                                                                           *
//****************************************************************************************************************************
//
public class HTTPCltLoc {
	
	static String Caminho = "";
	static String NomeArquivo = "";
	static String IPHost = "";
	static int ContMsgCoAP = 0;
	static String IPConcArd = "192.168.0.150";
	static int PortaUDP = 5683;
	
	//***************************************************************************************************************************
	//                                                                                                                          *
    // Rotina Principal (main) do Cliente HTTP Local                                                                            *
	//                                                                                                                          *
	//                                                                                                                          *
	// Funcao: fornece o arquivo XML ou Binário com os valores de tempo real para o servidor na nuvem.                          *
	//                                                                                                                          *
	//***************************************************************************************************************************
	//
	public static void main(String[] args) throws IOException {
		
		byte DH [] = new byte[6];
		byte Segundo;
		byte SegundoAnterior;
		VG.IniciaVarGlobais();
		VG.verbose = true;
		VG.PrtMsg = false;
		DH = Util.LeDataHora();
		Segundo = DH[2];
		SegundoAnterior = Segundo;
		//String EndIPCloud = "200.98.140.180";
		String EndIPCloud = "192.168.0.49";
		int Porta = 8080;
		VG.ContMsgCoAP = 0;
	
		InetAddress ip = InetAddress.getLocalHost();
		String NomeComputador = "";
		NomeComputador = ip.getHostName();
		if (NomeComputador.equals("raspberrypi")) {
			Caminho = "/home/pi/Desktop/Programas/";
			IPHost = "192.168.0.170";
			VG.verbose = true;
			VG.PrtMsg = false;
			System.out.println("Conversor CoAP - HTTP Iniciado no Computador Raspberry PI 3");
		}
		else {
			Caminho = "/home/antonio/workspace/Executavel/";
			IPHost = "192.168.0.49";
			System.out.println("Conversor CoAP - HTTP Iniciado no Computador de Mesa");
		}
		Util.LeArquivo(Caminho + "CapEnergia.fcl");
		
		int cont = 0;
		while (true) {
			DH = Util.LeDataHora();
			Segundo = DH[2];
			if (Segundo != SegundoAnterior) {
				cont = cont + 1;
				SegundoAnterior = Segundo;
			}
			if (cont >= 4) {
				
				//int TamMsgBin = SupConcArd.EnvRecMsgConc(IPConcArd, PortaUDP, VG.verbose);
				int TamMsgBin = SupConcArd.EnvRecMsgCoAPUDP(IPConcArd, PortaUDP, "estados", VG.verbose);
				
				String MsgRec = EnvRecMsgBinSrv(EndIPCloud, Porta, VG.receiveData1, TamMsgBin, "POST", "atualiza");
				if (!MsgRec.isEmpty()) {
					String Tk1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
					String Tk2 = "<CMD>";
					String Tk3 = "</CMD>";
					String Cmd = "";
					int indice1 = 0;
					int indice2 = 0;
					if (MsgRec.equals(Tk1) && MsgRec.equals(Tk2) && MsgRec.equals(Tk3)) {
						if (VG.verbose) { System.out.print(Util.LeImpHora() + " - Mensagem de Resposta OK"); }
						indice1 = MsgRec.indexOf("<CMD>") + 5;
						indice2 = MsgRec.indexOf("</CMD>");
						Cmd = MsgRec.substring(indice1, indice2);
						if (!(Cmd.isEmpty())) {
							if (VG.verbose) { System.out.println(" - Comando Recebido: " + Cmd); }
							ExecComandoHTTP(Cmd);
						}
						else {
							if (VG.verbose) { System.out.println(""); }
						}
					}
					cont = 0;
				} // if (!MsgRec.isEmpty())
			} // if (cont >= 4)
		} // while (true)
	} // public static void main(String[] args) throws IOException
	
	
	//****************************************************************************************************************************
	//                                                                                                                           *
	// Nome da Rotina: EnvRecMsgXMLSrv                                                                                           *
	//                                                                                                                           *
	// Funcao: envia uma mensagem HTTP de caracteres recebida de uma String para um servidor na nuvem e recebe a resposta        *
	//                                                                                                                           *
	// Entrada: (String) Endereço IP do servidor, (int) porta de conexão, (String) Mensagem a ser enviada                        *
	//                                                                                                                           *
	// Saida: boolean true = mensagem enviada / false = falha                                                                    *
	//                                                                                                                           *
	//****************************************************************************************************************************
	//
	static String EnvRecMsgXMLSrv(String EndIP, int Porta, String Msg, String Metodo, String Recurso) {
		String MsgRec = "";
		PrintWriter EnvChar = null; BufferedOutputStream EnvByte = null;
		BufferedReader RecChar = null;
				
		try {
			Socket socket = new Socket(EndIP, Porta);  // Cria o socket de conexão no Servidor HTTP PraxServer
			EnvByte = new BufferedOutputStream(socket.getOutputStream());
			EnvChar = new PrintWriter(EnvByte, true);
			RecChar = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setSoTimeout(3000);
			
			if (socket.isConnected()) {
				if (VG.verbose) {
					System.out.println(Util.LeImpHora() + " - " + socket.toString());
				}
			}
			
			String CabXML = Metodo + " /" + Recurso + " HTTP/1.1\n";
			CabXML = CabXML + "Host: " + IPHost + ":8080\n";
			CabXML = CabXML + "Content-Length: " + Msg.length() + "\n";
			CabXML = CabXML + "Content-Type: text/xml\n";
			CabXML = CabXML + "User-Agent: (Linux x86_64) PraxClient/1.0\n";
			
			// Transmite a mensagem para o Servidor
			EnvChar.println(CabXML);
			if (!Msg.isEmpty()) {    // Se a String Msg não estiver vazia,
				EnvChar.print(Msg);  // Transmite a Mensagem
			}
			EnvChar.flush();
				
			if (VG.verbose) {
				String MsgAviso = Util.LeImpHora() + " - Enviada Requisicao: " + Metodo + " /" + Recurso + " HTTP/1.1";
				System.out.println(MsgAviso);
			}
			
			try {
				String linha; 
				while ((linha = RecChar.readLine()) != null) {
					MsgRec = MsgRec + linha + "\n";
				}
			}
			catch(java.net.SocketTimeoutException tmo) {
				if (VG.verbose) {
					System.out.println(Util.LeImpHora() + " - Timeout na resposta do Servidor");
				}
			}
			socket.close();
		}
		catch (IOException err) {
			if (VG.verbose) {
				System.out.println(Util.LeImpHora() + " - Erro na Rotina EnvRecMsgXMLSrv: " + err);
			}
		}
		return(MsgRec);
	}
	
	
	//****************************************************************************************************************************
	//                                                                                                                           *
	// Nome da Rotina: EnvRecMsgBinSrv                                                                                           *
	//                                                                                                                           *
	// Funcao: envia uma mensagem de bytes recebida de um Buffer para um servidor na nuvem e recebe a resposta                   *
	//                                                                                                                           *
	// Entrada: Endereço IP do servidor, porta de conexão, Mensagem a ser enviada, Método (POST ou PUT), recurso (query)         *
	//                                                                                                                           *
	// Saida: String com a mensagem de resposta do servidor (se a mensagem de resposta é vazia, houve falha de comunicação       *
	//                                                                                                                           *
	//****************************************************************************************************************************
	//
	static String EnvRecMsgBinSrv(String EndIP, int Porta, byte[] ByteBuf, int TamMsgBin, String Metodo, String Recurso) {
		String MsgRec = "";
		PrintWriter EnvChar = null; BufferedOutputStream EnvByte = null;
		InputStreamReader RecByte = null; BufferedReader RecChar = null;
			
		try {
			Socket socket = new Socket(EndIP, Porta);  // Cria o socket de conexão no Servidor HTTP PraxServer
			EnvByte = new BufferedOutputStream(socket.getOutputStream());
			EnvChar = new PrintWriter(EnvByte, true);
			RecChar = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setSoTimeout(3000);
			
			if (socket.isConnected()) {
				if (VG.verbose) {
					String Mensagem = "\n" + Util.LeImpHora() + " - " + socket.toString();
					System.out.println(Mensagem);
				}
			}
			
			//int TamMsg = ByteBuf.length;
			String CabXML = Metodo + " /" + Recurso + " HTTP/1.1\r\n";
			CabXML = CabXML + "Host: " + IPHost + ":8080\r\n";
			CabXML = CabXML + "Content-Length: " + TamMsgBin + "\r\n";
			CabXML = CabXML + "Content-Type: application/octet-stream\r\n";
			CabXML = CabXML + "User-Agent: (Linux x86_64) PraxClient/1.0\r\n";
			CabXML = CabXML + "\r\n";
			
			// Transmite a mensagem para o Servidor
			EnvChar.print(CabXML);
			EnvChar.flush();
			EnvByte.write(ByteBuf, 0, TamMsgBin);
			EnvByte.flush();
			
			if (VG.verbose) {
				String MsgAviso = Util.LeImpHora() + " - Enviada Requisicao: " + Metodo + " /" + Recurso + " HTTP/1.1";
				System.out.println(MsgAviso);
				System.out.println(Util.LeImpHora() + " - Enviada Mensagem Binaria com " + TamMsgBin + " Bytes");
			}
			
			
			try {
				String linha; 
				while ((linha = RecChar.readLine()) != null) {
					MsgRec = MsgRec + linha + "\n";
				}
			}
			catch(java.net.SocketTimeoutException tmo) {
				if (VG.verbose) {
					String Mensagem = Util.LeImpHora() + " - Timeout na resposta do Servidor";
					System.out.println(Mensagem);
				}
			}
			
			socket.close();
		}
		catch (IOException err) {
			if (VG.verbose) {
				System.out.println(Util.LeImpHora() + " - Erro na Rotina EnvRecMsgSrv: " + err);
			}
		}
		
		return(MsgRec);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: ExecComandoHTTP                                                                                *
    //	                                                                                                              *
	// Funcao: carrega as variáveis VG.Comando e VG.ComRecHTTP para a rotina SupArduinoUDP enviar o comando           *
	// Entrada: EndIP1 = string com o código do comando recebido do cliente HTTP                                      *
	// Saida: não tem                                                                                                 *
    //	                                                                                                              *
	//*****************************************************************************************************************
	//
	private static boolean ExecComandoHTTP(String comrechttp) {
		boolean Res = false;
		if (comrechttp.equals("cmd=0002")) {
			Res = true;
			VG.Comando = 2;
			VG.ComRecHTTP = "Comando: Acerto Relogio";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0003")) {
			Res = true;
			VG.Comando = 3;
			VG.ComRecHTTP = "Comando: Modo Economia";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0004")) {
			Res = true;
			VG.Comando = 4;
			VG.ComRecHTTP = "Comando: Modo Normal";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0016")) {
			Res = true;
			VG.Comando = 16;
			VG.ComRecHTTP = "Comando: Manual Carga 1";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0017")) {
			Res = true;
			VG.Comando = 17;
			VG.ComRecHTTP = "Comando: Automatico Carga 1";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0005")) {
			Res = true;
			VG.Comando = 5;
			VG.ComRecHTTP = "Comando: Manual Cargas 234";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0006")) {
			Res = true;
			VG.Comando = 6;
			VG.ComRecHTTP = "Comando: Automatico Cargas 234";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0007")) {
			Res = true;
			VG.Comando = 7;
			VG.ComRecHTTP = "Comando: Habilita Carga 1";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0008")) {
			Res = true;
			VG.Comando = 8;
			VG.ComRecHTTP = "Comando: Desabilita Carga 1";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0009")) {
			Res = true;
			VG.Comando = 9;
			VG.ComRecHTTP = "Comando: Habilita Carga 2";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0010")) {
			Res = true;
			VG.Comando = 10;
			VG.ComRecHTTP = "Comando: Desabilita Carga 2";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0011")) {
			Res = true;
			VG.Comando = 11;
			VG.ComRecHTTP = "Comando: Habilita Carga 3";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0012")) {
			Res = true;
			VG.Comando = 12;
			VG.ComRecHTTP = "Comando: Desabilita Carga 3";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0013")) {
			Res = true;
			VG.Comando = 13;
			VG.ComRecHTTP = "Comando: Habilita Carga 4";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0014")) {
			Res = true;
			VG.Comando = 14;
			VG.ComRecHTTP = "Comando: Desabilita Carga 4";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0015")) {
			Res = true;
			VG.Comando = 15;
			VG.ComRecHTTP = "Comando: Apaga Indicadores de Falha";
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - " + VG.ComRecHTTP); }
		}
		return(Res);
	}
	
}