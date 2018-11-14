package heuristicaMOS;

public class Materia {
	
	String nombre;
	int creditos;
	int carga;
	int dificultad;
	int nivel;
	int[] prerequisitos;
	int[] correquisitos;
	int[] soyPreDe;
	int[] soyCoDe;
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
	}
	
	public void aniadirSoyPreDe(int[] soyPreDe){
		
	}
}
