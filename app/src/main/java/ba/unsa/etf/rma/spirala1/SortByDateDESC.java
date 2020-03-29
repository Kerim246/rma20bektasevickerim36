package ba.unsa.etf.rma.spirala1;

import java.util.Comparator;

public class SortByDateDESC implements Comparator<Transaction> {
    public int compare(Transaction a, Transaction b)
    {
        return b.getDate().compareTo(a.getDate());
    }
}
