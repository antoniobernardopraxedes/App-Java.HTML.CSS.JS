//*****************************************************************************************************************
//                                                                                                                *
// Autor: Antonio Bernardo de Vasconcellos Praxedes                                                               *
//                                                                                                                * 
// Data: 10/01/2020                                                                                               *
//                                                                                                                *
// Nome da Classe: VG                                                                                             *
//                                                                                                                *
// Funcao: Contém as Variáveis Globais do Programa Servidor em Nuvem                                              *
//                                                                                                                *
//*****************************************************************************************************************
//
public class VG {
	
	static String Msg = "";
	
	// Variáveis do Programa
	static boolean flagFim = false;
	static int Comando = 1;
	static String ComRecHTTP;
	static boolean verbose;
	static boolean PrtMsg;
	static int numIdent = -2;
	static boolean flagEventotextArea = false;
	static int length;
	static String MsgErro;
	
	static byte[] sendData1 = new byte[32];
	static byte[] receiveData1 = new byte[512];
	
	static int[] Med = new int[128];
	static byte[] ED = new byte[32];
	static byte[] SD = new byte[32];

	// Estados Gerais
	static byte Est24Vcc;
	static byte EstRede;
	static byte MdOp;
	static byte MdCom;
	static byte MdCtrl1;
	static byte MdCtrl;
	static byte Carga1;
	static byte Carga2;
	static byte Carga3;
	static byte Carga4;
	static byte HabCom;
	static byte EstadoInversor1;
	static byte EstadoInversor2;
	static byte EstadoCarga3;
	static byte CT2Inv;
	static byte CT1Inv;
	static byte CT3Inv;
	static byte UTR1Com;
	static byte UTR2Com;
	static byte CDBat;
	static byte FonteCC1Ligada;
	static byte FonteCC2Ligada;
	
	// Medidas Gerais
	static int Icarga3;       // Corrente Carga 3 (Geladeira)
	static int VRede;         // Tensão da Rede
	static int VBat;          // Tensão do Banco de Baterias
	static int VMBat;         // Tensão Média Estendida do Banco de Baterias
	static int ICircCC;       // Corrente Total dos Circuitos CC
	static int WCircCC;       // Potência Total dos Circuitos CC
	static int ITotCg;        // Corrente Total Consumida pelas Cargas
	static int WTotCg;        // Potência Total Consumida pelas Cargas
	static int IFonteCC;      // Corrente de Saída da Fonte CC
	static int WFonteCC;      // Potência de Saída da Fonte CC
	static int IBat;          // Corrente de Carga / Descarga do Banco de Baterias
	static int WBat;          // Potência de Carga / Descarga do Banco de Baterias
	static int TBat;          // Temperatura do Banco de Baterias
	static int SDBat;         // Valor de Saude das Baterias
	static int IFontesCC12;   // Corrente de Saída das Fontes CC1 e CC2

	// Estados Água
	static byte CircBoia;
	static byte BoiaCxAzul;
	static byte CircBomba;
	static byte AlRedeBomba;
	static byte BombaLigada;
	static byte CxAzNvBx;
	static byte EdCxAzCheia;
	static byte EstadoCxAz;

	// Medidas Água
	static int TmpBmbLig;
	static int TmpCxAzNvBx;

	// Estados do Inversor 1
	static byte Iv1Lig;
	static byte FalhaIv1;
	static byte SubTensaoInv1;
	static byte SobreTensaoInv1;
	static byte SobreTempDrInv1;
	static byte SobreTempTrInv1;
	static byte DjAbIv1;
	static byte DJEINV1;

	// Estados do Inversor 2
	static byte Iv2Lig;
	static byte FalhaIv2;
	static byte SubTensaoInv2;
	static byte SobreTensaoInv2;
	static byte SobreTempDrInv2;
	static byte SobreTempTrInv2;
	static byte DjAbIv2;
	static byte EstFonteCC;

	// Medidas da UTR2 - Comunicação com os Controladores de Carga
	static int VP12;              // Medida 00: 0x3100 - PV array voltage 1
	static int IS12;              // Medida 01: 0x3101 - PV array current 1
	static int WS12;              // Medida 02: 0x3102 - PV array power 1
	static int VBat1;             // Medida 03: 0x3104 - Battery voltage 1
	static int ISCC1;             // Medida 04: 0x3105 - Battery charging current 1
	static int WSCC1;             // Medida 05: 0x3106 - Battery charging power 1
	static int VP34;              // Medida 08: 0x3100 - PV array voltage 2
	static int IS34;              // Medida 09: 0x3101 - PV array current 2
	static int WS34;              // Medida 10: 0x3102 - PV array power 2
	static int VBat2;             // Medida 11: 0x3104 - Battery voltage 2
	static int ISCC2;             // Medida 12: 0x3105 - Battery charging current 2
	static int WSCC2;             // Medida 13: 0x3106 - Battery charging power 2 (Med[45])
	static int SDCC1;             // Valor de Saude do Controlador de Carga 1
	static int SDCC2;             // Valor de Saude do Controlador de Carga 2
	
	// Medidas da Geração
	static int ITotGer;           // Corrente Total Gerada
	static int WTotGer;           // Potência Total Gerada

	// Medidas do Inversor 2
	static int IEIv2;             // Corrente de Entrada do Inversor 2
	static int WEIv2;             // Potência de Entrada do Inversor 2
	static int VSIv2;             // Tensão de Saída do Inversor 2
	static int ISInv2;            // Corrente de Saída do Inversor 2
	static int WSInv2;            // Potência de Saída do Inversor 2
	static int TDInv2;            // Temperatura do Driver do Inversor 2
	static int TTInv2;            // Temperatura do Transformador do Inversor 2
	static int EfIv2;             // Eficiência do Inversor 2
	static int SDIv2;             // Saúde do Inversor 2
	static int EstrIv2;           // Estresse do Inversor 2

	// Medidas do Inversor 1
	static int IEIv1;             // Corrente de Entrada do Inversor 1
	static int WEIv1;             // Potência de Entrada do Inversor 1
	static int VSIv1;             // Tensão de Saída do Inversor 1
	static int ISInv1;            // Corrente de Saída do Inversor 1
	static int WSInv1;            // Potência de Saída do Inversor 1
	static int TDInv1;            // Temperatura do Driver do Inversor 1
	static int TTInv1;            // Temperatura do Transformador do Inversor 1
	static int EfIv1;             // Eficiência do Inversor 1
	static int SDIv1;             // Saúde do Inversor 1
	static int EstrIv1;           // Estresse do Inversor 1

	static int Contador;
	static int NumReg;
	static int ContadorCiclos;
	
	static String SrcRefresh = "";
	
	static byte Hora;
	static byte Minuto;
	static byte Segundo;
	static byte Dia;
	static byte Mes;
	static byte Ano;
	static byte EstComUTR;
	static byte EstComCC1;
	static byte EstComCC2;
		
	static String EndIP1;
	static byte EstCom1;
	static String StrEstCom1;
	static String StrEstComUTR;
	static String StrEstComCC1;
	static String StrEstComCC2;
	static String StrMdOp;
	static String StrMdCom;
	static String StrMdCtrl1;
	static String StrMdCtrl;
	static String StrCT2Inv;
	static String StrCT1Inv;
	static String StrCT3Inv;
	static String StrEstCxAzul;
	static String StrNivCxAzul;
	static String StrEstAlimBoia;
	static String StrAlRedeBomba;
	static String StrIv1Lig;
	static String StrEstBomba;
	static String StrEstFonteCC1;
	static String StrEstFonteCC2;
	static String StrEstIv2;
	static String StrEstVSIv2;
	static String StrEstTDIv2;
	static String StrEstTTIv2;
	static String StrEstIv1;
	static String StrEstVSIv1;
	static String CorTDIv2;
	static String CorTTIv2;
	static String CorTDIv1;
	static String CorTTIv1;
	static String StrEstRede;
	static String StrEstValCg3;
	static String StrEstValVBat;
	static String StrEstIBat;
	static String CorTBat;
	static String StrSaudeBat;
	static String StrValVP12;
	static String StrValVP34;
	
	//*****************************************************************************************************************
    //                                                                                                                *
	// Nome da Rotina: IniciaVarGlobais()                                                                             *
	//	                                                                                                              *
	// Funcao: Inicia as Variáveis Globais                                                                            *
    //                                                                                                                *
	// Entrada: array String com as Tags e os valores das variáveis                                                   *
    //                                                                                                                *
	// Saida: String com a mensagem XML                                                                               *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	private static void IniciaVarGlobais() {
		
		// Inicia as Variaveis Globais
		VG.ComRecHTTP = "     ";
		VG.flagFim = false;
		VG.Comando = 1;
		VG.ComRecHTTP = "";
		//VG.numIdent = -2;
		VG.Icarga3 = 0;                  // Corrente Carga 3 (Geladeira)
		VG.VRede = 0;                    // Tensão da Rede
		VG.VBat = 0;                     // Tensão do Banco de Baterias
		VG.VMBat = 0;                    // Tensão Média Estendida do Banco de Baterias
		VG.ICircCC = 0;                  // Corrente Total dos Circuitos CC
		VG.WCircCC = 0;                  // Potência Total dos Circuitos CC
		VG.ITotCg = 0;                   // Corrente Total Consumida pelas Cargas
		VG.WTotCg = 0;                   // Potência Total Consumida pelas Cargas
		VG.IFonteCC = 0;                 // Corrente de Saída da Fonte CC
		VG.WFonteCC = 0;                 // Potência de Saída da Fonte CC
		VG.IBat = 0;                     // Corrente de Carga / Descarga do Banco de Baterias
		VG.WBat = 0;                     // Potência de Carga / Descarga do Banco de Baterias
		VG.TBat = 0;                     // Temperatura do Banco de Baterias
		VG.TmpBmbLig = 0;                // Tempo da Bomba Ligada
		VG.ITotGer = 0;                  // Corrente Total Gerada
		VG.WTotGer = 0;                  // Potência Total Gerada
		VG.IEIv2 = 0;                    // Corrente de Entrada do Inversor 2
		VG.WEIv2 = 0;                    // Potência de Entrada do Inversor 2
		VG.VSIv2 = 0;                    // Tensão de Saída do Inversor 2
		VG.ISInv2 = 0;                   // Corrente de Saída do Inversor 2
		VG.WSInv2 = 0;                   // Potência de Saída do Inversor 2
		VG.TDInv2 = 0;                   // Temperatura do Driver do Inversor 2
		VG.TTInv2 = 0;                   // Temperatura do Transformador do Inversor 2
		VG.IEIv1 = 0;                    // Corrente de Entrada do Inversor 1
		VG.WEIv1 = 0;                    // Potência de Entrada do Inversor 1
		VG.VSIv1 = 0;                    // Tensão de Saída do Inversor 1
		VG.ISInv1 = 0;                   // Corrente de Saída do Inversor 1
		VG.WSInv1 = 0;                   // Potência de Saída do Inversor 1
		VG.TDInv1 = 0;                   // Temperatura do Driver do Inversor 1
		VG.TTInv1 = 0;                   // Temperatura do Transformador do Inversor 1
		VG.FonteCC2Ligada = 0;
		VG.ContadorCiclos = 0;
		
		VG.Hora = 0;
		VG.Minuto = 0;
		VG.Segundo = 0;
		VG.Dia = 1;
		VG.Mes = 1;
		VG.Ano = 19;
		VG.EstComUTR = 0;
		VG.EstComCC1 = 0;
		VG.EstComCC2 = 0;
		
	}

}