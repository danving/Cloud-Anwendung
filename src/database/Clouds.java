package database;

/**
 * Getter und Setter für die Informationen über die zu speichernden Clouds
 *
 * @author danvi
 */
public class Clouds {

    private String cloud;
    private int id;
    private int number;
    private int size;


    /**
     * Gibt den Pfad der Cloud zurück
     *
     * @return  Cloud-Pfad
     */
    public String getCloud() {
        return cloud;
    }

    /**
     * Setzte den Pfad der Cloud
     *
     * @param cloud Pfad der Cloud
     */
    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
    
    /**
     * Gibt die Id der Cloud zurück
     * @return ID der Cloud
     */
    public int getId() {
        return id;
    }
    
    /**
     * Setzt die Id der Cloud
     * @param id  Id der Cloud
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Gibt die Speicherkapazität der Cloud zurück
     * @return Speicherkapazität der Cloud
     */
    public int getSize() {
        return size;
    }

    /**
     * Setzt die Speicherkapazität der Cloud
     * @param size Größe der Cloud
     */
    public void setSize(int size) {
        this.size = size;
    }
   
    /**
     * Gibt die Anzahl der Clouds zurück
     *
     * @return Anzahl der Clouds
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setzt die Anzahl der Clouds
     *
     * @param number Anzahl der Clouds
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
