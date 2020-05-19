package mesh.plugin.fms;

import mesh.DBManager;
import mesh.db.Document;

import javax.persistence.EntityManager;

public final class fms {

    public static Double expiredDocument(Document doc) {
        EntityManager em = DBManager.getManager();
        Integer expiredCount = (Integer)em.createNativeQuery("select count(E.*)::::integer from expired E where E.type=:type and E.serial=:serial and E.number=:number")
                .setParameter("type", doc.getType())
                .setParameter("serial", doc.getSerial())
                .setParameter("number", doc.getNumber())
                .getSingleResult();
        return (expiredCount > 0 ? 0.0 : 1.0);
    }
}
