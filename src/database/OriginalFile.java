package database;

import java.io.Serializable;

/**
 *
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
     * @return
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
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Setzte den Pfad der Originaldatei
     *
     * @param name
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gibt den Namen der Originaldatei zurück
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setzte den Namen der Originaldatei
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Typ der Originaldatei zurück
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Setzt den Typ der Originaldatei
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gibt die Größe der Originaldatei zurück
     *
     * @return
     */
    public String getSize() {
        return size;
    }

    /**
     * Setzt die Größe der Originaldatei
     *
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Gibt das Datum zurück, an dem die Originaldatei hinzugefügt wurde
     *
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Setzt das Datum, an dem die Originaldatei hinzugefügt wurde
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt die Checksumme der Originaldatei zurück
     * @return 
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * Setzte die Checksumme der Originaldatei
     *
     * @param checkSum
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

}
