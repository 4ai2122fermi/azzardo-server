class Partita {
	private int balance;
	private static Partita i = null;
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
	}

	public String bet(int stake, int number) {
		return "WIN,100";
	}
}
