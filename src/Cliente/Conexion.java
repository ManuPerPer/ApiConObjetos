package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Objetos.Cliente;
import Objetos.Coche;

public class Conexion extends Thread {

	ObjectOutputStream escribe;
	ObjectInputStream escucha;
	DataOutputStream datao;
	DataInputStream datai;
	Socket socket;

	public Conexion(Socket socket) throws IOException {
		super();

		this.socket = socket;
		this.datao= new DataOutputStream(socket.getOutputStream());
		this.datai= new DataInputStream(socket.getInputStream());
		this.escribe = new ObjectOutputStream(socket.getOutputStream());
		this.escucha = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Scanner sc= new Scanner(System.in);
		Scanner scString= new Scanner (System.in);
		do {
			int eleccion=0;
			int okEleccion=0; // lo trataremos como valido cuando valga 100;
			int eleccionClase=0;
			int okElecClase=0;
			int peticionDni=0;
			int peticionMatricula=0;
			int envioObjeto=0;
			
			try {
		
			do {
				System.out.println("QUE QUIERES HACER?");
				System.out.println("1- RECIBIR?");
				System.out.println("2- ENVIAR?");
				System.out.println("3- CANTIDAD DE DATOS?");
				eleccion = sc.nextInt();
				this.datao.writeInt(eleccion);
				System.out.println("envia correcto "+ eleccion);
				okEleccion = this.datai.readInt();
				if(okEleccion==200) {
					System.out.println("Opcion no valida elige entre 1 y 3");
				}
				
			} while (okEleccion!=100);
			
			if (eleccion==1){
				// queremos recibir datos
				do {
					System.out.println("1- CLIENTE?");
					System.out.println("2- COCHE?");
					eleccionClase= sc.nextInt();
					this.datao.writeInt(eleccionClase);
					okElecClase=this.datai.readInt();
					if(okElecClase==200) {
						System.out.println("error en la eleccion");
					}
				} while (okElecClase!=100);
				
				if(eleccionClase==1) {
					System.out.println("Introduce un dni que quieras consultar");
					//quiere recibir informacion de un cliente
					//preguntara por un dni que sera el que pasemos
					String dni=scString.nextLine();
					this.datao.writeUTF(dni);
					peticionDni=this.datai.readInt();
					if(peticionDni==200) {
						System.out.println("el dni solicitado no existe");
					}
					else {
						Cliente recibido= (Cliente) this.escucha.readObject();	
						
						System.out.println("Los datos solicitados sobre el dni son los siguientes:");
						System.out.println("Nombre: "+recibido.getNombre()+", Apellidos : "+ recibido.getApellido1()+" "+ recibido.getApellido2());
					}
					
					
				}
				else if(eleccionClase==2) {
					//queremos recibir informacion de un coche
					// preguntara matricula
					
					System.out.println("Introduce la matricula que consultar");
					//quiere recibir informacion de un cliente
					//preguntara por un dni que sera el que pasemos
					String matricula=scString.nextLine();
					this.datao.writeUTF(matricula);
					peticionMatricula=this.datai.readInt();
					if(peticionMatricula==200) {
						System.out.println("la matricula solicitada no existe");
					}
					else {
						Coche recepcionado= (Coche) this.escucha.readObject();	
						
						System.out.println("Los datos solicitados sobre la matricula son los siguientes:");
						System.out.println("Marca: "+recepcionado.getMarca()+" Modelo: "+recepcionado.getModelo()+" Potencia: "+ recepcionado.getPotencia());
					}
				}
				
				
			
				
			}
			else if(eleccion==2) {
				// queremos setear datos
				do {
					System.out.println("1- CLIENTE?");
					System.out.println("2- COCHE?");
					eleccionClase= sc.nextInt();
					this.datao.writeInt(eleccionClase);
					okElecClase=this.datai.readInt();
					if(okElecClase==200) {
						System.out.println("error en la eleccion");
					}
				} while (okElecClase!=100);
				
				if(eleccionClase==1) {
					// creamos un cliente y lo enviamos
					// esperamos confirmacion
					System.out.println("INTRODUCE UN DNI");
					String dni=scString.nextLine();
					System.out.println("INTRODUCE UN NOMBRE");
					String nombre=scString.nextLine();
					System.out.println("INTRODUCE APELLIDO1");
					String apellido1=scString.nextLine();
					System.out.println("INTRODUCE APELLIDO2");
					String apellido2=scString.nextLine();
					
					Cliente aEnviar= new Cliente(dni, nombre, apellido1, apellido2);
					this.escribe.writeObject(aEnviar);
					envioObjeto=this.datai.readInt();
					
					if(envioObjeto==100) {
						System.out.println("el cliente ha sido añadido correctamente");
					}
					else if(envioObjeto==200) {
						System.out.println("Ya existe un cliente con ese dni");
					}
					
					
				}
				else if (eleccionClase==2) {
					//creamos un coche y lo enviamos
					System.out.println("INTRODUCE UNA MATRICULA");
					String matricula=scString.nextLine();
					System.out.println("INTRODUCE UNA MARCA");
					String marca=scString.nextLine();
					System.out.println("INTRODUCE MODELO");
					String modelo=scString.nextLine();
					System.out.println("INTRODUCE POTENCIA");
					double potencia=sc.nextDouble();
					Coche paraEnviar= new Coche(matricula, marca, modelo, potencia);
					this.escribe.writeObject(paraEnviar);
					
					envioObjeto=this.datai.readInt();
					
					if(envioObjeto==100) {
						System.out.println("el coche ha sido añadido correctamente");
					}
					else if(envioObjeto==200) {
						System.out.println("Ya existe un coche con esa Matricula");
					}
					
				}
				
			}
			else if(eleccion == 3) {
				// queremos ver datos de totales
				int totalClientes=this.datai.readInt();
				int totalCoches=this.datai.readInt();		
				
				System.out.println("EN ESTOS MOMENTOS HAY UN TOTAL DE "+ totalClientes+" CLIENTES");
				System.out.println("EN ESTOS MOMENTOS HAY UN TOTAL DE "+totalCoches+" COCHES");
				
				
			}
			}catch (Exception e) {
				System.out.println("algo falla");
			}
			
			
		//reiniciamos variables para siguiente bucle
			eleccion=0;
			okEleccion=0; // lo trataremos como valido cuando valga 100;
			eleccionClase=0;
			okElecClase=0;
			peticionDni=0;
			peticionMatricula=0;
			envioObjeto=0;	
			
		} while (true);
		
	
	
	
	}
	
	
}
