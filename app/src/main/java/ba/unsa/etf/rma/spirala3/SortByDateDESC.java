package ba.unsa.etf.rma.spirala3;

import java.util.Comparator;

public class SortByDateDESC implements Comparator<Transaction> {
    public int compare(Transaction a, Transaction b)
    {
        return b.getDate().compareTo(a.getDate());
    }
}
