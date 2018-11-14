package heuristicaMOS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class HeuristicaMOS {
	
	public final static String FILENAME= "./data/materias.txt";
	public static int carga=0;
	public static int dificultad=0;
	
	public static HashMap<Integer, Materia> materias= new HashMap<Integer,Materia>();
	
	public static Semestre[] semestres;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HeuristicaMOS();
	}
	
	
	public HeuristicaMOS(){
		cargarDatos();
	}


	private void cargarDatos() {
		BufferedReader br = null;
		InputStreamReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new InputStreamReader(new FileInputStream(FILENAME),"UTF8");
			br = new BufferedReader(fr);

			carga=Integer.parseInt(br.readLine().split(":")[1]);
			dificultad=Integer.parseInt(br.readLine().split(":")[1]);
			
			semestres= new Semestre[Integer.parseInt(br.readLine().split(":")[1])];
			for (int i = 0; i < semestres.length; i++) {
				String semestreActual= br.readLine();
				Semestre act= new Semestre(Integer.parseInt(semestreActual.split(":")[0]), Float.parseFloat(semestreActual.split(":")[1]));
				semestres[i]=act;
			}
			
			int numMaterias= Integer.parseInt(br.readLine().split(":")[1]);
			

			for (int i = 0; i < numMaterias; i++) {
				String[] info= br.readLine().split(":");
				String[] params= info[1].split(";",7);
				int id= Integer.parseInt(info[0]);
				
				System.out.println(info[1]);
				System.out.println(params.length);

				String[] prer = new String[0];
				if(!params[5].isEmpty()){
					prer= params[5].split(",");
				}
				
				int[] prerList= new int[prer.length];
				for (int j = 0; j < prerList.length; j++) {
					prerList[j]=Integer.parseInt(prer[j]);
				}
				
				String[] corr = new String[0];
				if(!params[6].isEmpty()){
					corr= params[6].split(",");
				}
				
				int[] corList= new int[corr.length];
				for (int j = 0; j < corList.length; j++) {
					corList[j]=Integer.parseInt(corr[j]);
				}
				if(i==51){
					System.out.println("hjoli");
				}
				
				Materia actual= new Materia(id,params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]), prerList, corList);
				materias.put(id, actual);
			}

			

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

}
