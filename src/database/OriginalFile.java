package database;

import java.io.Serializable;

/**
 * Getter und Setter für die Informationen der Original-Datei
 * @author danvi
 */
public class OriginalFile implements Serializable {

    private String id;
    private String path;
    private String name;
    private String type;
    private String size;
    private String date;
    private String checkSum;

    /**
     * Gibt die ID der Originaldatei zurück
     *
     * @return id der Original-Datei
     */
    public String getId() {
        return id;
    }

    /**
     * Setzt die ID der Originaldatei
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gibt den Pfad der Originaldatei zurück
     *
     * @return alter Pfad der Original-Datei
     */
    public String getPath() {
        return path;
    }

    /**
     * Setzte den Pfad der Originaldatei
     *
     * @param path Pfad der Original-Datei
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gibt den Namen der Originaldatei zurück
     *
     * @return Name der Original-Datei
     */
    public String getName() {
        return name;
    }

    /**
     * Setzte den Namen der Originaldatei
     *
     * @param name Name der Orignal-Datei
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Typ der Originaldatei zurück
     *
     * @return Datentyp der Original-Datei
     */
    public String getType() {
        return type;
    }

    /**
     * Setzt den Typ der Originaldatei
     *
     * @param type Datentyp der Original-Datei
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gibt die Größe der Originaldatei zurück
     *
     * @return Größe der Original-Datei
     */
    public String getSize() {
        return size;
    }

    /**
     * Setzt die Größe der Originaldatei
     *
     * @param size Größe der Original-Datei
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Gibt das Datum zurück, an dem die Originaldatei hinzugefügt wurde
     *
     * @return Datum an dem die Orignal-Datei eingefügt wurde
     */
    public String getDate() {
        return date;
    }

    /**
     * Setzt das Datum, an dem die Originaldatei hinzugefügt wurde
     *
     * @param date Darum der Original-Datei
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt die Checksumme der Originaldatei zurück
     * @return Checksumme der Original-Datei
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * Setzte die Checksumme der Originaldatei
     *
     * @param checkSum Checksumme der Original-Datei
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

}
