import java.util.Random;

class Partita {
	private int balance;
	private static Partita i = null;
	private int randomNumber;
	private boolean started;

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
		randomNumber = rand.nextInt(128);
		started = true;
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

	public boolean isStarted() {
		return started;
	}

	public void stop() {
		started = false;
	}
}
