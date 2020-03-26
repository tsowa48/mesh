package mesh.db;

import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table(name = "document")
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Integer id;
  @Column(nullable = false)
  private Integer type;
  @Column(nullable = false)
  private String serial;
  @Column(nullable = false)
  private String number;
  @Column(nullable = false)
  private String issued;//Date
  @Column(nullable = true)
  private String issuedBy;
  @Column(nullable = true)
  private Integer departmentCode;
  @Column(name = "cid", nullable = false)
  private Integer cid;

  public Integer getType() { return this.type; }
  public String getSerial() { return this.serial; }
  public String getNumber() { return this.number; }
  public String getIssued() { return issued; }

  public void setType(Integer type) { this.type = type; }
  public void setSerial(String serial) { this.serial = serial; }
  public void setNumber(String number) { this.number = number; }
  public void setIssued(String issued) { this.issued = issued; }
  public void setClient(Integer client) { this.cid = client; }

  public enum Type {
    PASPORT(0),
    SNILS(1),
    INN(2),
    OTHER(3);

    private Integer value;
    Type(Integer value) { this.value = value; }

    public Integer val() { return this.value; }
  }
}