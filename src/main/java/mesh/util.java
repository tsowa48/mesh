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
    //Double fsspDuty = fssp.get(client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getBirth(), false);
    Double fedsfmList = fedsfm.get(client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getBirth());
    Document passport = client.getDocuments().stream().filter(doc -> doc.getType() == 0).findFirst().get();
    Double fmsExpired = fms.expiredDocument(passport);
    //solvency -= fsspDuty;//TODO: convert fsspDuty to [0.0; 1.0]
    solvency *= fedsfmList;
    solvency *= fmsExpired;
    return solvency;
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

  public static void fuckDB(int maxRows) {
    String clientLine = "";
    String passportLine = "";
    javax.persistence.EntityManager em = DBManager.getManager();
    em.getTransaction().begin();
    try {
      java.io.BufferedReader br = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get("/work/data/48_utf.csv"));
      //int rowCount = maxRows;
      String line = "";
      while(((line = br.readLine()) != null && line.length() > 0)) {
        String[] currentLine = line.split(";");
        String firstName = currentLine[1];
        String lastName = currentLine[2];
        String patronymic = currentLine[3];
        String birth = currentLine[4].replace("/", ".");
        String isMale = currentLine[5].toLowerCase().startsWith("м") ? "true" : "false";
        String address = "";
        for (int i = 64; i < 79; i++) {
          if (!currentLine[i].isEmpty())
            address += currentLine[i] + ", ";
        }
        address = address.substring(0, address.length() - 2);
        clientLine = "insert into client(firstName, lastName, patronymic, birth, isMale, address) values (:firstName, :lastName, :patronymic, '" + birth + "', " + isMale + ", :address) returning id";

        String cid = em.createNativeQuery(clientLine)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("patronymic", patronymic)
                .setParameter("address", address)
                .getSingleResult().toString();

        String serial = currentLine[39].replace(" ", "");
        String number = currentLine[40];
        String issued = currentLine[44].replace("/", ".");
        String issuedBy = currentLine[42];
        String code = currentLine.length > 286 && currentLine[287].length() > 1 ? currentLine[287].replace("-", "") : "0";
        passportLine = "insert into document(type, serial, number, issued, issuedBy, departmentCode, cid) values (0, '" + serial + "', '" + number + "', '" + issued + "', '" + issuedBy + "', " + code + ", " + cid + ")";
        em.createNativeQuery(passportLine).executeUpdate();

        //rowCount--;
      }
      em.getTransaction().commit();
      System.out.println("----- OK");
    } catch (Exception ex) {
      System.out.println(clientLine);
      System.out.println(passportLine);
      //ex.printStackTrace();
      em.getTransaction().rollback();
    }
  }
}
