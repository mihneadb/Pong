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
		++ this.scoreBottom;
	}
	
	public void topScores() {
		++ this.scoreTop;
	}
	
	public int getScoreTop() {
		return this.scoreTop;
	}
	
	public int getScoreBottom() {
		return this.scoreBottom;
	}
	
}