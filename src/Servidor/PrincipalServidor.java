package Servidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Objetos.*;

public class PrincipalServidor {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	ServerSocket socket= new ServerSocket(7000);
	ArrayList<Cliente>clientes= new ArrayList<>();
	ArrayList<Coche>flotaCoches= new ArrayList<>();
		
	Cliente cliente1= new Cliente("11111", "Manuma", "Perez", "Perez");
	Cliente cliente2= new Cliente("11112", "Ramon", "Legaspi", "Ramoneo");
	Cliente cliente3= new Cliente("11113", "Jesus", "Lobeiras", "OnoLobeiras");
	clientes.add(cliente1);
	clientes.add(cliente2);
	clientes.add(cliente3);
	
	
	
	Coche coche1= new Coche("1111GLV", "Audi", "A3", 105.0);
	Coche coche2= new Coche("1234MFS", "Seat", "Toledo", 150.0);
	Coche coche3= new Coche("3456CJF", "Ford", "Mondeo", 90.0);
	flotaCoches.add(coche1);
	flotaCoches.add(coche2);
	flotaCoches.add(coche3);
	
	Socket socketCliente= socket.accept();
	System.out.println("Cliente conectado");
		
	AtiendeACliente cliente= new AtiendeACliente(clientes, flotaCoches, socketCliente);
	
	cliente.start();
	
	
	}

}
