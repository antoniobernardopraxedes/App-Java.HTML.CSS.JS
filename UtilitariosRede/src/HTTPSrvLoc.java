import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

//********************************************************************************************************************************
//                                                                                                                               *
// Autor: Antonio Bernardo de Vasconcellos Praxedes                                                                              *
//                                                                                                                               *  
// Data: 11/08/2021                                                                                                              *
//                                                                                                                               *
// Nome da Classe: HTTPSrvLoc                                                                                                    *
//                                                                                                                               *
// Funcao: Programa Principal do Servidor HTTP Local                                                                             *
//                                                                                                                               *
//********************************************************************************************************************************
//
public class HTTPSrvLoc implements Runnable {
	
	static String Caminho = "";
	static int PORT = 8080;
	static String IPConcArd = "192.168.0.150";
	static String IPUTRAQ = "192.168.0.155";
	static String IPConcUtil = "192.168.0.152";
	static int PortaUDP = 5683;
	static String NomeArqSupHTML = "TabelaSup.html";
	static String NomeArqAQHTML = "TabelaAQ.html";
	static String NomeArqSupJS = "tabelasupjs.js";
	static String NomeArqEstilos = "style.css";
	static String NomeArqIcone = "favicon.ico";
	
	private Socket connect;
		
	public HTTPSrvLoc(Socket c) {
		connect = c;
	}
		
	//***************************************************************************************************************************
	//                                                                                                                          *
    // Rotina Principal (main) do Servidor HTTP Local                                                                           *
	//                                                                                                                          *
	// Funcao: aguarda a conexao do Cliente (Browser)                                                                           *
	//                                                                                                                          *
	//***************************************************************************************************************************
	//
	public static void main(String[] args) {
		
		VG.IniciaVarGlobais();
		VG.verbose = true;
		VG.PrtMsg = false;
		
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			InetAddress ip = InetAddress.getLocalHost();
			String NomeComputador = ip.getHostName();
			
			if (NomeComputador.equals("raspberrypi")) {
				Caminho = "/home/pi/Desktop/Programas/";
				VG.verbose = true;
				VG.PrtMsg = false;
				System.out.println("Servidor Local Iniciado no Computador Raspberry PI 3");
			}
			else {
				Caminho = "/home/antonio/workspace/Executavel/";
				System.out.println("Servidor Local Iniciado no Computador de Mesa");
			}
			System.out.println("\nServidor HTTP Local - Esperando por Conexoes na Porta: " + PORT + "\n");
			Util.LeArquivo(Caminho + "CapEnergia.fcl");
			
			while (true) {    // Espera a conexão dos Clientes HTTP
				
				HTTPSrvLoc ServidorHTTP = new HTTPSrvLoc(serverConnect.accept());
				if (VG.verbose) {
					System.out.println("Conexao Aberta com o Cliente (" + new Date() + ")");
				}
				
				// Cria Threads dedicadas para gerenciar as conexões de cada cliente
				Thread thread = new Thread(ServidorHTTP);      
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Erro na Conexao com o Servidor: " + e.getMessage());
		}
	} // Fim da Rotina public static void main(String[] args) {
	
	
	//*****************************************************************************************************************
	//                                                                                                                *
    // Rotina que Processa a Solicitação do Cliente da ClasseHTTPSrvSup                                               *
	//                                                                                                                *
	// Funcao: processa a solicitação do Cliente HTTP                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	//@Override
	public void run() {
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		String Requisicao = null;
		String NomeArquivo;
		try {
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			out = new PrintWriter(connect.getOutputStream());
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			
			// Lê o Cabeçalho da Mensagem HTTP e carrega no array LinhaCab[]
			String LinhaCab[] = new String[12];
			int i = 0;
			while ((!(LinhaCab[i] = in.readLine()).isEmpty()) && (i < 11)) {
				i = i + 1;
			}
			
			StringTokenizer parse = new StringTokenizer(LinhaCab[0]);
			String method = parse.nextToken().toUpperCase();
			Requisicao = parse.nextToken().toLowerCase();
			
			if (VG.verbose) {
				System.out.println("Método: " + method + "  -  Requisicao: " + Requisicao);
			}
			
			boolean MsgRec = false;
			VG.Comando = 1;
			if (method.equals("GET") || method.equals("POST")) {
												
				if (method.equals("GET")) {
															
					// Página HTML do Sistema de Supervisão e Controle solicitada com GET/
					if (Requisicao.equals("/") || Requisicao.equals("/?")) {
						MsgRec = true;
						NomeArquivo = NomeArqSupHTML;
						EnvMsgArquivoTxt(Caminho, NomeArquivo);
						VG.Comando = 1;
					}
					else {
						String ArquivoReq = Requisicao.substring(1);
						
						// Página HTML solicitada com GET/a: página com informações de água quente
						if (ArquivoReq.equals("a") || ArquivoReq.equals("a?")) {
							MsgRec = true;
							NomeArquivo = NomeArqAQHTML;
							EnvMsgArquivoTxt(Caminho, NomeArquivo);
							VG.Comando = 1;
						}
						
						// Arquivo com os estilos CSS solicitada com GET/style.css
						if (ArquivoReq.endsWith(".css")) {	
							MsgRec = true;
							EnvMsgArquivoTxt(Caminho, NomeArqEstilos);
							VG.Comando = 1;
						}
						
						// Arquivo com o Programa Javascript solicitada com GET/tabelasupjs.js
						if (ArquivoReq.endsWith(".js")) {	
							MsgRec = true;
							EnvMsgArquivoTxt(Caminho, NomeArqSupJS);
							VG.Comando = 1;
						}
										
						// Arquivo de Imagem com o Ícone Principal
						if (ArquivoReq.endsWith(".ico")) {
							MsgRec = true;
							EnvMsgArquivoByte(Caminho, NomeArqIcone);
						}
						
						// Mensagem XML de Atualização de Valores solicitadas por função Javascript XMLHttpRequest
						if (ArquivoReq.equals("local001.xml")) {
							MsgRec = true;
							VG.EstCom1 = SupConcArd.EnvRecMsgCoAPUDP(IPConcArd, PortaUDP, "estados", VG.verbose);
							SupConcArd.EnvRecMsgBinUDP1(IPUTRAQ, 100, VG.verbose);
							String MsgRsp = MontaMsg.XML01();
							MsgRsp = MsgRsp + " ";
							EnvMsgStringTxt(MsgRsp, "text/xml");
						}
					}
					
				}  // if (method.equals("GET"))
			
				if (method.equals("POST")) {  // Se o método é POST indica comando recebido
					String ComId = Requisicao.substring(1,4);
					String Comando = Requisicao.substring(1);
					if (VG.verbose) {
						System.out.println("Recebido POST - Comando: " + Comando);
					}
					
					if (ComId.equals("cmd")) {
						if (ExecComandoHTTP(Comando)) {
							MsgRec = true;
							SupConcArd.EnvRecMsgCoAPUDP(IPConcArd, PortaUDP, "estados", VG.verbose);
						}
						String MsgRsp = MontaMsg.XML01();
						MsgRsp = MsgRsp + " ";
						EnvMsgStringTxt(MsgRsp, "text/xml");
					}
				}  // if (method.equals("POST"))
				
				if (!MsgRec) {                 // Se não está disponível o recurso solicitado pelo método GET ou POST
					EnvMsgStringTxtErro(404);  // envia mensagem de erro 404 (Recurso não Disponível)
				}
				
			}
			else {                         // Se não foi recebido método GET ou POST,
				EnvMsgStringTxtErro(501);  // envia mensagem de erro 501 (Método Não Implementado)
			}
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, Requisicao);
			} catch (IOException ioe) {
				System.out.println("Arquivo nao encontrado : " + ioe.getMessage());
			}
			
		} catch (IOException ioe) {
			System.err.println("Erro no Servidor: " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close();
			} catch (Exception e) {
				System.err.println("Erro no fechamento do stream : " + e.getMessage());
			} 
			
			if (VG.verbose) {
				System.out.println("Conexao com o Cliente Encerrada\n"); 
			}
		}
	}  // Fim da Rotina public void run() {
	
		
	//***************************************************************************************************************************
	// Nome da Rotina: EnvMsgArquivoTxt                                                                                         *
	//	                                                                                                                        *
	// Funcao: envia para o cliente conectado uma mensagem HTTP lida de uma String                                              *
	//                                                                                                                          *
	// Entrada: String com o Caminho do Arquivo; String com o Nome do Arquivo                                                   *
	//                                                                                                                          *
	// Saida: não tem                                                                                                           *
	//	                                                                                                                        *
	//***************************************************************************************************************************
	//
	void EnvMsgArquivoTxt(String Caminho, String NomeArquivo) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(connect.getOutputStream());
			File Arquivo = new File(Caminho, NomeArquivo);
			int TamArquivo = (int) Arquivo.length();
			String tipo = TipoArquivo(NomeArquivo);
			String DadosArquivo = LeArquivoTexto(Caminho, NomeArquivo);
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Java HTTP Server from PraxServer : 1.0");
			out.println("Date: " + new Date());
			out.println("Content-type: " + tipo);
			out.println("Content-length: " + TamArquivo);
			out.println();
			out.print(DadosArquivo);
			out.flush();
			if (VG.verbose) {
				System.out.println("Lido Arquivo Texto: " + NomeArquivo);
				System.out.println("Enviada mensagem do tipo " + tipo + " com " + TamArquivo + " Caracteres"); 
			}
			out.close();
		}
		catch (IOException ioe) {
			System.out.println("Erro");
		}
	} // Fim da Rotina
	
	
	//***************************************************************************************************************************
	// Nome da Rotina: EnvMsgArquivoByte                                                                                        *
	//	                                                                                                                        *
	// Funcao: envia para o cliente conectado uma mensagem HTTP lida de um arquivo em Bytes                                     *
	//                                                                                                                          *
	// Entrada: String com o Caminho do Arquivo; String com o Nome do Arquivo                                                   *
	//                                                                                                                          *
	// Saida: não tem                                                                                                           *
	//	                                                                                                                        *
	//***************************************************************************************************************************
	//
	void EnvMsgArquivoByte(String Caminho, String NomeArquivo) {
		PrintWriter out = null; BufferedOutputStream dataOut = null;
		try {
			out = new PrintWriter(connect.getOutputStream());
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			File Arquivo = new File(Caminho, NomeArquivo);
			int TamArquivo = (int) Arquivo.length();
			String tipo = TipoArquivo(NomeArquivo);
			byte[] MsgDados = readFileData(Arquivo, TamArquivo);
			
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Java HTTP Server from PraxServer : 1.0");
			out.println("Date: " + new Date());
			out.println("Content-type: " + tipo);
			out.println("Content-length: " + TamArquivo);
			out.println();
			out.flush();
			dataOut.write(MsgDados, 0, TamArquivo);
			dataOut.flush();
					
			if (VG.verbose) {
				System.out.println("Lido Arquivo Binário: " + NomeArquivo);
				System.out.println("Enviada mensagem do tipo " + tipo + " com " + TamArquivo + " Caracteres"); 
			}
			out.close();
		}
		catch (IOException ioe) {
			System.out.println("Erro");
		}
	} // Fim da Rotina
	
	//***************************************************************************************************************************
	// Nome da Rotina: EnvMsgStringTxt                                                                                          *
	//	                                                                                                                        *
	// Funcao: envia para o cliente conectado uma mensagem HTTP lida de uma String                                              *
	//                                                                                                                          *
	// Entrada: String com a Mensagem a ser Enviada; String com o Tipo da Mensagem                                              *
	//                                                                                                                          *
	// Saida: não tem                                                                                                           *
	//	                                                                                                                        *
	//***************************************************************************************************************************
	//
	void EnvMsgStringTxt(String Msg, String Tipo) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(connect.getOutputStream());
			int TamMsg = Msg.length();
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Java HTTP Server from PraxServer : 1.0");
			out.println("Date: " + new Date());
			out.println("Content-type: " + Tipo);
			out.println("Content-length: " + TamMsg);
			out.println();
			out.print(Msg);
			out.flush();
			if (VG.verbose) {
				System.out.println("Enviada Mensagem do tipo " + Tipo + " com " + TamMsg + " Caracteres");
			}
			out.close();
		}
		catch (IOException ioe) {
			System.out.println("Erro");
		}
	}
	
	
	//***************************************************************************************************************************
	// Nome da Rotina: EnvMsgStringTxtErro                                                                                      *
	//	                                                                                                                        *
	// Funcao: envia para o cliente conectado uma mensagem de erro HTTP lida de uma String                                      *
	//                                                                                                                          *
	// Entrada: int com o código do erro (404 ou 501)                                                                           *
	//                                                                                                                          *
	// Saida: não tem                                                                                                           *
	//	                                                                                                                        *
	//***************************************************************************************************************************
	//
	void EnvMsgStringTxtErro(int Erro) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(connect.getOutputStream());
			String LinhaInicial = "";
			String MsgErro = "";
			String Tipo = "text/html";
			if (Erro == 404) {
				LinhaInicial = "HTTP/1.1 404 File Not Found";
				MsgErro = "<h2>404 File Not Found</h2><h3>HTTP/1.1 PraxServer</h3>";
			}
			
			if (Erro == 501) {
				LinhaInicial = "HTTP/1.1 501 Not Implemented";
				MsgErro = "<h2>501 Not Implemented</h2><h3>HTTP/1.1 PraxServer</h3>";
			}
			int TamMsg = MsgErro.length();
			out.println(LinhaInicial);
			out.println("Server: Java HTTP Server from PraxServer : 1.0");
			out.println("Date: " + new Date());
			out.println("Content-type: " + Tipo);
			out.println("Content-length: " + TamMsg);
			out.println();
			out.print(MsgErro);
			out.flush();
			if (VG.verbose) {
				System.out.println("Enviada Mensagem de Erro: " + LinhaInicial);
			}
			out.close();
		}
		catch (IOException ioe) {
			System.out.println("Erro");
		}
	}

	
	//***************************************************************************************************************************
	// Nome da Rotina: ExecComandoHTTP                                                                                          *
    //	                                                                                                                        *
	// Funcao: carrega as variáveis VG.Comando e VG.ComRecHTTP para a rotina SupArduinoUDP enviar o comando                     *
	//                                                                                                                          *
	// Entrada: EndIP1 = string com o código do comando recebido do cliente HTTP                                                *
	//                                                                                                                          *
	// Saida: não tem                                                                                                           *
    //	                                                                                                                        *
	//***************************************************************************************************************************
	//
	private boolean ExecComandoHTTP(String comrechttp) {
		boolean Res = false;
		if (comrechttp.equals("cmd=0002")) {
			Res = true;
			VG.Comando = 2;
			VG.ComRecHTTP = "Comando: Acerto Relogio";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0003")) {
			Res = true;
			VG.Comando = 3;
			VG.ComRecHTTP = "Comando: Modo Economia";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0004")) {
			Res = true;
			VG.Comando = 4;
			VG.ComRecHTTP = "Comando: Modo Normal";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0016")) {
			Res = true;
			VG.Comando = 16;
			VG.ComRecHTTP = "Comando: Manual Carga 1";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0017")) {
			Res = true;
			VG.Comando = 17;
			VG.ComRecHTTP = "Comando: Automatico Carga 1";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0005")) {
			Res = true;
			VG.Comando = 5;
			VG.ComRecHTTP = "Comando: Manual Cargas 234";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0006")) {
			Res = true;
			VG.Comando = 6;
			VG.ComRecHTTP = "Comando: Automatico Cargas 234";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0007")) {
			Res = true;
			VG.Comando = 7;
			VG.ComRecHTTP = "Comando: Habilita Carga 1";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0008")) {
			Res = true;
			VG.Comando = 8;
			VG.ComRecHTTP = "Comando: Desabilita Carga 1";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0009")) {
			Res = true;
			VG.Comando = 9;
			VG.ComRecHTTP = "Comando: Habilita Carga 2";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0010")) {
			Res = true;
			VG.Comando = 10;
			VG.ComRecHTTP = "Comando: Desabilita Carga 2";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0011")) {
			Res = true;
			VG.Comando = 11;
			VG.ComRecHTTP = "Comando: Habilita Carga 3";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0012")) {
			Res = true;
			VG.Comando = 12;
			VG.ComRecHTTP = "Comando: Desabilita Carga 3";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0013")) {
			Res = true;
			VG.Comando = 13;
			VG.ComRecHTTP = "Comando: Habilita Carga 4";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0014")) {
			Res = true;
			VG.Comando = 14;
			VG.ComRecHTTP = "Comando: Desabilita Carga 4";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0015")) {
			Res = true;
			VG.Comando = 15;
			VG.ComRecHTTP = "Comando: Apaga Indicadores de Falha";
			if (VG.verbose) { System.out.println(VG.ComRecHTTP); }
		}
		if (comrechttp.equals("cmd=0020")) {
			Res = true;
			VG.SrcRefresh = "";
			VG.ComRecHTTP = "Atualiza";
		}
		if (comrechttp.equals("cmd=0021")) {
			Res = true;
			//VG.SrcRefresh = "<Meta http-equiv='refresh' content='2'>";
			//VG.ComRecHTTP = "Auto Refresh";
			VG.ComRecHTTP = "Teste";
		}
		return(Res);
	}
	
	
	//*****************************************************************************************************************
    //                                                                                                                *
	// Nome da Rotina: readFileData                                                                                   *
	//	                                                                                                              *
	// Funcao: lê um arquivo no formato de sequencia de bytes                                                         *
    //                                                                                                                *
	// Entrada: variavel tipo arquivo e tamanho do arquivo a ser lido                                                 *
    //                                                                                                                *
	// Saida: array de bytes com o conteudo do arquivo                                                                *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
	
	
	//*****************************************************************************************************************
    //                                                                                                                *
	// Nome da Rotina: LeArquivoTexto                                                                                 *
	//	                                                                                                              *
	// Funcao: lê um arquivo texto do HD                                                                              *
    //                                                                                                                *
	// Entrada: string com caminho do arquivo e string com o nome do arquivo a ser lido                               *
    //                                                                                                                *
	// Saida: String com o conteúdo do arquivo texto lido                                                             *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	private String LeArquivoTexto(String Caminho, String NomeArquivo) {
		String Res = "";
		File Arquivo = new File(Caminho + NomeArquivo);
		try {
			BufferedReader br = new BufferedReader(new FileReader(Arquivo));
		    
			String st; 
			while ((st = br.readLine()) != null) {
				Res = Res + st + "\n";
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Arquivo nao encontrado: " + Caminho + NomeArquivo);
		}
		
		return(Res);
	}
		
	
	//*****************************************************************************************************************
    //                                                                                                                *
	// Nome da Rotina: getContentType                                                                                 *
	//	                                                                                                              *
	// Funcao: determina o tipo do arquivo texto de acordo com a extensão                                             *
    //                                                                                                                *
	// Entrada: string com o tipo do arquivo texto (text/html ou text/javascript ou text/css ou text/plain            *
    //                                                                                                                *
	// Saida: String com o conteúdo do arquivo texto lido                                                             *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	private String TipoArquivo(String fileRequested) {
		String tipo = "text/plain";
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html")) {
			tipo = "text/html";
		}
		
		if (fileRequested.endsWith(".js")) {
			tipo = "text/javascript";
		}
		
		if (fileRequested.endsWith(".css")) {
			tipo = "text/css";
		}
		
		return(tipo);
	}
	
	
	//*****************************************************************************************************************
    //                                                                                                                *
	// Nome da Rotina: fileNotFound                                                                                   *
	//	                                                                                                              *
	// Funcao:                                                                                                        *
    //                                                                                                                *
	// Entrada:                                                                                                       *
    //                                                                                                                *
	// Saida:                                                                                                         *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		EnvMsgStringTxtErro(404);  // envia mensagem de erro 404 (Recurso não Disponível)
	}

}