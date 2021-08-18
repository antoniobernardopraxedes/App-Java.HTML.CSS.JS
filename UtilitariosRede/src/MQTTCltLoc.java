import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;

//****************************************************************************************************************************************
//                                                                                                                                       *
// Nome da Classe: MQTTCltLoc01                                                                                                          *
//                                                                                                                                       *
// Funcao: rotinas de cliente MQTT para comunicação com o Broker (Servidor MQTT) na NUvem em Protocolo MQTT                              *
//                                                                                                                                       *
//****************************************************************************************************************************************
//
public class MQTTCltLoc {
	
	static String Caminho = "";
	static String NomeArquivo = "";
	static String IPHost = "";
	static int ContMsgCoAP = 0;
	static String IPConcArd = "192.168.0.150";
	static int PortaUDP = 5683;
		
	//*************************************************************************************************************************************
	//                                                                                                                                    *
    // Rotina Principal (main) do Cliente MQTT Local                                                                                      *
	//                                                                                                                                    *
	//                                                                                                                                    *
	// Funcao: efetua a comunicação periódica com os Concentradores na rede local ethernet em Protocolo CoAP e realiza a                  *
	//         comunicação com o Broker MQTT na nuvem (Servidor) atualizando a base de dados e recebendo comandos.                        *
	//                                                                                                                                    *
	//*************************************************************************************************************************************
	//
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		byte DH [] = new byte[6];
		DH = Util.LeDataHora();
		byte Seg = DH[2];
		byte SegAnt = Seg;
		int EsperaConexao = 10;
		VG.IniciaVarGlobais();
		VG.verbose = true;
		VG.PrtMsg = false;
		
		// Endereço Servidor UOL = "200.98.140.180" / Porta = 8080;
		
		// EMQ deployment-wbe84b09 / Address: wbe84b09.en.emqx.cloud / Ports: 12932(mqtt), 12167(mqtts), 8083(ws), 8084(wss)
		
		// Public MQTT broker: Connect Address: broker.emqx.io / TCP Port: 1883 / Websocket Port: 8083
		// topic = "/esp8266/emqx" / mqtt_username = "emqx" / mqtt_password = "public"
		
		int Cont1 = EsperaConexao;
		
		while (true) {
		DH = Util.LeDataHora();
		Seg = DH[2];
		if (Seg != SegAnt) {
			Cont1 = Cont1 + 1;
			SegAnt = Seg;
		}
		if (Cont1 >= EsperaConexao) {
			Cont1 = 0;
		try {
			
			BufferedOutputStream EnvByte = null;
			InputStream RecByte = null;
			String EndSrv = "wbe84b09.en.emqx.cloud";
			int Porta = 12932;
		
		Caminho = IdentificaComputador();
		Util.LeArquivo(Caminho + "CapEnergia.fcl");
		
		// The Client Identifier (ClientId) identifies the Client to the Server. Each Client connecting to the Server has a unique ClientId.
		// The ClientId MUST be a UTF-8 encoded string as defined in Section 1.5.3 [MQTT-3.1.3-4].
		// The Server MUST allow ClientIds which are between 1 and 23 UTF-8 encoded bytes in length, and that contain only the characters:
		// "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" [MQTT-3.1.3-5].
		String Client_Id = "TesteJavaMQTT01";
		
		// The User Name MUST be a UTF-8 encoded string (Section 1.5.3). It can be used by the Server for authentication and authorization.
		//String UserName = "emqx";
		String UserName = "Teste1";
		boolean UserNameFlag = true;
		
		// The Password field contains 0 to 65535 bytes of binary data
		//byte[] Password = {'p','u','b','l','i','c'};
		String Password = "141198";
		boolean PasswordFlag = true;
		
		// if the Will Flag is set to 1 this indicates that, if the Connect request is accepted, a Will Message MUST be stored on the Server
		// and associated with the Network Connection. The Will Message MUST be published when the Network Connection is subsequently closed
		// unless the Will Message has been deleted by the Server on receipt of a DISCONNECT Packet [MQTT-3.1.2-8].
		boolean WillFlag = false;
		
		// The Will Topic MUST be a UTF-8 encoded string as defined in Section 1.5.3 [MQTT-3.1.3-10].
		String WillTopic = "";
				
		// The Will Message defines the Application Message that is to be published to the Will Topic as described in Section 3.1.2.5.
		// This field consists of a two byte length followed by the payload for the Will Message expressed as a sequence of zero or more bytes.
		// The length gives the number of bytes in the data that follows and does not include the 2 bytes taken up by the length itself.
		byte[] WillMsg = {};
		
		// Will QoS: these two bits specify the QoS level to be used when publishing the Will Message.
		// If the Will Flag is false, then the Will QoS MUST be set to 0 (0x00) [MQTT-3.1.2-13]
		// If the Will Flag is true, the value of Will QoS can be 0, 1, or 2 [MQTT-3.1.2-14].
		int WillQoS = 0;
		
		// Will Retain: This bit specifies if the Will Message is to be Retained when it is published.
		// If the Will Flag is set to 0, then the Will Retain Flag MUST be set to 0 [MQTT-3.1.2-15].
		boolean WillRetainFlag = false;
		
		// Clean Session: this bit specifies the handling of the Session state. The Client and Server can store Session state
		// to enable reliable messaging to continue across a sequence of Network Connections.
		// This bit is used to control the lifetime of the Session state.
		
		// If CleanSession is false, the Server MUST resume communications with the Client based on state from the current
		// Session (as identified by the Client identifier). If there is no Session associated with the Client identifier
		// the Server MUST create a new Session. The Client and Server MUST store the Session after the Client and Server
		// are disconnected [MQTT-3.1.2-4]. After the disconnection of a Session that had CleanSession = false, the Server
		// MUST store further QoS 1 and QoS 2 messages that match any subscriptions that the client had at the time of disconnection
		// as part of the Session state [MQTT-3.1.2-5]. It MAY also store QoS 0 messages that meet the same criteria.
		
		// If CleanSession = true, the Client and Server MUST discard any previous Session and start a new one.
		// This Session lasts as long as the Network Connection. State data associated with this Session MUST NOT be reused
		// in any subsequent Session [MQTT-3.1.2-6].
		boolean CleanSessionFlag = true;
		
		// Bytes com o Control Packet Type e Flags de cada tipo de Mensagem de Requisição
		byte SUBSCRIBE   = (byte)0x82; // Requisição de Inscrição
		byte SUBACK      = (byte)0x90; // Reconhecimento de Inscrição
		byte UNSUBSCRIBE = (byte)0Xa2; // Requisição de Retirada de Inscrição
		byte UNSUBACK    = (byte)0xb0; // Reconhecimento de Retirada de Inscrição
		byte DISCONNECT  = (byte)0xe0; // Requisição de Desconexão
		
		if (VG.verbose) { System.out.println(Util.LeImpHora() + " - Abrir Socket no Servidor MQTT no IP/URL: " + EndSrv); }
		Socket socket = new Socket(EndSrv, Porta);  // Cria o socket de conexão no Servidor MQTT na Nuvem (Broker)
		EnvByte = new BufferedOutputStream(socket.getOutputStream());
		RecByte = new BufferedInputStream(socket.getInputStream());
		socket.setSoTimeout(10000);
									
		if (socket.isConnected()) {
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - Socket Aberto com o Servidor em: " + socket.toString()); }
			int CFB = CalculaCFB(UserNameFlag, PasswordFlag, WillRetainFlag, WillQoS, WillFlag, CleanSessionFlag);
			int CodErr = EnvMsgConnectMQTT(EnvByte, RecByte, Client_Id, WillTopic, WillMsg, UserName, Password, CFB);
						
			// Se o Codigo de Erro = 0, então, a solicitação de conexão foi aceita pelo Servidor MQTT
			if (CodErr == 0) {
						
				// This field indicates the level of assurance for delivery of an Application Message.
				// The QoS levels are: 0 = At most once delivery / 1 = At least once delivery / 2 = Exactly once delivery
				int QoS = 1;
					
				// The Packet Identifier field is only present in PUBLISH Packets where the QoS level is 1 or 2.
				int PacketId = 10;
							
				// If the DUP flag = false, it indicates that this is the first occasion that the Client or Server has attempted
				// to send this MQTT PUBLISH Packet. If the DUP flag = true, it indicates that this might be re-delivery
				// of an earlier attempt to send the Packet.
				boolean DUP = false;
							
				// If the RETAIN flag = 1, in a PUBLISH Packet sent by a Client, the Server MUST store the Application Message and its QoS,
				// so that it can be delivered to future subscribers whose subscriptions match its topic name [MQTT-3.3.1-5].
				// When a new subscription is established, the last retained message, if any, on each matching topic name
				// MUST be sent to the subscriber [MQTT-3.3.1-6].
				// If Server receives QoS 0 message with RETAIN flag = 1, it MUST discard any message previously retained for that topic.
				// It SHOULD store the new QoS 0 message as the new retained message for that topic, but MAY choose to discard it at any time
				// - if this happens there will be no retained message for that topic [MQTT-3.3.1-7].
				boolean RETAIN = false;
							
				// The Topic Name identifies the information channel to which payload data is published.
				String TopicName = "Concentrador";
				
				byte Segundo = DH[2];
				byte SegundoAnterior = Segundo;
				int TmpEntreMsgPing = 5;         // Tempo entre as mensagens Ping (em segundos)
				int cont = TmpEntreMsgPing - 1;
				int ContMsg = 0;                 // Contador de Mensagens
				int NumMsg = 5; 
				boolean fim = false;
				int Err1 = 0;
				int Err2 = 0;
				
				// Loop de envio das mensagens de atualização dos valores das variáveis para o Servidor MQTT
				while (!fim) {
					DH = Util.LeDataHora();
					Segundo = DH[2];
					if (Segundo != SegundoAnterior) {
						cont = cont + 1;
						SegundoAnterior = Segundo;
					}
					if (cont >= TmpEntreMsgPing) {
						cont = 0;
						ContMsg = ContMsg + 1;
						if (ContMsg >= NumMsg) {
							ContMsg = 0;
							int TamMsgBin = SupConcArd.EnvRecMsgCoAPUDP(IPConcArd, PortaUDP, "estados", VG.verbose);
							Err1 = EnvMsgPublishMQTT(EnvByte, RecByte, PacketId, DUP, QoS, RETAIN, TopicName, VG.receiveData1, TamMsgBin);
							PacketId = PacketId + 1;
							if (PacketId > 127) { PacketId = 10; }
						}
						Err2 = EnvMsgPingMQTT(EnvByte, RecByte);
					}
					if ((Err1 == 6) || (Err2 == 6)) { fim = true; }
				} // while (!fim)
			} // if (CodErr == 0)
		} // if (socket.isConnected())
					
		socket.close();
		if (VG.verbose) { System.out.println("\n" + Util.LeImpHora() + " - Fechado Socket "); }
		}
		catch(IOException err) {
			if (VG.verbose) { System.out.println(Util.LeImpHora() + " - Erro: " + err); }
		}
		} // if
		} // while (true)
			
	} // public static void main(String[] args) throws IOException
	
	
	//****************************************************************************************************************************************
	// Nome da Rotina: EnvMsgConnectMQTT                                                                                                     *
	//                                                                                                                                       *
	// Entrada: Socket, Client ID, Will Topic, Will Message, User Name, Password, Connect Flags Byte                                         *
	//                                                                                                                                       *
	// Saída: Código de Erro (0 = a conexão com o servidor MQTT foi estabelecida e reconhecida)                                              *
	//                                                                                                                                       *
	//****************************************************************************************************************************************
	
	static int EnvMsgConnectMQTT(BufferedOutputStream EnviaByte,InputStream RecebeByte,String CltId,String WTpc,byte[] WMsg,String UsName,String Pswd,int CFB) throws IOException {
		int CodigoErro = 0;
		
		byte CONNECT = (byte)0x10;      // Requisição de Conexão
		byte CONNACK     = (byte)0x20;  // Reconhecimento de Requisição de Conexão
		int WillFlagMask = 0x04;
		int PasswordMask = 0x40;
		int UserNameMask = 0x80;
		int TamMsgTx = 0;
		int TamMsgRx = 0;
		byte[] TxBuf = new byte[512];
		byte[] RxBuf = new byte[512];
		
		// Se o Cliente se conectou ao Servidor MQTT, monta e envia a mensagem de Requisição (CONNECT)
		TxBuf[0] = CONNECT;
		// O byte TxBuf[1] (Remaining Length) é carregado no final com o valor restante do número de bytes da mensagem 
	
		// The variable header for the CONNECT Packet consists of four fields in the following order:
		// Protocol Name, Protocol Level, Connect Flags and Keep Alive.
		TxBuf[2] = 0;   // Protocol Name Length MSB (0) - Variable Header Byte 1
		TxBuf[3] = 4;   // Protocol Name Length LSB (4) - Variable Header Byte 2
		TxBuf[4] = 'M'; // Protocol Name (M)            - Variable Header Byte 3
		TxBuf[5] = 'Q'; // Protocol Name (Q)            - Variable Header Byte 4
		TxBuf[6] = 'T'; // Protocol Name (T)            - Variable Header Byte 5
		TxBuf[7] = 'T'; // Protocol Name (T)            - Variable Header Byte 6
		TxBuf[8] = 4;   // Protocol Level Byte (4)      - Variable Header Byte 7
		TxBuf[9] = (byte)CFB;    // Carrega o Connect Flags Byte   - Variable Header Byte 8
		TxBuf[10] = (byte)0x00;  // Carrega o Keep Alive MSB (0)   - Variable Header Byte 9
		TxBuf[11] = (byte)0x0a;  // Carrega o Keep Alive LSB (10)  - Variable Header Byte 10
	
		// The CONNECT Packet Payload contains one or more fields, whose presence is determined by the Connect Flags Byte
		// These fields, if present, MUST appear in the order: Client Identifier, Will Topic, Will Message, User Name, Password
		// The Client Identifier (ClientId) MUST be present and MUST be the first field in the CONNECT packet payload
		int TamClient_Id = CltId.length();  // TamClient_Id = numero de bytes do ClientId
		TxBuf[12] = 0;                      // Carrega o MSB do numero de bytes do ClientId
		TxBuf[13] = (byte)TamClient_Id;		// Carrega o LSB do numero de bytes do ClientId
		int Indice = 14;
		if (TamClient_Id > 0) {
			for (int k = Indice; k < (TamClient_Id + Indice); k = k + 1) {
				TxBuf[k] = (byte)CltId.charAt(k - Indice);
			}
			Indice = Indice + TamClient_Id;
		}
	
		// If the Will Flag is set to 1, the Will Topic is the next field in the payload.
		if ((CFB & WillFlagMask) != 0) {
			int TamWillTopic = WTpc.length();      // TamWillTopic = numero de bytes do Will Topic
			TxBuf[Indice] = 0;                          // Carrega o MSB do numero de bytes do Will Topic
			Indice = Indice + 1;
			TxBuf[Indice] = (byte)TamWillTopic;		    // Carrega o LSB do numero de bytes do Will Topic
			Indice = Indice + 1;
			if (TamWillTopic > 0) {
				for (int k = Indice; k < (TamWillTopic + Indice); k = k + 1) {
					TxBuf[k] = (byte)WTpc.charAt(k - Indice);
				}
				Indice = Indice + TamWillTopic;
			}
			int TamWillMessage = WMsg.length;    // TamWillMessage = numero de bytes da Will Message
			TxBuf[Indice] = 0;                          // Carrega o MSB do numero de bytes da Will Message
			Indice = Indice + 1;
			TxBuf[Indice] = (byte)TamWillMessage;		// Carrega o LSB do numero de bytes da Will Message
			Indice = Indice + 1;
			if (TamWillMessage > 0) {
				for (int k = Indice; k < (TamWillMessage + Indice); k = k + 1) {
					TxBuf[k] = WMsg[k - Indice];
				}
				Indice = Indice + TamWillMessage;
			}
		}
	
		// If the User Name Flag is set to 1, this is the next field in the payload.
		if ((CFB & UserNameMask) != 0) {
			int TamUserName = UsName.length();        // TamUserName = numero de bytes do User Name
			TxBuf[Indice] = 0;                          // Carrega o MSB do numero de bytes do User Name
			Indice = Indice + 1;
			TxBuf[Indice] = (byte)TamUserName;		    // Carrega o LSB do numero de bytes do User Name
			Indice = Indice + 1;
			if (TamUserName > 0) {
				for (int k = Indice; k < (TamUserName + Indice); k = k + 1) {
					TxBuf[k] = (byte)UsName.charAt(k - Indice);
				}
				Indice = Indice + TamUserName;
			}
		}
	
		// If the Password Flag is set to 1, this is the next field in the payload. The Password field contains 0 to 65535 bytes
		//  of binary data prefixed with a two byte length field which indicates the number of bytes used
		if ((CFB & PasswordMask) != 0) {
			int TamPassword = Pswd.length();      // TamPassword = numero de bytes da Password
			TxBuf[Indice] = 0;                    // Carrega o MSB do numero de bytes da Password
			Indice = Indice + 1;
			TxBuf[Indice] = (byte)TamPassword;    // Carrega o LSB do numero de bytes da Password
			Indice = Indice + 1;
			if (TamPassword > 0) {
				for (int k = Indice; k < (TamPassword + Indice); k = k + 1) {
					TxBuf[k] = (byte)Pswd.charAt(k - Indice);
				}
				Indice = Indice + TamPassword;
			}
		}
		int RemainingLength = Indice - 2;  // Remaining Length = Tamanho total da mensagem menos 2 bytes
		TxBuf[1] = (byte)RemainingLength;  // Carrega no Buffer de Transmissão
		TamMsgTx = Indice;                 // TamMsgTx = número de bytes da mensagem a ser transmitida
	
		if (VG.PrtMsg) {
			System.out.println("");
			ImprimeMensagem(TxBuf, TamMsgTx);
		}
	
		// Transmite os Bytes da Mensagem de Requisição de Conexão MQTT para o Servidor (CONNECT)
		EnviaByte.write(TxBuf, 0, TamMsgTx);
		EnviaByte.flush();
		if (VG.verbose) { System.out.print(Util.LeImpHora() + " - Enviada Mensagem CONNECT com " + TamMsgTx + " Bytes"); }
	
		// Espera a Mensagem de Reconhecimento da Recepção da Mensagem de Solicitação de Conexão (CONNACK)
		// Se a Requisição de Conexão foi aceita, ativa a flag Conexao_OK para posterior envio das mensagens de dados (PUBLISH)
		try {
			TamMsgRx = RecebeByte.read(RxBuf);
			if ((RxBuf[0] == CONNACK) && (RxBuf[1] == 2)) {
				if (VG.verbose) { System.out.print(" => " + Util.LeImpHora() + " Recebida Mensagem CONNACK"); }
				if (VG.verbose) {
					if (RxBuf[3] == 0) {
						CodigoErro = 0;
						System.out.println(" - (00) Connection Accepted");
					}
					else {
						CodigoErro = RxBuf[3];
						
						if (RxBuf[3] == 1) { System.out.println(" - (01) Connection Refused, unacceptable protocol version"); }
						if (RxBuf[3] == 2) { System.out.println(" - (02) Connection Refused, identifier rejected"); }
						if (RxBuf[3] == 3) { System.out.println(" - (03) Connection Refused, Server unavailable"); }
						if (RxBuf[3] == 4) { System.out.println(" - (04) Connection Refused, bad user name or password"); }
						if (RxBuf[3] == 5) { System.out.println(" - (05) Connection Refused, not authorized"); }
					}
				}
			}
		}
		catch(java.net.SocketTimeoutException tmo) {
			CodigoErro = 6;
			if (VG.verbose) { System.out.println(" => " + Util.LeImpHora() + " - Timeout na Recepção da Mensagem CONNACK"); }
		}
		return(CodigoErro);
	}
	
	
	//****************************************************************************************************************************************
	// Nome da Rotina: EnvMsgPublishMQTT                                                                                                     *
	//                                                                                                                                       *
	// Entrada: EnviaByte, RecebeByte, Client ID, Will Topic, Will Message, User Name, Password, Connect Flags Byte                          *
	//                                                                                                                                       *
	// Saída: Código de Erro (0 = a conexão com o servidor MQTT foi estabelecida e reconhecida)                                              *
	//                                                                                                                                       *
	//****************************************************************************************************************************************
	
	static int EnvMsgPublishMQTT(BufferedOutputStream EnviaByte,InputStream RecebeByte,int PacketId, boolean DUP,int QoS,boolean RETAIN,String Topic,byte[] Buffer,int TamMsgBin) throws IOException {
		int CodigoErro = 0;
		byte PUBLISH     = (byte)0x30;  // Requisição de Publicação
		byte PUBACK      = (byte)0x40;  // Reconhecimento de Publicação
		byte PUBREC      = (byte)0x50;  // Publicação Recebida
		byte PUBREL      = (byte)0x62;  // Publicação Liberada
		byte PUBCOMP     = (byte)0x70;  // Publicação Completada
		int TamMsgTx = 0;
		int TamMsgRx = 0;
		byte[] TxBuf = new byte[512];
		byte[] RxBuf = new byte[512];
				
		// Monta a Mensagem MQTT de Atualização dos Valores das Variáveis (PUBLISH)
		int Byte0 = PUBLISH;
		if ((QoS >= 0) && (QoS <= 2)) { Byte0 = Byte0 + QoS * 2; }
		if (DUP) { Byte0 = Byte0 + 8; }
		if (RETAIN) { Byte0 = Byte0 + 1; }
		if (PacketId > 127) { PacketId = 1; }
		
		TxBuf[0] = (byte)Byte0;  // Control Packet Type e Flags
		
		//TxBuf[1] Os dois bytes do Comprimento Restante
		//TxBuf[2]
		
		// Publish Packet Variable Header - Topic Name e Packet Identifier
		int Indice = 3;
		int TamTopic = Topic.length();
		if (TamTopic > 0) {
			TxBuf[Indice] = ByteHigh(TamTopic);                         // Carrega o MSB do numero de bytes do Topic
			Indice = Indice + 1;
			TxBuf[Indice] = ByteLow(TamTopic);                          // Carrega o LSB do numero de bytes do Topic
			Indice = Indice + 1;
			for (int k = Indice; k < (TamTopic + Indice); k = k + 1) {  // Carrega o Topic
				TxBuf[k] = (byte)Topic.charAt(k - Indice);
			}
		}
		Indice = Indice + TamTopic;
		TxBuf[Indice] = ByteHigh(PacketId);                             // Carrega o MSB do Packet Identifier
		Indice = Indice + 1;
		TxBuf[Indice] = ByteLow(PacketId);                              // Carrega o LSB do Packet Identifier
		Indice = Indice + 1;
		
		// The Payload contains the Application Message. The content and format of the data is application specific.
		//String TestePayload = "Teste";
		//int Tam = TestePayload.length();
		if (TamMsgBin > 0) {
			TxBuf[Indice] = ByteHigh(TamMsgBin);
			Indice = Indice + 1;
			TxBuf[Indice] = ByteLow(TamMsgBin);
			Indice = Indice + 1;
			for (int k = Indice; k < (TamMsgBin + Indice); k = k + 1) {
				TxBuf[k] = Buffer[k - Indice];
			}
		}
		Indice = Indice + TamMsgBin;
		TamMsgTx = Indice;
		int RemLength = Indice - 3;
		TxBuf[1] = ByteLow128(RemLength);
		TxBuf[2] = ByteHigh128(RemLength);
		
		if (VG.PrtMsg) { 
			System.out.println("");
			ImprimeMensagem(TxBuf, Indice);
		}
		
		// Transmite os Bytes da Mensagem PUBLISH para o Servidor na Nuvem
		EnviaByte.write(TxBuf, 0, TamMsgTx);
		EnviaByte.flush();
		if (VG.verbose) {
			System.out.print(Util.LeImpHora() + " - Enviada Mensagem PUBLISH com PacketId = " + PacketId + " (" + TamMsgTx + " Bytes) => ");
		}
		
		// Se QoS não é zero, espera a Mensagem de Reconhecimento da Recepção da Mensagem de Atualização (PUBACK)
		if (QoS > 0) {
			try {
				TamMsgRx = RecebeByte.read(RxBuf);
				if ((RxBuf[0] == PUBACK) && (RxBuf[1] == 2)) {
					int PacketIdRec = (256 * RxBuf[2]) + RxBuf[3];
					if (VG.verbose) {
						System.out.println(" => " + Util.LeImpHora() + " - Recebida Mensagem PUBACK com PacketId = " + PacketIdRec);
					}
				}
			}
			catch(java.net.SocketTimeoutException tmo) {
				CodigoErro = 6;
				if (VG.verbose) { System.out.println(" => " + Util.LeImpHora() + " - Timeout na Recepção da mensagem PUBACK"); }
			}
		} // if (QoS > 0)
		
		
		return(CodigoErro);
	}
	
	
	//****************************************************************************************************************************************
	// Nome da Rotina: EnvMsgPingMQTT                                                                                                        *
	//                                                                                                                                       *
	// Entrada: EnviaByte e RecebeByte                                                                                                       *
	//                                                                                                                                       *
	// Saída: Código de Erro (0 = a solicitação de Ping foi respondida)                                                                      *
	//                                                                                                                                       *
	//****************************************************************************************************************************************
		
	static int EnvMsgPingMQTT(BufferedOutputStream EnviaByte,InputStream RecebeByte) throws IOException {
		int CodErr = 1;
		byte PINGREQ     = (byte)0xc0; // Requisição de Ping
		byte PINGRESP    = (byte)0xd0; // Reconhecimento de Ping
		byte[] TxBuf = new byte[2];
		byte[] RxBuf = new byte[16];
		int TamMsgTx = 2;
		int TamMsgRx = 0;
		
		// Monta a mensagem de Requisição PINGREQ
		TxBuf[0] = PINGREQ;
		TxBuf[1] = 0;
		
		// Transmite os Bytes da Mensagem PUBLISH para o Servidor na NUvem
		EnviaByte.write(TxBuf, 0, TamMsgTx);
		EnviaByte.flush();
		if (VG.verbose) {
			System.out.print(Util.LeImpHora() + " - Enviada Mensagem PINGREQ");
		}
				
		// Espera a Mensagem de Reconhecimento da Recepção da Mensagem de Reconhecimento de Ping (PINGRESP)
		try {
			TamMsgRx = RecebeByte.read(RxBuf);
			if ((RxBuf[0] == PINGRESP) && (RxBuf[1] == 0)) {
				if (VG.verbose) {
					System.out.println(" => " + Util.LeImpHora() + " - Recebida Mensagem PINGRESP");
					CodErr = 0;
				}
			}
		}
		catch(java.net.SocketTimeoutException tmo) {
			CodErr = 6;
			if (VG.verbose) { System.out.println(" => " + Util.LeImpHora() + " - Timeout na Recepção da Mensagem PINGRESP"); }
		}
		
		return(CodErr);
	}
	
	
	//****************************************************************************************************************************************
	// Nome da Rotina: CalculaCFB                                                                                                            *
	//                                                                                                                                       *
	// Entrada: User Name Flag, Password Flag, Will Retain Flag, Will QoS (0 a 2), Will Flag, Clean Session Flag                             *
	//                                                                                                                                       *
	// Saída: Código de Erro (0 = a conexão com o servidor MQTT foi estabelecida e reconhecida)                                              *
	//                                                                                                                                       *
	// The Connect Flags byte contains a number of parameters specifying the behavior of the MQTT connection.                                *
	// It also indicates the presence or absence of fields in the payload.                                                                   *
	//------------------------------------------------------------------------------------------------------                                 *
	// Bit |     7     |     6     |     5     |     4     |     3     |     2     |     1     |     0     |                                 *
	//     | User Name | Password  |    Will   |       Will QoS        | Will Flag |   Clean   | Reserved  |                                 *
	//     |           |           |   Retain  |                       |           |   Session |           |                                 *
	//     |     1     |     1     |     0     |     0     |     1     |     1     |     1     |     0     |                                 *
	//****************************************************************************************************************************************
	
	static int CalculaCFB(boolean UserNFlag, boolean PwdFlag, boolean WRFlag, int WQoS, boolean WFlag, boolean CSFlag) {
		int CFB = 8 * WQoS;
		if (CSFlag) {CFB = CFB + 2;}
		if (WFlag) {CFB = CFB + 4;}
		if (WRFlag) {CFB = CFB + 32;}
		if (PwdFlag) {CFB = CFB + 64;}
		if (UserNFlag) {CFB = CFB + 128;}
		return(CFB);
	}
	
	
	//****************************************************************************************************************************************
	// Nome da Rotina: IdentificaComputador                                                                                                  *
	//                                                                                                                                       *
	// Entrada: Não tem                                                                                                                      *
	//                                                                                                                                       *
	// Saída: String com o caminho do diretório de trabalho do computador                                                                    *
	//                                                                                                                                       *
	//****************************************************************************************************************************************
	
	static String IdentificaComputador() throws IOException {
		String caminho = "";
		InetAddress ip = InetAddress.getLocalHost();
		String NomeComputador = "";
		NomeComputador = ip.getHostName();
		if (NomeComputador.equals("raspberrypi")) {
			caminho = "/home/pi/Desktop/Programas/";
			IPHost = "192.168.0.170";
			VG.verbose = false;
			VG.PrtMsg = false;
			System.out.println("\n" + Util.LeImpHora() + " - Atualizador de Servidor na Nuvem MQTT Iniciado no Computador Raspberry PI 3");
		}
		if (NomeComputador.equals("BernardoLinux")) {
			caminho = "/home/antonio/Documentos/Executavel/";
			IPHost = "192.168.0.50";
			System.out.println("\n" + Util.LeImpHora() + " - Atualizador de Servidor na Nuvem MQTT Iniciado no Computador Dell Inspiron");
		}
		return(caminho);
	}
	
	
	//****************************************************************************************************************************************
	// Nome da Rotina: ImprimeMensagem                                                                                                       *
	//                                                                                                                                       *
	// Entrada: Buffer de Dados (Bytes), Tamanho da Mensagem                                                                                 *
	//                                                                                                                                       *
	// Saída: Não Tem                                                                                                                        *
	//                                                                                                                                       *
	//****************************************************************************************************************************************
	
	static void ImprimeMensagem(byte Buffer[], int TamMsg) {
		for (int j = 0; j < TamMsg;j = j + 1) {
			int ByteMsg;
			if (Buffer[j] < 0) {
				ByteMsg = 256 + Buffer[j]; 
			}
			else {
				ByteMsg = Buffer[j];
			}
			int NumByte = j + 1;
			String Msg1;
			if (NumByte < 10) {
				Msg1 = "Byte 0" + NumByte;
			}
			else {
				Msg1 = "Byte " + NumByte;
			}
			String Msg2 = "";
			if (ByteMsg < 100) {
				if (ByteMsg > 9) {
					Msg2 = " - Dec = 0" + ByteMsg;
				}
				else {
					Msg2 = " - Dec = 00" + ByteMsg;
				}
			}
			else {
				Msg2 = " - Dec = " + ByteMsg;
			}
			
			String Msg4 = "";
			if ((ByteMsg > 31) && (ByteMsg < 123)) {
				Msg4 = " - Char = " + (char)(ByteMsg);
			}
			System.out.println(Msg1 + Msg2 + " - Hex = " + ByteDectoHex(ByteMsg) + Msg4);
		}
		System.out.println("");
	}
	
	static byte ByteHigh(int Valor) {
		byte BH = (byte)(Valor/256);
		return(BH);
	}
	
	static byte ByteLow(int Valor) {
		int BH = Valor / 256;
		int BL = Valor - (256 * BH);
		return((byte)BL);
	}
	
	static byte ByteHigh128(int Valor) {
		byte BH = (byte)(Valor/128);
		return(BH);
	}
	
	static byte ByteLow128(int Valor) {
		int BH = Valor / 128;
		int BL = Valor - (128 * BH);
		if (BH > 0) { BL = BL + 128; }
		return((byte)BL);
	}
	
	static String ValtoHexDigit(int Valor) {
		String HexDigit = "";
		switch (Valor) {
			case 0: HexDigit = "0"; break;
			case 1: HexDigit = "1"; break;
			case 2: HexDigit = "2"; break;
			case 3: HexDigit = "3"; break;
			case 4: HexDigit = "4"; break;
			case 5: HexDigit = "5"; break;
			case 6: HexDigit = "6"; break;
			case 7: HexDigit = "7"; break;
			case 8: HexDigit = "8"; break;
			case 9: HexDigit = "9"; break;
			case 10: HexDigit = "A"; break;
			case 11: HexDigit = "B"; break;
			case 12: HexDigit = "C"; break;
			case 13: HexDigit = "D"; break;
			case 14: HexDigit = "E"; break;
			case 15: HexDigit = "F"; break;
		}
		return(HexDigit);
	}
	
	static String ByteDectoHex(int Valor) {
		int BitsSup = Valor / 16;
		int BitsInf = Valor - (16 * BitsSup);
		String HexValue = ValtoHexDigit(BitsSup) + ValtoHexDigit(BitsInf);
		return(HexValue);
	}
		
}