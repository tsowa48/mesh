package mesh.db;

import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "orders")
public class Order {
  @Id
  private Integer id;
  
  @Column
  private Integer uid;//User id
  @Column
  private Integer cid;//Client id
  @Column
  private Integer lid;//Loan id
  @Column
  private Double desired_amount;//Wish summ
  @Column
  private Integer desired_term;//Wish date
  @Column
  private Double approved_amount;//Accepted summ
  @Column
  private Integer approved_term;//Accepted date
  @Column
  private Integer date;//Date - order date
}