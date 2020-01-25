package mesh.db;

import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "company")
public class Company {
  @Id
  private Integer id;
  @Column
  private String name;
  @Column
  private Integer inn;
  
  //@OneToMany(mappedBy = "cid", fetch=FetchType.LAZY)
  //public Set<User> users;
}
