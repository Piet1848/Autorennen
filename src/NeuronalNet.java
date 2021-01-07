
public class NeuronalNet {
	private String link = "...";
	
	private int numberNodes, numberSNodes, numberENodes;
	private double[] weights;
	private int[][] fromTo;
	
	private File fileReader;
	
	public NeuronalNet(String nLink) {
		link = nLink;
		fileReader = new File();
		fileReader.getData(link);
		init();
	}
	
	private boolean init() {
		numberNodes = fileReader.getNumberNodes();
		numberSNodes = fileReader.getNumberSWeights();
		numberENodes = fileReader.getNumberEWeights();
		weights = fileReader.getWeights();
		fromTo = fileReader.getFromTo();
		return false;
	}
	
	public double[] calculateNN(double[] input) { //caluclates the output depending on the input
		double[] nodes = new double[numberNodes];
		double[] output = new double[numberENodes];
		
		for(int i = 0; i < input.length; i++) {
			nodes[i] = input[i]; //input should be between 0 and 1
		}
		
		int startLayer = input.length;
		for(int i = 0; i < weights.length; i++) {
			if(fromTo[i][0] == startLayer) {
				nodes = sigmoidNodes(nodes, startLayer, fromTo[i][1]-1);
				startLayer = fromTo[i][1];
			}
			nodes[fromTo[i][1]] += weights[i] * nodes[fromTo[i][0]];
		}
		
//		int toOutput = input.length - numberENodes;
//		for(int i = numberENodes-1; i < input.length; i++) {
//			output[i] = 1/(1+Math.exp(-1*nodes[i+toOutput]));	//Sigmoidfunktion
//		}
		
		int toOutput = input.length - numberENodes;
		for(int i = 0; i < numberENodes; i++) {
			output[i] = 1./(1+Math.exp(-1.*nodes[i+toOutput]));	//Sigmoidfunktion
		}
		
		return output;
	}
	
	private double[] sigmoidNodes(double[] nodes,int startNode, int endNode) {
		for(int i = startNode; i <= endNode; i++) {
			nodes[i] = 1/(1+Math.exp(-1*nodes[i]));	//Sigmoidfunktion
		}
		return nodes;
	}

	public void setWeights(double[] nWeights) {	//to change weights
		weights = nWeights;
	}
	
	public int getNumberNodes() {
		return numberNodes;
	}
	
	public int getNumberSNodes() {
		return numberSNodes;
	}
	
	public int getNumberENodes() {
		return numberENodes;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	public int[][] getFromTo(){
		return fromTo;
	}
}
