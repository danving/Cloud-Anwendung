package database;

/**
 * Getter und Setter für die Informationen über die Teil-Dateien
 * @author danvi
 */
public class PartFiles {

    private String id;
    private String name;
    private String path;
    private int part;
    private long partsSize;
    private int chunkSize;

    /**
     * Gibt die ID der Teil-Datei zurück
     *
     * @return id der Teil-Datei
     */
    public String getId() {
        return id;
    }

    /**
     * Setzt die ID der Teil-Datei
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Teil-Datei zurück
     *
     * @return name der Teil-Datei
     */
    public String getName() {
        return name;
    }

    /**
     * Setzte den Namen der Teil-Datei
     *
     * @param name Name der Teil-Datei
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Pfad der Teil-Datei zurück
     *
     * @return Pfad der Teil-Datei
     */
    public String getPath() {
        return path;
    }

    /**
     * Setzt Pfad der Teil-Datei
     *
     * @param path Pfad der Teil-Datei
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gibt die Nummer der Teil-Datei zurück
     *
     * @return Nummer der Teil-Datei
     */
    public int getPart() {
        return part;
    }

    /**
     * Setzt die Nummer der Teil-Datei
     *
     * @param part Nummer der Teil-Datei
     */
    public void setPart(int part) {
        this.part = part;
    }

    /**
     * Gibt die Größe der Teil-Datei zurück
     * @return Größe der Teil-Datei
     */
    public long getPartsSize() {
        return partsSize;
    }

    /**
     * Setzt die Größe der Teil-Datei 
     * @param size Größe der Teil-Datei
     */
    public void setPartsSize(long size) {
        this.partsSize = size;
    }

    /**
     * Gibt die Chunk-Größe zurück, die zum Teilen verwendet wurde
     * @return Chunk-Größe der Teil-Datei
     */
    public int getChunkSize() {
        return chunkSize;
    }

    /**
     * Setzt die Chunk-Größe, die zum Teilen verwendet wurde
     * @param chunkSize Chunk-Größe der Teil-Datei
     */
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

}
