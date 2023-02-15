package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Objetos.*;

public class AtiendeACliente extends Thread {

	ArrayList<Cliente> clientes;
	ArrayList<Coche> flota;
	private int correcto = 100;
	private int fallo = 200;
	ObjectInputStream escucha;
	ObjectOutputStream escribe;
	DataOutputStream datao;
	DataInputStream datai;

	public AtiendeACliente(ArrayList<Cliente> a, ArrayList<Coche> b, Socket socket) throws IOException {

		this.clientes = a;
		this.flota = b;
		// con el socket que pasamos creamos los objetsInput y output
		
		
		this.datao= new DataOutputStream(socket.getOutputStream());
		this.datai= new DataInputStream(socket.getInputStream());
		this.escucha = new ObjectInputStream(socket.getInputStream());
		this.escribe = new ObjectOutputStream(socket.getOutputStream());

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		

		//estas variables se me reinician en cada vuelta de bucle
		
			
		do {
			int eleccion = 0;
			int eleccionClase = 0;
			String dniRecibido;
			boolean dniExiste = false;
			Cliente aPasar = null;
			String matriculaRecibida;
			boolean matriculaExiste = false;
			Coche aEnviar = null;
	
		
		do {
			try {
				// esperamos
			
				
				eleccion=this.datai.readInt();
				
				if (eleccion > 0 && eleccion < 4) {
					this.datao.writeInt(this.correcto);
				} else {
					this.datao.writeInt(this.fallo);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Fallo en la recepcion");
			}
		} while (eleccion < 1 || eleccion > 3);

		if (eleccion == 1) {
		
			// tenemos que enviar datos que el cliente quiere
			try {
				do {
					eleccionClase = this.datai.readInt();
					if (eleccionClase == 1 || eleccionClase == 2) {
						this.datao.writeInt(this.correcto);
					} else {
						this.datao.writeInt(this.fallo);
					}
				} while (eleccionClase < 1 || eleccionClase > 2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (eleccionClase == 1) {
				
				// esperamos la llegada de un dni
				try {
					dniRecibido = this.datai.readUTF();

					for (Cliente cliente : clientes) {
						
						
						if (cliente.getDni().equals(dniRecibido)) {
							dniExiste = true;
							aPasar = cliente;
							
						}

					}

					if (dniExiste == true) {
						
						this.datao.writeInt(this.correcto);
						this.escribe.writeObject(aPasar);
					} else {
						System.out.println();
						this.datao.writeInt(this.fallo);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (eleccionClase == 2) {
				// esperamos la llegada de una matricula

				try {
					matriculaRecibida = this.datai.readUTF();
					
					for (Coche coche : flota) {
						
						if (coche.getMatricula().equalsIgnoreCase(matriculaRecibida)) {
							aEnviar = coche;
							matriculaExiste = true;
						}
						
						

					}

					if (matriculaExiste == true) {
						this.datao.writeInt(this.correcto);
						this.escribe.writeObject(aEnviar);
					} else {
						this.datao.writeInt(this.fallo);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		else if (eleccion == 2) {
			// tenemos que estar a la espera para setear los datos que quiere enviar el
			// cliente

			try {

				do {
					System.out.println(" entra en espera eleccion de cliente o coche");
					eleccionClase = this.datai.readInt();
					if (eleccionClase == 1 || eleccionClase == 2) {
						this.datao.writeInt(this.correcto);
					} else {
						this.datao.writeInt(this.fallo);
					}
				} while (eleccionClase < 1 || eleccionClase > 2);
				
				if (eleccionClase == 1) {
					// recibiremos un cliente, revisaremos si existe el dni
					// si existe enviaremos 200 y si no existe daremos el ok

					Cliente clienteRecibido = (Cliente) this.escucha.readObject();

					for (Cliente cliente : clientes) {
						if (cliente.getDni().equalsIgnoreCase(clienteRecibido.getDni())) {

							dniExiste = true;
						}

					}

					if (dniExiste == true) {
						this.datao.writeInt(this.fallo);
					} else {
						this.datao.writeInt(this.correcto);
						clientes.add(clienteRecibido);

					}
				} else if (eleccionClase == 2) {
					// recibiremos un coche, revisaremos si existe su matricula
					// si existe enviaremos 200 y si no existe daremos el ok

					Coche cocheRecibido = (Coche) this.escucha.readObject();

					for (Coche coche : flota) {
						if (coche.getMatricula().equalsIgnoreCase(cocheRecibido.getMatricula())) {

						matriculaExiste = true;
						}

					}

					if (matriculaExiste == true) {
						this.datao.writeInt(this.fallo);
					} else {
						this.datao.writeInt(this.correcto);
						flota.add(cocheRecibido);

					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (eleccion == 3) {
			// nos pide datos de tota clientes y total vehiculos
			
			try {
				this.datao.writeInt(clientes.size());
				this.datao.writeInt(flota.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// resetearemos todas las variables para la siguiente consulta
		eleccion = 0;
		eleccionClase = 0;
		dniRecibido=null;
		dniExiste = false;
		aPasar = null;
		matriculaRecibida=null;
		matriculaExiste = false;
		aEnviar = null;
		}while(true);
	}

	public int eleccionClase() throws IOException {
		// estamos a la espera
		int clase = 0;
		

		return correcto;
	}

}
