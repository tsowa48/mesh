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
  private Integer id;
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
  @Column(name = "salary", nullable = true)
  private Double salary;
  @Column(name = "solvency", nullable = true)
  private Double solvency;
  
  @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
  private Set<Document> documents;
  
  @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
  private Set<Order> orders;
    
  public Client() {
    documents = new HashSet<>();
    orders = new HashSet<>();
  }

  public Integer getId() { return this.id; }
  public String getFirstName() { return this.firstName; }
  public String getLastName() { return this.lastName; }
  public String getPatronymic() { return this.patronymic; }
  public String getBirth() { return this.birth; }
  public Double getSalary() { return this.salary; }
  public Double getSolvency() { return this.solvency; }
  public Set<Document> getDocuments() { return this.documents; }
  public Boolean getSex() { return isMale; }
  public String getAddress() { return address; }
  public Set<Order> getOrders() { return orders; }

  public void setSolvency(Double solvency) { this.solvency = solvency; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
  public void setBirth(String birth) { this.birth = birth; }
  public void setMale(Boolean male) { isMale = male; }
  public void setAddress(String address) { this.address = address; }
  public void setSalary(Double salary) { this.salary = salary; }
  public void setDocuments(Set<Document> documents) { this.documents = documents; }
  public void addDocument(Document document) { this.documents.add(document); }
}
