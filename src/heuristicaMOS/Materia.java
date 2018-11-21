package heuristicaMOS;

import java.util.LinkedList;

import excepciones.ImpossibilityException;
import excepciones.RestrictedException;

public class Materia {

	String nombre;
	int creditos;
	int carga;
	int dificultad;
	int nivel;
	int[] prerequisitos;
	int[] correquisitos;
	LinkedList<Integer> soyPreDe;
	LinkedList<Integer> soyCoDe;
	int id;
	int minSemestre;
	int maxSemestre;
	Materia[] materias;

	LinkedList<Integer> materiasN1;
	LinkedList<Integer> materiasN2;
	LinkedList<Integer> materiasN3;
	LinkedList<Integer> materiasN4;


	public Materia(int pId,String pNombre, int pCreditos, int pCarga, int pDificultad, int pNivel, int[] pPrerequisitos, int[] pCorrequisitos, int pMaxSemestre, Materia[] pMaterias,
			LinkedList<Integer> materiasN12,LinkedList<Integer> materiasN22,LinkedList<Integer> materiasN32,LinkedList<Integer> materiasN42){
		nombre=pNombre;
		creditos=pCreditos;
		carga=pCarga;
		dificultad=pDificultad;
		nivel=pNivel;
		prerequisitos=pPrerequisitos;
		correquisitos=pCorrequisitos;
		id=pId;
		soyPreDe= new LinkedList<Integer>();
		soyCoDe= new LinkedList<Integer>();
		minSemestre=0;
		maxSemestre=pMaxSemestre;
		materias=pMaterias;

		materiasN1=materiasN12;
		materiasN2=materiasN22;
		materiasN3=materiasN32;
		materiasN4=materiasN42;
	}

	public void aniadirSoyPreDe(int pSoyPreDe){
		soyPreDe.add(pSoyPreDe);
	}

	public void aniadirSoyCoDe(int pSoyCoDe){
		soyCoDe.add(pSoyCoDe);
	}

	public void setMinSemInit(int pMinSemestre) {
		minSemestre=pMinSemestre;
	}

	public void setMaxSemInit(int pMaxSemestre) {
		maxSemestre=pMaxSemestre;
	}

	public void setMaxSemestre(int pMaxSem) throws RestrictedException, ImpossibilityException {
		if(pMaxSem<maxSemestre){
			maxSemestre=pMaxSem;
			for (Integer i: prerequisitos) {
				materias[i].setMaxSemestre(maxSemestre-1);
			}
			for (Integer i: correquisitos) {
				materias[i].setMaxSemestre(maxSemestre);
			}
			if(maxSemestre<minSemestre){
				throw new ImpossibilityException(id+"");
			}
			else if(minSemestre==maxSemestre){
				throw new RestrictedException(id+"");
			}	
			if(nivel==3){
				for (Integer i: materiasN1) {
					materias[i].setMaxSemestre(maxSemestre-1);
				}
			}
			else if(nivel==4){
				for (Integer i: materiasN1) {
					materias[i].setMaxSemestre(maxSemestre-1);
				}
				for (Integer i: materiasN2) {
					materias[i].setMaxSemestre(maxSemestre-1);
				}
			}
		}
	}

	public void setMinSemestre(int pMinSem) throws RestrictedException, ImpossibilityException {
		if(pMinSem>minSemestre){
			minSemestre=pMinSem;
			for (Integer i: soyCoDe) {
				materias[i].setMinSemestre(minSemestre);
			}
			for (Integer i: soyPreDe) {
				materias[i].setMinSemestre(minSemestre+1);
			}
			if(maxSemestre<minSemestre){
				throw new ImpossibilityException(id+"");
			}
			else if(minSemestre==maxSemestre){
				throw new RestrictedException(id+"");
			}
			if(nivel==1){
				for (Integer i: materiasN3) {
					materias[i].setMinSemestre(minSemestre+1);
				}
				for (Integer i: materiasN4) {
					materias[i].setMinSemestre(minSemestre+1);
				}
			}
			else if(nivel==2){
				for (Integer i: materiasN4) {
					materias[i].setMinSemestre(minSemestre+1);
				}
			}
		}
	}
}
