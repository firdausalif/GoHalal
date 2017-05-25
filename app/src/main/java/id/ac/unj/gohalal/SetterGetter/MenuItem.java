package id.ac.unj.gohalal.SetterGetter;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by SuperNova's on 25/05/2017.
 */

public class MenuItem {
    private String nama,deskripsi,image;
    private Integer idresto,rate,price;
    Context context;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getIdresto(){
        return idresto;
    }

    public void setIdresto(int idresto){
        this.idresto = idresto;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public int getRate(){
        return rate;
    }

    public void setRate(int rate){
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context){
        this.context = context;
    }

}
