package com.aquamorph.frcdrive;

import java.util.zip.CRC32;

public class CRIOPacket {
	public byte[] data = new byte[1024]; // The byte array to hold everything.

	// Constants:
	static int JOY1 = 8, JOY2 = 16, JOY3 = 24, JOY4 = 32;

	/*
	 * data[0] and data[1]: packet index data[2]: control byte: reset not_e_stop
	 * enabled auto fms_attached resync test fpga data[3]: digital input bits.
	 * data[4] and data[5]: team number data[6]: alliance, either red 'R' or
	 * blue 'B' data[7]: position data[8]: joystick 1 x value data[9]: joystick
	 * 1 y value data[14-15]: joystick 1 buttons bits backward format ...8 7 6 5
	 * 4 3 2 1 data[1020] to data[1023]: CRC checksum.
	 */
	public CRIOPacket() {
		data[2] |= 64; // Set the not_e_stop control bit.

		// Diverstation version, not sure if it needs this or not,
		// I just used the numbers on the packets captured from the original
		// driver station.
		data[72] = (byte) 0x30;
		data[73] = (byte) 0x31;
		data[74] = (byte) 0x30;
		data[75] = (byte) 0x34;
		data[76] = (byte) 0x31;
		data[77] = (byte) 0x34;
		data[78] = (byte) 0x30;
		data[79] = (byte) 0x30;
	}

	// Returns the low byte of a short.
	private byte int4(int i) {
		return (byte) (i & 0xFF);
	}

	// Returns the high byte of a short.
	private byte int3(int i) {
		return (byte) ((i & 0xFF00) >> 8);
	}

//	private byte int2(int i) {
//		return (byte) ((i & 0xFF0000) >> 16);
//	}
//
//	private int int1(int i) {
//		return (byte) ((i & 0xFF000000) >> 24);
//	}

	// Set the packet index to the provided value
	public void setIndex(int index) {
		data[0] = int3(index);
		data[1] = int4(index);
	}

	// Set robot to autonomous mode
	public void setAuto(boolean auto) {
		if (auto)
			data[2] |= 16;
		else
			data[2] &= ~16;
	}

	public void setEnabled(boolean enabled) {
		if (enabled)
			data[2] |= 32;
		else
			data[2] &= ~32;
	}

	// Set digital inputs, port is the digital io port number with base 0.
	public void setDigitalIn(int port, boolean set) {
		if (set)
			data[3] |= (byte) (128 / Math.pow(2, port));
		else
			data[3] &= ~((byte) (128 / Math.pow(2, port)));
	}

	// Set the team number Convert to string and concatenate, then convert back
	// to int as a whole number.
	public void setTeam(byte byte1, byte byte2) {
		int num = Integer.parseInt(Byte.toString(byte1) + Byte.toString(byte2));
		byte byt1 = (byte) ((num & 0xff00) >> 8); // Get first byte.
		byte byt2 = (byte) (num & 0xff); // Second byte.

		data[4] = byt1;
		data[5] = byt2;
	}

	// Set alliance, either red 'R' or blue 'B'
	public void setAlliance(char redOrBlue) {
		data[6] = (byte) redOrBlue;
	}

	// Set team position.
	public void setPosition(int pos) {
		data[7] = (byte) pos;
	}

	// Set the joystick
	public void setJoystick(byte x, byte y, int joystick) {
		data[joystick] = x;
		data[joystick + 1] = (byte) -y;
	}

	//Set the buttons
	public void setButton(int button, boolean set, int joystick) {
		if (set){
			if(joystick==1){
				if(button<8)data[15] |= (byte) (Math.pow(2, button));
				else data[14] |= (byte) (Math.pow(2, (button-8)));
			} else if(joystick==2){
				if(button<8)data[23] |= (byte) (Math.pow(2, button));
				else data[22] |= (byte) (Math.pow(2, (button-8)));
			}
		}
		else {
			if(joystick==1){
				data[15] &= ~((byte) Math.pow(2, button));
				data[14] &= ~((byte) Math.pow(2, button));
			} else if(joystick==2){
				data[23] &= ~((byte) Math.pow(2, button));
				data[22] &= ~((byte) Math.pow(2, button));
			}
		}
	}

	public void makeCRC() {
		CRC32 crc = new CRC32();
		crc.update(data);
		long checksum = crc.getValue();
		data[1020] = (byte) ((checksum & 0xff000000) >> 24);
		data[1021] = (byte) ((checksum & 0xff0000) >> 16);
		data[1022] = (byte) ((checksum & 0xff00) >> 8);
		data[1023] = (byte) (checksum & 0xff);
	}

	public void clearCRC() {
		data[1020] = 0;
		data[1021] = 0;
		data[1022] = 0;
		data[1023] = 0;
	}
}
