package mesh.db;

import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "loan")
public class Loan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  public Integer id;
  //@Column(nullable = false)
  //private Integer cid;//Company id
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  public Double min_amount;
  @Column(nullable = false)
  public Double max_amount;
  @Column(nullable = false)
  public Integer min_term;//in days
  @Column(nullable = false)
  public Integer max_term;//in days
  @Column(nullable = false)
  public Double min_percent;
  @Column(nullable = false)
  public Double max_percent;
  @Column(nullable = false)
  public Double min_solvency;
  @Column(nullable = false)
  public Double max_solvency;
  //@Column
  //private Integer calc_type;
  
  //@OneToMany(mappedBy = "lid", fetch=FetchType.LAZY)
  //private Set<Order> orders;
}
