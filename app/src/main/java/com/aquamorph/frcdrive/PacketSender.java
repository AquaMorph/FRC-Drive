package com.aquamorph.frcdrive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class PacketSender extends Thread{

    private int packetIndex;;
    private byte teamNum1;
    private byte teamNum2;
    private DatagramSocket sock;
    private Packets rioPacket = new Packets();
    private DatagramPacket packet;
    private TextView messageLog;
    private Activity activity;
    private Controls ui;
    private PhysicalJoystick phyJoy;
    private int leftjoy;
    private int rightjoy;
    private int physicaljoy;
    private int leftthrottle;
    private int rightthrottle;
    private boolean isRobotNet = false;
    private byte[] joystick1Axis= new byte[6];
    private byte[] joystick2Axis= new byte[6];
    private byte[] joystick3Axis= new byte[6];
    private byte[] joystick4Axis= new byte[6];

    public PacketSender(Activity activity, Controls uiMngr, PhysicalJoystick physicalJoystick) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
        leftjoy = Integer.parseInt(settings.getString("leftjoy", "1"));
        rightjoy = Integer.parseInt(settings.getString("rightjoy", "2"));
        physicaljoy = Integer.parseInt(settings.getString("physicaljoy", "3"));
        leftthrottle = Integer.parseInt(settings.getString("leftthrottle", "3"))-1;
        rightthrottle = Integer.parseInt(settings.getString("rightthrottle", "3"))-1;
        this.activity = activity;
        ui = uiMngr;
        phyJoy = physicalJoystick;

        //Set up an error console.
        messageLog = (TextView)activity.findViewById(R.id.control_log);

        //Find the team number from current ip address.
        WifiManager wifi = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
        int address = wifi.getConnectionInfo().getIpAddress();
        teamNum1 = (byte)((address & 0xff00)>>8);
        teamNum2 = (byte)((address & 0xff0000)>>16);

        //Set up the datagram packet.
        InetAddress crioAddr;
        try {
            crioAddr = InetAddress.getByAddress(new byte[]
                    {10, teamNum1, teamNum2, 2});
            postMessage("Robot IP " + crioAddr.toString());		//Tell the user the robot's IP
            packet = new DatagramPacket(rioPacket.data, 1024, crioAddr, 1110);
            if((address & 0xff) != 10 && (address & 0xff000000)>>24 != 6)	//Let the application know if it is
            {																//not connected to a robot network.
                isRobotNet = false;
            } else {
                isRobotNet = true;
            }
        } catch (UnknownHostException e) {
            postMessage("Packet Error: " + e.getMessage());
            e.printStackTrace();
        }


        //Make a socket
        try {
            sock = new DatagramSocket();
        } catch (SocketException e) {
            postMessage("Network Error: " + e.getMessage());
        }

        if(isRobotNet) {
            rioPacket.setAlliance('R');
            rioPacket.setPosition(0x36);
            rioPacket.setTeam(teamNum1, teamNum2);
        }
    }

    public byte[] arrayReset(byte[] array) {
        for(int i=0;array.length>i;i++) {
            array[i]=(byte) 0;
        }
        return array;
    }



    //Thread function.
    @Override
    public void run(){
        //Set all of the fields in the packet and send it, then wait for 20 ms.
        try {
            while(isRobotNet) {
                arrayReset(joystick1Axis);
                arrayReset(joystick2Axis);
                arrayReset(joystick3Axis);
                arrayReset(joystick4Axis);

                //Left onscreen controls
                if(leftjoy==1) {
                    joystick1Axis[0]=(byte) (joystick1Axis[0]+ui.joy1X);
                    joystick1Axis[1]=(byte) (joystick1Axis[1]+ui.joy1Y);
                    joystick1Axis[leftthrottle]=(byte) (joystick1Axis[leftthrottle]+ui.throttleAxis1);
                } else if(leftjoy==2) {
                    joystick2Axis[0]=(byte) (joystick2Axis[0]+ui.joy1X);
                    joystick2Axis[1]=(byte) (joystick2Axis[1]+ui.joy1Y);
                    joystick2Axis[leftthrottle]=(byte) (joystick2Axis[leftthrottle]+ui.throttleAxis1);
                } else if(leftjoy==3) {
                    joystick3Axis[0]=(byte) (joystick3Axis[0]+ui.joy1X);
                    joystick3Axis[1]=(byte) (joystick3Axis[1]+ui.joy1Y);
                    joystick3Axis[leftthrottle]=(byte) (joystick3Axis[leftthrottle]+ui.throttleAxis1);
                } else if(leftjoy==4) {
                    joystick4Axis[0]=(byte) (joystick4Axis[0]+ui.joy1X);
                    joystick4Axis[1]=(byte) (joystick4Axis[1]+ui.joy1Y);
                    joystick4Axis[leftthrottle]=(byte) (joystick4Axis[leftthrottle]+ui.throttleAxis1);
                }

                //Right onscreen controls
                if(rightjoy==1) {
                    joystick1Axis[0]=(byte) (joystick1Axis[0]+ui.joy2X);
                    joystick1Axis[1]=(byte) (joystick1Axis[1]+ui.joy2Y);
                    joystick1Axis[rightthrottle]=(byte) (joystick1Axis[rightthrottle]+ui.throttleAxis2);
                } else if(rightjoy==2) {
                    joystick2Axis[0]=(byte) (joystick2Axis[0]+ui.joy2X);
                    joystick2Axis[1]=(byte) (joystick2Axis[1]+ui.joy2Y);
                    joystick2Axis[rightthrottle]=(byte) (joystick2Axis[rightthrottle]+ui.throttleAxis2);
                } else if(rightjoy==3) {
                    joystick3Axis[0]=(byte) (joystick3Axis[0]+ui.joy2X);
                    joystick3Axis[1]=(byte) (joystick3Axis[1]+ui.joy2Y);
                    joystick3Axis[rightthrottle]=(byte) (joystick3Axis[rightthrottle]+ui.throttleAxis2);
                } else if(rightjoy==4) {
                    joystick4Axis[0]=(byte) (joystick4Axis[0]+ui.joy2X);
                    joystick4Axis[1]=(byte) (joystick4Axis[1]+ui.joy2Y);
                    joystick4Axis[rightthrottle]=(byte) (joystick4Axis[rightthrottle]+ui.throttleAxis2);
                }

                //Physical controls
                if(physicaljoy==1) {
                    joystick1Axis[0]=(byte) (joystick1Axis[0]+phyJoy.joyPhy1X);
                    joystick1Axis[1]=(byte) (joystick1Axis[1]+phyJoy.joyPhy1Y);
                    joystick1Axis[2]=(byte) (joystick1Axis[2]+phyJoy.joyPhy2X);
                    joystick1Axis[3]=(byte) (joystick1Axis[3]+phyJoy.joyPhy2Y);
                } else if(physicaljoy==2) {
                    joystick2Axis[0]=(byte) (joystick2Axis[0]+phyJoy.joyPhy1X);
                    joystick2Axis[1]=(byte) (joystick2Axis[1]+phyJoy.joyPhy1Y);
                    joystick2Axis[2]=(byte) (joystick2Axis[2]+phyJoy.joyPhy2X);
                    joystick2Axis[3]=(byte) (joystick2Axis[3]+phyJoy.joyPhy2Y);
                } else if(physicaljoy==3) {
                    joystick3Axis[0]=(byte) (joystick3Axis[0]+phyJoy.joyPhy1X);
                    joystick3Axis[1]=(byte) (joystick3Axis[1]+phyJoy.joyPhy1Y);
                    joystick3Axis[2]=(byte) (joystick3Axis[2]+phyJoy.joyPhy2X);
                    joystick3Axis[3]=(byte) (joystick3Axis[3]+phyJoy.joyPhy2Y);
                } else if(physicaljoy==4) {
                    joystick4Axis[0]=(byte) (joystick4Axis[0]+phyJoy.joyPhy1X);
                    joystick4Axis[1]=(byte) (joystick4Axis[1]+phyJoy.joyPhy1Y);
                    joystick4Axis[2]=(byte) (joystick4Axis[2]+phyJoy.joyPhy2X);
                    joystick4Axis[3]=(byte) (joystick4Axis[3]+phyJoy.joyPhy2Y);
                }

                //Buttons
                for(int i = 0; i < 12; i++)	{
                    rioPacket.setButton(i, ui.Joy1Bttns[i],leftjoy);
                    rioPacket.setButton(i, ui.Joy2Bttns[i],rightjoy);
                    rioPacket.setButton(i, phyJoy.Joy3Bttns[i],physicaljoy);
                }
                for(int i = 0; i < 8; i++)	{
                    rioPacket.setDigitalIn(i, true);
                }
                rioPacket.setIndex(packetIndex);
                rioPacket.setJoystick(joystick1Axis[0], joystick1Axis[1], joystick1Axis[2], joystick1Axis[3], joystick1Axis[4], joystick1Axis[5], Packets.JOY1);
                rioPacket.setJoystick(joystick2Axis[0], joystick2Axis[1], joystick2Axis[2], joystick2Axis[3], joystick2Axis[4], joystick2Axis[5], Packets.JOY2);
                rioPacket.setJoystick(joystick3Axis[0], joystick3Axis[1], joystick3Axis[2], joystick3Axis[3], joystick3Axis[4], joystick3Axis[5], Packets.JOY3);
                rioPacket.setJoystick(joystick4Axis[0], joystick4Axis[1], joystick4Axis[2], joystick4Axis[3], joystick4Axis[4], joystick4Axis[5], Packets.JOY4);
                rioPacket.setAuto(ui.auto);
                rioPacket.setEnabled(ui.enabled);
                rioPacket.makeCRC();
                packet.setData(rioPacket.data);
                sock.send(packet);
                rioPacket.clearCRC();
                packetIndex++;
                Thread.sleep(20);
            }
            postMessage("Robot not connected");
        } catch (IOException e) {
            postMessage("Network Error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            postMessage("Interruption Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    //Function to print a message to the error console.
    private void postMessage(final String s){
        activity.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                messageLog.setText(s + '\n' + messageLog.getText());
            }
        });
    }
}