package heuristicaMOS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class HeuristicaMOS {

	public final static String FILENAME= "./data/materias.txt";
	public static int carga=0;
	public static int dificultad=0;
	public static Materia[] materias;
	public static LinkedList<Integer> materiasN1= new LinkedList<Integer>();
	public static LinkedList<Integer> materiasN2= new LinkedList<Integer>();
	public static LinkedList<Integer> materiasN3= new LinkedList<Integer>();
	public static LinkedList<Integer> materiasN4= new LinkedList<Integer>();


	public static Semestre[] semestres;

	public static void main(String[] args) {
		new HeuristicaMOS();
	}


	public HeuristicaMOS(){
		cargarDatos();
		setearRestriccionesIniciales();
		setearRestriccionesPreCo();
	}


	private void setearRestriccionesPreCo() {
		for (int i = 1; i < materias.length; i++) {
			Materia act= materias[i];
			int [] pres=act.prerequisitos;
			for (int j = 0; j < pres.length; j++) {
				materias[pres[j]].setMaxSemestre(act.maxSemestre-1);
			}
			int [] cor=act.correquisitos;
			for (int j = 0; j < cor.length; j++) {
				materias[cor[j]].setMaxSemestre(act.maxSemestre);
			}
			LinkedList<Integer> preD=act.soyPreDe;
			for (Integer j: preD) {
				materias[j.intValue()].setMinSemestre(act.minSemestre+1);
			}
			LinkedList<Integer> corD=act.soyCoDe;
			for (Integer j: corD) {
				materias[j.intValue()].setMinSemestre(act.minSemestre);
			}
		}
	}


	private void setearRestriccionesIniciales() {
		boolean seteo=false;
		int semActInd=0;
		Semestre semAct=semestres[semActInd];
		int credAcum=0;
		int minL3=1;
		//Min semestre para materias N3 y N4
		for (int materia : materiasN1) {
			credAcum+=materias[materia].creditos;
			if(credAcum>semAct.creditos){
				minL3++;
				semActInd++;
				semAct=semestres[semActInd];
				credAcum=materias[materia].creditos;
			}
		}
		int minL4=minL3;
		for (int materian3: materiasN3) {
			materias[materian3].setMinSemInit(minL3);
		}
		for (int materia : materiasN2) {
			credAcum+=materias[materia].creditos;
			if(credAcum>semAct.creditos){
				minL4++;
				semActInd++;
				semAct=semestres[semActInd];
				credAcum=materias[materia].creditos;
			}
		}
		for (int materian4: materiasN4) {
			materias[materian4].setMinSemInit(minL4);
		}
		//Max semestre para materias N1 y N2
		semActInd=semestres.length-1;
		semAct=semestres[semActInd];
		credAcum=0;
		int maxL2=semestres.length-2;
		for (int materia : materiasN4) {
			credAcum+=materias[materia].creditos;
			if(credAcum>semAct.creditos){
				maxL2--;
				semActInd--;
				semAct=semestres[semActInd];
				credAcum=materias[materia].creditos;
			}
		}
		int maxL1=maxL2;
		for (int materian2: materiasN2) {
			materias[materian2].setMaxSemInit(maxL2);
		}
		for (int materia : materiasN1) {
			credAcum+=materias[materia].creditos;
			if(credAcum>semAct.creditos){
				maxL1--;
				semActInd--;
				semAct=semestres[semActInd];
				credAcum=materias[materia].creditos;
			}
		}
		for (int materian1: materiasN1) {
			materias[materian1].setMaxSemInit(maxL1);
		}
		System.out.println("Holi");
	}


	private void cargarDatos() {
		LinkedList<Integer> listaVacia= new LinkedList<Integer>();
		int[] arregloVacio= new int[0];
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
						Materia nueva= new Materia(preInt, "", -1, -1, -1, -1, new int[]{id}, arregloVacio,semestres.length-1,materias,listaVacia,listaVacia,listaVacia,listaVacia);
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
						Materia nueva= new Materia(coInt, "", -1, -1, -1, -1, arregloVacio,new int[]{id},semestres.length-1,materias,listaVacia,listaVacia,listaVacia,listaVacia);
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
				int nivel= Integer.parseInt(params[4]);
				switch(nivel){
				case 1:
					materiasN1.add(id);
					break;
				case 2:
					materiasN2.add(id);
					break;
				case 3:
					materiasN3.add(id);
					break;
				case 4:
					materiasN4.add(id);
					break;
				}
				Materia actual= new Materia(id,params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), nivel, prerList, corList,semestres.length-1,materias,materiasN1,materiasN2,materiasN3,materiasN4);
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
