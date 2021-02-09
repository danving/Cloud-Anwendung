package cloudtestfxml;

/**
 * Klasse erstellt einen dynamischen Pfad zur Cloud.
 *
 * @author danvi
 */
//TODO Username und Verzeichnis für Systemunabhängig abrufen
public class PlaceholderPath {

    /**
     * Ersetzt den Platzhalter im Pfad durch den Usernamen aus dem System
     *
     * @param path Pfad mit Platzhalter
     * @return Pfad ohne Platzhalter
     */
    public String replacePlaceholder(String path) {
        if (path != null) {
            String username = new com.sun.security.auth.module.NTSystem().getName();
            String homeDir = System.getProperty("user.home");
            String cloudPlaceholder = "";
            for (int i = 0; i < path.length() - 15; i++) {
                if (path.substring(i, i + 15).equals("Homeverzeichnis")) {
                    for (int j = i + 6; j < path.length(); j++) {
                        if (path.charAt(j) == '\\') {
                            cloudPlaceholder = homeDir + "\\" + path.substring(j+1);
                            //System.out.println(cloudPlaceholder);
                            path = cloudPlaceholder;
                            break;
                        }
                    }
                    break;
                }
            }
        }

        return path;
    }

    /**
     * Ersetzt den Usernamen in dem Pfad durch einen Platzhalter
     *
     * @param path Pfad ohne Platzhalter
     * @return Pfad mit Platzhalter
     */
    public String setPlaceholder(String path) {

        String homePlaceholder = "Platzhalter/Homeverzeichnis/";
        String userPlaceholder = "PlatzhalterUser";
        String cloudPlaceholder = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.substring(i, i + 6).equals("Users\\")) {
                for (int j = i + 6; j < path.length(); j++) {
                    if (path.charAt(j) == '\\') {
                        cloudPlaceholder = homePlaceholder + userPlaceholder + path.substring(j);
                        //System.out.println(cloudPlaceholder);
                        break;
                    }
                }
                break;
            }
        }
        return cloudPlaceholder;
    }
}
