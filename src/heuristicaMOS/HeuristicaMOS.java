package heuristicaMOS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class HeuristicaMOS {
	
	public final static String FILENAME= "./data/materias.txt";
	public static int carga=0;
	public static int dificultad=0;
	public static Materia[] materias;
	
	
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
			materias= new Materia[numMaterias+1];		

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
					int preInt= Integer.parseInt(prer[j]);
					prerList[j]=preInt;
					Materia pre= materias[preInt];
					if(pre!=null){
						pre.aniadirSoyPreDe(id);
					}
					else{
						Materia nueva= new Materia(preInt, "", -1, -1, -1, -1, new int[]{id}, new int[0]);
						materias[preInt]= nueva;
					}
				}
				
				String[] corr = new String[0];
				if(!params[6].isEmpty()){
					corr= params[6].split(",");
				}
				
				int[] corList= new int[corr.length];
				for (int j = 0; j < corList.length; j++) {
					int coInt= Integer.parseInt(corr[j]);
					corList[j]=coInt;
					Materia co= materias[coInt];
					if(co!=null){
						co.aniadirSoyCoDe(id);
					}
					else{
						Materia nueva= new Materia(coInt, "", -1, -1, -1, -1, new int[0],new int[]{id});
						materias[coInt]= nueva;
					}
				}
				
				if(materias[id]!=null){
					if(materias[id].prerequisitos.length>0){
						int[] both = Arrays.copyOf(prerList, prerList.length + materias[id].prerequisitos.length);
						System.arraycopy(materias[id].prerequisitos, 0, both, prerList.length, materias[id].prerequisitos.length);
						prerList=both;
					}
					if(materias[id].correquisitos.length>0){
						int[] both = Arrays.copyOf(corList, corList.length + materias[id].correquisitos.length);
						System.arraycopy(materias[id].correquisitos, 0, both, corList.length, materias[id].correquisitos.length);
						corList=both;
					}
				}
				
				if(id==51){
					System.out.println("ñam");
				}
				
				Materia actual= new Materia(id,params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]), prerList, corList);
				materias[id]= actual;
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
