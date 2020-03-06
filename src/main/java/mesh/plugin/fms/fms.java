package mesh.plugin.fms;

public final class fms {
    //private static final Stream<String> lines;

    static {
        //INFO: Download (444MB) & read: http://guvm.mvd.ru/upload/expired-passports/list_of_expired_passports.csv.bz2
        //INFO: last load: 06.03.2020
        //BufferedReader br = new BufferedReader(new InputStreamReader(fms.class.getResourceAsStream("list_of_expired_passports.csv")));
        //lines = br.lines();
    }

    public static Double expiredDocument(String serial, String number) {
        Long expiredCount = 0L;//lines.filter(line -> line.equals(serial + "," + number)).count();
        return (expiredCount > 0L ? 0.0 : 1.0);
    }
}
