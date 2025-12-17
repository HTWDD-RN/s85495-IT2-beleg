/*Generierung von FEC-Paketen auf der Server-Seite und deren Verarbeitung auf der Client-Seite.*/

package rtp;

import java.util.HashMap;
import java.util.List;

public class FecHandler extends  FecHandlerDemo{
    public FecHandler(boolean size) {
        super(size);
    }

    public FecHandler(int size) {
        super(size);
    }

    @Override
    boolean checkCorrection(int nr, HashMap<Integer, RtpPacket> mediaPackets) {

        if (fecList.get(nr) ==null)  {  //Abbruch
            return  false;
        }
        int c=0;
        List<Integer> l=fecList.get(nr);

        for(Integer i:l) {
            if(mediaPackets.get(i) !=null) {
                c++;
            }
        }
        return c >=l.size() -1;  // max 1 paket fehlt , dann 1 sonst 0
    }


    @Override
    RtpPacket correctRtp(int nr, HashMap<Integer, RtpPacket> mediaPackets) {

        List<Integer> l =fecList.get(nr);
        int fnr=fecNr.get(nr);//welche nummer
        fec =fecStack.get(fnr);

        for (Integer i : l) {
            if (i != nr) {
                rtp = mediaPackets.get(i);
                fec.addRtp(rtp);
            }
        }
        return fec.getLostRtp(nr);
    }
}

