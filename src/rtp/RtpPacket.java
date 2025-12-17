package rtp;

public class RtpPacket extends RtpPacketDemo {

    public RtpPacket(int PType, int Framenb, int Time, int Mar, byte[] data, int data_length) {
        super(PType, Framenb, Time, Mar, data, data_length);
    }

    public RtpPacket(byte[] packet, int packet_size) {
        super(packet, packet_size);
    }

    @Override
    void setRtpHeader() {
    header[0]=(byte)((Version<<6) |(Padding <<5 ) |(Extension <<4)| CC);
    header[1]=(byte) ((Marker <<7)|PayloadType);
    header[2]=(byte) (SequenceNumber >> 8);
    header[3]=(byte) (SequenceNumber & 0xFF);
    header[4]=(byte) (TimeStamp >> 24);
    header[5]=(byte) (TimeStamp >> 16);
    header[6]=(byte) (TimeStamp >> 8);
    header[7]=(byte) (TimeStamp & 0xFF);
    header[8]=(byte) (Ssrc >> 24);
    header[9]=(byte) (Ssrc >> 16);
    header[10]=(byte) (Ssrc >> 8);
    header[11]=(byte) (Ssrc & 0xFF);
    }
}
