import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFile {
	private FileWriter fWriter;
	private String link;

	public WriteFile(String nLink) {
		link = nLink;
		try {
			fWriter = new FileWriter(link+".txt", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public WriteFile() {
		
	}

	public void saveNN(NeuronalNet nN, String link) {
		try {
			fWriter = new FileWriter(link, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fWriter.write("W\n");
			fWriter.write(Integer.toString(nN.getNumberNodes()) + "\n");
			double[] weights = nN.getWeights();
			fWriter.write(Integer.toString(weights.length) + "\n");
			fWriter.write(Integer.toString(nN.getNumberSNodes()) + "\n");
			fWriter.write(Integer.toString(nN.getNumberENodes()) + "\n");
			for(int i = 0; i < weights.length; i++) {
				fWriter.write(Double.toString(weights[i]) + "\n");
			}
			int[][] fromTo = nN.getFromTo();
			for(int i = 0; i < fromTo.length; i++) {
				fWriter.write(Integer.toString(fromTo[i][0]) + "\n");
				fWriter.write(Integer.toString(fromTo[i][1]) + "\n");
			}
			fWriter.flush();
			fWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveText(String[] text) {
		try {
			for(int i = 0; i < text.length; i++) {
				fWriter.write(text[i]+ "\n");
			}
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePG(int[][] playGround, ArrayList<Point> playerVektor) {
		try {
			fWriter.write("pG\n");
			fWriter.write(Integer.toString(playGround.length) + "\n");
			fWriter.write(Integer.toString(playGround[0].length) + "\n");
			fWriter.write(toString(playerVektor) + "\n");
			for(int i = 0; i < playGround.length; i++) {
				for(int j = 0; j < playGround[i].length; j++) {
					fWriter.write(Integer.toString(playGround[i][j]) + "\n");
				}
			}
			fWriter.flush();
			fWriter.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String toString(ArrayList<Point> playerVektor) {
		String text = "";
		for(int i = 1; i < playerVektor.size(); i++) {
			int x = playerVektor.get(i).x - playerVektor.get(i-1).x;
			int y = playerVektor.get(i).y - playerVektor.get(i-1).y;
			text += pointToInt(x , y);
		}
		return text;
	}

	private int pointToInt(int x, int y) {
		switch(x) {
		case(-1):
			switch (y) {
			case(-1):
				return 7;
			case(0):
				return 4;
			case(1):
				return 1;
			}
		break;
		case(0):
			switch (y) {
			case(-1):
				return 8;
			case(0):
				return 5;
			case(1):
				return 2;
			}
		break;
		case(1):
			switch (y) {
			case(-1):
				return 9;
			case(0):
				return 6;
			case(1):
				return 3;
			}
		break;
		}
		return -1;	//should never occour
	}

	public void add(String toAdd) {
		try {
			fWriter.append("\n" +toAdd);
			fWriter.flush();
			fWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//		File file = new File();
		//		String[] strings = file.openText(link);
		//		file.delete(link);
		//		
		//		try {
		//			for(int i = 0; i < strings.length; i++) {
		//				fWriter.write(strings[i]+"\n");
		//			}
		//			fWriter.write(toAdd);
		//			fWriter.flush();
		//			fWriter.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
	}
}

