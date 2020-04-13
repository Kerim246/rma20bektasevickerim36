package ba.unsa.etf.rma.spirala2;

import java.util.Comparator;

public class SortByAmountDESC implements Comparator<Transaction> {
    public int compare(Transaction a, Transaction b)
    {
        return b.getAmount()-a.getAmount();
    }
}
