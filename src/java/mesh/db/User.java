package mesh.db;

import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "users")
public class User {
  @Id
  public Integer id;
  @Column
  private String login;
  @Column
  public String password;
  @Column
  public String fio;
  @Column
  public String token;
  
  //@Column
  //public Integer cid;//Company id
  //@Column
  //public Integer rid;//Role id
  
  @OneToMany(mappedBy = "uid", fetch=FetchType.LAZY)
  public Set<Order> orders;
}