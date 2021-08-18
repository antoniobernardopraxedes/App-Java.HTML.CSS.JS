
public class MontaMsg {

	//**********************************************************************************************************************
	// Nome da Rotina: XML01()                                                                                             *
    //	                                                                                                                   *
	// Data: 23/12/2019                                                                                                    *
	//                                                                                                                     *
	// Funcao: lê e calcula o valor de todas as variáveis de supervisão e retorna uma String contendo a mensagem XML       *
	//         de resposta com todos os valores atualizados.                                                               *
	//                                                                                                                     *
	// Entrada: não tem                                                                                                    *
	//                                                                                                                     *
	// Saida: string com a mensagem XML de resposta                                                                        *
    //	                                                                                                                   *
	//**********************************************************************************************************************
	//
	public static String XML01() {
		
		// Estados de Comunicacao
		VG.StrEstCom1 = "Falha";
		if (VG.EstCom1 == 1) { VG.StrEstCom1 = "Normal"; }
			
		VG.StrEstComUTR = "Falha";
		if (VG.EstComUTR == 1) { VG.StrEstComUTR = "Normal"; }
		
		VG.StrEstComCC1 = "Falha";
		if (VG.EstComCC1 == 1) { VG.StrEstComCC1 = "Normal"; }
		
		VG.StrEstComCC2 = "Falha";
		if (VG.EstComCC2 == 1) { VG.StrEstComCC2 = "Normal"; }
		
		// Estados Gerais
		VG.StrMdOp = "Economia";
		if (VG.MdOp == 1) {	VG.StrMdOp = "Normal";	}

		VG.StrMdCom = "Local";
		if (VG.MdCom == 1) { VG.StrMdCom = "Remoto"; }
		
		VG.StrMdCtrl1 = "Manual";
		if (VG.MdCtrl1 == 1) { VG.StrMdCtrl1 = "Automatico"; }
		
		VG.StrMdCtrl = "Manual";
		if (VG.MdCtrl == 1) { VG.StrMdCtrl = "Automatico"; }

		VG.StrCT2Inv = "Rede";                // Fonte de Energia Carga 1
		if (VG.CT2Inv == 1) {
			VG.StrCT2Inv = "Inversor 2"; 
		}
		else {
			if (VG.Carga1 == 1) {
				VG.StrCT2Inv = "Rede (Hab)";
			}
		}
		
		VG.StrCT1Inv = "Rede";                // Fonte de Energia Carga 2
		if (VG.CT1Inv == 1) { VG.StrCT1Inv = "Inversor 2"; }
		else { if (VG.Carga2 == 1) { VG.StrCT1Inv = "Rede (Hab)"; } }
		
		VG.StrCT3Inv = "Rede";                // Fonte de Energia Carga 3
		if (VG.CT3Inv == 1) { VG.StrCT3Inv = "Inversor 2"; }
		else { if (VG.Carga3 == 1) { VG.StrCT3Inv = "Rede (Hab)"; } }
		
		VG.StrEstCxAzul = "";
		VG.StrNivCxAzul = "";
		switch (VG.EstadoCxAz) {
		
			case 0:  //  EstadoCxAz = 0 => Estado da Caixa Azul = Indefinido 
				VG.StrEstCxAzul = "Indefinido";
				VG.StrNivCxAzul = "Indefinido";
			break;
		
			case 1:  //  EstadoCxAz = 1 => Estado da Caixa Azul = Precisa Encher Nivel Baixo
				VG.StrEstCxAzul = "Precisa Encher";
				VG.StrNivCxAzul = "Baixo";
			break;
	    
			case 2:  //  EstadoCxAz = 2 => Estado da Caixa Azul = Precisa Encher Nivel Normal
				VG.StrEstCxAzul = "Precisa Encher";
				VG.StrNivCxAzul = "Normal";
			break;
	    
			case 3:  //  EstadoCxAz = 3 => Estado da Caixa Azul = Cheia
				VG.StrEstCxAzul = "Cheia";
				VG.StrNivCxAzul = "Normal";
			break;
	    
			case 4:  //  EstadoCxAz = 4 => Estado da Caixa Azul = Falha de Sinalizacao 1
				VG.StrEstCxAzul = "Falha Boia";
				VG.StrNivCxAzul = "";
			break;
	    
			case 5:  // EstadoCxAz = 5 => Estado da Caixa Azul = Falha de Sinalizacao 2
				VG.StrEstCxAzul = "Falha Boia";
				VG.StrNivCxAzul = "";
			break;
		}
		
		VG.StrEstAlimBoia = "";
		if (VG.CircBoia == 1) { VG.StrEstAlimBoia = "Ligado"; }
		else { VG.StrEstAlimBoia = "Desligado"; }
		
		VG.StrAlRedeBomba = "";
		if (VG.EstRede == 1) {
			if (VG.AlRedeBomba == 1) { VG.StrAlRedeBomba = "Ligado"; }
			else { VG.StrAlRedeBomba = "Desligado"; }
		}
		else {
			VG.StrAlRedeBomba = "Falta CA";
		}
	
		VG.StrIv1Lig = "Rede";                // Fonte de energia da bomba
		if (VG.Iv1Lig == 1) {
			VG.StrIv1Lig = "Inversor 1"; 
		}
		else {
			if (VG.Carga4 == 1) {
				VG.StrIv1Lig = "Rede (Hab)";	
			}
		}
		
		VG.StrEstBomba = "Desligada";                   // Estado da alimentação da bomba
		if (VG.CircBomba == 1) { VG.StrEstBomba = "Ligada"; }
		
		VG.StrEstFonteCC1 = "";            // Estado das Fontes CC1 (Inversor 2 e Carregador de Baterias) e CC2 (Cargas 24Vcc)
		VG.StrEstFonteCC2 = "";
		if (VG.EstRede == 1) {                    // Se a tensao da Rede esta OK,
			if (VG.FonteCC1Ligada == 1) {         // e se a fonte CC1 está fornecendo tensão,
				VG.StrEstFonteCC1 = "Ligada";     // Carrega a mensagem de que a fonte CC1 está ligada
			}
			else {                                // Se a fonte CC1 não está fornecendo tensão,
				VG.StrEstFonteCC1 = "Desligada";  // Carrega a mensagem de que a fonte CC1 está desligada
			}
			if (VG.FonteCC2Ligada == 1) {         // e se a fonte CC2 está fornecendo tensão,
				VG.StrEstFonteCC2 = "Ligada";     // Carrega a mensagem de que a fonte CC1 está ligada
			}
			else {                                // Se a fonte CC1 não está fornecendo tensão,
				VG.StrEstFonteCC2 = "Desligada";  // Carrega a mensagem de que a fonte CC1 está desligada
			}
		}
		else {                                    // Se falta CA,
			if (VG.FonteCC1Ligada == 0) {         // e se a saida da fonte está sem tensao,
				VG.StrEstFonteCC1 = "Falta CA";   // Carrega a mensagem de que Falta CA
			}
			else {
				VG.StrEstFonteCC1 = "Falha";      // Carrega a mensagem de Falha
			}
			if (VG.FonteCC2Ligada == 0) {         // e se a saida da fonte está sem tensao,
				VG.StrEstFonteCC2 = "Falta CA";   // Carrega a mensagem de que Falta CA
			}
			else {
				VG.StrEstFonteCC2 = "Falha";      // Carrega a mensagem de Falha
			}
		}
		
		VG.StrEstIv2 = "Desligado";
		VG.StrEstVSIv2 = "      ";
		if (VG.Iv2Lig == 1) { 
			VG.StrEstIv2 = "Ligado";
			if (VG.VSIv2 < 21000) { VG.StrEstVSIv2 = "Baixa"; }
			if ((VG.VSIv2 >= 21000) && (VG.VSIv2 <= 22500)) { VG.StrEstVSIv2 = "Normal"; }
			if (VG.VSIv2 > 22500) { VG.StrEstVSIv2 = "Alta"; }
		}
		else {
			VG.IEIv2 = 0;
			VG.WEIv2 = 0;
			VG.ISInv2 = 0;
			VG.WSInv2 = 0;
		}
		
		VG.StrEstTDIv2 = "          ";
		if (VG.TDInv2 < 4600) { VG.StrEstTDIv2 = "Normal";	}
		if ((VG.TDInv2 >= 4600) && (VG.TDInv2 < 5000)) { VG.StrEstTDIv2 = "Alta"; }
		if (VG.TDInv2 >= 5000) { VG.StrEstTDIv2 = "Muito Alta"; }
		
		VG.StrEstTTIv2 = "          ";
		if (VG.TTInv2 < 4600) { VG.StrEstTTIv2 = "Normal";	}
		if ((VG.TTInv2 >= 4600) && (VG.TTInv2 < 5000)) { VG.StrEstTTIv2 = "Alta"; }
		if (VG.TTInv2 >= 5000) { VG.StrEstTTIv2 = "Muito Alta"; }
		
		VG.StrEstIv1 = "Desligado";
		VG.StrEstVSIv1 = "      ";
		if (VG.Iv1Lig == 1) {
			VG.StrEstIv1 = "Ligado";
			if (VG.VSIv1 < 17500) { VG.StrEstVSIv1 = "Baixa"; }
			if ((VG.VSIv1 >= 17500) && (VG.VSIv1 <= 20000)) { VG.StrEstVSIv1 = "Normal"; }
			if (VG.VSIv1 > 20000) { VG.StrEstVSIv1 = "Alta"; }
		}
		else {
			VG.IEIv1 = 0;
			VG.WEIv1 = 0;
			VG.ISInv1 = 0;
			VG.WSInv1 = 0;
		}
		
		VG.CorTDIv2 = "";
		if (VG.TDInv2 >= 5000) { VG.CorTDIv2 = "style='color:red;'"; }
		VG.CorTTIv2 = "";
		if (VG.TTInv2 >= 5000) { VG.CorTTIv2 = "style='color:red;'"; }
		
		VG.CorTDIv1 = "";
		if (VG.TDInv1 >= 5000) { VG.CorTDIv1 = "style='color:red;'"; }
		String CorTTIv1 = "";
		if (VG.TTInv1 >= 5000) { VG.CorTTIv1 = "style='color:red;'"; }
		
		VG.StrEstRede = "";
		if (VG.EstRede == 1) {
			if (VG.VRede > 19000) { VG.StrEstRede = "Normal"; }
			else { VG.StrEstRede = "(Baixa)"; }
		}
		else { VG.StrEstRede = "Falta CA"; }
		
		VG.StrEstValCg3 = "         ";
		if (VG.Icarga3 < 100) { VG.StrEstValCg3 = "Deslig"; }
		if (VG.Icarga3 > 400) { VG.StrEstValCg3 = "Ligada"; }
		
		VG.StrEstValVBat = "           ";
		if (VG.VBat < 2300) { VG.StrEstValVBat = "Baixa"; }
		if ((VG.VBat >= 2300) && (VG.VBat < 2640)) { VG.StrEstValVBat = "Carga/Desc.";	}		
		if ((VG.VBat >= 2640) && (VG.VBat <= 2760)) { VG.StrEstValVBat = "Flutuação"; }
		if ((VG.VBat > 2760) && (VG.VBat < 2900)) { VG.StrEstValVBat = "Equalização"; }
		if (VG.VBat > 2900) { VG.StrEstValVBat = "Alta"; }
		
		VG.StrEstIBat = "        ";
		if (VG.CDBat == 0) { VG.StrEstIBat = "Descarga"; }
		else { VG.StrEstIBat = "Carga"; }
		
		VG.CorTBat = "";
		if (VG.TBat > 4000) { VG.CorTBat = "style='color:red;'"; }
		
		VG.StrSaudeBat = "Normal";
		if (VG.SDBat < 85) { VG.StrSaudeBat = "Atenção"; }
		
		VG.StrValVP12 = "      ";
		if (VG.VP12 < 3000) { VG.StrValVP12 = "Baixa"; }
		if (VG.VP12 >= 3000) { VG.StrValVP12 = "Normal"; }
		
		VG.StrValVP34 = "      ";
		if (VG.VP34 < 3000) { VG.StrValVP34 = "Baixa"; }
		if (VG.VP34 >= 3000) { VG.StrValVP34 = "Normal"; }
		
		String EstUTRAQ = "Falha";
		if (VG.EstUTRAQ == 1) { EstUTRAQ = "Normal"; }
		
		String EstBombaAQ = "Desligada";
		if (VG.EstadoBombaAQ == 1) { EstBombaAQ = "Ligada"; }
		
		String EstAquecedor = "Desligado";
		if (VG.EstadoAquecedor == 1) { EstAquecedor = "Ligado"; }
		
		String AlmFALIV2 = "Normal";
		String AlmSUVIV2 = "Normal";
		String AlmSOVIV2 = "Normal";
		String AlmSOTDIV2 = "Normal";
		String AlmSOTTIV2 = "Normal";
		String AlmDJAIV2 = "Normal";
		
		if (VG.FalhaIv2 == 1) {	AlmFALIV2 = "Falha"; }
		if (VG.SubTensaoInv2 == 1) { AlmSUVIV2 = "Falha"; }
		if (VG.SobreTensaoInv2 == 1) { AlmSOVIV2 = "Falha"; }
		if (VG.SobreTempDrInv2 == 1) { AlmSOTDIV2 = "Falha"; }
		if (VG.SobreTempTrInv2 == 1) { AlmSOTTIV2 = "Falha"; }
		if (VG.DjAbIv2 == 1) { AlmDJAIV2 = "Falha"; }
		
		String AlmFALIV1 = "Normal";
		String AlmSUVIV1 = "Normal";
		String AlmSOVIV1 = "Normal";
		String AlmSOTDIV1 = "Normal";
		String AlmSOTTIV1 = "Normal";
		String AlmDJAIV1 = "Normal";
		
		if (VG.FalhaIv1 == 1) {	AlmFALIV1 = "Falha"; }
		if (VG.SubTensaoInv1 == 1) { AlmSUVIV1 = "Falha"; }
		if (VG.SobreTensaoInv1 == 1) { AlmSOVIV1 = "Falha"; }
		if (VG.SobreTempDrInv1 == 1) { AlmSOTDIV1 = "Falha"; }
		if (VG.SobreTempTrInv1 == 1) { AlmSOTTIV1 = "Falha"; }
		if (VG.DjAbIv1 == 1) { AlmDJAIV1 = "Falha"; }
		
		// Carrega na StringXML Array os Tags de Níveis 0,1,e 2 e as variáveis de supervisão
		String MsgXMLArray[][][][] = new String[1][10][40][2];
		byte IdNv0 = 0;
		byte IdNv1 = 0;
		MsgXMLArray[IdNv0][IdNv1][0][0] = "LOCAL001";
		MsgXMLArray[IdNv0][IdNv1][0][1] = "05"; 
			
		IdNv1 = 1; // Grupo de 19 Variáveis de Informação GERAL                             
		MsgXMLArray[IdNv0][IdNv1][0][0] = "GERAL";
		MsgXMLArray[IdNv0][IdNv1][0][1] = "21";
			
		MsgXMLArray[IdNv0][IdNv1][1][0] = "COMCNV";
		MsgXMLArray[IdNv0][IdNv1][1][1] = "Normal";
		MsgXMLArray[IdNv0][IdNv1][2][0] = "COMCNC";
		MsgXMLArray[IdNv0][IdNv1][2][1] = VG.StrEstCom1;
		MsgXMLArray[IdNv0][IdNv1][3][0] = "COMUTR";
		MsgXMLArray[IdNv0][IdNv1][3][1] = VG.StrEstComUTR;
		MsgXMLArray[IdNv0][IdNv1][4][0] = "COMCC1";
		MsgXMLArray[IdNv0][IdNv1][4][1] = VG.StrEstComCC1;
		MsgXMLArray[IdNv0][IdNv1][5][0] = "COMCC2";
		MsgXMLArray[IdNv0][IdNv1][5][1] = VG.StrEstComCC1;
		MsgXMLArray[IdNv0][IdNv1][6][0] = "CLK";
		MsgXMLArray[IdNv0][IdNv1][6][1] = Util.ImpHora(VG.Hora, VG.Minuto, VG.Segundo);
		MsgXMLArray[IdNv0][IdNv1][7][0] = "DATA";
		MsgXMLArray[IdNv0][IdNv1][7][1] = Util.ImpData(VG.Dia, VG.Mes, VG.Ano); 
		MsgXMLArray[IdNv0][IdNv1][8][0] = "CMDEX";
		MsgXMLArray[IdNv0][IdNv1][8][1] = VG.ComRecHTTP;
		MsgXMLArray[IdNv0][IdNv1][9][0] = "MDOP";
		MsgXMLArray[IdNv0][IdNv1][9][1] = VG.StrMdOp;
		MsgXMLArray[IdNv0][IdNv1][10][0] = "MDCOM";
		MsgXMLArray[IdNv0][IdNv1][10][1] = VG.StrMdCom;
		MsgXMLArray[IdNv0][IdNv1][11][0] = "MDCT1";
		MsgXMLArray[IdNv0][IdNv1][11][1] = VG.StrMdCtrl1;
		MsgXMLArray[IdNv0][IdNv1][12][0] = "MDCT234";
		MsgXMLArray[IdNv0][IdNv1][12][1] = VG.StrMdCtrl;
		MsgXMLArray[IdNv0][IdNv1][13][0] = "ENCG1";
		MsgXMLArray[IdNv0][IdNv1][13][1] = VG.StrCT2Inv;
		MsgXMLArray[IdNv0][IdNv1][14][0] = "ENCG2";
		MsgXMLArray[IdNv0][IdNv1][14][1] = VG.StrCT1Inv;
		MsgXMLArray[IdNv0][IdNv1][15][0] = "ENCG3";
		MsgXMLArray[IdNv0][IdNv1][15][1] = VG.StrCT3Inv;
		MsgXMLArray[IdNv0][IdNv1][16][0] = "ICG3";
		MsgXMLArray[IdNv0][IdNv1][16][1] = Util.FrmAna3(VG.Icarga3," A");
		MsgXMLArray[IdNv0][IdNv1][17][0] = "VBAT";
		MsgXMLArray[IdNv0][IdNv1][17][1] = Util.FrmAna(VG.VBat," V");
		MsgXMLArray[IdNv0][IdNv1][18][0] = "VREDE";
		MsgXMLArray[IdNv0][IdNv1][18][1] = Util.FrmAna(VG.VRede," V");
		MsgXMLArray[IdNv0][IdNv1][19][0] = "ESTVRD";
		MsgXMLArray[IdNv0][IdNv1][19][1] = VG.StrEstRede;
		MsgXMLArray[IdNv0][IdNv1][20][0] = "TBAT";
		MsgXMLArray[IdNv0][IdNv1][20][1] = Util.FrmAna(VG.TBat,"°C");
		MsgXMLArray[IdNv0][IdNv1][21][0] = "SDBAT";
		MsgXMLArray[IdNv0][IdNv1][21][1] = Util.FrmAnaInt(VG.SDBat," %");
		
		IdNv1 = 2; // Grupo de 07 Variáveis de Informação da Bomba do Poço e da Caixa Azul
		MsgXMLArray[IdNv0][IdNv1][0][0] = "AGUA";
		MsgXMLArray[IdNv0][IdNv1][0][1] = "07";
			
		MsgXMLArray[IdNv0][IdNv1][1][0] = "ESTCXAZ";
		MsgXMLArray[IdNv0][IdNv1][1][1] = VG.StrEstCxAzul;
		MsgXMLArray[IdNv0][IdNv1][2][0] = "NIVCXAZ";
		MsgXMLArray[IdNv0][IdNv1][2][1] = VG.StrNivCxAzul;
		MsgXMLArray[IdNv0][IdNv1][3][0] = "ESTBMB";
		MsgXMLArray[IdNv0][IdNv1][3][1] = VG.StrEstBomba;
		MsgXMLArray[IdNv0][IdNv1][4][0] = "ESTDJB";
		MsgXMLArray[IdNv0][IdNv1][4][1] = VG.StrEstAlimBoia;
		MsgXMLArray[IdNv0][IdNv1][5][0] = "ESTDJRB";
		MsgXMLArray[IdNv0][IdNv1][5][1] = VG.StrAlRedeBomba;
		MsgXMLArray[IdNv0][IdNv1][6][0] = "ENBMB";
		MsgXMLArray[IdNv0][IdNv1][6][1] = VG.StrIv1Lig;
		MsgXMLArray[IdNv0][IdNv1][7][0] = "TMPBL";
		MsgXMLArray[IdNv0][IdNv1][7][1] = Util.FormAnaHora(VG.TmpBmbLig);
			
		IdNv1 = 3; // Grupo de 18 Variáveis de Informação da Geração Solar e do Consumo
		MsgXMLArray[IdNv0][IdNv1][0][0] = "GERCONS";
		MsgXMLArray[IdNv0][IdNv1][0][1] = "19";
		
		MsgXMLArray[IdNv0][IdNv1][1][0] = "VP12";
		MsgXMLArray[IdNv0][IdNv1][1][1] = Util.FrmAna(VG.VP12," V");
		MsgXMLArray[IdNv0][IdNv1][2][0] = "IS12";
		MsgXMLArray[IdNv0][IdNv1][2][1] = Util.FrmAna(VG.IS12," A");
		MsgXMLArray[IdNv0][IdNv1][3][0] = "ISCC1";
		MsgXMLArray[IdNv0][IdNv1][3][1] = Util.FrmAna(VG.ISCC1," A");
		MsgXMLArray[IdNv0][IdNv1][4][0] = "WSCC1";
		MsgXMLArray[IdNv0][IdNv1][4][1] = Util.FrmAna(VG.WSCC1," W");
		MsgXMLArray[IdNv0][IdNv1][5][0] = "SDCC1";
		MsgXMLArray[IdNv0][IdNv1][5][1] = Util.FrmAnaInt(VG.SDCC1," %");
		MsgXMLArray[IdNv0][IdNv1][6][0] = "VP34";
		MsgXMLArray[IdNv0][IdNv1][6][1] = Util.FrmAna(VG.VP34," V");
		MsgXMLArray[IdNv0][IdNv1][7][0] = "IS34";
		MsgXMLArray[IdNv0][IdNv1][7][1] = Util.FrmAna(VG.IS34," A");
		MsgXMLArray[IdNv0][IdNv1][8][0] = "ISCC2"; 
		MsgXMLArray[IdNv0][IdNv1][8][1] = Util.FrmAna(VG.ISCC2," A");
		MsgXMLArray[IdNv0][IdNv1][9][0] = "WSCC2";
		MsgXMLArray[IdNv0][IdNv1][9][1] = Util.FrmAna(VG.WSCC2," W");
		MsgXMLArray[IdNv0][IdNv1][10][0] = "SDCC2";
		MsgXMLArray[IdNv0][IdNv1][10][1] = Util.FrmAnaInt(VG.SDCC2," %");
		MsgXMLArray[IdNv0][IdNv1][11][0] = "ITOTGER";
		MsgXMLArray[IdNv0][IdNv1][11][1] = Util.FrmAna(VG.ITotGer," A");
		MsgXMLArray[IdNv0][IdNv1][12][0] = "WTOTGER";
		MsgXMLArray[IdNv0][IdNv1][12][1] = Util.FrmAna(VG.WTotGer," W");
		MsgXMLArray[IdNv0][IdNv1][13][0] = "ITOTCG";
		MsgXMLArray[IdNv0][IdNv1][13][1] = Util.FrmAna(VG.ITotCg," A");
		MsgXMLArray[IdNv0][IdNv1][14][0] = "WTOTCG";
		MsgXMLArray[IdNv0][IdNv1][14][1] = Util.FrmAna(VG.WTotCg," W");
		MsgXMLArray[IdNv0][IdNv1][15][0] = "ESTFT1";
		MsgXMLArray[IdNv0][IdNv1][15][1] = VG.StrEstFonteCC1;
		MsgXMLArray[IdNv0][IdNv1][16][0] = "ESTFT2";
		MsgXMLArray[IdNv0][IdNv1][16][1] = VG.StrEstFonteCC2;
		MsgXMLArray[IdNv0][IdNv1][17][0] = "ICIRCC";
		MsgXMLArray[IdNv0][IdNv1][17][1] = Util.FrmAna3(VG.ICircCC," A");
		MsgXMLArray[IdNv0][IdNv1][18][0] = "WCIRCC";
		MsgXMLArray[IdNv0][IdNv1][18][1] = Util.FrmAna(VG.WCircCC," W");
		MsgXMLArray[IdNv0][IdNv1][19][0] = "IFONCC";
		MsgXMLArray[IdNv0][IdNv1][19][1] = Util.FrmAna(VG.IFonteCC," A");
		
		IdNv1 = 4; // Grupo de 32 Variáveis de Informação dos Inversores 1 e 2
        MsgXMLArray[IdNv0][IdNv1][0][0] = "INV";
        MsgXMLArray[IdNv0][IdNv1][0][1] = "32";
            
		MsgXMLArray[IdNv0][IdNv1][1][0] = "ESTIV2";
		MsgXMLArray[IdNv0][IdNv1][1][1] = VG.StrEstIv2;
		MsgXMLArray[IdNv0][IdNv1][2][0] = "IEIV2";
		MsgXMLArray[IdNv0][IdNv1][2][1] = Util.FrmAna(VG.IEIv2," A");
		MsgXMLArray[IdNv0][IdNv1][3][0] = "WEIV2";
		MsgXMLArray[IdNv0][IdNv1][3][1] = Util.FrmAna(VG.WEIv2," W");
		MsgXMLArray[IdNv0][IdNv1][4][0] = "VSIV2";
		MsgXMLArray[IdNv0][IdNv1][4][1] = Util.FrmAna(VG.VSIv2," V");
		MsgXMLArray[IdNv0][IdNv1][5][0] = "ISIV2";
		MsgXMLArray[IdNv0][IdNv1][5][1] = Util.FrmAna3(VG.ISInv2," A");
		MsgXMLArray[IdNv0][IdNv1][6][0] = "WSIV2";
		MsgXMLArray[IdNv0][IdNv1][6][1] = Util.FrmAna(VG.WSInv2," W");
		MsgXMLArray[IdNv0][IdNv1][7][0] = "TDIV2";
		MsgXMLArray[IdNv0][IdNv1][7][1] = Util.FrmAna(VG.TDInv2," C");
		MsgXMLArray[IdNv0][IdNv1][8][0] = "TTIV2"; 
		MsgXMLArray[IdNv0][IdNv1][8][1] = Util.FrmAna(VG.TTInv2," C");
		MsgXMLArray[IdNv0][IdNv1][9][0] = "EFIV2";
		MsgXMLArray[IdNv0][IdNv1][9][1] = Util.FrmAnaInt(VG.EfIv2," %");
		MsgXMLArray[IdNv0][IdNv1][10][0] = "SDIV2";
		MsgXMLArray[IdNv0][IdNv1][10][1] = Util.FrmAnaInt(VG.SDIv2," %");
		MsgXMLArray[IdNv0][IdNv1][11][0] = "FALIV2";   // Alarme de Falha do Inversor 2
		MsgXMLArray[IdNv0][IdNv1][11][1] = AlmFALIV2;
		MsgXMLArray[IdNv0][IdNv1][12][0] = "SUVIV2";   // Alarme de Sub Tensão do Inversor 2
		MsgXMLArray[IdNv0][IdNv1][12][1] = AlmSUVIV2;
		MsgXMLArray[IdNv0][IdNv1][13][0] = "SOVIV2";   // Alarme de Sobre Tensão do Inversor 2
		MsgXMLArray[IdNv0][IdNv1][13][1] = AlmSOVIV2;
		MsgXMLArray[IdNv0][IdNv1][14][0] = "SOTDIV2";  // Alarme de Sobre Temperatura do Driver do Inversor 2
		MsgXMLArray[IdNv0][IdNv1][14][1] = AlmSOTDIV2;
		MsgXMLArray[IdNv0][IdNv1][15][0] = "SOTTIV2";  // Alarme de Sobre Temperatura do Transformador do Inversor 2
		MsgXMLArray[IdNv0][IdNv1][15][1] = AlmSOTTIV2;
		MsgXMLArray[IdNv0][IdNv1][16][0] = "DJAIV2";   // Alarme de Disjuntor de Entrada Aberto do Inversor 2
		MsgXMLArray[IdNv0][IdNv1][16][1] = AlmDJAIV2;
			
		MsgXMLArray[IdNv0][IdNv1][17][0] = "ESTIV1";
		MsgXMLArray[IdNv0][IdNv1][17][1] = VG.StrEstIv1;
		MsgXMLArray[IdNv0][IdNv1][18][0] = "IEIV1";
		MsgXMLArray[IdNv0][IdNv1][18][1] = Util.FrmAna(VG.IEIv1," A");
		MsgXMLArray[IdNv0][IdNv1][19][0] = "WEIV1";
		MsgXMLArray[IdNv0][IdNv1][19][1] = Util.FrmAna(VG.WEIv1," W");
		MsgXMLArray[IdNv0][IdNv1][20][0] = "VSIV1";
		MsgXMLArray[IdNv0][IdNv1][20][1] = Util.FrmAna(VG.VSIv1," V");
		MsgXMLArray[IdNv0][IdNv1][21][0] = "ISIV1";
		MsgXMLArray[IdNv0][IdNv1][21][1] = Util.FrmAna3(VG.ISInv1," A");
		MsgXMLArray[IdNv0][IdNv1][22][0] = "WSIV1";
		MsgXMLArray[IdNv0][IdNv1][22][1] = Util.FrmAna(VG.WSInv1," W");
		MsgXMLArray[IdNv0][IdNv1][23][0] = "TDIV1";
		MsgXMLArray[IdNv0][IdNv1][23][1] = Util.FrmAna(VG.TDInv1," C");
		MsgXMLArray[IdNv0][IdNv1][24][0] = "TTIV1";
		MsgXMLArray[IdNv0][IdNv1][24][1] = Util.FrmAna(VG.TTInv1," C");
		MsgXMLArray[IdNv0][IdNv1][25][0] = "EFIV1";
		MsgXMLArray[IdNv0][IdNv1][25][1] = Util.FrmAnaInt(VG.EfIv1," %");
		MsgXMLArray[IdNv0][IdNv1][26][0] = "SDIV1";
		MsgXMLArray[IdNv0][IdNv1][26][1] = Util.FrmAnaInt(VG.SDIv1," %");
		
	    MsgXMLArray[IdNv0][IdNv1][27][0] = "FALIV1";   // Alarme de Falha do Inversor 1
		MsgXMLArray[IdNv0][IdNv1][27][1] = AlmFALIV1;
		MsgXMLArray[IdNv0][IdNv1][28][0] = "SUVIV1";   // Alarme de Sub Tensão do Inversor 1
		MsgXMLArray[IdNv0][IdNv1][28][1] = AlmSUVIV1;
		MsgXMLArray[IdNv0][IdNv1][29][0] = "SOVIV1";   // Alarme de Sobre Tensão do Inversor 1
		MsgXMLArray[IdNv0][IdNv1][29][1] = AlmSOVIV1;
		MsgXMLArray[IdNv0][IdNv1][30][0] = "SOTDIV1";  // Alarme de Sobre Temperatura do Driver do Inversor 1
		MsgXMLArray[IdNv0][IdNv1][30][1] = AlmSOTDIV1;
		MsgXMLArray[IdNv0][IdNv1][31][0] = "SOTTIV1";  // Alarme de Sobre Temperatura do Transformador do Inversor 1
		MsgXMLArray[IdNv0][IdNv1][31][1] = AlmSOTTIV1;
		MsgXMLArray[IdNv0][IdNv1][32][0] = "DJAIV1";   // Alarme de Disjuntor de Entrada Aberto do Inversor 1
		MsgXMLArray[IdNv0][IdNv1][32][1] = AlmDJAIV1;
		
		IdNv1 = 5; // Grupo de 6 Variáveis de Informação da Água Quente
		MsgXMLArray[IdNv0][IdNv1][0][0] = "AGUAQ";
	    MsgXMLArray[IdNv0][IdNv1][0][1] = "07";
	    
	    MsgXMLArray[IdNv0][IdNv1][1][0] = "ESTUAQ";
		MsgXMLArray[IdNv0][IdNv1][1][1] = EstUTRAQ;
		MsgXMLArray[IdNv0][IdNv1][2][0] = "ESTBAQ";
		MsgXMLArray[IdNv0][IdNv1][2][1] = EstBombaAQ;
		MsgXMLArray[IdNv0][IdNv1][3][0] = "ESTAQ";
		MsgXMLArray[IdNv0][IdNv1][3][1] = EstAquecedor;
		MsgXMLArray[IdNv0][IdNv1][4][0] = "TEMPBL";
		MsgXMLArray[IdNv0][IdNv1][4][1] = Util.FrmAna(VG.TemperaturaBoiler," C");
		MsgXMLArray[IdNv0][IdNv1][5][0] = "TEMPPS";
		MsgXMLArray[IdNv0][IdNv1][5][1] = Util.FrmAna(VG.TemperaturaPlaca," C");
		MsgXMLArray[IdNv0][IdNv1][6][0] = "TMPBL";
		MsgXMLArray[IdNv0][IdNv1][6][1] = Util.FrmAnaInt(VG.TempoBmbLigada," s");
		MsgXMLArray[IdNv0][IdNv1][7][0] = "TMPBD";
		MsgXMLArray[IdNv0][IdNv1][7][1] = Util.FrmAnaInt(VG.TempoBmbDesligada," s");
		
		// Retorna a Mensagem XML completa em formato de String
		return(StringXML(MsgXMLArray));
		
	} // Fim da Rotina
	
	
	//*****************************************************************************************************************
    //                                                                                                                *
	// Nome da Rotina: StringXML()                                                                                    *
	//	                                                                                                              *
	// Funcao: monta uma String com a mensagem XML de resposta inserindo o valor das variáveis                        *
    //                                                                                                                *
	// Entrada: array String com as Tags dos Níveis 0, 1 e 2 e os valores das variáveis de supervisão                 *
    //                                                                                                                *
	// Saida: String com a mensagem XML                                                                               *
	//	                                                                                                              *
	//*****************************************************************************************************************
	//
	static String StringXML(String MsgXMLArray[][][][]) {
		String MsgXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";  // Imprime a linha de Versão e Codificação de Caracteres  
		MsgXML = MsgXML + "<" + MsgXMLArray[0][0][0][0] + ">\n";         // Imprime a Tag de Nivel 0

		char Dezena = MsgXMLArray[0][0][0][1].charAt(0);
		char Unidade = MsgXMLArray[0][0][0][1].charAt(1);
		int NmTagNv1 = Util.TwoCharToInt(Dezena, Unidade);               // Obtem o Numero de Tags de Nivel 1
		
		for (int i = 1; i <= NmTagNv1; i++) {                            // Repete até imprimir todas as Tags de Nível 1 e Nível 2
			MsgXML = MsgXML + "  <" + MsgXMLArray[0][i][0][0] + ">\n";   // Imprime a Tag de Nivel 1 de Início do Grupo
			char DzNumVar = MsgXMLArray[0][i][0][1].charAt(0);
			char UnNumVar = MsgXMLArray[0][i][0][1].charAt(1);
			int NumVar = Util.TwoCharToInt(DzNumVar, UnNumVar);          // Obtém o Número de Variáveis do Grupo
		    
			for (int j = 1; j <= NumVar; j++) {                          // Repeta até imprimir todas as Tags de Nível 2 e suas variáveis
				MsgXML = MsgXML + "    <"+MsgXMLArray[0][i][j][0]+">" +  // Imprime as Tags de Nível 2 e os Valores das Variáveis 
			                              MsgXMLArray[0][i][j][1] +
			                         "</"+MsgXMLArray[0][i][j][0]+">\n";
			}
			MsgXML = MsgXML + "  </" + MsgXMLArray[0][i][0][0] + ">\n";  // Imprime a Tag de Nivel 1 de Fim de Grupo
		}
		MsgXML = MsgXML + "</" + MsgXMLArray[0][0][0][0] + ">";          // Imprime a Tag de Nivel 0 de Fim
		
		return(MsgXML);
	}
	
}