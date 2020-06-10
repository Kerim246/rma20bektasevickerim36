package ba.unsa.etf.rma.spirala3;

import android.content.Context;

interface IMovieDetailInteractor {
    Transaction getTransaction(Context context, Integer id);

}
