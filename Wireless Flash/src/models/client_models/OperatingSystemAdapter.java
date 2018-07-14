package models.client_models;

/**
 * Class used to make changes to some functionalities as to adapt to different operating systems
 */
public class OperatingSystemAdapter {

    private static OperatingSystemAdapter os;
    private static char dash;
    private static char fileDash = '\\';

    /**
     * method used to get a singleton instance of OperatingSystemAdapter
     * @return an instance of OperatingSystemAdapter
     */
    public static OperatingSystemAdapter getOS(){
        if(os == null){
            os = new OperatingSystemAdapter();

            /*
            Initialize OS here
             */
            if(System.getProperty("os.name").contains("Windows")){
                dash = '\\';
            }else{
                dash = '/';
            }
        }
        return os;
    }

    /**
     * Get method for the dash attribute of the OS
     * @return The dash used in the file path of the system
     */
    public char getDash(){
        return dash;
    }

    /**
     * Get method for the fileDash attribute
     * @return The dash used in the file path of the storage device being used (linux by default -> "/")
     */
    public char getFileDash(){
        return fileDash;
    }


}
