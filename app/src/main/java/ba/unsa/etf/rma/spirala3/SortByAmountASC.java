package ba.unsa.etf.rma.spirala3;

import java.util.Comparator;

public class SortByAmountASC implements Comparator<Transaction> {
    public int compare(Transaction a, Transaction b)
    {
        return a.getAmount()-b.getAmount();
    }
}
