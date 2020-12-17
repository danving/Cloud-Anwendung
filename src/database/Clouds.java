package database;

/**
 * 
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
     * @return
     */
    public String getCloud() {
        return cloud;
    }

    /**
     * Setzte den Pfad der Cloud
     *
     * @param cloud
     */
    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
    
    /**
     * Gibt die Id der Cloud zurück
     * @return 
     */
    public int getId() {
        return id;
    }
    
    /**
     * Setzt die Id der Cloud
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Gibt die Speicherkapazität der Cloud zurück
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * Setzt die Speicherkapazität der Cloud
     * @param size 
     */
    public void setSize(int size) {
        this.size = size;
    }
   
    /**
     * Gibt die Anzahl der Clouds zurück
     *
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setzt die Anzahl der Clouds
     *
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
