package Cliente;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Objetos.*;

public class PrincipalCliente {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		
	
		Socket socket= new Socket("127.0.0.1",7000);
	
		Conexion conectaConServidor= new Conexion(socket);
		conectaConServidor.start();
		

		
	}



	
	

}
