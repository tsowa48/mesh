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
  private Integer id;
  //@Column(nullable = false)
  //private Integer cid;//Company id
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private Double min_amount;
  @Column(nullable = false)
  private Double max_amount;
  @Column(nullable = false)
  private Integer min_term;//in days
  @Column(nullable = false)
  private Integer max_term;//in days
  @Column(nullable = false)
  private Double min_percent;
  @Column(nullable = false)
  private Double max_percent;
  @Column(nullable = false)
  private Double min_solvency;
  @Column(nullable = false)
  private Double max_solvency;
  //@Column
  //private Integer calc_type;
  
  //@OneToMany(mappedBy = "lid", fetch=FetchType.LAZY)
  //private Set<Order> orders;


  public Integer getId() { return this.id; }
  public String getName() { return this.name; }
  public Double getMinAmount() { return this.min_amount; }
  public Double getMaxAmount() { return this.max_amount; }
  public Integer getMinTerm() { return this.min_term; }
  public Integer getMaxTerm() { return this.max_term; }
  public Double getMinPercent() { return this.min_percent; }
  public Double getMaxPercent() { return this.max_percent; }
  public Double getMinSolvency() { return this.min_solvency; }
  public Double getMaxSolvency() { return this.max_solvency; }

  public void setName(String name) { this.name = name; }
  public void setMinAmount(Double min_amount) { this.min_amount = min_amount; }
  public void setMaxAmount(Double max_amount) { this.max_amount = max_amount; }
  public void setMinTerm(Integer min_term) { this.min_term = min_term; }
  public void setMaxTerm(Integer max_term) { this.max_term = max_term; }
  public void setMinPercent(Double min_percent) { this.min_percent = min_percent; }
  public void setMaxPercent(Double max_percent) { this.max_percent = max_percent; }
  public void setMinSolvency(Double min_solvency) { this.min_solvency = min_solvency; }
  public void setMaxSolvency(Double max_solvency) { this.max_solvency = max_solvency; }
}
