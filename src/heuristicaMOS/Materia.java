package heuristicaMOS;

import java.util.LinkedList;

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

	public Materia(int pId,String pNombre, int pCreditos, int pCarga, int pDificultad, int pNivel, int[] pPrerequisitos, int[] pCorrequisitos){
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
	}
	
	public void aniadirSoyPreDe(int pSoyPreDe){
		soyPreDe.add(pSoyPreDe);
	}
	
	public void aniadirSoyCoDe(int pSoyCoDe){
		soyCoDe.add(pSoyCoDe);
	}
}
