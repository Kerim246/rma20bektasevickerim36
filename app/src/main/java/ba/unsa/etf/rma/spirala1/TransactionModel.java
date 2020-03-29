package ba.unsa.etf.rma.spirala1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARPAYMENT;


public final class TransactionModel {
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    static {
        transactions.add(new Transaction(LocalDate.of(2020,3,10),150,"Prva",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,3,8),70,"Druga",REGULARPAYMENT,"Ma hajte molim vas",10,LocalDate.of(2020,11,10)));
        transactions.add(new Transaction(LocalDate.of(2020,4,16),40,"Treca",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,5,11),30,"Cetvrta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,3,12),250,"Peta",REGULARINCOME,null,10,LocalDate.of(2020,10,10)));
        transactions.add(new Transaction(LocalDate.of(2020,4,9),350,"Sesta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,3,8),20,"Sedma",REGULARPAYMENT,"Ne Znam sta da kazem",10,LocalDate.of(2020,9,10)));
        transactions.add(new Transaction(LocalDate.of(2020,6,7),30,"Osma",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,7,6),40,"Deveta",REGULARINCOME,null,10,LocalDate.of(2020,8,10)));
        transactions.add(new Transaction(LocalDate.of(2020,6,10),45,"Deseta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,7,8),55,"Jedanaesta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,5,16),60,"Dvanaeasta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,7,11),5,"Trinaeasta",INDIVIDUALINCOME,null,0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,12),10,"Cetrnaesta",REGULARINCOME,null,10,LocalDate.of(2020,8,10)));
        transactions.add(new Transaction(LocalDate.of(2020,3,9),11,"Petnaesta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,8),13,"Sesnaesta",INDIVIDUALINCOME,null,0,null));
        transactions.add(new Transaction(LocalDate.of(2020,5,7),77,"Sedamnaesta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,3,11),82,"Osamnaesta",INDIVIDUALINCOME,null,0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,6),79,"Devetnaesta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",10,null));
        transactions.add(new Transaction(LocalDate.of(2020,6,6),54,"Dvadeseta",INDIVIDUALINCOME,null,0,null));
    }


    public static ArrayList<Transaction> getTransactions(){
        return transactions;
    }
}