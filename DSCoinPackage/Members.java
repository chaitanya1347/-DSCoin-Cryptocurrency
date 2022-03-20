package DSCoinPackage;

import java.util.*;
import HelperClasses.*;

class SortFirst implements Comparator<Pair<String, TransactionBlock>> {
 public int compare(Pair<String, TransactionBlock> p1, Pair<String, TransactionBlock> p2) {
  return Integer.valueOf(p1.get_first()) - Integer.valueOf(p2.get_first());
 }
}

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans=new Transaction[100];


public static boolean isValid(Transaction t, DSCoin_Honest DSObj) {
    TransactionBlock tB = t.coinsrc_block;
    boolean flag1 = false;
    if (tB != null) {
     for (int i = 0; i < tB.trarray.length; i++) {
        Transaction t1 = tB.trarray[i];
        if ((t1.coinID.equals(t.coinID)) && (t1.Destination == t.Source)) {
         flag1 = true;
        }
     }
  }

  boolean flag2 = true;
  TransactionBlock curr = DSObj.bChain.lastBlock;
  while (curr != t.coinsrc_block) {
   for (int i = 0; i < curr.trarray.length; i++) {
      if (curr.trarray[i].coinID.equals(t.coinID)) {
       flag2 = false;
       break;
      }
   }
       if (flag2 == true) {
        curr = curr.previous;
       } else {
        break;
       }
  }
  return (flag2 && flag1);
 }



 public static boolean isValid(Transaction t, DSCoin_Malicious DSObj) {

  TransactionBlock tB = t.coinsrc_block;
  boolean flag1 = false;
  if (tB != null) {
   for (int i = 0; i < tB.trarray.length; i++) {
    Transaction t1 = tB.trarray[i];
      if ((t1.coinID.equals(t.coinID)) && (t1.Destination == t.Source)) {
       flag1 = true;
      }
  }
}

  boolean flag2 = true;
  TransactionBlock curr = DSObj.bChain.FindLongestValidChain ();
    while (curr != t.coinsrc_block) {

         for (int i = 0; i < curr.trarray.length; i++) {
              if (curr.trarray[i].coinID.equals(t.coinID)) {
               flag2 = false;
               break;
              }
         }
       
        if (flag2 == true) {
            curr = curr.previous;
           } else {
            break;
        }
    }
  return (flag2 && flag1);
 }

  public List<Pair<String,String>> QueryDocument(int doc_idx,TreeNode rootnode,int numdocs){
                List<Pair<String,String>>list=new ArrayList<Pair<String,String>>();
                Queue<TreeNode>q=new LinkedList<TreeNode>();
                q.add(rootnode);

                while(q.size()!=numdocs){
                        TreeNode temp=q.peek();
                        q.add(temp.left);
                        q.add(temp.right);
                        q.poll();
                       
                }
                TreeNode start=new TreeNode();
                for(int i=0;i<numdocs;i++){    
                        if(i==doc_idx){
                                start=q.peek();
                                break;
                        }
                        q.poll();
                }
                while(true){
                        TreeNode ances=start.parent;
                        if(ances==null){
                                Pair<String,String> pair= new Pair<String,String>(start.val,null);
                                list.add(pair);
                                break;
                        }else{
                        Pair<String,String> pair= new Pair<String,String>(ances.left.val,ances.right.val);
                        list.add(pair);
                        start=ances;
                }
        }
                return list;
}


  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
          Pair<String,TransactionBlock> temp;
          temp=this.mycoins.get(0);
          this.mycoins.remove(0);
          Transaction tobj=new Transaction();
          tobj.coinsrc_block=temp.get_second();
          tobj.coinID=temp.get_first();
          tobj.Source=this;
          for (int i=0;i<DSobj.memberlist.length ;i++ ) {
             if(DSobj.memberlist[i].UID==destUID){
                tobj.Destination=DSobj.memberlist[i];
                break;
             }
           } 
          for(int i=0;i<in_process_trans.length;i++){
             
              if(in_process_trans[i]==null){
                in_process_trans[i]=tobj;
                break;
              }
          }
          DSobj.pendingTransactions.AddTransactions(tobj);

  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    boolean f=false;
    BlockChain_Honest chain=DSObj.bChain;
    TransactionBlock tB=chain.lastBlock;
    int i=0;
    while(tB!=null){
      
      for(i=0;i<tB.trarray.length;i++){
        if(tB.trarray[i]==tobj){
          f=true;
          break;
        }
      }
      if(f==true) break;
      tB=tB.previous;
    }
    if(f==true){
        MerkleTree t= tB.Tree;
        List<Pair<String,String>>out1=new ArrayList<Pair<String, String>>();
         
        out1 =QueryDocument(i,t.rootnode,t.numdocs);

  
        List<Pair<String,String>>out2=new ArrayList<Pair<String, String>>();
        TransactionBlock temp=chain.lastBlock;
        while(temp!=tB.previous){
          Pair<String,String>ad=new Pair<String,String>(temp.dgst,temp.previous.dgst + "#" + temp.trsummary + "#" + temp.nonce);
          out2.add(ad);
          temp=temp.previous;
        }
        Pair<String,String>ad=new Pair<String,String>(tB.previous.dgst,null);
        out2.add(ad);
        Collections.reverse(out2);
        for (int j=0; j<in_process_trans.length;j++) {
            if(in_process_trans[j]==tobj){
              in_process_trans[j]=null;
            }
        }
        Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock>(tobj.coinID,tB);
        tobj.Destination.mycoins.add(p);
        Collections.sort( tobj.Destination.mycoins,new SortFirst());

        Pair<List<Pair<String, String>>, List<Pair<String, String>>> ans=new  Pair<List<Pair<String, String>>, List<Pair<String, String>>>(out1,out2);

        return ans;
        
    }else{
      throw new MissingTransactionException();
    }
  }

  public void MineCoin(DSCoin_Honest DSObj) {
      int c=DSObj.bChain.tr_count-1;
      List<Transaction>temp=new ArrayList<Transaction>();

      while(temp.size()!=c) {
        Transaction p=new Transaction();
        try{
            p=DSObj.pendingTransactions.RemoveTransaction();
        }catch(EmptyQueueException e){
          break;
        }

        int i=0;
        for (i=0;i<temp.size() ; i++) {
            if(p.coinID.compareTo(temp.get(i).coinID)==0) break;
        }

      if(isValid(p,DSObj)==true){
        if(i==temp.size()) temp.add(p);
      }
    }

      Transaction minerRewardTransaction=new Transaction();
      String s=DSObj.latestCoinID;
      int num=Integer.parseInt(s);
      num+=1;
      s = Integer.toString(num);

      minerRewardTransaction.coinID=s;
      minerRewardTransaction.Source=null;
      minerRewardTransaction.coinsrc_block=null;
      minerRewardTransaction.Destination=this;
      temp.add(minerRewardTransaction);
      Transaction[] t= new Transaction[temp.size()];
      for (int i=0;i<temp.size() ; i++) {
        t[i]=temp.get(i);
       
      }
      TransactionBlock blo=new TransactionBlock(t);
      DSObj.bChain.InsertBlock_Honest(blo);

      Pair<String,TransactionBlock>p=new Pair<String, TransactionBlock>(s,blo);
      this.mycoins.add(p);
      DSObj.latestCoinID=s;
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
      int c=DSObj.bChain.tr_count-1;
      List<Transaction>temp=new ArrayList<Transaction>();
      while(temp.size()!=c) {
        
        Transaction p=new Transaction();
        try{
            p=DSObj.pendingTransactions.RemoveTransaction();
        }catch(EmptyQueueException e){ 
          break;
        }


      if(isValid(p,DSObj)==true){
        int i=0;

        if(temp.size()==0){
          temp.add(p);
        }else{
              for (i=0;i<temp.size() ; i++) {
                  if(p.coinID.compareTo(temp.get(i).coinID)==0) break;
              }
              if(i==temp.size()) temp.add(p);
        }
      }
  }

      Transaction minerRewardTransaction=new Transaction();
      String s=DSObj.latestCoinID;
      int num=Integer.parseInt(s);
      num+=1;
      s = Integer.toString(num);

      minerRewardTransaction.coinID=s;
      minerRewardTransaction.Source=null;
      minerRewardTransaction.coinsrc_block=null;
      minerRewardTransaction.Destination=this;
      temp.add(minerRewardTransaction);
      
      Transaction[] t= new Transaction[temp.size()];
      for (int i=0;i<temp.size() ; i++) {
        t[i]=temp.get(i);
       
      }
      TransactionBlock blo=new TransactionBlock(t);
      DSObj.bChain.InsertBlock_Malicious(blo);

      Pair<String,TransactionBlock>p=new Pair<String, TransactionBlock>(s,blo);
      this.mycoins.add(p);
      DSObj.latestCoinID=s;
  }  
}
