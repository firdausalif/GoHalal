package id.ac.unj.gohalal.SetterGetter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by SuperNova's on 29/05/2017.
 */

public class Maps {
    LatLng currentPos, shopPos;


    public LatLng getCurrentPos(){
        return currentPos;
    }

    public void setCurrentPos( LatLng currentPos){
        this.currentPos = currentPos;
    }

    public LatLng getShopPos(){
        return shopPos;
    }

    public void setShopPos(LatLng shopPos){
        this.shopPos = shopPos;
    }
}
