package heuristicaMOS;

import java.util.LinkedList;

public class Semestre {
	
	int semestre;
	
	float creditos;
	
	LinkedList<Integer> materias;

	public Semestre( int pSemestre, float pCreditos){
		semestre=pSemestre;
		creditos=pCreditos;
		materias= new LinkedList<Integer>();
	}
	
	public void aniadirMateria(int pMateria){
		materias.add(pMateria);
	}
}
