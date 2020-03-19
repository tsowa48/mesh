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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  protected Integer id;
  @Column
  protected String login;
  @Column
  protected String password;
  @Column
  protected String fio;
  @Column
  protected String token;
  
  //@Column
  //public Integer cid;//Company id
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "rid", nullable = false)
  protected Role role;
  
  @OneToMany(mappedBy = "uid", fetch = FetchType.LAZY)
  protected Set<Order> orders;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFio() {
    return fio;
  }

  public void setFio(String fio) {
    this.fio = fio;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }
}