package cloudtestfxml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CheckSumSHA {
   
    /**
     * Überprüft, ob zwei Checksummen identisch sind
     * @param path1
     * @param checkSum2
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException 
     */
   public boolean checkSumTwoFiles(String path1, String checkSum2) throws NoSuchAlgorithmException, IOException {
       MessageDigest md = MessageDigest.getInstance("SHA-256");
       String checkSum1 = checkSum(path1);
       
       boolean isEqual;
       
       if(checkSum1.equals(checkSum2)){
           isEqual = true;
       } else {
           isEqual = false;
       }
       return isEqual;
   }
    
   /**
    * Erstellt die Checksumme einer Datei
    * @param filepath Pfad zur Datei
    * @return
    * @throws FileNotFoundException
    * @throws IOException
    * @throws NoSuchAlgorithmException 
    */
   public String checkSum(String filepath) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
       MessageDigest md = MessageDigest.getInstance("SHA-256");
       try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ; 
            md = dis.getMessageDigest();
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
   }
 }
