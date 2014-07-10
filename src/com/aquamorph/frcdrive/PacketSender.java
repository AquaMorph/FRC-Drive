package com.aquamorph.frcdrive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.TextView;

import com.aquamorph.frcdrive.Packets;
import com.aquamorph.frcdrive.R;
import com.aquamorph.frcdrive.UIManager;

public class PacketSender extends Thread{
	
	private int packetIndex;;
	private byte teamNum1;
	private byte teamNum2;
	private DatagramSocket sock;
	private Packets rioPacket = new Packets();
	private DatagramPacket packet;
	private TextView messageLog;
	private Activity activity;
	private UIManager ui;
	private PhysicalJoystick phyJoy;
	private boolean isRobotNet = false;
	
	public PacketSender(Activity activity, UIManager uiMngr, PhysicalJoystick physicalJoystick){
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
	

	
	//Thread function.
	@Override
	public void run(){
		//Set all of the fields in the packet and send it, then wait for 20 ms.
			try {
				while(isRobotNet){
					for(int i = 0; i < 12; i++)	{
						rioPacket.setButton(i, ui.Joy1Bttns[i],1);
						rioPacket.setButton(i, ui.Joy2Bttns[i],2);
					}
					for(int i = 0; i < 8; i++)	{
						rioPacket.setDigitalIn(i, true);
					}
					rioPacket.setIndex(packetIndex);
					rioPacket.setJoystick(ui.throttleAxis1, ui.joy1Y, Packets.JOY1);
					rioPacket.setJoystick(ui.joy2X, ui.joy2Y, Packets.JOY2);
					rioPacket.setJoystick(phyJoy.joyPhy1X, phyJoy.joyPhy1Y, Packets.JOY3);
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