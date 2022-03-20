package DSCoinPackage;
import java.util.*;
import HelperClasses.*;
public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
        String start="100000";
        Members[] people;
        people=DSObj.memberlist;
        int c=0;
        int i=0;
        List<Transaction>temp=new ArrayList<Transaction>();
        while(c<coinCount){
            i=i%(people.length);       
            Transaction t = new Transaction();
            t.coinsrc_block=null;
            t.Destination=people[i];
            t.coinID=start;
            DSObj.latestCoinID=start;
            Members mod=new Members();
            mod.UID="Moderator";
            t.Source=mod;
            temp.add(t);
            i++;
            c++;
            int num=Integer.parseInt(start);
            num+=1;
            start = Integer.toString(num);
        }
        c=0;
        i=0;
        start="100000";
        for ( i=0;i<temp.size() ; i++) {
             DSObj.pendingTransactions.AddTransactions(temp.get(i));
        }
        
        i=0;

        while(DSObj.pendingTransactions .size()!=0){
                Transaction[] arr=new Transaction[DSObj.bChain.tr_count];
                for (int j=0;j<DSObj.bChain.tr_count ;j++ ){
                    Transaction p=new Transaction();
                    try{
                        p=DSObj.pendingTransactions.RemoveTransaction();
                    }catch(EmptyQueueException e){}
                    arr[j]=p;
                }
            TransactionBlock newelem=new TransactionBlock(arr);
           
            c=0;
            while(c<arr.length){
                    i=i%(people.length);
                    Pair<String, TransactionBlock>p=new Pair<String, TransactionBlock>(start,newelem);
                    people[i].mycoins.add(p);
                    i++;
                    c++;
                    int num=Integer.parseInt(start);
                    num+=1;
                    start = Integer.toString(num);
        }
        DSObj.bChain.InsertBlock_Honest(newelem);
    }
       
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
               String start="100000";
        Members[] people;
        people=DSObj.memberlist;
        int c=0;
        int i=0;
        List<Transaction>temp=new ArrayList<Transaction>();
        while(c<coinCount){
            i=i%(people.length);       
            Transaction t = new Transaction();
            t.coinsrc_block=null;
            t.Destination=people[i];
            t.coinID=start;
            DSObj.latestCoinID=start;
            Members mod=new Members();
            mod.UID="Moderator";
            t.Source=mod;
            temp.add(t);
            i++;
            c++;
            int num=Integer.parseInt(start);
            num+=1;
            start = Integer.toString(num);
        }
        c=0;
        i=0;
        start="100000";
        for ( i=0;i<temp.size() ; i++) {
             DSObj.pendingTransactions.AddTransactions(temp.get(i));
        }
        
        i=0;

        while(DSObj.pendingTransactions .size()!=0){
                Transaction[] arr=new Transaction[DSObj.bChain.tr_count];
                for (int j=0;j<DSObj.bChain.tr_count ;j++ ){
                    Transaction p=new Transaction();
                    try{
                        p=DSObj.pendingTransactions.RemoveTransaction();
                    }catch(EmptyQueueException e){}
                    arr[j]=p;
                }
            TransactionBlock newelem=new TransactionBlock(arr);
           
            c=0;
            while(c<arr.length){
                    i=i%(people.length);
                    Pair<String, TransactionBlock>p=new Pair<String, TransactionBlock>(start,newelem);
                    people[i].mycoins.add(p);
                    i++;
                    c++;
                    int num=Integer.parseInt(start);
                    num+=1;
                    start = Integer.toString(num);
        }
        DSObj.bChain.InsertBlock_Malicious(newelem);
    }
}
}
    