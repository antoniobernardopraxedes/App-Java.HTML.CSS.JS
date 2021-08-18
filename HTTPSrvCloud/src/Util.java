import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {

	//*****************************************************************************************************************
	// Nome da Rotina: BytetoInt                                                                                      *
	//                                                                                                                *
	// Funcao: converte um valor byte para inteiro (conversao sem sinal)                                              *
	// Entrada: valor byte sem sinal de 0 a 255                                                                       *
	// Saida: valor (inteiro) sempre positivo de 0 a 255                                                              *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int BytetoInt(int valor) {
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
	static int DoisBytesInt(int LSByte, int MSByte) {
		int lsb;
		int msb;
		if (LSByte < 0) { lsb = LSByte + 256; }
		else { lsb = LSByte; }
		if (MSByte < 0) { msb = MSByte + 256; }
		else { msb = MSByte; }
		return (lsb + 256*msb);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: ThreeBytetoInt                                                                                 *
	//                                                                                                                *
	// Funcao: converte tres bytes em sequencia de um array para inteiro (conversao sem sinal)                        *
	// Entrada: dois bytes consecutivos (LSB e MSB) sem sinal de 0 a 255                                              *
	// Saida: valor (inteiro) sempre positivo de 0 a 65535                                                            *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int ThreeBytetoInt(int LSByte, int MSByte, int HSByte) {
		int lsb;
		int msb;
		int hsb;
		if (LSByte < 0) { lsb = LSByte + 256; }
		else { lsb = LSByte; }
		if (MSByte < 0) { msb = MSByte + 256; }
		else { msb = MSByte; }
		if (HSByte < 0) { hsb = HSByte + 256; }
		else { hsb = HSByte; }
		return (lsb + 256*msb + 65536*hsb);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: FormAnaHora                                                                                    *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo para uma string no formato HH:MM:SS  (hora:minuto:segundo)                *
	// Entrada: valor inteiro em segundos                                                                             *
	// Saida: string do numero no formato "parte inteira","duas casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FormAnaHora(int valor) {
		int Hora = valor / 3600;
		int Minuto = ((valor - (Hora * 3600)) / 60);
		int Segundo = valor - (Minuto * 60) - (Hora * 3600);
		String HMS = "";
		if (Hora < 10) {
			HMS = "0";
		}
		HMS = (HMS + Hora + ":");
		if (Minuto < 10) {
			HMS = HMS + "0";
		}
		HMS = (HMS + Minuto + ":");
		if (Segundo < 10) {
			HMS = HMS + "0";
		}
		HMS = HMS + Segundo;
		
		return (HMS);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: CharToByte                                                                                     *
	//                                                                                                                *
	// Funcao: converte um caracter numerico em um valor numerico de 0 a 9                                            *
	// Entrada: caracter: '0' a '9'                                                                                   *
	// Saida: byte (valor numerico de 0 a 9)                                                                          *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int CharToByte(char caracter) {
		byte Num = 10;
		switch (caracter) {
			case '0': Num = 0;
			break;
			case '1': Num = 1;
			break;
			case '2': Num = 2;
			break;
			case '3': Num = 3;
			break;
			case '4': Num = 4;
			break;
			case '5': Num = 5;
			break;
			case '6': Num = 6;
			break;
			case '7': Num = 7;
			break;
			case '8': Num = 8;
			break;
			case '9': Num = 9;
			break;
		}
		return (Num);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: CharToInt                                                                                      *
	//                                                                                                                *
	// Funcao: converte um caracter numerico em um valor numerico de 0 a 9                                            *
	// Entrada: caracter: '0' a '9'                                                                                   *
	// Saida: int (valor numerico de 0 a 9)                                                                           *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int ChToInt(char caracter) {
		int Num = 10;
		switch (caracter) {
			case '0': Num = 0;
			break;
			case '1': Num = 1;
			break;
			case '2': Num = 2;
			break;
			case '3': Num = 3;
			break;
			case '4': Num = 4;
			break;
			case '5': Num = 5;
			break;
			case '6': Num = 6;
			break;
			case '7': Num = 7;
			break;
			case '8': Num = 8;
			break;
			case '9': Num = 9;
			break;
		}
		return (Num);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: TwoCharToByte                                                                                  *
	//                                                                                                                *
	// Funcao: converte dois caracteres numericos em um valor numerico de 00 a 99                                     *
	// Entrada: caracteres dezena e unidade ('0' a '9')                                                               *
	// Saida: byte (valor numerico de 00 a 99)                                                                        *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int TwoCharToByte(char Ch10, char Ch1) {
		int Num = 10*CharToByte(Ch10) + CharToByte(Ch1);
		return ((byte)Num);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: TwoCharToInt                                                                                  *
	//                                                                                                                *
	// Funcao: converte dois caracteres numericos em um valor numerico de 00 a 99                                     *
	// Entrada: caracteres dezena e unidade ('0' a '9')                                                               *
	// Saida: int (valor numerico de 00 a 99)                                                                        *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int TwoCharToInt(char Ch10, char Ch1) {
		int Num = 10*CharToByte(Ch10) + CharToByte(Ch1);
		return (Num);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: FourCharToInt                                                                                  *
	//                                                                                                                *
	// Funcao: converte quatro caracteres numericos em um valor numerico de 0000 a 9999                               *
	// Entrada: caracteres milhar, centena, dezena e unidade ('0' a '9')                                              *
	// Saida: int (valor numerico de 0000 a 9999)                                                                     *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int FourCharToInt(char Ch1000, char Ch100, char Ch10, char Ch1) {
		int Num = 1000*CharToByte(Ch1000) + 100*CharToByte(Ch100) + 10*CharToByte(Ch10) + CharToByte(Ch1);
		return (Num);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: StringToInt                                                                                    *
	//                                                                                                                *
	// Funcao: converte uma string de até quatro caracteres numericos em um valor numerico de 0000 a 9999             *
	// Entrada: string com um numero entre 0 e 9999                                                                   *
	// Saida: int (valor numerico de 0000 a 9999 correspondente à string de entrada)                                  *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int StringToInt(String StNm) {
		int Num = 0;
		int TamNum = StNm.length();
		
		if (TamNum == 1) {
			Num = ChToInt(StNm.charAt(0));
		}
		if (TamNum == 2) {
			Num = 10*ChToInt(StNm.charAt(0)) + ChToInt(StNm.charAt(1));
		}
		if (TamNum == 3) {
			Num = 100*ChToInt(StNm.charAt(0)) + 10*ChToInt(StNm.charAt(1)) + ChToInt(StNm.charAt(2));
		}
		if (TamNum == 4) {
			Num = 1000*ChToInt(StNm.charAt(0))+100*ChToInt(StNm.charAt(1))+10*ChToInt(StNm.charAt(2))+ChToInt(StNm.charAt(3));
		}	
		return (Num);
	}
	
	//*****************************************************************************************************************
	// Nome da Rotina: ByteLow                                                                                        *
	//                                                                                                                *
	// Funcao: obtem o byte menos significativo de um valor inteiro                                                   *
	// Entrada: valor inteiro                                                                                         *
	// Saida: byte menos significativo                                                                                *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int ByteLow(int valor) {
		int BH = valor / 256;
		int BL = valor - 256*BH;
		return ((byte)BL);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: ByteHigh                                                                                       *
	//                                                                                                                *
	// Funcao: obtem o byte mais significativo de um valor inteiro                                                    *
	// Entrada: valor inteiro                                                                                         *
	// Saida: byte mais significativo                                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static int ByteHigh(int valor) {
		int BH = valor / 256;
		return ((byte)BH);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: ImpHora                                                                                        *
	//                                                                                                                *
	// Funcao: gera umna string com hora:minuto:segundo dia/mes/ano                                                   *
	// Entrada: valor hora, minuto, segundo, dia, mes, ano                                                            *
	// Saida: byte mais significativo                                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String ImpHora(int hora, int minuto, int segundo) {
	
	String Msg = "";
		if (hora < 10) { Msg = Msg + "0"; }
		Msg = Msg + hora + ":";
		
		if (minuto < 10) { Msg = Msg + "0"; }
		Msg = Msg + minuto + ":";
		
		if (segundo < 10) { Msg = Msg + "0"; }
		Msg = Msg + segundo;
		
		return(Msg);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: ImpData                                                                                        *
	//                                                                                                                *
	// Funcao: gera umna string com hora:minuto:segundo dia/mes/ano                                                   *
	// Entrada: valor hora, minuto, segundo, dia, mes, ano                                                            *
	// Saida: byte mais significativo                                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String ImpData(int dia, int mes, int ano) {
	
	String Msg = "";
		if (dia < 10) { Msg = Msg + "0"; }
		Msg = Msg + dia + "/";
		
		if (mes < 10) { Msg = Msg + "0"; }
		Msg = Msg + mes + "/" + ano + " ";
		
		return(Msg);
	}
	
	
	static String ImprimeHoraData(byte hora,byte minuto,byte segundo,byte dia,byte mes,byte ano) {
		
		String Msg = "";
		if (hora < 10) { Msg = Msg + "0"; }
		Msg = Msg + hora + ":";
		if (minuto < 10) { Msg = Msg + "0"; }
		Msg = Msg + minuto + ":";
		if (segundo < 10) { Msg = Msg + "0"; }
		Msg = Msg + segundo;
		Msg = Msg + " - ";
		if (dia < 10) { Msg = Msg + "0"; }
		Msg = Msg + dia + "/";
		if (mes < 10) { Msg = Msg + "0"; }
		Msg = Msg + mes + "/" + ano + " ";
		return(Msg);
	}
	
	//*****************************************************************************************************************
	// Nome da Rotina: LeHoraData                                                                                     *
	//                                                                                                                *
	// Funcao: le a data e hora do relogio do computador usando ZonedDateTime no formato string                       *
	//          "2020-01-01T10:38:17.240-03:00[America/Araguaina]"                                                    *
	//                                                                                                                *
	// Entrada: não tem                                                                                               *
	//                                                                                                                *
	// Saida: array com 6 Bytes: [0] = Hora, [1] = Minuto, [2] = Segundo, [3] = Dia, [4] = Mês, [5] = Ano             *                                                                                 *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static byte[] LeDataHora() {
		
		//getting current date and time using Date class
		DateFormat dfor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date obj = new Date();
		String DataHora = dfor.format(obj); // 04/08/2021 17:22:48
											// 0123456789012345678
											// 0000000000111111111
				
		byte DH [] = new byte[7];
		
		int Dia = 10 * Character.digit(DataHora.charAt(0), 10) + Character.digit(DataHora.charAt(1), 10);
		int Mes = 10 * Character.digit(DataHora.charAt(3), 10) + Character.digit(DataHora.charAt(4), 10);
		int AnoCentenas = 10 * Character.digit(DataHora.charAt(6), 10) + Character.digit(DataHora.charAt(7), 10);
	    int AnoUnidades = 10 * Character.digit(DataHora.charAt(8), 10) + Character.digit(DataHora.charAt(9), 10);
	    
	    int Hora = 10 * Character.digit(DataHora.charAt(11), 10) + Character.digit(DataHora.charAt(12), 10);
	    int Minuto = 10 * Character.digit(DataHora.charAt(14), 10) + Character.digit(DataHora.charAt(15), 10);
	    int Segundo = 10 * Character.digit(DataHora.charAt(17), 10) + Character.digit(DataHora.charAt(18), 10);
	    
	    DH[0] = (byte)ByteLow(Hora);         // Hora
	    DH[1] = (byte)ByteLow(Minuto);       // Minuto
	    DH[2] = (byte)ByteLow(Segundo);      // Segundo
	    DH[3] = (byte)ByteLow(Dia);          // Dia
	    DH[4] = (byte)ByteLow(Mes);          // Mes
	    DH[5] = (byte)ByteLow(AnoUnidades);  // Ano (Unidades)
	    DH[6] = (byte)ByteLow(AnoCentenas);  // Ano (Centenas)
		
	    //if (VG.verbose) {
	    //	System.out.println("DataHora = "+ImpData(DH[3],DH[4],DH[5])+" "+ImpHora(DH[0],DH[1],DH[2])+" Fuso Horario = -" + FusoHorario);
	    //}
	    
	    return(DH);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: GravaArquivo()                                                                                 *
    //	                                                                                                              *
	// Funcao: grava um arquivo                                                                                       *
	// Entrada: string com o nome do caminho e do arquivo e texto a ser escrito no arquivo                            *
	// Saida: = 1 leu arquivo / = 0 falha ao ler o arquivo                                                            *
    //	                                                                                                              *
	//*****************************************************************************************************************
	//
	public static void GravaArquivo(String nomeArquivo, String texto) {

	try {
		PrintWriter out = new PrintWriter(new FileWriter(nomeArquivo));
		out.write(texto);
		out.close();
		} catch (IOException e) {
			System.out.print("Erro = " + e);

		}
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: FormAna                                                                                        *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo em formato x100 para uma string com parte inteira e duas casas decimais   *
	// Entrada: valor inteiro no formato x100                                                                         *
	// Saida: string do numero no formato "parte inteira","duas casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FormAna(int valor) {
		int inteiro;
		int decimal;
		inteiro = valor / 100;
		decimal = valor - 100*inteiro;
		String Valor = (inteiro + ",");
		if (decimal > 9) {
			Valor = Valor + decimal; 
		}
		else {
			Valor = Valor + "0" + decimal;
		}
		return (Valor);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: FrmAna                                                                                         *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo em formato x100 para uma string com parte inteira e duas casas decimais   *
	// Entrada: valor inteiro no formato x100 e unidade em string                                                     *
	// Saida: string do numero no formato "parte inteira","duas casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FrmAna(int valor, String Unidade) {
		int inteiro;
		int decimal;
		inteiro = valor / 100;
		decimal = valor - 100*inteiro;
		String Valor = (inteiro + ",");
		if (decimal > 9) {
			Valor = Valor + decimal; 
		}
		else {
			Valor = Valor + "0" + decimal;
		}
		return (Valor + Unidade);
	}

		
	//*****************************************************************************************************************
	// Nome da Rotina: FrmAna3                                                                                        *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo em formato x1000 para uma string com parte inteira e tres casas decimais  *
	// Entrada: valor inteiro no formato x1000 e unidade em string                                                    *
	// Saida: string do numero no formato "parte inteira","tres casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FrmAna3(int valor, String Unidade) {
		int inteiro;
		int decimal;
		inteiro = valor / 1000;
		decimal = valor - 1000 * inteiro;
		String Valor = (inteiro + ",");
		if (decimal > 90) {
			Valor = Valor + decimal; 
		}
		else {
			Valor = Valor + "0" + decimal;
		}
		return (Valor + Unidade);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: FormAna3                                                                                       *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo em formato x1000 para uma string com parte inteira e tres casas decimais  *
	// Entrada: valor inteiro no formato x1000                                                                        *
	// Saida: string do numero no formato "parte inteira","tres casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FormAna3(int valor) {
		int inteiro;
		int decimal;
		inteiro = valor / 1000;
		decimal = valor - 1000 * inteiro;
		String Valor = (inteiro + ",");
		if (decimal > 90) {
			Valor = Valor + decimal; 
		}
		else {
			Valor = Valor + "0" + decimal;
		}
		return (Valor);
	}


	//*****************************************************************************************************************
	// Nome da Rotina: FormAnaInt                                                                                     *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo para uma string                                                           *
	// Entrada: valor inteiro no formato x100                                                                         *
	// Saida: string do numero no formato "parte inteira","duas casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FormAnaInt(int valor) {

		String Valor = (valor + " ");

		return (Valor);
	}
	
	
	//*****************************************************************************************************************
	// Nome da Rotina: FormAnaInt                                                                                     *
	//                                                                                                                *
	// Funcao: converte um inteiro positivo para uma string                                                           *
	// Entrada: valor inteiro no formato x100                                                                         *
	// Saida: string do numero no formato "parte inteira","duas casas decimais"                                       *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	static String FrmAnaInt(int valor, String Unidade) {

		String Valor = (valor + Unidade);

		return (Valor);
	}

}