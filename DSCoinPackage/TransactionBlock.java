package DSCoinPackage;

import HelperClasses.*;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

TransactionBlock(Transaction[] t) {
    trarray = new Transaction[t.length];
    for (int i = 0; i < t.length; i++) {
      trarray[i] = t[i];
    }
    previous = null;
    MerkleTree T = new MerkleTree();
    this.trsummary = T.Build(trarray);
    this.Tree = T;
    dgst = null;
  }


public boolean checkTransaction (Transaction t) {
    TransactionBlock curr = this;
    if (t.coinsrc_block == null) {
      return true;
    }
    while (curr != t.coinsrc_block) {
      for (int i = 0; i < curr.trarray.length; i++) {
        if (curr.trarray[i].coinID.equals(t.coinID)) {
          return false;
        }
      }
      curr = curr.previous;
    }
    for (int i = 0; i < curr.trarray.length; i++) {
      Transaction t1 = curr.trarray[i];
      if ((t1.coinID.equals(t.coinID)) && (t1.Destination == t.Source) ) {
        return true;
      }
    }
    return false;
  }
}
