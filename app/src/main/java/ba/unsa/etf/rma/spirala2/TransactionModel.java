package ba.unsa.etf.rma.spirala2;

import java.time.LocalDate;
import java.util.ArrayList;

import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARPAYMENT;


public class TransactionModel {
    public static ArrayList<Transaction> transactions = new ArrayList<Transaction>();


    static {
        transactions.add(new Transaction(LocalDate.of(2020,3,12),150,"Prva",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,3,8),70,"Druga",REGULARPAYMENT,"Ma hajte molim vas",12,LocalDate.of(2020,11,10)));
        transactions.add(new Transaction(LocalDate.of(2020,6,16),40,"Treca",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,11),30,"Cetvrta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,2,12),250,"Peta",REGULARINCOME,null,13,LocalDate.of(2020,10,10)));
        transactions.add(new Transaction(LocalDate.of(2020,4,9),140,"Sesta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,8),20,"Sedma",REGULARPAYMENT,"Ne Znam sta da kazem",10,LocalDate.of(2020,9,10)));
        transactions.add(new Transaction(LocalDate.of(2020,4,7),30,"Osma",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,7,6),340,"Deveta",REGULARINCOME,null,15,LocalDate.of(2020,8,10)));
        transactions.add(new Transaction(LocalDate.of(2020,6,10),45,"Deseta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,7,8),55,"Jedanaesta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,5,16),60,"Dvanaeasta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,7,11),5,"Trinaeasta",INDIVIDUALINCOME,null,0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,12),10,"Cetrnaesta",REGULARINCOME,null,11,LocalDate.of(2020,8,10)));
        transactions.add(new Transaction(LocalDate.of(2020,3,9),11,"Petnaesta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,8),13,"Sesnaesta",INDIVIDUALINCOME,null,0,null));
        transactions.add(new Transaction(LocalDate.of(2020,5,7),77,"Sedamnaesta",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,3,11),82,"Osamnaesta",INDIVIDUALINCOME,null,0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,6),79,"Devetnaesta",INDIVIDUALPAYMENT,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,6,6),54,"Dvadeseta",INDIVIDUALINCOME,null,0,null));

        transactions.add(new Transaction(LocalDate.of(2020,5,24),1,"Dvadesetprv",REGULARINCOME,null,12,LocalDate.of(2020,7,11)));
        transactions.add(new Transaction(LocalDate.of(2020,4,7),25,"Dvadesetdruga",REGULARPAYMENT,"Ne Znam sta da kazem",0,LocalDate.of(2020,6,3)));
        transactions.add(new Transaction(LocalDate.of(2020,3,11),44,"DvadesetTreca",PURCHASE,"Ne Znam sta da kazem",0,null));
        transactions.add(new Transaction(LocalDate.of(2020,4,13),22,"DvadesetCet",REGULARPAYMENT,"Ne Znam sta da kazem",0,LocalDate.of(2020,5,3)));
        transactions.add(new Transaction(LocalDate.of(2020,5,6),88,"DvadesetPet",INDIVIDUALINCOME,null,0,null));


    }


    public static ArrayList<Transaction> getTransactions(){
        return transactions;
    }


}
