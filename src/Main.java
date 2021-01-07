import javax.swing.JFrame;

public class Main{
	private JFrame graphic;
	private Anzeige o;
	private String[] links;
	private KI ki;
	private String neuronalNetLink = "neuronalNet";
	
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
//		createNN();
		init();
	}
	
	private void init() {
		getLinks("Links.txt");
		ki = new KI(neuronalNetLink + ".txt");
		ki.startLearning(links);
		graphic = new JFrame("Autorennen");
		graphic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o = new Anzeige(ki, links.length);
		graphic.add(o);
		graphic.setSize(1000, 1000); //TODO
		graphic.setVisible(true);
	}
	
	private void getLinks(String link) {
		File reader = new File();
		links = reader.openText(link);
		for(int i = 0; i < links.length; i++) {
			links[i] += ".txt";
		}
	}
	
	private void createNN() {	//to create the initial NN
		int weights = 2859770;
		String[] text = new String[8579316];
		text[0] = "W";
		text[1] = "3931"; 	//numberNodes
		text[2] = "" + weights;	//numberWeights
		text[3] = "2644";		//numberSNodes
		text[4] = "2"; 		//numberENodes
		for(int i = 5; i < weights+5; i++) {
			text[i] = "" + ((Math.random()-0.5)/40.);
		}
		int textCounter = weights+5;
		int counter = 0;
		for(int i = 0; i < 2644; i++) {
			for(int j = 0; j < 1000; j++) {
				text[textCounter++] = ""+i;
				text[textCounter++] = ""+(j+2644);
			}
		}
		counter = 2644;
		for(int i = 0; i < 1000; i++) {
			for(int j = 0; j < 200; j++) {
				text[textCounter++] = ""+(i+counter);
				text[textCounter++] = ""+(j+1000+counter);
			}
		}
		counter += 1000;
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 75; j++) {
				text[textCounter++] = ""+(i+counter);
				text[textCounter++] = ""+(j+200+counter);
			}
		}
		counter += 200;
		for(int i = 0; i < 75; i++) {
			for(int j = 0; j < 10; j++) {
				text[textCounter++] = ""+(i+counter);
				text[textCounter++] = ""+(j+75+counter);
			}
		}
		counter += 75;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 2; j++) {
				text[textCounter++] = ""+(i+counter);
				text[textCounter++] = ""+(j+10+counter);
			}
		}
		
		WriteFile writerFile = new WriteFile(neuronalNetLink);
		writerFile.saveText(text);
		System.out.println("Saved NN");
	}
}
