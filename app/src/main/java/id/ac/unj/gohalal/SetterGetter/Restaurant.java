package id.ac.unj.gohalal.SetterGetter;

import java.io.Serializable;

/**
 * Created by SuperNova's on 23/05/2017.
 */


public class Restaurant implements Serializable {
    String restoName, alamat, deskripsi,telp,email,image;
    int restoId;

    public Restaurant(int restoId,String restoName, String alamat, String deskripsi, String image, String telp, String email){
        this.setRestoId(restoId);
        this.setRestoName(restoName);
        this.setAlamat(alamat);
        this.setDeskripsi(deskripsi);
        this.setImage(image);
        this.setTelp(telp);
        this.setEmail(email);
    }

    public int getRestoId(){
        return restoId;
    }

    public void setRestoId(int restoId){
        this.restoId = restoId;
    }

    public String getRestoName(){
        return restoName;
    }

    public void setRestoName(String restoName){
            this.restoName = restoName;
    }

    public String getAlamat(){
        return alamat;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getDeskripsi(){
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getTelp(){
        return  telp;
    }

    public void setTelp(String telp){
        this.telp = telp;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
