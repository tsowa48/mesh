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
  @Column(nullable = false)
  private Integer cid;

  public Integer getType() { return this.type; }
  public String getSerial() { return this.serial; }
  public String getNumber() { return this.number; }
}