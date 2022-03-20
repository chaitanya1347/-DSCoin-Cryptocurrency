package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {

      CRF obj =new CRF(64);
      String s="1000000001";
      if(lastBlock==null){
            while(true){
              String comp=obj.Fn(start_string + "#" + newBlock.trsummary+ "#"+ s);

              if((comp.substring(0,4)).compareTo("0000")==0){
                  newBlock.nonce = s;
                  break;
              }
              int num=Integer.parseInt(s);
              num+=1;
              s = Integer.toString(num);
            }

          newBlock.dgst=obj.Fn(start_string +"#" + newBlock.trsummary + "#"+ newBlock.nonce);
          lastBlock=newBlock;
      }else{

            while(true){
              String comp=obj.Fn(lastBlock.dgst+ "#" + newBlock.trsummary+ "#"+ s);
              if((comp.substring(0,4)).compareTo("0000")==0){
                  newBlock.nonce = s;
                  break;
              }
              int num=Integer.parseInt(s);
              num+=1;
              s = Integer.toString(num);
            }
         newBlock.dgst=obj.Fn(lastBlock.dgst +"#" + newBlock.trsummary + "#"+ newBlock.nonce);
         newBlock.previous=lastBlock;
         lastBlock=newBlock;
        }
    }
}
