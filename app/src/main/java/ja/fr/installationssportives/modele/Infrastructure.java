package ja.fr.installationssportives.modele;

/**
 * Created by Formation on 18/01/2018.
 */

public class Infrastructure {

    private String name;

    private String adress;

    private Integer nbEquipements;

    private Double latitude;

    private Double longitude;

    public Infrastructure() {
    }

    public String getName() {
        return name;
    }

    public Infrastructure setName(String name) {
        this.name = name;
        return this;
    }

    public String getAdress() {
        return adress;
    }

    public Infrastructure setAdress(String adress) {
        this.adress = adress;
        return this;
    }

    public Integer getNbEquipements() {
        return nbEquipements;
    }

    public Infrastructure setNbEquipements(Integer nbEquipements) {
        this.nbEquipements = nbEquipements;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Infrastructure setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Infrastructure setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
}
