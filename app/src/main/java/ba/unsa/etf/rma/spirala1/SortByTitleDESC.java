package ba.unsa.etf.rma.spirala1;

import java.util.Comparator;

public class SortByTitleDESC implements Comparator<Transaction> {
    public int compare(Transaction a, Transaction b)
    {
        return b.getTitle().compareTo(a.getTitle());
    }
}