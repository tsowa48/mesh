package mesh.db;

import javax.persistence.*;
import java.util.Set;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  protected Integer id;
  @Column
  protected String name;
  @Column
  protected String access;//Integer

  @OneToMany(mappedBy = "id", fetch=FetchType.LAZY)
  protected Set<User> users;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAccess() {
    return access;
  }

  public void setAccess(String access) {
    this.access = access;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}
