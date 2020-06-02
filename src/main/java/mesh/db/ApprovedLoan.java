package mesh.db;

/**
 *
 * @author tsowa
 */
public class ApprovedLoan {
  private Loan loan;
  private Double amount;
  private Integer date;
  private Double percent;
  private Double monthPayment;
  
  public ApprovedLoan(Loan loan, Double amount, Integer date, Double percent, Double monthPayment) {
    this.loan = loan;
    this.amount = amount;
    this.date = date;
    this.percent = percent;
    this.monthPayment = monthPayment;
  }

  public Loan getLoan() { return loan; }
  public Double getAmount() { return amount; }
  public Integer getDate() { return date; }
  public Double getPercent() { return percent; }
  public Double getMonthPayment() { return monthPayment; }
}
