package heuristicaMOS;

import java.util.LinkedList;
import excepciones.ImpossibilityException;

public class Semestre {
	
	int semestre;
	
	float creditos;
	
	float creditosAct;
	
	LinkedList<Integer> materias;

	public Semestre( int pSemestre, float pCreditos, float pCreditosAct){
		semestre=pSemestre;
		creditos=pCreditos;
		materias= new LinkedList<Integer>();
		creditosAct=pCreditosAct;
	}
	
	public void aniadirMateria(int pMateria,float pCreditos) throws ImpossibilityException{
		if(creditos<creditosAct+pCreditos){
			throw new ImpossibilityException(pMateria+"");
		}
		materias.add(pMateria);
		creditosAct+=pCreditos;
	}
	
	@SuppressWarnings("unchecked")
	public Semestre clone(){
		Semestre nuevo= new Semestre(semestre,creditos,creditosAct);
		nuevo.setMaterias((LinkedList<Integer>) materias.clone());
		return nuevo;		
	}

	private void setMaterias(LinkedList<Integer> materiasDeep) {
		materias=materiasDeep;
	}
}
