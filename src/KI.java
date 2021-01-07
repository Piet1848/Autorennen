import java.awt.Point;
import java.util.ArrayList;

import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JFrame;

public class KI {
	private PlayGround pGround;
	private NeuronalNet neuronalNet;
	private String link;
	JFrame graphic;
	Anzeige o;

	public KI(String nLink) {
		link = nLink;
		neuronalNet = new NeuronalNet(link);
	}

	public void startThatPGround(PlayGround nPGround) {	//player needs to be set
		pGround = nPGround;
		playGame();
	}

	private ArrayList<DPoint> playGame(){	//let the neuronalNet play 
		int round = pGround.getRound();
		if(round < 50) {
			Point playerPos = pGround.getPlayerPos()[0].get(round-1);
			Point playerVektor = pGround.getPlayerVektor()[0].get(round-1);
			int[][] playGround = pGround.getPlayGround();
			double[] input = new double[playGround.length*playGround[0].length+4];
			input[0] = playerPos.x/30.-0.5;
			input[1] = playerPos.y/22.-0.5;
			input[2] = playerVektor.x;
			input[3] = playerVektor.y;
			int counter = 4;
			for(int i = 0; i < playGround.length; i++) {
				for(int j = 0; j < playGround[i].length; j++) {
					double in; 
					switch (playGround[i][j]) {
					case 0:
						in = -1;
						break;
					case 1:
						in = 0.5;
						break;
					case 2:
						in = 0.5;
						break;
					case 3:
						in = 1;
						break;
					default:
						in = 0;
						break;
					}
					input[counter] = in;
					counter++;
				}
			}
			double[] output = neuronalNet.calculateNN(input);
			int x = 0;
			int y = 0;
			if(output[0] > 1/3) {
				x = 1;
			}else if(output[0] < -1/3) {
				x= -1;
			}
			if(output[1] > 1/3) {
				y = 1;
			}else if(output[1] < -1/3) {
				y= -1;
			}
			Point vektor = new Point(x, y);
			if(pGround.moveVektor(vektor)) {
				ArrayList<DPoint> result = playGame();
				result.add(new DPoint(output[0], output[1]));
				return result;
			}else {
				//			new Anzeige(pGround);	//display the moves

				ArrayList<DPoint> result = new ArrayList<>();
				result.add(new DPoint(output[0], output[1]));
				if(pGround.getRank().size() != 0) {
					result.add(0, null);	//to mark that the Ki finished the game
				}
				return result;
			}
		}else {
			ArrayList<DPoint> result = new ArrayList<>();
			result.add(0, null);
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public void startLearning(String[] links) {	//gets Links to Files with pGround
		System.out.println("StartLearning: " + System.currentTimeMillis());
		double mistake;
		int[][][] pG = new int[links.length][][];
		ArrayList<Point>[] optOutput = new ArrayList[links.length];
		File readerFile = new File();
		for(int i = 0; i < links.length; i++) {
			readerFile.getData(links[i]);
			pG[i] = readerFile.getPG();
			optOutput[i] = readerFile.getBestMoves();
		}
		int counter = 0;
		double faktor = 1.;
		do {
			mistake = 0;
			for(int i = 0; i < links.length; i++) {
//				System.out.println("Actual Link: " + links[i]);
				pGround = new PlayGround(pG[i]);
				ArrayList<DPoint> output = playGame();
				double nMistake = calculateMistake(output, optOutput[i]);
				if(nMistake == 0) {
					faktor *= 9999/10000;	//adjuste faktor, because Ai was perfekt
//					System.out.println("Perfekt AI at: " + i);
//					display(pGround);
				}else {
					trainNet(nMistake, faktor, optOutput[i]);
					mistake += nMistake;
				}
//				removeO();
			}
			counter++;
			System.out.print("Counter: " + counter + ", Mistake: " + mistake + ", TimeStamp: " + System.currentTimeMillis());
			save();	//saves the current neuronalNet
			}while(mistake/links.length > 0.1);
		System.out.println("Final Counter: " + counter + ", Mistake: " + mistake);
	}

	private void display(PlayGround pGround2) {
		graphic = new JFrame("Autorennen");
		graphic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o = new Anzeige(pGround2);
		graphic.add(o);
		graphic.setSize(1000, 1000); //TODO
		graphic.setVisible(true);
		o.repaint();
	}
	private void removeO() {
		if(graphic != null) {
			graphic.remove(o);
		}
	}

	private double calculateMistake(ArrayList<DPoint> output, ArrayList<Point> optOut) {
		double mistake = 0;
		if(output.get(0) == null) {	//AI finished
			if(output.size()-1 <= optOut.size()) {	//AI was better than training data
				/* 	Idea:
				 * optOut = output;
				 */
				return 0;
			}else {
				for(int i = 1; i < output.size(); i++) {
					mistake += Math.abs(output.get(i).x-(double) optOut.get(i-1).x);
					mistake += Math.abs(output.get(i).y-(double)optOut.get(i-1).y);
				}
				mistake /= output.size()-1.;
			}
		}else {
			if(output.size() <= optOut.size()) {
				for(int i = 0; i < output.size(); i++) {
					mistake += Math.abs(output.get(i).x-(double)optOut.get(i).x);
					mistake += Math.abs(output.get(i).y-(double)optOut.get(i).y);
				}
				mistake /= output.size();
			}else {
				for(int i = 0; i < optOut.size(); i++) {
					mistake += Math.abs(output.get(i).x-(double)optOut.get(i).x);
					mistake += Math.abs(output.get(i).y-(double)optOut.get(i).y);
				}
				mistake /= optOut.size();
			}
		}
		return mistake;
	}

	private void trainNet(double mistake, double fak, ArrayList<Point> optOutput) {
		double[] weights = neuronalNet.getWeights();
		double percentage = 0.0001;	//0.0001: ca 30min/save
		for(int i = weights.length-1; i >= 0; i--) {	//adjusts every weight form last to first
			if(Math.random() < percentage) {	//not changing every weight Just testing
				weights[i] += 0.5;
				neuronalNet.setWeights(weights);
				double nMistake = calculateMistake(playGame(), optOutput);
				weights[i] -= 0.5;
//				System.out.println("MistakeFaktor: " + (fak*(mistake-nMistake)));
				weights[i] += fak*(mistake-nMistake);	//adjust weight depending on the result change
//				System.out.println("Weights done: " + i);
			}
		}
	}
 
	private void save() {
		WriteFile writer = new WriteFile();
		writer.saveNN(neuronalNet, link);
		System.out.println(" Saved");
	}
}
