package mesh.db;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "client")
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  public Integer id;
  @Column(name = "firstname", nullable = false)
  private String firstName;
  @Column(name = "lastname", nullable = false)
  private String lastName;
  @Column(nullable = true)
  private String patronymic;
  @Column(nullable = false)
  private String birth;//Date
  @Column(name = "ismale", nullable = false)
  private Boolean isMale;
  @Column(nullable = false)
  private String address;
  @Column(name = "solvency", nullable = false)
  private Double solvency;
  
  @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
  private Set<Document> documents;
  
  @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
  private Set<Order> orders;
    
  public Client() {
    documents = new HashSet<>();
    orders = new HashSet<>();
  }

  public String getFirstName() { return this.firstName; }
  public String getLastName() { return this.lastName; }
  public String getPatronymic() { return this.patronymic; }
  public String getBirth() { return this.birth; }
  public Double getSolvency() { return this.solvency; }
  public Set<Document> getDocuments() { return this.documents; }

  public void setSolvency(Double solvency) { this.solvency = solvency; }
}
