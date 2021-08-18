
import java.net.*;
import java.io.IOException;
import java.lang.String;

public class SupConcArd {
	//*********************************************************************************************************************************
	// Nome da Rotina: EnvRecMsgCoAPUDP                                                                                               *
	//                                                                                                                                *
	// Funcao: envia uma mensagem de requisição e recebe a mensagem de resposta do Concentrador Arduino Mega em Protocolo CoAP        *
	//                                                                                                                                *
	// Byte |           0         |        1        |        2        |        3        |        4        |        5        |         *
	// Bit  | 7 6 | 5 4 | 3 2 1 0 | 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 |         *
	//      | Ver |Tipo |  Token  |  Código (c.m)	|              Message ID           |      Option     |   Payload ID    |         *
    //                                                                                                                                *
	// Ver (Versão) = 01 (O número da versão do protocolo CoAP é fixo)  / TKL (Token) = 0000 (não é usado)                            *
    // Tipo (Tipo de Mensagem): 00 Confirmável (CON) / 01 Não-Confirmável (NON) / 10 Reconhecimento (ACK) / 11 Reset (RST)            *
	//                                                                                                                                *
	// Códigos de Solicitação: 0000 0000 EMPTY / 0000 0001 GET   / 0000 0010 POST / 0000 0011 PUT / 0000 0100 DELETE                  * 
	//                                                                                                                                *
	// Cód. Resposta (Sucesso): 0100 0001 Created / 0100 0010 Deleted / 0100 0011 Valid / 0100 0100 Changed / 0100 0101 Content       *
	//                                                                                                                                *
	// Cód. Erro Cliente: 1000 0000 Bad Request / 1000 0001 Unauthorized / 1000 0010 Bad Option / 1000 0011 Forbidden                 *
	// 1000 0100 Not Found / 1000 0101 Method Not Allowed / 1000 0110 Not Acceptable / 1000 1100 Request Entity Incomplete            *
	//                                                                                                                                *
	// Cód. Erro Servidor: 1010 0000 Internal Server Error / 1010 0001 Not Implemented / 1010 0010 Bad Gateway                        *
	//                     1010 0011 Service Unavailable / 1010 0100 Gateway Timeout / 1010 0101 Proxying Not Supported               *
	//                                                                                                                                *
	// Message ID (Identificação da mensagem): inteiro de 16 bits sem sinal Mensagem Enviada e Mensagem de Resposta com mesmo ID      *
	//                                                                                                                                *
	// Option (Opções) = 0000 0000 (não é usado) / Identificador de Início do Payload: 1111 1111                                      *
	//*********************************************************************************************************************************
	//
	static int EnvRecMsgCoAPUDP(String EndIP, int Porta, String URI, boolean Imp) {
		int TamMsgRspCoAP = 320;
		
		try {
			byte[] MsgReqCoAP = new byte[32];               // Define o Buffer de Transmissao
			int TamURI = URI.length();
			byte DH [] = new byte[6];
			DH = Util.LeDataHora();
			
			MsgReqCoAP[0] = 0x40;                           // Versão = 01 / Tipo = 00 / Token = 0000
			MsgReqCoAP[1] = 0x01;                           // Código de Solicitação: 0.01 GET
			VG.ContMsgCoAP = VG.ContMsgCoAP + 1;            // Incrementa o Identificador de mensagens
			MsgReqCoAP[2] = Util.ByteHigh(VG.ContMsgCoAP);  // Byte Mais Significativo do Identificador da Mensagem
			MsgReqCoAP[3] = Util.ByteLow(VG.ContMsgCoAP);   // Byte Menos Significativo do Identificador da Mensagem
			MsgReqCoAP[4] = (byte)(0xB0 + TamURI);          // Delta = 11 => Primeira Opcao 11: Uri-path e Número de Bytes da URI
			int j = 5;
			for (int i = 0; i < TamURI; i++) {              // Carrega os codigos ASCII da URI
				char Char = URI.charAt(i);
				int ASCII = (int)Char;
				MsgReqCoAP[i + 5] =(byte)ASCII;
				j = j + 1;
			}
			MsgReqCoAP[j] = (byte)0x11;        // Delta = 1 => Segunda Opcao (11 + 1 = 12): Content-format e Número de Bytes (1)
			j = j + 1;
			MsgReqCoAP[j] = 42;                // Codigo da Opcao Content-format: application/octet-stream
			j = j + 1;
			MsgReqCoAP[j] = -1;                // Identificador de Inicio do Payload (255)
			j = j + 1;
			MsgReqCoAP[j] = (byte)VG.Comando;  // Carrega o Código do Comando no Payload
			j = j + 1;
			MsgReqCoAP[j] = DH[0];             // Carrega a Hora do Computador no Payload
			j = j + 1;
			MsgReqCoAP[j] = DH[1];             // Carrega a Minuto do Computador no Payload
			j = j + 1;
			MsgReqCoAP[j] = DH[2];             // Carrega a Segundo do Computador no Payload
			j = j + 1;
			MsgReqCoAP[j] = DH[3];             // Carrega a Dia do Computador no Payload
			j = j + 1;
			MsgReqCoAP[j] = DH[4];             // Carrega a Mes do Computador no Payload
			j = j + 1;
			MsgReqCoAP[j] = DH[5];             // Carrega a Ano do Computador no Payload
			j = j + 1;
			int TamCab = j;                    // Carrega o número de bytes do cabeçalho
			
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(EndIP);
			clientSocket.setSoTimeout(1000);
			DatagramPacket sendPacket = new DatagramPacket(MsgReqCoAP, TamCab, IPAddress, Porta);
			DatagramPacket receivePacket = new DatagramPacket(VG.receiveData1, TamMsgRspCoAP);
			clientSocket.send(sendPacket);
			if (Imp) {
				System.out.println(Util.LeImpHora() + " - Enviada Requisicao CoAP");
			}
			
			// Espera a Mensagem CoAP de Resposta. Se a mensagem de resposta  for recebida, carrega nas variáveis
			try {
				clientSocket.receive(receivePacket);
				VG.receiveData1[30] = 1;
				LeEstMedsPayload();
				if (Imp) {
					System.out.println(Util.LeImpHora() + " - Recebida Mensagem CoAP");
				}
			}
			catch(java.net.SocketTimeoutException e) { 
				VG.receiveData1[0] = 0x40;
				VG.receiveData1[1] = 1;
				VG.receiveData1[30] = 0;
				if (Imp) {
					System.out.println(Util.LeImpHora() + " - Erro: o Dispositivo nao Respondeu " + VG.receiveData1[14]);
				}
			}
			clientSocket.close();
		}
		catch (IOException err) {
			if (VG.verbose) {
				System.out.println(Util.LeImpHora() + " - Erro na Rotina EnvRecMsgSrv: " + err);
			}
		}
		return(TamMsgRspCoAP);
	}
	
	//*****************************************************************************************************************
	// Nome da Rotina: LeEstMedsPayload()                                                                             *
	//                                                                                                                *
	// Funcao: lê as informações dos estados e medidas recebidas do Concentrador Arduino Mega na mensagem em          *
	//         formato CoAP-OSA-CBM                                                                                   *
	//                                                                                                                *
	// Medidas (64): bytes 160 a 288 - 2 bytes por medida                                                             *
	//                                                                                                                *
	// Entrada: nao tem                                                                                               *
	// Saida: nao tem                                                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static void LeEstMedsPayload() {

		// Le as Informações de Estado do Concentrador Arduino Mega
		VG.Hora = VG.receiveData1[21];
		VG.Minuto = VG.receiveData1[22];
		VG.Segundo = VG.receiveData1[23];
		VG.Dia = VG.receiveData1[24];
		VG.Mes = VG.receiveData1[25];
		VG.Ano = VG.receiveData1[26];
        VG.EstComUTR = VG.receiveData1[27];
        VG.EstComCC1 = VG.receiveData1[28];
        VG.EstComCC2 = VG.receiveData1[29];
        VG.EstCom1 = VG.receiveData1[30];
        		
		// Le as Entradas Digitais recebidas na mensagem recebida do Concentrador Arduino Mega
		VG.DJEINV1 = VG.receiveData1[37];
		VG.CircBoia = VG.receiveData1[38];
		VG.BoiaCxAzul = VG.receiveData1[39];
		VG.CircBomba = VG.receiveData1[40];
		VG.AlRedeBomba = VG.receiveData1[41];
		VG.EstRede = VG.receiveData1[42];
		VG.MdOp = VG.receiveData1[43];
		VG.MdCom = VG.receiveData1[44];
		VG.MdCtrl1 = VG.receiveData1[55];
		VG.MdCtrl = VG.receiveData1[45];
		VG.Carga1 = VG.receiveData1[46];
		VG.Carga2 = VG.receiveData1[47];
		VG.Carga3 = VG.receiveData1[48];
		VG.Carga4 = VG.receiveData1[49];
		VG.HabCom = VG.receiveData1[50];
		VG.EstadoInversor1 = VG.receiveData1[51];
		VG.EstadoInversor2 = VG.receiveData1[52];
		VG.EstadoCarga3 = VG.receiveData1[53];
		VG.BombaLigada = VG.receiveData1[54];
	
		// Le os Alarmes da mensagem recebida do Concentrador Arduino Mega
		VG.FalhaIv1 = VG.receiveData1[56];
		VG.SubTensaoInv1 = VG.receiveData1[57];
		VG.SobreTensaoInv1 = VG.receiveData1[58];
		VG.SobreTempDrInv1 = VG.receiveData1[59];
		VG.SobreTempTrInv1 = VG.receiveData1[60];
		VG.DjAbIv1 = VG.receiveData1[61];
		VG.FalhaIv2 = VG.receiveData1[62];
		VG.SubTensaoInv2 = VG.receiveData1[63];
		VG.SobreTensaoInv2 = VG.receiveData1[64];
		VG.SobreTempDrInv2 = VG.receiveData1[65];
		VG.SobreTempTrInv2 = VG.receiveData1[66];
		VG.DjAbIv2 = VG.receiveData1[67];
		
		VG.CDBat = VG.receiveData1[68];
		VG.CxAzNvBx = VG.receiveData1[69];
		VG.EdCxAzCheia = VG.receiveData1[70];
		VG.FonteCC2Ligada = VG.receiveData1[71];
		VG.EstadoCxAz = VG.receiveData1[72];
		VG.FonteCC1Ligada = VG.receiveData1[73];
		
		VG.SobreCorrInv1 = VG.receiveData1[74];
		VG.SobreCorrInv2 = VG.receiveData1[75];
    
		// Le o estado das saidas digitais
		int k = 112;
		for (int i = 0; i < 32; i++){
			VG.SD[i] = VG.receiveData1[k];
			k = k + 1;
		}
	
		// Carrega as variaveis com os valores das saidas digitais do Concentrador Arduino Mega
		VG.Iv1Lig = VG.SD[1];
		VG.CT2Inv = VG.SD[17];
		VG.CT1Inv = VG.SD[0];
		VG.CT3Inv = VG.SD[2];
		VG.Iv2Lig = VG.SD[10];
		VG.EstFonteCC = VG.SD[16];
	
		// Le as Medidas da mensagem recebida do Concentrador Arduino Mega (medidas 0 a 47)
		k = 160;
		for (byte i = 0; i < 48; i++){
			VG.Med[i] = Util.DoisBytesInt(VG.receiveData1[k], VG.receiveData1[k + 1]);
			k = k + 2;
		}
	
		// Carrega as medidas lidas do Concentrador Arduino Mega nas variaveis
		VG.VBat = VG.Med[0];           // Tensão do Banco de Baterias
		VG.VMBat = VG.Med[16];         // Tensão Média Estendida do Banco de Baterias
		VG.VRede = VG.Med[5];          // Tensão da Rede
		VG.Icarga3 = VG.Med[14];       // Corrente Carga 3 (Geladeira)
		VG.ICircCC = VG.Med[3];        // Corrente Total dos Circuitos CC
		VG.IFonteCC = VG.Med[11];      // Corrente de Saída da Fonte CC
		
		
		VG.TmpBmbLig = VG.Med[17];     // Tempo da Bomba Ligada
		VG.TmpCxAzNvBx = VG.Med[46];   // Tempo da Caixa Azul em Nivel Baixo
		
		// Leitura e Cálculo das Medidas referentes à Geração e Consumo
		VG.VP12 = VG.Med[18];          // 0x3100 - PV array voltage 1
		VG.IS12 = VG.Med[19];          // 0x3101 - PV array current 1
		VG.WS12 = VG.Med[20];          // 0x3102 - PV array power 1
		VG.VBat1 = VG.Med[21];         // 0x3104 - Battery voltage 1
		VG.ISCC1 = VG.Med[22];         // 0x3105 - Battery charging current 1
		VG.WSCC1 = VG.Med[23];         // 0x3106 - Battery charging power 1
		VG.TBat =  VG.Med[24];         // 0x3110 - Battery Temperature 1
	
		VG.VP34 = VG.Med[26];          // 0x3100 - PV array voltage 2
		VG.IS34 = VG.Med[27];          // 0x3101 - PV array current 2
		VG.WS34 = VG.Med[28];          // 0x3102 - PV array power 2
		VG.VBat2 = VG.Med[29];         // 0x3104 - Battery voltage 2
		VG.ISCC2 = VG.Med[30];         // 0x3105 - Battery charging current 2
		VG.WSCC2 = VG.Med[31];         // 0x3106 - Battery charging power 2 (VG.Med[45])
		
		VG.ITotGer = VG.Med[33];       					// Corrente Total Gerada
		VG.WCircCC = VG.Med[35];       					// Potencia Consumida pelos Circuitos de 24Vcc
		VG.WFonteCC = VG.Med[36];      					// Potencia Fornecida pela Fonte 24Vcc
		VG.IBat = VG.Med[37];          					// Corrente de Carga ou Descarga do Banco de Baterias
		VG.WBat = (VG.VBat * VG.IBat)/100;				// Potência de Carga/Descarga do Banco de Baterias
		VG.ITotGer = VG.ISCC1 + VG.ISCC2;				// Corrente Total Gerada
		VG.WTotGer = VG.WSCC1 + VG.WSCC2;				// Potência Total Gerada 
		VG.ITotCg = VG.IEIv1+VG.IEIv2+(VG.ICircCC/10);	// Corrente Total Consumida pelas Cargas
		VG.WTotCg =  VG.WEIv1+VG.WEIv2+VG.WCircCC;		// Potência Total Consumida pelas Cargas
		
		// Leitura e Cálculo das Medidas referentes ao Inversor 1
		VG.IEIv1 = VG.Med[12];         					// Corrente de Entrada do Inversor 1 (15)
		VG.WEIv1 = (VG.VBat * VG.IEIv1)/100;			// Potência de Entrada do Inversor 1 (VG.Med[41])
		VG.VSIv1 = VG.Med[4];          					// Tensão de Saída do Inversor 1
		VG.ISInv1 = (7*VG.Med[10])/10;        			// Corrente de Saída do Inversor 1 (13)
		VG.WSInv1 = (VG.VSIv1 * VG.ISInv1)/1000;		// Potencia de Saida do Inversor 1 (VG.Med[42])
		VG.TDInv1 = VG.Med[8];         					// Temperatura do Driver do Inversor 1 (2)
		VG.TTInv1 = VG.Med[9];         					// Temperatura do Transformador do Inversor 1 (7)
		if (VG.WEIv1 > 2000) {                          // Se o Inversor 1 está ligado,
			VG.EfIv1 = (100*VG.WSInv1)/VG.WEIv1;		// calcula a Eficiência do Inversor 1
		}
		else {
			VG.EfIv1 = 0;
		}
		
		// Leitura e Cálculo das Medidas referentes ao Inversor 2
		double IEInversor2 = 838*VG.Med[15];  //  838
		VG.IEIv2 = (int)(IEInversor2 / 1000); 			 // Corrente de Entrada do Inversor 2 (12)
		VG.WEIv2 = (VG.VBat * VG.IEIv2)/100;         	 // Potencia de Entrada do Inversor 2 (VG.Med[38])
		VG.VSIv2 = VG.Med[6];          					 // Tensão de Saída do Inversor 2
		VG.ISInv2 = VG.Med[13];
		VG.WSInv2 = (VG.VSIv2 * VG.ISInv2)/1000;       	 // Potencia de Saida do Inversor 2 (VG.Med[39])
		VG.TDInv2 = VG.Med[2];         					 // Temperatura do Driver do Inversor 2 (8)
		VG.TTInv2 = VG.Med[7];         					 // Temperatura do Transformador do Inversor 2 (9)
		if (VG.WEIv2 > 2000) {                           // Se o Inversor 2 está ligado,
			VG.EfIv2 = (100*VG.WSInv2)/VG.WEIv2;		 // calcula a Eficiência do Inversor 2
		}
		else {
			VG.EfIv2 = 0;
		}
		
		// Cálculo da Saude do Controlador de Carga 1
		if (VG.WSCC1 > 0) {
			VG.SDCC1 = 100* (VG.WS12 / VG.WSCC1);
		}
		else {
			if (VG.WS12 == 0) {
				VG.SDCC1 = 100;
			}
			else {
				VG.SDCC1 = 0;
			}
		}
		
		// Cálculo da Saude do Controlador de Carga 2
		if (VG.WSCC2 > 0) {
			VG.SDCC2 = 100* (VG.WS34 / VG.WSCC2);
		}
		else {
			if (VG.WS34 == 0) {
				VG.SDCC2 = 100;
			}
			else {
				VG.SDCC2 = 0;
			}
		}
		
		
		VG.SDBat = 95;
		
		// As seguintes medidas são calculadas e carregadas no buffer para o servidor em nuvem
		CB2Bytes(VG.ITotGer, 33);              		// Corrente Total Gerada
		CB2Bytes(VG.WTotGer, 34);              		// Potência Total Gerada
		CB2Bytes(VG.ITotCg, 44);               		// Corrente Total Cargas
		CB2Bytes(VG.WTotCg, 45);               		// Potência Total Cargas
		
		CB2Bytes(VG.TemperaturaBoiler, 48);     	// 
		CB2Bytes(VG.TemperaturaPlaca, 49);      	//
		CB2Bytes(VG.TempoBmbLigada, 50);        	//
		CB2Bytes(VG.TempoBmbDesligada, 51);     	//
		
		VG.receiveData1[144] = (byte)VG.EfIv1;  	// Eficiência do Inversor 1
		InferenciaFuzzyInv1();                  	// Calcula a Saude do Inversor 1
		VG.receiveData1[145] = (byte)VG.SDIv1;  	// Carrega a Saude do Inversor 1 no Buffer
		InferenciaFuzzyInv2();                  	// Calcula a Saude do Inversor 2
		VG.receiveData1[146] = (byte)VG.SDIv2;  	// Carrega a Saude do Inversor 2 no Buffer
		VG.receiveData1[147] = (byte)VG.EfIv2;  	// Eficiência do Inversor 2
		VG.receiveData1[148] = (byte)VG.SDCC1;  	// Saude do Controlador de Carga 1
		VG.receiveData1[149] = (byte)VG.SDCC2;  	// Saude do Controlador de Carga 2
		VG.receiveData1[150] = (byte)VG.SDBat;  	// Saude do Banco de Baterias
	
		VG.receiveData1[151] = (byte)VG.EstadoBombaAQ;
		VG.receiveData1[152] = (byte)VG.EstadoAquecedor;
		
	} // Fim da Rotina LeEstMedsPayload()
	
	
	//******************************************************************************************************************
	// Nome da Rotina: CB2Bytes                                                                                        *
	//	                                                                                                               *
	// Funcao: carrega um valor int em 2 bytes no buffer BufTxEst no formato (Low, High)                               *
	//	                                                                                                               *
	// Entrada: valor int a ser carregado, índice da medida no buffer (0 a 47)                                         *
	// Saida: nao tem                                                                                                  *
	//	                                                                                                               *
	//******************************************************************************************************************
	//
	static void CB2Bytes(int valor, int EndMed) {

		int EndIni = (EndMed * 2) + 160; 
		int ByteHigh = valor / 256;
		int ByteLow = valor - (256 * ByteHigh);
		
		VG.receiveData1[EndIni] = (byte)ByteLow;
		VG.receiveData1[EndIni + 1] = (byte)ByteHigh;

	}


	//*********************************************************************************************************************
	// Nome da Rotina: InferenciaFuzzyInv1()                                                                              *
	//                                                                                                                    *
	// Funcao: calcula as variáveis de saída dos procedimentos de Inferência Fuzzy do Inversor 1                          *
	//*********************************************************************************************************************
	//
	static void InferenciaFuzzyInv1() {
		double Ve = VG.VBat/100;
		double We = VG.WEIv1/100;
		double Vs = VG.VSIv1/100;
		double Ws = VG.WSInv1/100;
		double Ei = VG.EfIv1;
		double Td = VG.TDInv1/100;
		double Tt = VG.TTInv1/100;

		// Entrar com o valor das entradas
		VG.fis.setVariable("Ve", Ve);
		VG.fis.setVariable("Vs", Vs);
		VG.fis.setVariable("We", We);
		VG.fis.setVariable("Ws", Ws);
		VG.fis.setVariable("Ei", Ei);
		VG.fis.setVariable("Td", Td);
		VG.fis.setVariable("Tt", Tt);
	   
		// Executar o arquivo FCL
		VG.fis.evaluate();
		String StrEstresse = "" + VG.fis.getVariable("EstresseInversor");
		String StrSaude = "" + VG.fis.getVariable("SaudeInversor");
		String Ponto = ".";
	    
		int Indice = StrEstresse.indexOf("value") + 7;
		VG.EstrIv1 = Util.CharToByte(StrEstresse.charAt(Indice));
		Indice = Indice + 1;
		if (StrEstresse.charAt(Indice) == Ponto.charAt(0)) {
			Indice = Indice + 1;
		}
		else {
			VG.EstrIv1 = 10*VG.EstrIv1 + Util.CharToByte(StrEstresse.charAt(Indice));
			Indice = Indice + 1;
		}
	    
		Indice = StrSaude.indexOf("value") + 7;
		VG.SDIv1 = Util.CharToByte(StrSaude.charAt(Indice));
		Indice = Indice + 1;
		if (StrSaude.charAt(Indice) == Ponto.charAt(0)) {
			Indice = Indice + 1;
		}
		else {
			VG.SDIv1 = 10*VG.SDIv1 + Util.CharToByte(StrEstresse.charAt(Indice));
			Indice = Indice + 1;
		}
		
	} // Fim da Rotina InferenciaFuzzyInv1
	
		
	//*****************************************************************************************************************
	// Nome da Rotina: InferenciaFuzzyInv2()                                                                          *
	//                                                                                                                *
	// Funcao: calcula as variáveis de saída dos procedimentos de Inferência Fuzzy do Inversor 2                      *
	// Entrada: nao tem                                                                                               *
	// Saida: nao tem                                                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static void InferenciaFuzzyInv2() {
		double Ve = VG.VBat/100;
		double We = VG.WEIv2/100;
		double Vs = VG.VSIv2/100;
		double Ws = VG.WSInv2/100;
		double Ei = VG.EfIv2;
		double Td = VG.TDInv2/100;
		double Tt = VG.TTInv2/100;

		// Entrar com o valor das entradas
		VG.fis.setVariable("Ve", Ve);
		VG.fis.setVariable("Vs", Vs);
		VG.fis.setVariable("We", We);
		VG.fis.setVariable("Ws", Ws);
		VG.fis.setVariable("Ei", Ei);
		VG.fis.setVariable("Td", Td);
		VG.fis.setVariable("Tt", Tt);
    
		// Executar o arquivo FCL
		VG.fis.evaluate();
		String StrSaude = "" + VG.fis.getVariable("SaudeInversor");
		String Ponto = ".";
    
		//System.out.println(StrSaude);
		int Indice = StrSaude.indexOf("value") + 7;
		int SaudeInv2 = Util.CharToByte(StrSaude.charAt(Indice));
		Indice = Indice + 1;
		if (StrSaude.charAt(Indice) == Ponto.charAt(0)) {
			VG.SDIv2 = SaudeInv2;
		}
		else {
			VG.SDIv2 = 10*SaudeInv2 + Util.CharToByte(StrSaude.charAt(Indice));
		}
	} // Fim da Rotina
	
	
	//*********************************************************************************************************************************
	//                                                                                                                                *
	// Nome da Rotina: EnvRecMsgBinUDP1                                                                                               *
	//                                                                                                                                *
	// Funcao: envia uma mensagem de requisição e recebe a mensagem de resposta do Controlador de Água Quente em Protocolo Binário    *
	//                                                                                                                                *
	//*********************************************************************************************************************************
	//
	static int EnvRecMsgBinUDP1(String EndIP, int Porta, boolean Imp) {
		int TamMsgRsp = 84;
		
		try {
			byte[] MsgReq = new byte[16]; 	                // Define o Buffer de Transmissao
			int TamCab = 8;
			MsgReq[0]= 1;
			
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(EndIP);
			clientSocket.setSoTimeout(2000);
			DatagramPacket sendPacket = new DatagramPacket(MsgReq, TamCab, IPAddress, Porta);
			
			clientSocket.send(sendPacket);
			
			if (Imp) {
				System.out.println(Util.LeImpHora() + " - Enviada Requisicao Binaria para o Controlador de Água Quente");
			}
			VG.EstUTRAQ = 0;
			// Espera a Mensagem Binária de Resposta. Se a mensagem de resposta  for recebida, carrega nas variáveis
			try {
				DatagramPacket receivePacket = new DatagramPacket(VG.receiveData2, TamMsgRsp);
				clientSocket.receive(receivePacket);
				
				LeEstMeds1();
								
				if (Imp) {
					System.out.println(Util.LeImpHora() + " - Recebida Mensagem Binaria do Controlador de Água Quente");
				}
				VG.EstUTRAQ = 1;
			}
			catch(java.net.SocketTimeoutException e) { 
				VG.receiveData1[0] = 0x40;
				VG.receiveData1[1] = 1;
				VG.receiveData1[30] = 0;
				if (Imp) {
					System.out.println(Util.LeImpHora() + " - Erro: o Controlador de Água Quente nao Respondeu ");
				}
			}
			clientSocket.close();
		}
		catch (IOException err) {
			if (VG.verbose) {
				System.out.println(Util.LeImpHora() + " - Erro na Rotina EnvRecMsgSrv: " + err);
			}
		}
		return(TamMsgRsp);
	} // Fim da Rotina
	
	
	//*********************************************************************************************************************************
	//                                                                                                                                *
	// Nome da Rotina: LeEstMeds1()                                                                                                   *
	//                                                                                                                                *
	// Funcao: carrega as informações recebidas do Controlador de Água Quente nas variáveis                                           *
	//                                                                                                                                *
	//                                                                                                                                *
	//*********************************************************************************************************************************
	//
	static void LeEstMeds1() {
		
		VG.EstComUTR = VG.receiveData1[27];
        VG.EstCom1 = VG.receiveData1[30];
		
		// Le os Estados Digitais da mensagem recebida
		VG.EstadoBombaAQ = BytetoInt(VG.receiveData2[73]);		// Estado da Bomba de Água Quente
		VG.EstadoAquecedor = BytetoInt(VG.receiveData2[72]);	// Estado do Aquecedor do Boiler
				 				
		// Le as Medidas da mensagem recebida
		VG.TemperaturaBoiler = TwoBytetoInt(VG.receiveData2[48], VG.receiveData2[49]); 	// Temperatura do Boiler
		VG.TemperaturaPlaca = TwoBytetoInt(VG.receiveData2[51], VG.receiveData2[52]); 	// Temperatura da Placa Solar
		VG.TempoBmbLigada = TwoBytetoInt(VG.receiveData2[66], VG.receiveData2[67]); 	// Tempo da Bomba Ligada
		VG.TempoBmbDesligada = TwoBytetoInt(VG.receiveData2[69], VG.receiveData2[70]); 	// Tempo da Bomba Desligada
		
	}
	
	
	//*********************************************************************************************************************************
	//                                                                                                                                *
	// Nome da Rotina: EnvRecMsgBinUDP2                                                                                               *
	//                                                                                                                                *
	// Funcao: envia uma mensagem de requisição e recebe a mensagem de resposta do Concentrador de Utilidades em Protocolo Binário    *
	//                                                                                                                                *
	//*********************************************************************************************************************************
	//
	static int EnvRecMsgBinUDP2(String EndIP, int Porta, boolean Imp) {
		int TamMsgRsp = 84;
		
		try {
			byte[] MsgReq = new byte[16]; 	                // Define o Buffer de Transmissao
			int TamCab = 8;
			MsgReq[0]= 1;
			
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(EndIP);
			clientSocket.setSoTimeout(2000);
			DatagramPacket sendPacket = new DatagramPacket(MsgReq, TamCab, IPAddress, Porta);
			
			clientSocket.send(sendPacket);
			
			if (Imp) {
				System.out.println(Util.LeImpHora() + " - Enviada Requisicao Binaria para o Concentrador de Utilidades");
			}
			VG.EstConcUtil = 0;
			// Espera a Mensagem Binária de Resposta. Se a mensagem de resposta  for recebida, carrega nas variáveis
			try {
				DatagramPacket receivePacket = new DatagramPacket(VG.receiveData2, TamMsgRsp);
				clientSocket.receive(receivePacket);
				
				LeEstMeds2();
								
				if (Imp) {
					System.out.println(Util.LeImpHora() + " - Recebida Mensagem Binaria do Concentrador de Utilidades");
				}
				VG.EstConcUtil = 1;
			}
			catch(java.net.SocketTimeoutException e) { 
				VG.receiveData2[0] = 0x40;
				VG.receiveData2[1] = 1;
				VG.receiveData2[30] = 0;
				if (Imp) {
					System.out.println(Util.LeImpHora() + " - Erro: o Concentrador de Utilidades nao Respondeu ");
				}
			}
			clientSocket.close();
		}
		catch (IOException err) {
			if (VG.verbose) {
				System.out.println(Util.LeImpHora() + " - Erro na Rotina EnvRecMsgSrv: " + err);
			}
		}
		return(TamMsgRsp);
	} // Fim da Rotina
	
	
	//*********************************************************************************************************************************
	//                                                                                                                                *
	// Nome da Rotina: LeEstMeds2()                                                                                                   *
	//                                                                                                                                *
	// Funcao: carrega as informações recebidas do Controlador de Água Quente nas variáveis                                           *
	//                                                                                                                                *
	//                                                                                                                                *
	//*********************************************************************************************************************************
	//
	static void LeEstMeds2() {
		
		System.out.println(Util.LeImpHora() + " - Comunicação com o Concentrador de Utilidades");
		
		//VG.EstComUTR = VG.receiveData1[27];
        //VG.EstCom1 = VG.receiveData1[30];
		
		// Le os Estados Digitais da mensagem recebida
		//VG.EstadoBombaAQ = BytetoInt(VG.receiveData2[73]);		// Estado da Bomba de Água Quente
		//VG.EstadoAquecedor = BytetoInt(VG.receiveData2[72]);	// Estado do Aquecedor do Boiler
				 				
		// Le as Medidas da mensagem recebida
		//VG.TemperaturaBoiler = TwoBytetoInt(VG.receiveData2[48], VG.receiveData2[49]); 	// Temperatura do Boiler
		//VG.TemperaturaPlaca = TwoBytetoInt(VG.receiveData2[51], VG.receiveData2[52]); 	// Temperatura da Placa Solar
		//VG.TempoBmbLigada = TwoBytetoInt(VG.receiveData2[66], VG.receiveData2[67]); 	// Tempo da Bomba Ligada
		//VG.TempoBmbDesligada = TwoBytetoInt(VG.receiveData2[69], VG.receiveData2[70]); 	// Tempo da Bomba Desligada
		
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: BytetoInt                                                                                      *
	//                                                                                                                *
	// Funcao: converte um valor byte para inteiro (conversao sem sinal)                                              *
	// Entrada: valor byte sem sinal de 0 a 255                                                                       *
	// Saida: valor (inteiro) sempre positivo de 0 a 255                                                              *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int BytetoInt(byte valor) {
		if (valor < 0) {
			return(256 + valor);
		}
		else {
			return(valor);
		}
	}
	
	//*****************************************************************************************************************
	// Nome da Rotina: TwoBytetoInt                                                                                   *
	//                                                                                                                *
	// Funcao: converte dois bytes em sequencia de um array para inteiro (conversao sem sinal)                        *
	// Entrada: dois bytes consecutivos (LSB e MSB) sem sinal de 0 a 255                                              *
	// Saida: valor (inteiro) sempre positivo de 0 a 65535                                                            *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int TwoBytetoInt(byte LSByte, byte MSByte) {
		int lsb;
		int msb;
		if (LSByte < 0) { lsb = LSByte + 256; }
		else { lsb = LSByte; }
		if (MSByte < 0) { msb = MSByte + 256; }
		else { msb = MSByte; }
		return (lsb + 256*msb);
	}
	
	
	
}