package mesh.db;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Integer id;
  @Column(nullable = false)
  private Integer uid;//User id
  @Column(nullable = false)
  public Integer cid;//Client id
  @Column(nullable = true)
  public Integer lid;//Loan id
  @Column(nullable = false)
  private Double desired_amount;//Wish summ
  @Column(nullable = false)
  private Integer desired_term;//Wish date
  @Column(nullable = false)
  private Integer date;//Date - order date
  @Transient
  public Set<ApprovedLoan> approved;
  
  public Order() {
    approved = new HashSet<>();
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public Integer getLid() { return lid; }
  public void setLid(Integer lid) { this.lid = lid; }
  public Double getDesired_amount() { return desired_amount; }
  public void setDesired_amount(Double desired_amount) { this.desired_amount = desired_amount; }
  public Integer getDesired_term() { return desired_term; }
  public void setDesired_term(Integer desired_term) { this.desired_term = desired_term; }
  public Integer getDate() { return date; }
  public void setDate(Integer date) { this.date = date; }
  public Set<ApprovedLoan> getApproved() { return approved; }
  public void setApproved(Set<ApprovedLoan> approved) { this.approved = approved; }
}