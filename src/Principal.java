import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import modelo.Particula;
import processing.core.PApplet;
import processing.core.PVector;

public class Principal extends PApplet {

	public static void main(String[] args) {
		PApplet.main("Principal");
	}

	private Socket socket;

	BufferedReader reader;
	BufferedWriter writer;


	// private ArrayList<PVector> particulas;
	private ArrayList<Particula> particulas;

	private int x, y, r, g, b, cantidad, mov;
	private String nombre;
	int xf, yf;

	float angulo = 0;
	float R = 100;

	boolean borrar, crear, mover;

	@Override
	public void settings() // void Awake
	{
		size(600, 500);
	}

	@Override
	public void setup() // void Start

	{
		// particulas = new ArrayList<PVector>();
		particulas = new ArrayList<Particula>();
		mover = true;

		iniciarServidor();

	}

	@Override
	public void draw() // void Update
	{

		background(254, 249, 231);

		for (int i = 0; i < particulas.size(); i++) {
			noStroke();
			fill(particulas.get(i).getR(), particulas.get(i).getG(), particulas.get(i).getB());
			ellipse(xf, yf, 20, 20);
			
			xf = particulas.get(i).getX() + mov;
			yf = particulas.get(i).getY() + mov;

	
			tocarParticula();

			if (mover == true) {
			movimientoParticulas();
			} else {
				mover= false;
				tocarParticula();
			}
			
			if (borrar == true) {
				System.out.println("bueno, y si borramos todo :D");
				particulas.clear();
			}

		}

	}

	public void iniciarServidor() {

		new Thread(() -> {

			try {
				
				ServerSocket server = new ServerSocket(5000);
				System.out.println("Esperando  cliente "+ server);
				socket = server.accept();
				System.out.println("El cliente se conectó");

				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(isr);

				OutputStream os = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				writer = new BufferedWriter(osw);

				while (true) {
					System.out.println("Esperando mensaje del cliente...");
					String line = reader.readLine();
					System.out.println("Recibido" + line);

					Gson gson = new Gson();

					Particula particula = gson.fromJson(line, Particula.class);
					
					for (int i = 0; i < particula.getCantidad(); i++) {

						int x = particula.getX();
						int y = particula.getY();

						int r = particula.getR();
						int g = particula.getG();
						int b = particula.getB();

						String nombre = particula.getNombre();

						int  cantidad = particula.getCantidad();
						borrar = particula.getBorrar();
						Boolean crear = particula.getCrear();

						System.out.println(x + "," + y + " ," + nombre + " ," + cantidad + " (" + r + " ," + g + ", " + b + ") ");

						particulas.add(new Particula(x, y, nombre, cantidad, r, g, b, borrar, crear));

					}

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

	public void movimientoParticulas() {
		for (int i = 0; i < particulas.size(); i++) {

			mov = (int) random(0, 5);
			
		

			if (mov == 1) {

				particulas.get(i).setX(particulas.get(i).getX() + 2);
			}
			if (mov == 2) {

				particulas.get(i).setX(particulas.get(i).getX() - 2);
			}
			if (mov == 3) {

				particulas.get(i).setY(particulas.get(i).getY() + 2);
			}
			if (mov == 4) {

				particulas.get(i).setY(particulas.get(i).getY() - 2);
			}
		}

		if (xf >= 600) {
			xf = 559;

		}

		else if (xf <= 0) {
			xf = 1;
		}

		if (yf >= 500) {
			yf = 499;
		}

		else if (yf <= 0) {
			yf = 1;
		}
	}
	


	public void tocarParticula() { 
		for (int i = 0; i < particulas.size(); i++) {
			if (dist(mouseX, mouseY, particulas.get(i).getX(), particulas.get(i).getY()) < 25) {
				fill(particulas.get(i).getR(), particulas.get(i).getG(), particulas.get(i).getB());
				stroke(3);
				fill(250);
				rect(mouseX-10,mouseY-15,40,20);
				fill(particulas.get(i).getR(), particulas.get(i).getG(), particulas.get(i).getB());
				text(particulas.get(i).getNombre(), mouseX, mouseY);
				
				mover = false;
				mov = 0;

				particulas.get(i).setX(particulas.get(i).getX() + mov);
				particulas.get(i).setY(particulas.get(i).getY() + mov);

			} else {
				
				mover = true;
			}
		}
		
	}

}
