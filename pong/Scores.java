package pong;

public class Scores {

	private int scoreTop, scoreBottom;
	
	public Scores() {
		resetState();
	}
	
	public void resetState() {
		scoreTop = 0;
		scoreBottom = 0;
	}

	public void bottomScores() {
		++ scoreBottom;
	}
	
	public void topScores() {
		++ scoreTop;
	}
	
	public int getScoreTop() {
		return scoreTop;
	}
	
	public int getScoreBottom() {
		return scoreBottom;
	}
	
}