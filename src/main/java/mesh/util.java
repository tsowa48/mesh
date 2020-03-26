package mesh;

import mesh.db.*;
import mesh.plugin.fedsfm.fedsfm;
import mesh.plugin.fms.fms;
import mesh.plugin.fssp.fssp;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author tsowa
 */
public class util {
  private static final sun.misc.BASE64Decoder b64Decoder = new sun.misc.BASE64Decoder();

  public static ResourceBundle rb;

  public static String detectField(String query) {
    String criterion = "LOWER(C.address) like '%" + query + "%' or LOWER(C.firstname) like '%" + query + "%' or LOWER(C.lastname) like '%" + query + "%' or LOWER(C.patronymic) like '%" + query + "%'";
    if(containsInt(query))
      criterion = "LOWER(C.birth) like '%" + query + "%' or LOWER(D.serial) like '%" + query + "%' or LOWER(D.number) like '%" + query + "%' or LOWER(D.issued) like '%" + query + "%'";
    if(query.contains(" ")) {
      String[] subQuery = query.split(" ");
      if(containsInt(query)) {
        criterion = "LOWER(D.serial) like '%" + subQuery[0] + "%' and LOWER(D.number) like '%" + subQuery[1] + "%'";
      } else {
        String subCrt;
        if(subQuery.length > 2)
          subCrt = "LOWER(C.firstname) like '%" + subQuery[0] + "%' and LOWER(C.lastname) like '%" + subQuery[1] + "%' and LOWER(C.patronymic) like '%" + subQuery[2] + "%'";
        else if(subQuery.length == 2)
          subCrt = "LOWER(C.firstname) like '%" + subQuery[0] + "%' and LOWER(C.lastname) like '%" + subQuery[1] + "%'";
        else
          subCrt = "LOWER(C.firstname) like '%" + subQuery[0] + "%'";
        criterion = "LOWER(C.address) like '%" + query + "%' or " + subCrt;
      }
    }
    return criterion;
  }
  
  private static boolean containsInt(String s) {
    return s.contains("0") || s.contains("1") || s.contains("2")
            || s.contains("3") || s.contains("4") || s.contains("5")
            || s.contains("6") || s.contains("7") || s.contains("8")
            || s.contains("9");
  }
  
  public static Double recountSolvency(Client client) {
    Double solvency = 1.0;
    Double fsspDuty = fssp.get(client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getBirth());
    Double fedsfmList = fedsfm.get(client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getBirth());
    Document passport = client.getDocuments().stream().filter(doc -> doc.getType() == 0).findFirst().get();
    Double fmsExpired = fms.expiredDocument(passport.getSerial(), passport.getNumber());
    //solvency -= fsspDuty;//TODO: convert fsspDuty to [0.0; 1.0]
    solvency *= fedsfmList;
    solvency *= fmsExpired;
    return solvency;
  }
  
  public static Set<ApprovedLoan> approveLoans(Client client, List<Loan> loans) {
    final Double solvency = client.getSolvency();
    final Set<ApprovedLoan> aloans = new HashSet<>();
    final Integer isWhiteListed = solvency == 0.0 ? 0 : 1;
    loans.forEach(it -> {
      Double percentSolvency = solvency >= it.max_solvency ? 1.0 :
              (solvency < it.min_solvency ? 0.0 :
              (solvency == it.min_solvency ? 0.01 :
              (solvency - it.min_solvency) / (it.max_solvency - it.min_solvency)));
      Double amount = it.min_amount + (it.max_amount - it.min_amount) * percentSolvency;
      Integer term = ((Long)Math.round(it.min_term + (it.max_term - it.min_term) * percentSolvency)).intValue();
      Double percent = it.max_term - (it.max_term - it.min_term) * percentSolvency;
      ApprovedLoan al = new ApprovedLoan(it.id, amount * isWhiteListed, term * isWhiteListed, percent * isWhiteListed);
    });
    return aloans;
  }
}
