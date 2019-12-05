package eora3D;

import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JButton;
import eora3D.eora3D_bluetooth;
import tinyb.BluetoothDevice;

public class Eora3D_MainWindow extends JDialog implements ActionListener {
	static eora3D_bluetooth m_e3D_bluetooth;
	private JLabel laser_selector;
	private BluetoothDevice laser = null;
	private JLabel turntable_selector;
	private BluetoothDevice turntable = null;
	private JComboBox<String> camera_selector;
	
	public Eora3D_MainWindow()
	{
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblEoradLaserTurret = new JLabel("Eora3D Laser Turret");
		lblEoradLaserTurret.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEoradLaserTurret.setBounds(10, 11, 170, 30);
		getContentPane().add(lblEoradLaserTurret);
		
		laser_selector = new JLabel();
		laser_selector.setText("Not found");
		laser_selector.setBounds(10, 52, 423, 30);
		getContentPane().add(laser_selector);
		
		JLabel lblEoradTurntable = new JLabel("Eora3D Turntable");
		lblEoradTurntable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEoradTurntable.setBounds(10, 93, 249, 30);
		getContentPane().add(lblEoradTurntable);
		
		turntable_selector = new JLabel();
		turntable_selector.setText("Not found");
		turntable_selector.setBounds(10, 134, 423, 30);
		getContentPane().add(turntable_selector);
		
		JLabel lblCamera = new JLabel("Camera");
		lblCamera.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCamera.setBounds(10, 175, 249, 30);
		getContentPane().add(lblCamera);
		
		camera_selector = new JComboBox<String>();
		camera_selector.setBounds(10, 216, 423, 30);
		getContentPane().add(camera_selector);
		
		JButton btnBluetoothRescan = new JButton("Bluetooth Rescan");
		btnBluetoothRescan.setBounds(20, 258, 160, 23);
		getContentPane().add(btnBluetoothRescan);
		btnBluetoothRescan.addActionListener(this);
		
		JButton btnCameraRescan = new JButton("Camera Rescan");
		btnCameraRescan.setBounds(189, 258, 160, 23);
		getContentPane().add(btnCameraRescan);
		btnCameraRescan.addActionListener(this);
		
		JButton btndScanning = new JButton("3D Scanning");
		btndScanning.setBounds(361, 258, 116, 23);
		getContentPane().add(btndScanning);
		btndScanning.addActionListener(this);
		
		JButton btnLasertest = new JButton("Laser test");
		btnLasertest.setBounds(20, 292, 117, 25);
		getContentPane().add(btnLasertest);
		btnLasertest.addActionListener(this);
		
		setSize(449+16, 320+64);
		
		setVisible(true);
		
		Bluetooth_Rescan();
	}
	
	void Bluetooth_Rescan()
	{
		if(m_e3D_bluetooth == null) m_e3D_bluetooth = new eora3D_bluetooth();
		
		getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		m_e3D_bluetooth.discover();
		getContentPane().setCursor(Cursor.getDefaultCursor());
		laser_selector.setText("Not found");
		turntable_selector.setText("Not found");
		if(m_e3D_bluetooth.devices!=null)
		{
			for (BluetoothDevice device : m_e3D_bluetooth.devices) {
				if(device.getName().substring(0, 4).equals("E3DS"))
				{
					laser_selector.setText(device.getName());
					laser = device;
					m_e3D_bluetooth.setLaser(laser);
			        System.out.print("Found laser: ");
			        eora3D_bluetooth.printDevice(laser);
					break;
				}
			}
		}
		if(m_e3D_bluetooth.devices!=null)
		{
			for (BluetoothDevice device : m_e3D_bluetooth.devices) {
				if(device.getName().substring(0, 4).equals("E3DT"))
				{
					turntable_selector.setText(device.getName());
					turntable = device;
					m_e3D_bluetooth.setTurntable(turntable);
			        System.out.print("Found turntable: ");
			        eora3D_bluetooth.printDevice(turntable);
					break;
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="Bluetooth Rescan")
		{
			Bluetooth_Rescan();
			return;
		}
		if(e.getActionCommand()=="Laser test")
		{
			if(m_e3D_bluetooth==null || laser==null) return;
			new eora3D_laser_controller(m_e3D_bluetooth).setVisible(true);
			return;
		}
	}
}