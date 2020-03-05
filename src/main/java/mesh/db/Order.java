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
}