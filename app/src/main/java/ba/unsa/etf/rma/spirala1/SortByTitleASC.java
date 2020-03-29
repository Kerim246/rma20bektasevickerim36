package ba.unsa.etf.rma.spirala1;

import java.util.Comparator;

public class SortByTitleASC implements Comparator<Transaction> {
    public int compare(Transaction a, Transaction b)
    {
        return a.getTitle().compareTo(b.getTitle());
    }
}
