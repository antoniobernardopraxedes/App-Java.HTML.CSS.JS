import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

//***************************************************************************************************************************
//                                                                                                                          *
//Autor: Antonio Bernardo de Vasconcellos Praxedes                                                                          *
//                                                                                                                          *  
//Data: 18/08/2021                                                                                                          *
//                                                                                                                          *
//Nome da Classe: HTTPSrvCloud                                                                                              *
//                                                                                                                          *
//Funcao: Programa Principal Servidor HTTP para ser instalado no Servidor em Nuvem                                          *
//                                                                                                                          *
//***************************************************************************************************************************
//
public class HTTPSrvCloud implements Runnable {
	
	static String MsgXML = "";
	static int Porta = 8080;
	private Socket connect;
	
	static String Caminho = "";
	static String CaminhoNuvem = "/home/bernardo/Executavel/";
	static String CaminhoLocal = "/home/antonio/workspace/Cloud/";
	
	//static String ClientIP;
	//static int NumCharMsgXML = 0;
	//static int CodMsgRec = 0;
			
	public HTTPSrvCloud(Socket c) {
		connect = c;
	}

	//***************************************************************************************************************************
	//                                                                                                                          *
    // Rotina Principal (main) da ClasseHTTPSrvSup                                                                              *
	//                                                                                                                          *
	// Funcao: aguarda a conexao do Cliente (Browser)                                                                           *
	//                                                                                                                          *
	//***************************************************************************************************************************
	//
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MntMsg.IniciaVarGlobais();
		VG.verbose = true;
		MntMsg.PrtMsg = false;
		MsgXML = MntMsg.XML01Falha(0);
		
		try {
			ServerSocket serverConnect = new ServerSocket(Porta);
			InetAddress ip = InetAddress.getLocalHost();
			String NomeComputador = "";
			NomeComputador = ip.getHostName();
			
			if (NomeComputador.equals("antonio-Vostro1510")) {
				Caminho = CaminhoLocal;
				System.out.println("\n\nServidor Iniciado no Computador Local");
			}
			else {
				Caminho = CaminhoNuvem;
				System.out.println("\n\nServidor Iniciado no Computador na Nuvem");
			}
			
			System.out.println("Esperando por Conexoes na Porta: " + Porta);
			
			while (true) {    // Espera a conexão do cliente
				HTTPSrvCloud myServer = new HTTPSrvCloud(serverConnect.accept());
				if (VG.verbose) {
					System.out.println("Conexao Aberta com o Cliente (" + new Date() + ")");
				}
				Thread thread = new Thread(myServer);      // Thread para gerenciar a conexão do cliente
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Erro na Conexao com o Servidor: " + e.getMessage());
		}
	} // Fim da Rotina public static void main(String[] args) {
	
	
	//***************************************************************************************************************************
	//                                                                                                                          *
    // Rotina que Processa a Solicitação do Cliente da ClasseHTTPSrvSup                                                         *
	//                                                                                                                          *
	// Funcao: processa a solicitação do Cliente HTTP                                                                           *
	//                                                                                                                          *
	//***************************************************************************************************************************
	//
	//@Override
	public void run() {
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		InputStreamReader dataIn = null; InputStream ByteIn = null; String fileRequested = null;
		try {
			ByteIn = connect.getInputStream();
			dataIn = new InputStreamReader(ByteIn); //
			in = new BufferedReader(dataIn);  //new InputStreamReader(connect.getInputStream()));
			out = new PrintWriter(connect.getOutputStream());
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			
			String LinhaCab[] = new String[12];
			int CChar = 0;
			int CLin = 0;
			int ChRec = 0;
			int CR = 13;
			int LF = 10;
			boolean Leu_CRLF = false;
			boolean fim = false;
			String Requisicao = null;
			LinhaCab[0] = "";
			int Contador = 0;
			boolean mobile = false;
									
			while (!fim) {
				ChRec = ByteIn.read();
				CChar = CChar + 1;
				if (CChar > 2000) { fim = true; }
				if (ChRec == CR) {
					ChRec = ByteIn.read();
					if (ChRec == LF) {
						if (Leu_CRLF) {
							fim = true;
						}
						else {
							LinhaCab[CLin] = LinhaCab[CLin] + "\n";
							CLin = CLin + 1;
							LinhaCab[CLin] = "";
							Leu_CRLF = true;
						}
					}
				}
				else {
					LinhaCab[CLin] = LinhaCab[CLin] + (char)ChRec;
					Leu_CRLF = false;
				}
			}	
					
			// Monta o Cabeçalho da Requisição na String CabHTTP
			String CabHTTP = "";
			for (int k = 0; k < CLin; k++){
				CabHTTP = CabHTTP + LinhaCab[k] + "\n";
				//System.out.println("k = " + k + " - Linha: " + LinhaCab[k]);
			}
			
			if (CabHTTP.toLowerCase().indexOf("mobile") >= 0) {
				mobile = true;
				System.out.println("Acesso por Dispositivo Móvel");
			}
			else {
				mobile = false;
			}
			
			StringTokenizer parseLinha1 = new StringTokenizer(LinhaCab[0]);
			String method = parseLinha1.nextToken().toUpperCase();
			Requisicao = parseLinha1.nextToken();
			String ArquivoReq = Requisicao.substring(1);
			int TamArqReq = ArquivoReq.length();
			String ArqReq = "";
			
			boolean RecMetodoValido = false;
			boolean RecReqValida = false;
			
			if (VG.verbose) {
				System.out.println("Método: " + method + "  -  Arquivo Requisitado: " + ArquivoReq);
			}
			
			if (method.equals("GET")) {  // Trata o método GET
				RecMetodoValido = true;
					
				// Se não há requisição de arquivo, solicita arquivo index.html (página raiz)
				if (Requisicao.equals("/") || Requisicao.equals("/?")) {
					RecReqValida = EnvMsgArquivoTxt(Caminho, "index.html");
				}
				else { // Trata a requisição do método GET
					
					// Trata requisições de arquivos texto de página HTML
					if (ArquivoReq.endsWith(".html")) {
						
						if (mobile) {
							ArqReq = ArquivoReq.substring(0, TamArqReq - 5);
							ArqReq = ArqReq + ".m.html"; 
							}
						else {
							ArqReq = ArquivoReq;
						}
						if (VG.verbose) {
							System.out.println("Ler Arquivo HTML = " + ArqReq);
						}
						RecReqValida = EnvMsgArquivoTxt(Caminho, ArqReq);
					}
					
					// Trata requisições de arquivos texto de estilos (CSS)
					if (ArquivoReq.endsWith(".css")) {
						if (mobile) {
							ArqReq = ArquivoReq.substring(0, TamArqReq - 4);
							ArqReq = ArqReq + ".m.css"; 
							}
						else {
							ArqReq = ArquivoReq;
						}
						if (VG.verbose) {
							System.out.println("Ler Arquivo CSS = " + ArqReq);
						}
						RecReqValida = EnvMsgArquivoTxt(Caminho, ArqReq);
					}
					
					// Trata requisições de arquivos de programas Javascript
					if (ArquivoReq.endsWith(".js")) {
						if (mobile) {
							ArqReq = ArquivoReq.substring(0, TamArqReq - 3);
							ArqReq = ArqReq + ".m.js"; 
							}
						else {
							ArqReq = ArquivoReq;
						}
						if (VG.verbose) {
							System.out.println("Ler Arquivo Javascript = " + ArqReq);
						}
						RecReqValida = EnvMsgArquivoTxt(Caminho, ArqReq);
					}
					
					// Trata requisições de arquivos de imagem
					if (ArquivoReq.endsWith(".ico") || ArquivoReq.endsWith(".jpg") || ArquivoReq.endsWith(".png")) {
						RecReqValida = EnvMsgArquivoByte(Caminho, ArquivoReq);
					}
						
					// Trata requisição de mensagem XML de Atualização dos Valores das Variáveis
					if (ArquivoReq.endsWith("local001.xml")) {
						RecReqValida = true;
						Contador = Contador + 1;
						if (Contador < 8) {
							EnvMsgStringTxt(MsgXML, "text/xml", "200");
						}
						else {
							EnvMsgStringTxt(MntMsg.XML01Falha(0), "text/xml", "200");
						}
					}
				
				} // else if (Requisicao.equals("/") || Requisicao.equals("/?")) {
			}  // if (method.equals("GET"))
							    
			if (method.equals("POST")) {              // Se método = POST,
				RecMetodoValido = true;
				if (ArquivoReq.equals("atualiza")) {  // e requisição = "atualiza", indica mensagem binária de atualização
					RecReqValida = true;
					
					if (VG.verbose) {
						System.out.println("Recebida mensagem de atualização");
					}
					
					String TamMsg = "";       // TamMsg = string com o número de caracteres/bytes da mensagem
					String TipoMsg = "";      // TipoMsg = string com o tipo da mensagem (XML ou octet/stream"
					int TamanhoMensagem = 0;  // TamanhoMensagem = inteiro com o número de caracteres/bytes da mensagem
					StringTokenizer parseLinha3 = new StringTokenizer(LinhaCab[2]); // Linha 3
					String IdLinha3 =  parseLinha3.nextToken().toLowerCase();       // IdLinha3 minúsculo deve ser "Content-Length:"
					StringTokenizer parseLinha4 = new StringTokenizer(LinhaCab[3]); // Linha 4
					String IdLinha4 = parseLinha4.nextToken().toLowerCase();        // IdLinha4 minúsculo deve ser "Content-Type:"
					
					if (IdLinha3.equals("content-length:") && IdLinha4.equals("content-type:")) {
						TamMsg = parseLinha3.nextToken();                  
						TamanhoMensagem = Util.StringToInt(TamMsg);
						TipoMsg = parseLinha4.nextToken().toLowerCase();
												
						if (TipoMsg.equals("application/octet-stream")) {  // Se é mensagem do tipo binária
								
							for (int i = 0; i < TamanhoMensagem; i++){
								MntMsg.receiveData1[i] = ByteIn.read();    // Recebe os bytes e carrega no buffer
							}
							
							int Byte0 = MntMsg.receiveData1[0];
							int Byte1 = MntMsg.receiveData1[1];
							boolean MsgBinOK = false;
							if ((Byte0 == 0x60) && (Byte1 == 0x45)) {  // Se recebeu mensagem CoAP válida,
								MntMsg.LeEstMedsPayload();             // le as variaveis
								MsgBinOK = true;
							}
							if (MsgBinOK) {                         // Se a mensagem CoAP recebida é válida,
								if (MntMsg.EstCom1 == 1) {   		// e se a comunicacao com o programa de atualização está OK,
									MsgXML = MntMsg.XML01();        // monta a mensagem XML
									MsgXML = MsgXML + " ";
								}
								else {                        		// Se a comunicacao com o programa de atualização está em falha,
									MntMsg.XML01Falha(1);     		// monta a mensagem XML de falha
								}
								if (MntMsg.verbose) {
									System.out.println("Recebida Msg HTTP Binaria de Atualizacao com " + TamanhoMensagem + " Bytes");
									System.out.println("Hora da UTR: " + Util.ImpHora(MntMsg.Hora ,MntMsg.Minuto, MntMsg.Segundo));
								}
								
								// Responde com mensagem de XML de comando
								String StrComando = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
								StrComando = StrComando + "<CMD></CMD>";
								EnvMsgStringTxt(StrComando, "text/xml", "200");
								Contador = 0;
							}
							else {
								if (MntMsg.verbose) {
									System.out.println("Recebida Mensagem Binaria Invalida");
								}
								String StrComando = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
								StrComando = StrComando + "<CMD>MsgInv</CMD>";
								EnvMsgStringTxt(StrComando, "text/xml", "200");	
							}
						} // if (TipoMsg.equals("application/octet-stream"))
					} // if ((IdLinha3 == "content-length:") && (IdLinha4 == "content-type:"))
				}  // if (Requisicao.equals("atualiza"))
			} // if (method.equals("POST"))
			
			if (RecMetodoValido) {             // Se foi recebido um método válido,
				if (!RecReqValida) {           // e se não está disponível o recurso solicitado pelo método GET ou POST
					EnvMsgStringTxtErro(404);  // envia mensagem de erro 404 (Recurso não Disponível)
				}
			}
			else {                             // Se não foi recebido um método válido,
				EnvMsgStringTxtErro(501);      // envia mensagem de erro 501 (Método Não Implementado)
			}
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Erro: arquivo nao encontrado : " + ioe.getMessage());
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
			if (MntMsg.verbose) {
				System.out.println("Conexao com o Cliente Encerrada\n"); 
			}
		}
	}  // Fim da Rotina public void run() {
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: EnvMsgArquivoTxt                                                                               *
	//	                                                                                                              *
	// Funcao: envia para o cliente conectado uma mensagem HTTP lida de um arquivo em caracteres                      *
	//                                                                                                                *
	// Entrada: String com o Caminho do Arquivo; String com o Nome do Arquivo                                         *
	//                                                                                                                *
	// Saida: se o arquivo foi lido corretamente retorna true                                                         *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	boolean EnvMsgArquivoTxt(String Caminho, String NomeArquivo) {
		PrintWriter out = null;
		boolean ArquivoLido = false;
		try {
			out = new PrintWriter(connect.getOutputStream());
			File Arquivo = new File(Caminho, NomeArquivo);
			int TamArquivo = (int) Arquivo.length();
			String DadosArquivo = LeArquivoTexto(Caminho, NomeArquivo);
			
			if (!DadosArquivo.equals("erro")) {          // Se o arquivo foi lido corretamente
				ArquivoLido = true;
				String tipo = TipoArquivo(NomeArquivo);  // obtém o tipo do arquivo, monta o cabeçalho e a mensagem
				out.println("HTTP/1.1 200 OK");
				out.println("Server: Java HTTP Server from PraxServer : 1.0");
				out.println("Date: " + new Date());
				out.println("Content-type: " + tipo);
				out.println("Content-length: " + TamArquivo);
				out.println();
				out.print(DadosArquivo);
				out.flush();
				if (VG.verbose) {
					System.out.println("Lido --- Arquivo com Mensagem HTTP: " + NomeArquivo);
					System.out.println("Enviada Mensagem HTTP do tipo " + tipo + " com " + TamArquivo + " Caracteres"); 
				}
			}
			else {
				if (VG.verbose) {
					System.out.println("Erro na leitura do arquivo: " + NomeArquivo);
				}
			}
			return(ArquivoLido);
		}
		catch (IOException ioe) {
			if (VG.verbose) {
				System.out.println("Erro na Rotina EnvMsgArquivoTxt");
			}
			return(false);
		}
	} // Fim da Rotina
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: EnvMsgArquivoByte                                                                              *
	//	                                                                                                              *
	// Funcao: envia para o cliente conectado uma mensagem HTTP lida de um arquivo em Bytes                           *
	//                                                                                                                *
	// Entrada: String com o Caminho do Arquivo; String com o Nome do Arquivo                                         *
	//                                                                                                                *
	// Saida: não tem                                                                                                 *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	boolean EnvMsgArquivoByte(String Caminho, String NomeArquivo) {
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
				System.out.println("Lido Arquivo com Mensagem HTTP: " + NomeArquivo);
				System.out.println("Enviada Mensagem HTTP do tipo " + tipo + " com " + TamArquivo + " Caracteres"); 
			}
			return(true);
		}
		catch (IOException ioe) {
			if (VG.verbose) {
				System.out.println("Erro na Rotina EnvMsgArquivoByte");
			}
			return(false);
		}
	} // Fim da Rotina
	
	//*****************************************************************************************************************
	// Nome da Rotina: EnvMsgStringTxt                                                                                *
	//	                                                                                                              *
	// Funcao: envia para o cliente conectado uma mensagem HTTP lida de uma String                                    *
	//                                                                                                                *
	// Entrada: String com a Mensagem a ser Enviada; String com o Tipo da Mensagem                                    *
	//                                                                                                                *
	// Saida: não tem                                                                                                 *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	void EnvMsgStringTxt(String Msg, String Tipo, String CodRsp) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(connect.getOutputStream());
			int TamMsg = Msg.length();
			out.println("HTTP/1.1 " + CodRsp + " OK");
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
		}
		catch (IOException ioe) {
			if (VG.verbose) {
				System.out.println("Erro na Rotina EnvMsgArquivoByte");
			}
		}
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: EnvMsgStringTxtErro                                                                            *
	//	                                                                                                              *
	// Funcao: envia para o cliente conectado uma mensagem de erro HTTP lida de uma String                            *
	//                                                                                                                *
	// Entrada: int com o código do erro (404 ou 501)                                                                 *
	//                                                                                                                *
	// Saida: não tem                                                                                                 *
	//	                                                                                                              *
	//*****************************************************************************************************************
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
		}
		catch (IOException ioe) {
			System.out.println("Erro");
		}
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
	// Funcao: lê um arquivo texto                                                                                    *
    //                                                                                                                *
	// Entrada: string com caminho do arquivo e string com o nome do arquivo a ser lido                               *
    //                                                                                                                *
	// Saida: String com o conteúdo do arquivo texto lido                                                             *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	private String LeArquivoTexto(String Caminho, String NomeArquivo) {
		String Arq = "";
		File Arquivo = new File(Caminho + NomeArquivo);
		try {
			BufferedReader br = new BufferedReader(new FileReader(Arquivo));
		    
			String st; 
			while ((st = br.readLine()) != null) {
				Arq = Arq + st + "\n";
			}
		} catch (IOException e) {
			return("erro");
		}
		
		return(Arq);
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
		
		if (fileRequested.endsWith(".jpg")  ||  fileRequested.endsWith(".jpeg")) {
			tipo = "image/jpeg";
		}
		
		if (fileRequested.endsWith(".gif")) {
			tipo = "image/gif";
		}
		
		if (fileRequested.endsWith(".png")) {
			tipo = "image/png";
		}
		
		if (fileRequested.endsWith(".bmp")) {
			tipo = "image/bmp";
		}
		
		return(tipo);
	}
	
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		EnvMsgStringTxtErro(404);  // envia mensagem de erro 404 (Recurso não Disponível)
	}
	
}
