import java.util.Random;

class Partita {
	private int balance;
	private static Partita i = null;
	private int randomNumber;
	private Partita() {
		balance = 0;
	}

	public static Partita getInstance() {
		if (i == null)
			i = new Partita();
		return i;
	}

	public void start(int balance) {
		this.balance = balance;
		Random rand = new Random();
		randomNumber = rand.nextInt(0, 128);
	}

	public String bet(int stake, int number) {
		if(number == randomNumber) {
			if(stake*2 > balance) 
				return "NOMONEY";
				
			balance -= 2*stake;
			return "WIN," + Integer.toString(stake * 2);
		} else {
			balance += stake;
			return "LOST";
		}
	}
}
