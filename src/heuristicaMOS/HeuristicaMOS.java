package heuristicaMOS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import excepciones.ImpossibilityException;

public class HeuristicaMOS {

	public final static String FILENAME= "./data/materias3.txt";
	public int carga=0;
	public int dificultad=0;
	public Materia[] materias;
	public LinkedList<Integer> materiasN1= new LinkedList<Integer>();
	public LinkedList<Integer> materiasN2= new LinkedList<Integer>();
	public LinkedList<Integer> materiasN3= new LinkedList<Integer>();
	public LinkedList<Integer> materiasN4= new LinkedList<Integer>();

	public Semestre[] resultado;
	public double pesoResultado= Double.MAX_VALUE;


	public static Semestre[] semestres;

	public static void main(String[] args) {
		new HeuristicaMOS();
	}


	public HeuristicaMOS(){
		cargarDatos();
		setearRestriccionesIniciales();
		try {
			setearRestriccionesPreCo();
			setearMateriasImportantes();
		} catch (ImpossibilityException e) {
			e.printStackTrace();
			System.out.println("El sistema no es factible, una materia involucrada con el error es: "+materias[Integer.parseInt(e.getMessage())].nombre);
		}
		System.out.println("Holi");
		PriorityQueue<Materia> pq= new PriorityQueue<Materia>(materias.length-1, new MateriaComparator(carga, dificultad));
		for (int i = 1; i < materias.length; i++) {
			pq.add(materias[i]);
		}
		magiaRecursiva(semestres,materias,pq);
		calcularMinimo();
	}

	private void calcularMinimo() {
		if(resultado==null || resultado.length==0){
			System.out.println("No se encontró ninguna solución factible para este problema");
		}
		else{
			System.out.println("Resultado encontrado!");
			System.out.println("El pensum de menor peso es el siguiente:");
			for (int i = 0; i < resultado.length; i++) {
				System.out.println("Semestre "+(i+1)+":");
				for (Integer sMateria : resultado[i].materias) {
					System.out.println("Materia: "+ materias[sMateria].nombre);
				}
			}
		}

	}


	private void magiaRecursiva(Semestre[] rSemestres, Materia[] rMaterias, PriorityQueue<Materia> pq){
		if(pq.isEmpty()){
			System.out.println("llega");
			double peso=0;
			double pesoCargas=0;
			double pesoDificultades=0;
			for (Semestre semestre : rSemestres) {
				double pesoCarga=0;
				double pesoDificultad=0;
				for (Integer sMateria : semestre.materias) {
					pesoCarga+=materias[sMateria].carga;
					pesoDificultad+=materias[sMateria].dificultad;
				}
				pesoCargas+=pesoCarga*pesoCarga;
				pesoDificultades+=pesoDificultad*pesoDificultad;
			}
			peso=pesoCargas*carga+pesoDificultades*dificultad;
			if(peso<pesoResultado){
				pesoResultado=peso;
				resultado=rSemestres;
			}
			return;
		}
		Materia actual= pq.poll();
		for (int i = actual.minSemestre; i <= actual.maxSemestre; i++) {
			if(rSemestres[i].creditos<rSemestres[i].creditosAct+actual.creditos){
				continue;
			}
			Semestre[] semestresDeep= new Semestre[rSemestres.length];
			for (int j = 0; j < semestresDeep.length; j++) {
				Semestre clonado= rSemestres[j].clone();
				semestresDeep[j]=clonado;
			}
			Materia[] materiasDeep= new Materia[rMaterias.length];
			for (int j = 1; j < materiasDeep.length; j++) {
				Materia clonada= rMaterias[j].clone();
				clonada.setSemestres(semestresDeep);
				clonada.setMaterias(materiasDeep);
				materiasDeep[j]=clonada;
			}
			try {
				materiasDeep[actual.id].setSemestre(i);
				System.out.println(pq.size());
				PriorityQueue<Materia> nPq=pq;
				if(!pq.isEmpty()){
					nPq=new PriorityQueue<Materia>(pq.size(),pq.comparator());
					for (int j = 1; j < materiasDeep.length; j++) {
						if(!materiasDeep[j].isSeteada){
							nPq.add(materiasDeep[j]);
						}
					}
				}
				magiaRecursiva(semestresDeep, materiasDeep,nPq);
			} catch (ImpossibilityException e) {
			}

		}
	}



	private void setearMateriasImportantes() throws ImpossibilityException {
		for (int i = 1; i < materias.length; i++) {
			if(materias[i].nombre.contains("Introducción")){
				materias[i].setSemestre(0);
			}
			if(materias[i].nombre.contains("Proyecto")){
				materias[i].setSemestre(semestres.length-1);
			}
		}
	}


	private void setearRestriccionesPreCo() throws ImpossibilityException {
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
				Semestre act= new Semestre(Integer.parseInt(semestreActual.split(":")[0]), Float.parseFloat(semestreActual.split(":")[1]),0);
				semestres[i]=act;
			}

			int numMaterias= Integer.parseInt(br.readLine().split(":")[1]);
			materias= new Materia[numMaterias+1];		

			for (int i = 0; i < numMaterias; i++) {
				String[] info= br.readLine().split(":");
				String[] params= info[1].split(";",7);
				int id= Integer.parseInt(info[0]);

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
						LinkedList<Integer> soyPre= new LinkedList<Integer>();
						soyPre.add(id);
						Materia nueva= new Materia(preInt, "", -1, -1, -1, -1, arregloVacio, arregloVacio,soyPre, new LinkedList<Integer>(), semestres.length-1,materias,listaVacia,listaVacia,listaVacia,listaVacia,new Semestre[0]);
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
						LinkedList<Integer> soyCo= new LinkedList<Integer>();
						soyCo.add(id);
						Materia nueva= new Materia(coInt, "", -1, -1, -1, -1, arregloVacio,arregloVacio,new LinkedList<Integer>(),soyCo,semestres.length-1,materias,listaVacia,listaVacia,listaVacia,listaVacia,new Semestre[0]);
						materias[coInt]= nueva;
					}
				}

				if(materias[id]!=null){
					if(materias[id].prerequisitos.length>0){
						ArrayList<Integer> repetidos= new ArrayList<Integer>();
						for (int j = 0; j < prerList.length; j++) {
							for (int k = 0; k < materias[id].prerequisitos.length; k++) {
								if(prerList[j]==materias[id].prerequisitos[k]){
									repetidos.add(prerList[j]);
								}
							}
						}
						int[] both= new int[materias[id].prerequisitos.length+prerList.length-repetidos.size()];
						int k=0;
						for (int j = 0; j < materias[id].prerequisitos.length; j++) {
							if(!repetidos.contains(materias[id].prerequisitos[j])){
								both[k]=materias[id].prerequisitos[j];
								k++;
							}
							else{
								repetidos.remove(new Integer(materias[id].correquisitos[j]));
							}
						}
						for (int j = 0; j < prerList.length; j++) {
							if(!repetidos.contains(prerList[j])){
								both[k]=prerList[j];
								k++;
							}
						}
						//int[] both = Arrays.copyOf(corList, corList.length + materias[id].correquisitos.length);
						//System.arraycopy(materias[id].correquisitos, 0, both, corList.length, materias[id].correquisitos.length);
						prerList=both;
					}
					if(materias[id].correquisitos.length>0){
						ArrayList<Integer> repetidos= new ArrayList<Integer>();
						for (int j = 0; j < corList.length; j++) {
							for (int k = 0; k < materias[id].correquisitos.length; k++) {
								if(corList[j]==materias[id].correquisitos[k]){
									repetidos.add(corList[j]);
								}
							}
						}
						int[] both= new int[materias[id].correquisitos.length+corList.length-repetidos.size()];
						int k=0;
						for (int j = 0; j < materias[id].correquisitos.length; j++) {
							if(!repetidos.contains(materias[id].correquisitos[j])){
								both[k]=materias[id].correquisitos[j];
								k++;
							}
							else{
								repetidos.remove(new Integer(materias[id].correquisitos[j]));
							}
						}
						for (int j = 0; j < corList.length; j++) {
							if(!repetidos.contains(corList[j])){
								both[k]=corList[j];
								k++;
							}
						}
						//int[] both = Arrays.copyOf(corList, corList.length + materias[id].correquisitos.length);
						//System.arraycopy(materias[id].correquisitos, 0, both, corList.length, materias[id].correquisitos.length);
						corList=both;
					}
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
				LinkedList<Integer> soyCo= new LinkedList<Integer>();
				LinkedList<Integer> soyPre= new LinkedList<Integer>();
				if(materias[id]!=null){
					soyCo=materias[id].soyCoDe;
					soyPre=materias[id].soyPreDe;
				}
				Materia actual= new Materia(id,params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), nivel, prerList, corList,soyPre, soyCo, semestres.length-1,materias,materiasN1,materiasN2,materiasN3,materiasN4, semestres);
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
