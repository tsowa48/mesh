package mesh.db;

import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author tsowa
 */
@Entity
@Table
public class Document {
  @Id
  private Integer id;
  @Column
  private Integer type;
  @Column
  private String serial;
  @Column
  private String number;
  @Column
  private Integer issued;//Date
  @Column
  private String issuedBy;
  @Column
  private Integer departmentCode;
  @Column
  private Integer cid;
}