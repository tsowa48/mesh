package mesh.db;

import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "role")
public class Role {
  @Id
  private Integer id;
  @Column
  private String name;
  @Column
  private Integer access;
  
  //@OneToMany(mappedBy = "rid", fetch=FetchType.LAZY)
  //public Set<User> users;
}
