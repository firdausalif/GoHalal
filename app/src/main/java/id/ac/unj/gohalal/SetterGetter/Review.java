package id.ac.unj.gohalal.SetterGetter;

import android.content.Context;

/**
 * Created by SuperNova's on 26/05/2017.
 */

public class Review {
    String namauser,namamenu,review,created;
    int id,iduser,idmenu,rate;
    Context context;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setNamauser(String namauser){
        this.namauser = namauser;
    }

    public String getNamauser(){
        return namauser;
    }

    public void setNamamenu(String namamenu){
        this.namamenu = namamenu;
    }

    public String getNamamenu(){
        return namamenu;
    }

    public void setReview(String review){
        this.review = review;
    }

    public String getReview(){
        return review;
    }

    public void setIduser(int iduser){
        this.iduser = iduser;
    }

    public int getIduser(){
        return iduser;
    }


    public void setIdmenu(int idmenu){
        this.idmenu = idmenu;
    }

    public int getIdmenu(){
        return idmenu;
    }


    public void setRate(int rate){
        this.rate = rate;
    }

    public int getRate(){
        return rate;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    public void setCreated (String created){
        this.created = created;
    }

    public String getCreated(){
        return created;
    }

}
