package rtsp;

import video.VideoMetadata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.logging.Level;

public class Rtsp extends RtspDemo {
    public Rtsp(URI url, int rtpRcvPort) {
        super(url, rtpRcvPort);
    }

    public Rtsp(BufferedReader RTSPBufferedReader, BufferedWriter RTSPBufferedWriter) {
        super(RTSPBufferedReader, RTSPBufferedWriter);
    }


    public   boolean setup() {
    // request is only valid if client is in correct state
    if (state != State.INIT) {
        logger.log(Level.WARNING, "RTSP state: " + state);
        return false;
    }
    RTSPSeqNb++;  // increase RTSP sequence number for every RTSP request sent
    send_RTSP_request("SETUP");
    // Wait for the response
    logger.log(Level.INFO, "Wait for response...");
    if (parse_server_response() != 200) {
        logger.log(Level.WARNING, "Invalid Server Response");
        return false;
    } else {
        state = State.READY;
        logger.log(Level.INFO, "New RTSP state: READY\n");
        return true;
    }
}


    @Override
    public boolean play() {
        if (state != State.READY) {
            logger.log(Level.WARNING, "RTSP state: " + state);
            return  false;
        }
        RTSPSeqNb++;
        send_RTSP_request("PLAY");
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
            return false;
        } else {
            state = State.PLAYING;
            logger.log(Level.INFO, "New RTSP state: PLAYING\n");
            return true;
        }
    }


    @Override
    public boolean pause() {
        if (state != State.PLAYING) {
            logger.log(Level.WARNING, "RTSP state: " + state);
            return  false;
        }
        RTSPSeqNb++;
        send_RTSP_request("PAUSE");
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
            return false;
        } else {
            state = State.READY;
            logger.log(Level.INFO, "New RTSP state: READY\n");
            return true;
        }
    }

    @Override
    public boolean teardown() {
  /* if (state != State.READY || state != State.PLAYING ) {
     logger.log(Level.WARNING, "RTSP state: " + state);
     return  false;
   }*/
        RTSPSeqNb++;
        send_RTSP_request("TEARDOWN");
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
            return false;
        } else {
            state = State.INIT;
            logger.log(Level.INFO, "New RTSP state: INIT\n");
            return true;
        }
    }


    @Override
    public  void describe()  {
        RTSPSeqNb++;
        send_RTSP_request("DESCRIBE");
        parse_server_response();


    }


    @Override
    public void options() {
        RTSPSeqNb++;
        send_RTSP_request("OPTIONS");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
        }

    }

    @Override
    public void send_RTSP_request(String request_type) {{

        logger.log(Level.INFO, "Request:  " + request_type + " " + RTSPSeqNb);
        try {
            String rtsp = url.toString();
            if (request_type.equals("SETUP")) {
                rtsp = rtsp + "/trackID=0"+CRLF;
            }
            String rtspReq = request_type + " " + rtsp + " RTSP/1.0" + CRLF;
            rtspReq += "CSeq: " + RTSPSeqNb + CRLF;

            //if request is SETUP
            if (request_type.equals("SETUP")) {
                rtspReq += "Transport: RTP/AVP;unicast;client_port=" + RTP_RCV_PORT + "-" + (RTP_RCV_PORT + 1) + CRLF;
            }

            //Session if available
            else {
                rtspReq += "Session: " + RTSPid + CRLF;
            }

            logger.log(Level.CONFIG, rtspReq);
            RTSPBufferedWriter.write(rtspReq + CRLF);
            RTSPBufferedWriter.flush();
            logger.log(Level.INFO, "*** RTSP-Request " + request_type + "send ***");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    }



    @Override
    String getDescribe(VideoMetadata meta, int RTP_dest_port) {
        StringWriter rtspHeader = new StringWriter();
        StringWriter rtspBody = new StringWriter();

        // Write the body first, so we can get the size later

        // Session description
        // v=  (protocol version)
        // o=  (owner/creator and session identifier).
        // s=  (session name)
        rtspBody.write("v=0" + CRLF);
        rtspBody.write("o=- 0 0 IN IP4 0.0.0.0" + CRLF);
        rtspBody.write("s=RTSP-Streaming" + CRLF);
        rtspBody.write("i=" + CRLF);

        // Time description
        // t= (time the session is active)
        rtspBody.write("t=0 0" + CRLF);
        // Stream control
        //

        // Media description
         //m=  (media name and transport address)
        rtspBody.write("m=video " +RTP_dest_port+ " RTP/AVP "+MJPEG_TYPE + CRLF); //beschreibt das Video mit dem RTP-Port und dem Media Type.
        rtspBody.write("a=control:trackerID=0" + CRLF); // gibt an, wie auf den Stream zugegriffen werden kann.
        rtspBody.write("a=rtpmap:" + MJPEG_TYPE + " JPEG/90000" + CRLF); //Medienformat und die Zeitbasis.
        // rtspBody.write("a=mimetype:string;\"video/mjpeg\"" + CRLF);
        rtspBody.write("a=framerate:" + meta.getFramerate() + CRLF);//erwendet den Wert aus den Metadaten
        // Audio ist not supported yet
        //rtspBody.write("m=audio " + "0" + " RTP/AVP " + "0" + CRLF);
        //rtspBody.write("a=rtpmap:" + "0" + " PCMU/8000" + CRLF);
        //rtspBody.write("a=control:trackID=" + "1" + CRLF);



        //
        rtspBody.write("a=range:npt=0-");
        if (meta.getDuration() > 0.0) {
            rtspBody.write(Double.toString(meta.getDuration()));
        }
        rtspBody.write(CRLF);

        // rtspHeader.write("Content-Base: " + VideoFileName + CRLF);
        rtspHeader.write("Content-Type: " + "application/sdp" + CRLF);
        rtspHeader.write("Content-Length: " + rtspBody.toString().length() + CRLF);
        rtspHeader.write(CRLF);

        return rtspHeader + rtspBody.toString();
    }
}
