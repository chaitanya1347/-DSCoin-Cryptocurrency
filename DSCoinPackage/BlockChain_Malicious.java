package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList=new TransactionBlock[100];

  public static boolean checkTransactionBlock (TransactionBlock tB) {
      String s1=tB.dgst;
      if((s1.substring(0,4)).compareTo("0000")!=0) return false;

      CRF obj =new CRF(64);
      String s2;
      if(tB.previous==null) s2= obj.Fn(start_string +"#" + tB.trsummary + "#"+ tB.nonce);
      else s2=obj.Fn(tB.previous.dgst +"#" + tB.trsummary + "#"+ tB.nonce);

      if(s2.compareTo(tB.dgst)!=0) return false;
 
      MerkleTree temp=new MerkleTree();
      Transaction[] arr=tB.trarray;
      String s3=temp.Build(arr);
      if(s3.compareTo(tB.trsummary)!=0) return false;

      if(tB.previous!=null){
      for(int i=0;i<arr.length;i++){
        if(tB.previous.checkTransaction(arr[i])==false) return false;
      }
    }
      return true;
  }

  public TransactionBlock FindLongestValidChain () {
      TransactionBlock temp=null;
      int maxi=-1;
      int x=0;
      while(lastBlocksList[x]!=null){
        x++;
      }
      if(x==0) return null;

      for (int i=0; i<x ; i++ ) {
        TransactionBlock trav=lastBlocksList[i];
        TransactionBlock store=lastBlocksList[i];
        int count=0;
        while(trav!=null){
            if(checkTransactionBlock(trav)==true){
              trav=trav.previous;
              count++;
            }else{
                trav=trav.previous;
                store=trav;   
                count=0;
            }
        }
            if(count>maxi){
                  temp=store;
                  maxi=count;
            }
      }
      return temp;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
      TransactionBlock last=FindLongestValidChain();
      String s="1000000001";
        CRF obj=new CRF(64);
        if(last==null){
          while(true){
  
                  String comp=obj.Fn(start_string+ "#" + newBlock.trsummary+ "#"+ s);
                  if((comp.substring(0,4)).compareTo("0000")==0){
                      newBlock.nonce = s;
                     
                      break;
                  }
                  int num=Integer.parseInt(s);
                  num+=1;
                  s = Integer.toString(num);
             
              }
              newBlock.dgst= obj.Fn(start_string+ "#" + newBlock.trsummary+ "#"+newBlock.nonce);
              newBlock.previous=last;
          }else{
                while(true){
                 
                        String comp=obj.Fn(last.dgst+ "#" + newBlock.trsummary+ "#"+ s);
                        if((comp.substring(0,4)).compareTo("0000")==0){
                            newBlock.nonce = s;
                            break;
                        }
                        int num=Integer.parseInt(s);
                        num+=1;
                        s = Integer.toString(num);
                  }
                   newBlock.dgst= obj.Fn(last.dgst+ "#" + newBlock.trsummary+ "#"+newBlock.nonce);
                   newBlock.previous=last;
              }
            boolean flag=true;
            for(int i=0;i<lastBlocksList.length;i++){
              if(last==lastBlocksList[i]){
                lastBlocksList[i]=newBlock;
                flag = false;
                break;
              }
            }
            if(flag==true){
              for(int q=0;q<lastBlocksList.length;q++){
                if(lastBlocksList[q]==null){
                  lastBlocksList[q]=newBlock;
                  break;
                }
              }
            }
              
    }
}
