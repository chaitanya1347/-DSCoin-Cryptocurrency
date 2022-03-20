package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {

        if(this.firstTransaction==null){
          
            this.firstTransaction=transaction; 
            this.lastTransaction=transaction;

        }else{
            this.lastTransaction.next=transaction;
           
            this.lastTransaction=transaction;
        }

        numTransactions++;
        return;
  }
  
  public Transaction RemoveTransaction() throws EmptyQueueException {
      Transaction temp =firstTransaction;
      if(this.firstTransaction==null){
        throw new EmptyQueueException();
      }else{
          this.firstTransaction=this.firstTransaction.next;
       
      }
      numTransactions--;
      return temp;
    
  }

  public int size() {
      return this.numTransactions;
  }
}