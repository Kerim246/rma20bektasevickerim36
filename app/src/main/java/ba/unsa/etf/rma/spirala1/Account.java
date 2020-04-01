package ba.unsa.etf.rma.spirala1;

public class Account {
    public static int budget = 10000;
    public static int totalLimit = 5000;
    public static int monthLimit = 1000;

    public Account() {
    }

    public Account(int budget, int totalLimit, int monthLimit) {
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(int totalLimit) {
        this.totalLimit = totalLimit;
    }

    public int getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(int monthLimit) {
        this.monthLimit = monthLimit;
    }

}
