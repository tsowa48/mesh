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
  private Integer id;
  @Column
  private String name;
  @Column
  private Double min_amount;
  @Column
  private Double max_amount;
  @Column
  private Integer min_term;//in days
  @Column
  private Integer max_term;//in days
  @Column
  private Double percent;
  @Column
  private Integer calc_type;
  
  //@OneToMany(mappedBy = "lid", fetch=FetchType.LAZY)
  //private Set<Order> orders;
}
