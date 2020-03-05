package mesh.db;

/**
 *
 * @author tsowa
 */
public class ApprovedLoan {
  private Integer lid;
  private Double amount;
  private Integer date;
  private Double percent;
  
  public ApprovedLoan(Integer lid, Double amount, Integer date, Double percent) {
    this.lid = lid;
    this.amount = amount;
    this.date = date;
    this.percent = percent;
  }
}
