package heuristicaMOS;

import java.util.Comparator;

//The Comparators that look at different numbers in FancyClass
public class MateriaComparator implements Comparator<Materia> {
	int carga;
	int dificultad;
	
	public MateriaComparator(int pCarga, int pDificultad){
		carga=pCarga;
		dificultad=pDificultad;
	}

	public int compare(Materia m1, Materia m2) {
		int diff= m1.maxSemestre-m1.minSemestre;
		int mDiff= m2.maxSemestre-m2.minSemestre;
		if(diff!=mDiff){
			return diff-mDiff;
		}
		else{
			return (m2.carga*carga+m2.dificultad*dificultad)-(m1.carga*carga+m1.dificultad*dificultad);
		}
	}
}
