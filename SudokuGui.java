package poo.sudoku;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**@author Marco Greco */

public class SudokuGui{

	public static void main(String[] args) {
		JFrame fin = new FinestraApp();
		fin.setVisible(true);
	}
}

class FinestraApp extends JFrame {
	JTextField[][][][] matriceSu = new JTextField[3][3][][];
	JTextField sol;
	JLabel s,c;
	JButton risolvi,previous,reset,next,salva,carica;
	ActionListener listener = new AscoltatoreEventi();
	int soluzione=0;
	private Sudoku sud;
	
	public FinestraApp() {
		setTitle("RISOLUTORE SUDOKU");
		setSize(1000,800);
		setLocation(465,150);
		setResizable(false);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JPanel pannello = new JPanel();
		add(pannello);
		pannello.setBackground(Color.GRAY);
		pannello.setLayout(null);
		
		s = new JLabel("Salva");
		s.setForeground(Color.WHITE);
		s.setBounds(940, 635, 50, 30);
		pannello.add(s);
		
		c = new JLabel("Carica");
		c.setForeground(Color.WHITE);
		c.setBounds(940, 685, 50, 30);
		pannello.add(c);
		
		salva = new JButton("");
		salva.setBounds(900, 635, 30, 30);
		salva.addActionListener(listener);
		salva.setEnabled(false);
		pannello.add(salva);
		
		carica = new JButton("");
		carica.setBounds(900, 685, 30, 30);
		carica.addActionListener(listener);
		pannello.add(carica);

		risolvi = new JButton("RISOLVI");
		risolvi.setBounds(30, 685, 400, 60);
		risolvi.addActionListener(listener);
		pannello.add(risolvi);

		previous = new JButton("PREC");
		previous.setEnabled(false);
		previous.setBounds(700, 335, 100, 50);
		previous.addActionListener(listener);
		pannello.add(previous);

		reset = new JButton("RESETTA");
		reset.setBounds(440, 685, 210, 60);
		reset.addActionListener(listener);
		pannello.add(reset);

		next = new JButton("SUCC");
		next.setEnabled(false);
		next.setBounds(850, 335, 100, 50);
		next.addActionListener(listener);
		pannello.add(next);

		Font font = new Font("Italic", Font.BOLD, 25);

		JLabel title= new JLabel("RISOLUTORE SUDOKU");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("TimesRoman", Font.PLAIN, 27));
		title.setBounds(682,70,450,100);
		pannello.add(title);

		sol = new JTextField();
		sol.setBounds(700,230,250,100);
		sol.setBackground(Color.BLACK);
		sol.setForeground(Color.WHITE);
		sol.setHorizontalAlignment(SwingConstants.CENTER);
		sol.setFont(new Font("Italic", Font.BOLD, 22));
		sol.setEditable(false);
		pannello.add(sol);

		JPanel griglia = new JPanel();
		griglia.setLayout(null);
		griglia.setBackground(Color.BLACK);
		griglia.setBounds(30,50, 620, 620);
		pannello.add(griglia);
	
		int y=5;
		for(int i=0;i<3;++i) {
			int x = 5;
			if(i!=0) y+=205;
			for(int j=0;j<3;++j) {
				JPanel Panel3x3 = new JPanel(new GridLayout(3,3));
				JTextField[][] matrice3x3 = new JTextField[3][3];
				for(int m=0;m<3;++m)
					for(int n=0;n<3;++n) {
						JTextField jt = new JTextField();
						jt.setHorizontalAlignment(SwingConstants.CENTER);
						jt.setFont(font);
						jt.setBorder(BorderFactory.createLineBorder(Color.black));
						matrice3x3[m][n]=jt;
						matriceSu[i][j]=matrice3x3;
						Panel3x3.add(jt);
					}
				Panel3x3.setBounds(x, y, 200, 200);
				x+=205;
				griglia.add(Panel3x3);
			}
		}
	}
	


	private void stampaSoluzione(Sudoku sudoku,int numeroSoluzione) {
		for(int i=0;i<matriceSu.length;i++) 
			for(int c=0;c<matriceSu[0].length;++c) 
				for(int j=0;j<matriceSu[0][0].length;++j) 
					for(int m=0;m<matriceSu[0][0][0].length;++m)
						matriceSu[i][j][c][m].setText(Integer.toString(sudoku.listaSoluzioni.get(numeroSoluzione)[c+3*i][m+3*j]));
	}

	private int contaCellePreimpostate() {
		int N=0;
		for(int i=0;i<matriceSu.length;i++) 
			for(int c=0;c<matriceSu[0].length;++c) 
				for(int j=0;j<matriceSu[0][0].length;++j) 
					for(int m=0;m<matriceSu[0][0][0].length;++m)
						if(!(matriceSu[i][j][c][m].getText().isEmpty()))
							N++;
		return N;
	}

	private class AscoltatoreEventi implements ActionListener{
		public void actionPerformed(ActionEvent e) { 

			if(e.getSource()==risolvi) {
				soluzione=0;
				next.setEnabled(false);
				previous.setEnabled(false);
				int[][] imposta= new int[contaCellePreimpostate()][3];
				int count=0;
				ciclo: for(int i=0;i<matriceSu.length;i++) {
					for(int c=0;c<matriceSu[0].length;++c) { 
						for(int j=0;j<matriceSu[0][0].length;++j) {
							for(int m=0;m<matriceSu[0][0][0].length;++m) {
								if(!(matriceSu[i][j][c][m].getText().isEmpty())) {
									imposta[count][0]=c+3*i;
									imposta[count][1]=m+3*j;
									try {
										imposta[count][2]=Integer.parseInt(matriceSu[i][j][c][m].getText());
									}catch(NumberFormatException exc) {break ciclo;} //il messaggio di avviso lo gestisco alla riga 190
									count++;
								}
							}
						}
					}
				}
				try {
					sud = new Sudoku(imposta);
					sud.risolvi();
					stampaSoluzione(sud,0);
					sol.setText("SOLUZIONE "+(soluzione+1)+" DI "+(sud.numSol-1));
					if(sud.numSol-1!=1)next.setEnabled(true);
					salva.setEnabled(true);
				}catch(IllegalArgumentException ex){JOptionPane.showMessageDialog(null, "Parametri Iniziali Errati !");}
			}   //Cattura anche l'eccezione in cui gli passo una stringa. 

			if(e.getSource()==previous) {
				if(soluzione-1<1) previous.setEnabled(false);
				soluzione--;
				stampaSoluzione(sud,soluzione);
				sol.setText("SOLUZIONE "+(soluzione+1)+" DI "+(sud.numSol-1));
				next.setEnabled(true);
			}

			if(e.getSource()==next) {
				if(soluzione+1>=sud.numSol-2) next.setEnabled(false);
				soluzione++;
				stampaSoluzione(sud,soluzione);
				sol.setText("SOLUZIONE "+(soluzione+1)+" DI "+(sud.numSol-1));
				previous.setEnabled(true);
			}
			
			if(e.getSource()==reset) {
				for(int i=0;i<matriceSu.length;i++) 
					for(int c=0;c<matriceSu[0].length;++c) 
						for(int j=0;j<matriceSu[0][0].length;++j) 
							for(int m=0;m<matriceSu[0][0][0].length;++m)
								matriceSu[i][j][c][m].setText("");
				next.setEnabled(false);
				previous.setEnabled(false);
				soluzione=0;
				sol.setText("");
				salva.setEnabled(false);
			}
			
			if(e.getSource()==salva || e.getSource()==carica) {
				String nomefile=null;
				int valore;
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("File TXT", "txt");
				JFileChooser jfc = new JFileChooser();
				jfc.setFileFilter(filtro);
				if(e.getSource()==salva) valore = jfc.showSaveDialog(null);
				else valore = jfc.showOpenDialog(null);
				if(valore==JFileChooser.APPROVE_OPTION) {
					nomefile=jfc.getSelectedFile().getAbsolutePath();
					if(e.getSource()==salva) {
						PrintWriter pw;
						try {
							pw = new PrintWriter(new BufferedWriter(new FileWriter(nomefile)));
							for(int i=0;i<sud.griglia.length;++i) {
								for(int j=0;j<sud.griglia[0].length;++j) {
									if(sud.grigliaInput[i][j]==true) {
										pw.println(i);
										pw.println(j);
										pw.println(Integer.toString(sud.griglia[i][j]));
									}
								}
							}
							pw.close();
							JOptionPane.showMessageDialog(null,"Hai salvato il tuo operato sul file: " + nomefile);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null,"Qualcosa è andato storto..");
							e1.printStackTrace();
						}
					}
					
					else {
						reset.doClick();
						try {
							BufferedReader br = new BufferedReader(new FileReader(nomefile));
							String linea=null;
							cicloinf:for(;;) {
								int riga=0;
								int colonna=0;
								for(int i=0;i<3;i++) {
									linea=br.readLine();
									if(linea==null) break cicloinf;
									switch(i) {
									case 0: riga=Integer.parseInt(linea);break;
									case 1: colonna=Integer.parseInt(linea);break;
									case 2: matriceSu[riga/3][colonna/3][riga-riga/3*3][colonna-colonna/3*3].setText(linea);break;
									}
								}
							}
							br.close();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null,"Qualcosa è andato storto.. Il file potrebbe essere corrotto");
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}


}
