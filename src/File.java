import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class File {
	private int[][] pG;
	private int numberNodes, numberWeights, numberSNodes, numberENodes;
	private double[] weights;
	private int[][] fromTo;
	private ArrayList<Point> bestMoves;
	
	public File() {
		
	}
	
	public void getData(String link) { //wird aufgerufen
		String[] text = openText(link);
		if(text != null) {
			if(text[0].equals("pG")) {
				createPG(text);
			}else if(text[0].equals("W")) {
				createWeights(text);
			}else {
				System.out.print("Wrong Textfile");
			}
		}
	}
	
	private void createWeights(String[] text) { 	//Text in ein Neuronales netz umwandeln
		numberNodes = Integer.parseInt(text[1]);
		numberWeights = Integer.parseInt(text[2]);
		numberSNodes = Integer.parseInt(text[3]);	//number of Inputs (Start)
		numberENodes = Integer.parseInt(text[4]);	//number of Outputs (Exit)
		weights = new double[numberWeights];
		fromTo = new int[numberWeights][2];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = Double.parseDouble(text[5+i]); 
		}
		
		for(int i = 0; i < numberWeights; i++) {
			fromTo[i][0] = Integer.parseInt(text[2*i+numberWeights+5]);
			fromTo[i][1] = Integer.parseInt(text[2*i+numberWeights+6]);
		}
	}

	private void createPG(String[] text) {	//ein Spielbrett aus dem Text machen
		pG = new int[Integer.parseInt(text[1])][Integer.parseInt(text[2])];
		createBestMoves(text[3]);
		int counter = 4;
		for(int i = 0; i < pG.length; i++) {
			for(int j = 0; j < pG[i].length; j++) {
				pG[i][j] = Integer.parseInt(text[counter]);
				counter++;
			}
		}
	}
	
	private void createBestMoves(String text) {
		bestMoves = new ArrayList<Point>();
		char[] c = text.toCharArray();
		for(int i = 0; i < c.length; i++) {
			bestMoves.add(getVektor(Character.getNumericValue(c[i])));
		}
	}
	
	private Point getVektor(int value) {
		Point vektor = new Point();
		switch(value) {
		case(1):
			vektor.x = -1;
		vektor.y = 1;
		break;
		case(2):
			vektor.x = 0;
		vektor.y = 1;
		break;
		case(3):
			vektor.x = 1;
		vektor.y = 1;
		break;
		case(4):
			vektor.x = -1;
		vektor.y = 0;
		break;
		case(5):
			vektor.x = 0;
		vektor.y = 0;
		break;
		case(6):
			vektor.x = 1;
		vektor.y = 0;
		break;
		case(7):
			vektor.x = -1;
		vektor.y = -1;
		break;
		case(8):
			vektor.x = 0;
		vektor.y = -1;
		break;
		case(9):
			vektor.x = 1;
		vektor.y = -1;
		break;

		}
		return vektor;
	}

//	public String[] openText(String link) {	//text öffnen zum einlesen von Daten
//		FileReader fr;
//		String[] text;
//		try {
//			fr = new FileReader(link);
//			BufferedReader br = new BufferedReader(fr);
//			Stream<String> stream = br.lines();
//			
//			Object[] sObject = stream.toArray();
//			text = new String[sObject.length];
//			for(int i = 0; i < sObject.length; i++) {
//				text[i] += sObject[i];
//			}
//			//text = (String[]) stream.toArray();
//			br.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return text;
//	}
	
	public String[] openText(String link) {	//text öffnen zum einlesen von Daten
		FileReader fr;
		String[] text;
		Scanner scanner;
		try {
			scanner = new Scanner(new FileReader(link));
			ArrayList<String> inputArrayList = new ArrayList<>();
			while(scanner.hasNext()) {
				inputArrayList.add(scanner.next());
			}
			scanner.close();
			text = new String[inputArrayList.size()];
			for(int i = 0; i < text.length; i++) {
//				System.out.println("input: " + inputArrayList.get(i));
				text[i] = inputArrayList.get(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return text;
	}
	
	public void delete(String link) {
		java.io.File file = new java.io.File(link);
		file.delete();
	}
		
	public int[][] getPG(){
		return pG;
	}
	
	public ArrayList<Point> getBestMoves() {
		return bestMoves;
	}
	
	public int getNumberNodes() {
		return numberNodes;
	}

	public int getNumberSWeights() {
		return numberSNodes;
	}

	public int getNumberEWeights() {
		return numberENodes;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	public int[][] getFromTo(){
		return fromTo;
	}
}
