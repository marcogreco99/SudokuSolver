package poo.sudoku;

import java.util.ArrayList;

/**@author Marco Greco */

public class Sudoku {
	ArrayList<int[][]> listaSoluzioni= new ArrayList<>();
	int[][] griglia;
	boolean[][] grigliaInput;
	int numSol=1;
	
	
	public Sudoku() {
		griglia=new int[9][9];
		grigliaInput=new boolean[9][9];
	}
	
	public Sudoku(int[][] imp) {
		griglia=new int[9][9];
		grigliaInput=new boolean[9][9];
		for(int i=0;i<imp.length;i++) {
			imposta(imp[i][0],imp[i][1],imp[i][2]);
		}
	}
	
	public void imposta(int i, int j,int v) {
		Cella cel = new Cella(i,j);
		if(!assegnabile(cel,v)||v<=0||v>9) throw new IllegalArgumentException("Parametri iniziali non validi");
		grigliaInput[cel.rig][cel.col]=true;
		griglia[cel.rig][cel.col]=v;
	}
	
	private class Cella{
		int rig,col;
		
		@SuppressWarnings("unused")
		public Cella() {
			this.rig=0; this.col=0;
		}
		public Cella(int riga,int colonna) {
			if(riga<0||riga>=griglia.length||colonna<0||colonna>=griglia.length) throw new IllegalArgumentException();
			this.rig=riga;this.col=colonna;
		}
	}
	

	private void colloca(Cella c) {
		if(numSol==201) return;
		for(int num=1;num<=9;++num) {
			if(!grigliaInput[c.rig][c.col] ) {
				if(assegnabile(c,num)) {
					assegna(c,num);
					if(c.rig==griglia.length-1 && c.col==griglia.length-1) scriviSoluzione();
					else {
						if(c.col>=griglia.length-1) colloca(new Cella(c.rig+1,0));
						else colloca(new Cella(c.rig,c.col+1));
					}
					deassegna(c);
				}
			}
		}
		//Non ho messo l'else perchè deve stare fuori dal ciclo
		if(grigliaInput[c.rig][c.col]) {
			if(!(c.rig==griglia.length-1 && c.col==griglia.length-1)) {
				if(c.col==griglia.length-1) colloca(new Cella(c.rig+1,0));
				else colloca(new Cella(c.rig,c.col+1));
			}
			else scriviSoluzione();
		}
	}

	
	private boolean assegnabile(Cella cella,int numero) {
			for(int i=0;i<griglia.length;++i) {
				if(griglia[cella.rig][i]==numero) return false;
			}
			for(int j=0;j<griglia.length;++j) {
				if(griglia[j][cella.col]==numero) return false;
			}
			
			int x=cella.rig/3*3;
		    int y=cella.col/3*3;
		    for(int i=x; i<x+3; i++)
		        for(int j=y; j<y+3; j++)
		            if(griglia[i][j]==numero) return false; 
		    
		return true;
	}
	
	private  void assegna(Cella cella,int numero) {
		griglia[cella.rig][cella.col]=numero;
	}
	
	private void deassegna(Cella cella) {
		griglia[cella.rig][cella.col]=0;
	}
	
	void risolvi() {
		Cella cella = new Cella(0,0);
		colloca(cella);
	}
	
	void scriviSoluzione() {
		int[][] soluz = new int[9][9];
		System.out.println("    Soluzione "+numSol);
		numSol++;
		for( int i=0; i<griglia.length; ++i ){
			for( int j=0; j<griglia[0].length; ++j ) {
				soluz[i][j]=griglia[i][j];
				System.out.print(griglia[i][j]+" ");
				if(j==2 || j==5) System.out.print("| ");
			}
			System.out.println();
			if(i==2 || i==5) {
				for(int k=0;k<griglia.length*2+3;++k) System.out.print("-");
				System.out.println();
			}
		}
		listaSoluzioni.add(soluz);
		System.out.println();
		
	}
	
	public static void main(String args[]) {
		int[][] m = {{0,1,4},  
                {1,0,6},
                {1,2,8},  
                {0,3,5},
                {0,4,7},  
                {0,6,8},  
                {1,7,2},
                {2,4,3},  
                {2,7,9},  
                //{3,1,6},
                {3,5,3},  
                {3,7,8},  
                {4,0,4},
                {4,3,2},  
                {4,6,6},  
                {4,7,7},
                //{5,1,5},  
                {5,4,1},  
                {5,5,7},
                {5,6,4},  
                {6,0,7},  
                //{6,2,4},
                {6,3,9},  
                {6,5,8},  
                {6,6,3},
                {6,8,6},
                {7,0,3},
                {7,5,1},
                //{7,8,2},
                {8,0,5},
                {8,5,4},
                {8,8,8},
                };
		
		Sudoku s = new Sudoku(m);
 		s.risolvi();
	}
}
