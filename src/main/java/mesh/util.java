package mesh;

import mesh.db.*;
import mesh.plugin.cbr.cbr;
import mesh.plugin.fedsfm.fedsfm;
import mesh.plugin.fedstat.fedstat;
import mesh.plugin.fms.fms;
import mesh.plugin.fssp.fssp;

import java.util.*;

/**
 *
 * @author tsowa
 */
public class util {
  private static final sun.misc.BASE64Decoder b64Decoder = new sun.misc.BASE64Decoder();

  public static ResourceBundle rb;

  public static String detectField(String query) {
    String criterion = "LOWER(C.address) like '%" + query + "%' or LOWER(C.firstname) like '%" + query + "%' or LOWER(C.lastname) like '%" + query + "%' or LOWER(C.patronymic) like '%" + query + "%'";
    if(containsInt(query) && !query.contains(" "))
      criterion = "LOWER(C.birth) like '%" + query + "%' or LOWER(D.serial) like '%" + query + "%' or LOWER(D.number) like '%" + query + "%' or LOWER(D.issued) like '%" + query + "%'";
    if(query.contains(" ")) {
      String[] subQuery = query.split(" ");
      if(containsInt(subQuery[0]) && containsInt(subQuery[1])) {
        criterion = "LOWER(D.serial) like '%" + subQuery[0] + "%' and LOWER(D.number) like '%" + subQuery[1] + "%'";
      } else {
        String subCrt;
        if(subQuery.length > 2)
          subCrt = "LOWER(C.firstname) like '%" + subQuery[0] + "%' and LOWER(C.lastname) like '%" + subQuery[1] + "%' and LOWER(C.patronymic) like '%" + subQuery[2] + "%'";
        else if(subQuery.length == 2)
          subCrt = "LOWER(C.firstname) like '%" + subQuery[0] + "%' and LOWER(C.lastname) like '%" + subQuery[1] + "%'";
        else
          subCrt = "LOWER(C.firstname) like '%" + subQuery[0] + "%'";
        String subCrtAddr = String.join("%' and LOWER(C.address) like '%", subQuery);
        criterion = "(LOWER(C.address) like '%" + subCrtAddr + "%') or (" + subCrt + ")";
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
  
  public static Double recountSolvency(Client client, Order currentOrder) {
    Double solvency;
    Document passport = client.getDocuments().stream().filter(doc -> doc.getType() == 0).findFirst().get();
    Double salary = client.getSalary() == null ? 0.0 : client.getSalary();
    Double monthAmount = maxAmount(currentOrder.getDesired_amount(), currentOrder.getDesired_term(), cbr.getKeyRate());

    Double fsspDuty = fssp.get(client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getBirth(), false);
    Double fedsfmList = fedsfm.get(client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getBirth());
    Double fmsExpired = fms.expiredDocument(passport);
    Double livingWage = fedstat.getLivingWage(new Date().getYear() + 1900 - 1);

    salary -= livingWage;
    salary -= fsspDuty;

    if(salary <= 0.0)
      return 0.0;

    solvency = 1.0 - monthAmount / salary;
    solvency *= fedsfmList;
    solvency *= fmsExpired;

    solvency = solvency == null ? 0.0 : solvency;
    solvency = solvency < 0.0 ? 0.0 : solvency;
    solvency = solvency > 1.0 ? 1.0 : solvency;
    return solvency;
  }

  /**
   * Count amount for first month
   * @param D full amount
   * @param n months
   * @param i percent
   * @return Y - max(monthAmount)
   */
  private static Double maxAmount(Double D, Integer n, Double i) {
    return D * i + D / (12 * n);
  }
  
  public static Set<ApprovedLoan> approveLoans(Client client, List<Loan> loans) {
    final Double solvency = client.getSolvency() == null ? 0.0 : client.getSolvency();//TODO: fixme
    final Set<ApprovedLoan> aloans = new HashSet<>();
    final Integer isWhiteListed = solvency == 0.0 ? 0 : 1;
    loans.forEach(it -> {
      Double percentSolvency = solvency >= it.getMaxSolvency() ? 1.0 :
              (solvency < it.getMinSolvency() ? 0.0 :
              (solvency == it.getMinSolvency() ? 0.01 :
              (solvency - it.getMinSolvency()) / (it.getMaxSolvency() - it.getMinSolvency())));
      Double amount = it.getMinAmount() + (it.getMaxAmount() - it.getMinAmount()) * percentSolvency;
      Integer term = ((Long)Math.round(it.getMinTerm() + (it.getMaxTerm() - it.getMinTerm()) * percentSolvency)).intValue();
      Double percent = it.getMaxTerm() - (it.getMaxTerm() - it.getMinTerm()) * percentSolvency;
      ApprovedLoan al = new ApprovedLoan(it, amount * isWhiteListed, term * isWhiteListed, percent * isWhiteListed);
      aloans.add(al);
    });
    return aloans;
  }
}
