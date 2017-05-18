package cs;

import javax.swing.JFrame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@SuppressWarnings("unused")
public class Cryptography {


	private static char[][] sub_matrix = new char[128][223];

	//constructor
	private Cryptography(){}


	public static void buildMatrix(String key){
		int i, j;
		//using key of 128 length
		for (i = 0; i < 128; i++){
			int ascii = (int)key.charAt(i);
			for (j = 0; j < 223; j++){
				if(ascii + j < 256){
					sub_matrix[i][j] = (char)(ascii + j);
				}
				else{
					sub_matrix[i][j] = (char)(ascii + j - 223);
				}
			}
		}
	}

	private static int index_column(String key, char message, int char_index){
		int column;
		int mes = (int)message;
		int key_at = (int)key.charAt(char_index%128);
		column = mes ^ key_at;
		return column;
	}

	private static int index_line(String key, int char_index){
		int line;
		int key_at = (int)key.charAt(char_index%128);
		line = (char_index * key_at)%128;
		return line;

	}

	protected static char encChar(String key, int char_index, char message){

		int column = index_column(key, message, char_index);
		int line = index_line(key, char_index);

		char cipher = sub_matrix[line][column];
		return cipher;
	}

	private static char decChar(String key, int char_index, char cipher){
		char messageAtN = ' ';

		int line = index_line(key, char_index);

		int key_at = (int)key.charAt(char_index%128);
		int column = -1;

		//find which column has Cn
		for (int j = 0; j < 223; j++){
			if (sub_matrix[line][j] == cipher){
				column = j;
			}
		}

		if (column > -1){
			int message_ascii = column ^ key_at;
			messageAtN = (char)message_ascii;
		}
		else{
			System.out.println("Caracter nao encontrado. Indice: ");
			System.out.println(char_index);
		}

		return messageAtN;
	}

	public static void encrypt(String public_key, File input_file, File output_folder) throws IOException{

		buildMatrix(public_key);

		// read the input file and put all the characters on the "everything" string
		String everything;
		BufferedReader br = new BufferedReader(new FileReader(input_file));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} finally {
		    br.close();
		}

		//go trough every char on "everything" and encrypt it
		char aux;
		char enc_char;
		final int zero = 0;
		String cipherText = "";
		for(int i = 0; i < everything.length()-1; i++){
			aux = everything.charAt(i);

			enc_char = encChar(public_key, i, aux);
			cipherText += enc_char;
		}

		//write output file with ciphertext
		try{
            Writer output = null;
            File out = new File(output_folder, "encrypted_message.txt");
            output = new BufferedWriter(new FileWriter(out));

            output.write(cipherText);
            output.close();
            System.out.println("Output File has been written");

        }catch(Exception e){
            System.out.println("Could not create file");
        }
	}

	public static void decrypt(String public_key, File input_file, File output_folder) throws IOException{
		System.out.println(output_folder + "\\encrypted_message.txt");
		//String windows_path = output_folder.replaceAll("\\", "\\");

		buildMatrix(public_key);

		// read the encrypted file and put all the characters on the "everything" string
		String everything;
		BufferedReader br = new BufferedReader(new FileReader(input_file));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} finally {
		    br.close();
		}

		//go trough every char on "everything" and decrypts it
		char dec_char;
		char aux;
		String plainText = "";
		for(int i = 0; i < everything.length(); i++){
			aux = everything.charAt(i);
			dec_char = decChar(public_key, i, aux);
			plainText += dec_char;
		}



		//escrever arquivo de saida
        try{
            Writer output = null;
            File out = new File(output_folder, "decrypted_message.txt");
            output = new BufferedWriter(new FileWriter(out));

            output.write(plainText);
               //CODE TO FETCH RESULTS AND WRITE FILE
            output.close();
            System.out.println("Output File has been written");

        }catch(Exception e){
            System.out.println("Could not create file");
        }

	}


	//main: abre a janela JFrame
	public static void main(String[] args) {
        JFrame frame = new JFrame("Joao's Smart Home");
        frame.add(new UserInterface());
        frame.setVisible(true);
        frame.setSize(300, 175);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
