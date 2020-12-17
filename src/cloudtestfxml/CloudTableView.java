package cloudtestfxml;

/**
 * CloudTable dient zur Verwaltung der Dateien, die in die Cloud verschoben
 * werden und im Hauptfenster in einer Tabelle angezeigt werden. Es werden
 * Dateiname, Typ, Größe und das Datum verwaltet
 *
 * @author danvi
 */
public class CloudTableView {

    //Variablen für Dateiinformationen: Name, Datum, Typ, Größe
    private String file = null;
    private String date = null;
    private String type = null;
    private String size = null;

    public CloudTableView() {
    }

    /**
     * Konstruktor für die Klasse CloudTable
     *
     * @param file Name der Date
     * @param date Datum an dem die Datei verschoben wurde
     * @param type Dateityp
     * @param size Größe der Datei
     */
    public CloudTableView(String file, String date, String type, String size) {
        this.file = file;
        this.date = date;
        this.type = type;
        this.size = size;
    }

    /**
     * Gibt den Namen der Datei zurück
     *
     * @return Name der Datei
     */
    public String getFile() {
        return file;
    }

    /**
     * Setzt den Namen der Datei
     *
     * @param file neuer Name der Datei
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Gibt das Datum, an dem die Datei verschoben wurde, zurück
     *
     * @return Datum
     */
    public String getDate() {
        return date;
    }

    /**
     * Setzt das Datum der Datei
     *
     * @param date neues Datum der Datei
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt den Dateitypen zurück
     *
     * @return Dateityp
     */
    public String getType() {
        return type;
    }

    /**
     * Setzt den Dateitypen
     *
     * @param type neuer Dateityp
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gibt die Größe der Datei zurück
     *
     * @return Größe
     */
    public String getSize() {
        return size;
    }

    /**
     * Setzt die Dateigröße
     *
     * @param size neue Dateigröße
     */
    public void setSize(String size) {
        this.size = size;

    }

}
