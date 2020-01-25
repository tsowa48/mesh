package mesh.db;

import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "client")
public class Client {
  @Id
  public Integer id;
  @Column(name = "firstname")
  public String firstName;
  @Column(name = "lastname")
  private String lastName;
  @Column
  private String patronymic;
  @Column
  private Integer birth;//Date
  @Column(name = "ismale")
  private Boolean isMale;
  @Column
  private String address;
  @Column(name = "solvency")
  public Double solvency;
  
  @OneToMany(mappedBy = "cid", fetch=FetchType.LAZY)
  private Set<Document> documents;
  
  @OneToMany(mappedBy = "cid", fetch=FetchType.LAZY)
  private Set<Order> orders;
    
  public Client() {
    //documents = new ArrayList<>();
  }

}
