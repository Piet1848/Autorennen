
import java.awt.Point;
import java.util.ArrayList;

public class PlayGround{
	private int[][] playGround;
	private int numberPlayer = 0;
	private int round = 1;
	private ArrayList<Point>[] playerPos;
	private ArrayList<Point>[] playerVektor;
	private boolean[] playerOK;
	private int turn;

	private ArrayList<Integer> rank;

	public PlayGround(int width, int height) {
		playGround = new int[width][height];
		rank = new ArrayList<Integer>();
	}
	
	public PlayGround(int[][] pG) {
		playGround = pG;
		rank = new ArrayList<Integer>();
		initPlayer();
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

	public boolean moveChoosed(int value) {	//called by Anzeige
		return moveVektor(getVektor(value));
	}

	public boolean moveVektor(Point vektor) {	//called by Ki
		if(numberPlayer == 0) {
			initPlayer();
		}
		boolean alive = true;
		int vX = vektor.x + playerVektor[turn].get(round-1).x;
		int vY = vektor.y + playerVektor[turn].get(round-1).y;

		int x = playerPos[turn].get(round-1).x + vX;
		int y = playerPos[turn].get(round-1).y + vY;
		if(x>=0 && y>=0 && x<playGround.length && y<playGround[x].length && playGround[x][y] != 0) {	
			playerVektor[turn].add(new Point(vX, vY));
			playerPos[turn].add(new Point(x,y));
			if(playGround[x][y] == 3) {
				rank.add(turn);
				playerOK[turn] = false;
				alive = false;
			}
		}else {
			playerOK[turn] = false;
			alive = false;
		}

		if(!alive || !checkDeath()) {
			boolean oneIn = false;
			for(int i = 0; i < numberPlayer; i++) {
				if(playerOK[i]) oneIn = true;
			}
			if(!oneIn) {
				return false;
			}
		}
		do{
			turn++;
			if(turn >= numberPlayer) {
				turn = 0;
				round++;
			}
		}while(!playerOK[turn]);
		return true;
	}

	@SuppressWarnings("unchecked")
	private void initPlayer() {	//sets one player on the first start field
		numberPlayer = 1;
		int i = 0;
		int j = 0;
		while(playGround[i][j] != 2) {		// Attention: Error out of bounce if there is no start field
			j++;
			if(j>= playGround[i].length) {
				j = 0;
				i++;
			}
		}
		playerPos = new ArrayList[numberPlayer];	//inittiallize playerPos and playerVektor
		playerPos[0] = new ArrayList<Point>();
		playerPos[0].add(new Point(i, j));
		playerVektor = new ArrayList[numberPlayer];
		playerVektor[0] = new ArrayList<Point>();
		playerVektor[0].add(new Point(0,0));
		playerOK = new boolean[numberPlayer];
		playerOK[0] = true;
	}

	private boolean checkDeath() {
		for(int i = 1; i <= 9; i++) {
			Point v = getVektor(i);
			int xPlayer = playerPos[turn].get(round).x + v.x + playerVektor[turn].get(round).x;
			int yPlayer = playerPos[turn].get(round).y + v.y + playerVektor[turn].get(round).y;
			if(xPlayer >= 0 && yPlayer >= 0 && xPlayer < playGround.length && yPlayer < playGround[0].length) {
				if(playGround[xPlayer][yPlayer] != 0) {
					//				if(playGround[xPlayer][yPlayer] == 3) {	//not needed anymore.
					//					rank.add(turn);
					//					playerOK[turn] = false;
					//					return false;
					//				}
					return true;
				}
			}
		}
		playerOK[turn] = false;
		return false;
	}

	public void setPlayer(ArrayList<Point>[] playerPos2, ArrayList<Point>[] playerVektor2, boolean[] playerOK2) {//receave Player Pos from Anzeige
		playerPos = playerPos2;
		playerVektor = playerVektor2;
		playerOK = playerOK2;
		numberPlayer = playerPos.length;
	}

	public ArrayList<Point>[] getPlayerPos(){
		return playerPos;
	}

	public ArrayList<Point>[] getPlayerVektor(){
		return playerVektor;
	}

	public boolean[] getPlayerOk() {
		return playerOK;
	}

	public int[][] getPlayGround() {
		return playGround;
	}

	public int getTurn() {
		return turn;
	}

	public int getRound() {
		return round;
	}

	public ArrayList<Integer> getRank() {
		return rank;
	}
}
