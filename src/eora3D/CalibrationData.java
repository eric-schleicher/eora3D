package eora3D;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

class CalibrationData
{
	// mm
	public double board_w_mm = 160.0f*(403.5f/130.0f);
	public double board_h_mm = 220.0f*(403.5f/130.0f);
	public double spot_r_mm = 2.0f;
	public double spot_sep_w_mm = 130.0f*(403.5f/130.0f);
	public double spot_sep_h_mm = 191.0f*(403.5f/130.0f);
	
	// Calculation inputs
	// pixels
	public int capture_w_pix = 1280;
	public int capture_h_pix = 720;
	public int v_offset_pix = 0;
//	public int v_offset_2 = 0;
	
	private double cal_pos_1_percent = 0.90f;
//	private double cal_pos_2_per = 0.70f;
	
	// Calculation outputs
	public Rectangle pos_1_tl_det_spot; // Rectangle surrounding detection spot
	public Rectangle pos_1_tr_det_spot;
	public Rectangle pos_1_bl_det_spot;
	public Rectangle pos_1_br_det_spot;

/*	public Rectangle pos_2_tl;
	public Rectangle pos_2_tr;
	public Rectangle pos_2_bl;
	public Rectangle pos_2_br;*/
	
	public Rectangle pos_1_board_pix;
//	public Rectangle pos_2_board;
	
	public int v_offset_minmax = 0;

	public int detection_box = 20;
	public int circle_center_threshold = 5;
	public int circle_radius_threshold = 3;
	
	public Point cal_square_bl_pix;
	public Point cal_square_br_pix;
/*	public Point cal_square_tl;
	public Point cal_square_tr;*/
	
	public int m_minz = 0;
	public int m_maxz = 0;
	
	public double focal_length_pix_x = 0.0f;
	public double focal_length_pix_y = 0.0f;
	
	public int m_laser_to_camera_sep_pix = 0;
	
	public double m_pix_to_mm = 1.0f;
	
	void calculate()
	{
		double cal_board_target_h_pix;
		double cal_board_target_w_pix;
		System.out.println("Capture W "+capture_w_pix+" H "+capture_h_pix);
		if(capture_w_pix > capture_h_pix) // landscape
		{
			// use the height as the limit
			cal_board_target_h_pix = ((double)capture_h_pix * cal_pos_1_percent);
			cal_board_target_w_pix = (cal_board_target_h_pix/board_h_mm)*board_w_mm;
		}
		else // portrait
		{
			// Use the width as the limit
			cal_board_target_w_pix = ((double)capture_w_pix * cal_pos_1_percent);
			cal_board_target_h_pix = (cal_board_target_w_pix/board_w_mm)*board_h_mm;
		}
		pos_1_board_pix = new Rectangle();
		pos_1_board_pix.height = (int)cal_board_target_h_pix;
/*		if(Eora3D_MainWindow.m_e3d_config.sm_camera_res_w>=Eora3D_MainWindow.m_e3d_config.sm_camera_res_h)
		{
			System.out.println("cw "+capture_w_pix+" tw "+cal_board_target_w_pix);
			v_offset_minmax = (capture_w_pix - (int)cal_board_target_w_pix)/2;
		}
		else*/
		{
			System.out.println("ch "+capture_h_pix+" th "+cal_board_target_h_pix);
			v_offset_minmax = (capture_h_pix - (int)cal_board_target_h_pix)/2;
		}
		pos_1_board_pix.width = (int)cal_board_target_w_pix;
		pos_1_board_pix.x = (capture_w_pix-pos_1_board_pix.width)/2;
		pos_1_board_pix.y = (capture_h_pix-pos_1_board_pix.height)/2 + v_offset_pix;
		
		pos_1_tl_det_spot = new Rectangle();
		pos_1_tl_det_spot.width = (int)(((cal_board_target_w_pix/board_w_mm)*spot_r_mm))*2;
		pos_1_tl_det_spot.height = (int)(((cal_board_target_h_pix/board_h_mm)*spot_r_mm))*2;
		pos_1_tl_det_spot.x = (int)(capture_w_pix -((cal_board_target_w_pix/board_w_mm)*spot_sep_w_mm))/2 - pos_1_tl_det_spot.width;
		pos_1_tl_det_spot.y = (int)(capture_h_pix -((cal_board_target_h_pix/board_h_mm)*spot_sep_h_mm))/2 - pos_1_tl_det_spot.height + v_offset_pix;

		pos_1_tr_det_spot = new Rectangle();
		pos_1_tr_det_spot.width = pos_1_tl_det_spot.width;
		pos_1_tr_det_spot.height = pos_1_tl_det_spot.height;
		pos_1_tr_det_spot.x = (int)(capture_w_pix - pos_1_tl_det_spot.x) - pos_1_tl_det_spot.width*2;
		pos_1_tr_det_spot.y = pos_1_tl_det_spot.y;

		pos_1_bl_det_spot = new Rectangle();
		pos_1_bl_det_spot.width = pos_1_tl_det_spot.width;
		pos_1_bl_det_spot.height = pos_1_tl_det_spot.height;
		pos_1_bl_det_spot.x = pos_1_tl_det_spot.x;
		pos_1_bl_det_spot.y = (int)(capture_h_pix - pos_1_tl_det_spot.y) - pos_1_tl_det_spot.height*2 + v_offset_pix*2;

		pos_1_br_det_spot = new Rectangle();
		pos_1_br_det_spot.width = pos_1_tl_det_spot.width;
		pos_1_br_det_spot.height = pos_1_tl_det_spot.height;
		pos_1_br_det_spot.x = (int)(capture_w_pix - pos_1_tl_det_spot.x) - pos_1_tl_det_spot.width*2;
		pos_1_br_det_spot.y = (int)(capture_h_pix - pos_1_tl_det_spot.y) - pos_1_tl_det_spot.height*2 + v_offset_pix*2;

	
/*		target_h = ((double)capture_h * cal_pos_2_per);
		target_w = (target_h/board_h)*board_w;
		v_offset_2 = (int)(((double)v_offset) * (cal_pos_2_per/cal_pos_1_per));
		pos_2_board = new Rectangle();
		pos_2_board.height = (int)target_h;
		pos_2_board.width = (int)target_w;
		pos_2_board.x = (capture_w-pos_2_board.width)/2;
		pos_2_board.y = (capture_h-pos_2_board.height)/2 + v_offset_2;
		
		pos_2_tl = new Rectangle();
		pos_2_tl.width = (int)(((target_w/board_w)*spot_r))*2;
		pos_2_tl.height = (int)(((target_h/board_h)*spot_r))*2;
		pos_2_tl.x = (int)(capture_w -((target_w/board_w)*spot_sep_w))/2 - pos_2_tl.width;
		pos_2_tl.y = (int)(capture_h -((target_h/board_h)*spot_sep_h))/2 - pos_2_tl.height + v_offset_2;

		pos_2_tr = new Rectangle();
		pos_2_tr.width = pos_2_tl.width;
		pos_2_tr.height = pos_2_tl.height;
		pos_2_tr.x = (int)(capture_w - pos_2_tl.x) - pos_2_tl.width*2;
		pos_2_tr.y = pos_2_tl.y;

		pos_2_bl = new Rectangle();
		pos_2_bl.width = pos_2_tl.width;
		pos_2_bl.height = pos_2_tl.height;
		pos_2_bl.x = pos_2_tl.x;
		pos_2_bl.y = (int)(capture_h - pos_2_tl.y) - pos_2_tl.height*2 + v_offset_2*2;

		pos_2_br = new Rectangle();
		pos_2_br.width = pos_2_tl.width;
		pos_2_br.height = pos_2_tl.height;
		pos_2_br.x = (int)(capture_w - pos_2_tl.x) - pos_2_tl.width*2;
		pos_2_br.y = (int)(capture_h - pos_2_tl.y) - pos_2_tl.height*2 + v_offset_2*2;*/
	}
	
	void calculateBaseCoords()
	{//cal_square_bl
		double l_to_camera_steps = Eora3D_MainWindow.m_e3d_config.sm_laser_0_offset+Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg*90;
		double alpha = (l_to_camera_steps - Eora3D_MainWindow.m_e3d_config.sm_calibration_tr_motorpos_1)/Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg;
		double beta = (l_to_camera_steps - Eora3D_MainWindow.m_e3d_config.sm_calibration_tl_motorpos_1)/Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg;
//		double alpha_prime = (l_to_camera_steps - Eora3D_MainWindow.m_e3d_config.sm_calibration_tr_motorpos_2)/Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg;
//		double beta_prime = (l_to_camera_steps - Eora3D_MainWindow.m_e3d_config.sm_calibration_tl_motorpos_2)/Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg;
		
		double x, y;
		
		cal_square_bl_pix = new Point();
		cal_square_br_pix = new Point();
/*		cal_square_tl = new Point();
		cal_square_tr = new Point();*/
		
		double l_spot_sep_pix_x = pos_1_tr_det_spot.x - pos_1_tl_det_spot.x;
		double l_spot_sep_pix_y = pos_1_bl_det_spot.y - pos_1_tl_det_spot.y;
		
		x = ((l_spot_sep_pix_x)*Math.tan(Math.toRadians(alpha)))/(Math.tan(Math.toRadians(beta))-Math.tan(Math.toRadians(alpha)));
		cal_square_bl_pix.x = (int)x;
		y = x*Math.tan(Math.toRadians(beta));
		cal_square_bl_pix.y = (int)y;
		cal_square_br_pix.y = (int)y;
		cal_square_br_pix.x = (int) (cal_square_bl_pix.x + l_spot_sep_pix_x);
		
/*		x = (((double)pos_2_board.width)*Math.tan(Math.toRadians(alpha_prime)))/(Math.tan(Math.toRadians(beta_prime))-Math.tan(Math.toRadians(alpha_prime)));
		cal_square_tl.x = (int)x;
		y = x*Math.tan(Math.toRadians(beta_prime));
		cal_square_tl.y = (int)y;
		cal_square_tr.y = (int)y;
		cal_square_tr.x = cal_square_bl.x + pos_2_board.width;*/
		
		// Calculate focal length
		x = (((double)spot_sep_w_mm)*Math.tan(Math.toRadians(alpha)))/(Math.tan(Math.toRadians(beta))-Math.tan(Math.toRadians(alpha)));
		y = x*Math.tan(Math.toRadians(beta));
		double d_mm = y;
		double h_pix = (l_spot_sep_pix_x)/*(spot_sep_w_mm/board_w_mm)*/;
		double H_mm = spot_sep_w_mm;
		focal_length_pix_x = (d_mm*h_pix)/H_mm;

		System.out.println("spot sep in pix is "+h_pix);
		
		m_pix_to_mm = spot_sep_w_mm / h_pix; 

		d_mm = y;
		h_pix = l_spot_sep_pix_y /**(spot_sep_h_mm/board_h_mm)*/;
		H_mm = spot_sep_h_mm;
		focal_length_pix_y = (d_mm*h_pix)/H_mm;
//		focal_length_pix_y = focal_length_pix_x;
		//focal_length_pix *= 1.1f;
		System.out.println("X focal length in pixels: "+focal_length_pix_x);
		System.out.println("Y focal length in pixels: "+focal_length_pix_y);
		System.out.println("pix_to_mm: "+m_pix_to_mm);

		// Camera center to laser center separation
		m_laser_to_camera_sep_pix = cal_square_bl_pix.x + pos_1_board_pix.width/2;
		//m_laser_to_camera_sep_pix *= 1.2f;
		System.out.println("Camera laser sep pixels: "+m_laser_to_camera_sep_pix);
		
		/*
		float board_left = 36;//pos_1_tl.x;
		float board_right = 684;//pos_1_tr.x;
		RGB3DPoint bl = getPointOffset(Eora3D_MainWindow.m_e3d_config.sm_calibration_tl_motorpos_1, (int)board_left, 0);
		RGB3DPoint br = getPointOffset(Eora3D_MainWindow.m_e3d_config.sm_calibration_tr_motorpos_1, (int)board_right, 0);
		
		int board_width_pixels = bl.m_x - br.m_x;
		
		//m_pix_to_mm = spot_sep_w/board_width_pixels;
		
		System.out.println("bl x : "+bl.m_x+" br x: "+br.m_x+" spot_sep_w "+spot_sep_w+" bwp "+board_width_pixels+" pix to mm "+m_pix_to_mm);*/
	}
	
	RGB3DPoint getPointOffset(int a_angle_steps, int screen_x, int screen_y)
	{
		double l_to_camera_steps = Eora3D_MainWindow.m_e3d_config.sm_laser_0_offset+Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg*90;
//		double alpha = (l_to_camera_steps - Eora3D_MainWindow.m_e3d_config.sm_calibration_tl_motorpos_1)/Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg;
		double C = (l_to_camera_steps - a_angle_steps)/Eora3D_MainWindow.m_e3d_config.sm_laser_steps_per_deg;
		
		double x_pos = (double)screen_x - ((double)capture_w_pix/2.0f) ;
		
		double alpha = Math.toDegrees(Math.atan(x_pos/focal_length_pix_x));
		double A = 90.0f + alpha;
		double B = 180.0f - A - C;
		double b = m_laser_to_camera_sep_pix;
		
		double c = (b*Math.sin(Math.toRadians(C))) / (Math.sin(Math.toRadians(B)));
		
		double z = c * Math.sin(Math.toRadians(A));
		double x = c * Math.cos(Math.toRadians(A));
		
		double y = z * (screen_y-capture_h_pix/2.0f)/focal_length_pix_y;
		
		//System.out.println("In: angle: "+a_angle_steps+" x: "+screen_x+" y: "+screen_y+" Out: x: "+x+"y: "+y+" z: "+z);
		
		RGB3DPoint l_point = new RGB3DPoint(/*capture_w-*/(int)x, (int)y, (int)z);
		return l_point;
	}
	
	boolean checkThreshold(int r, int g, int b)
	{
		if(Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_logic.contentEquals("Or"))
		{
    		if(r>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_r ||
    				g>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_g ||
    				b>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_b) return true;
		} else if(Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_logic.contentEquals("And"))
		{
    		if(r>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_r &&
    				g>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_g &&
    				b>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_b) return true;
		} else if(Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_logic.contentEquals("%"))
		{
			float pc = ((float)r/255.0f + (float)g/255.0f + (float)b/255.0f)*100.0f;
    		if(pc>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_percent) return true;
		} else if(Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_logic.contentEquals("Weighted %"))
		{
			float pc = (((float)r/255.0f)*40.0f + ((float)g/255.0f)*20.0f + ((float)b/255.0f)*40.0f);
    		if(pc>Eora3D_MainWindow.m_e3d_config.sm_laser_detection_threshold_percent) return true;
		}
		return false;
	}
	
	int findLaserPoint(BufferedImage a_base, BufferedImage a_in, int a_y, int a_x_min, int a_x_max)
	{
		for(int x = a_x_max-1; x>=a_x_min; --x)
		{
			int argb = a_in.getRGB(x, a_y);
			int r = (argb & 0xff0000) >> 16;
        	int g = (argb & 0x00ff00) >> 8;
    		int b = (argb & 0x0000ff);
    		
			int argb_base = a_base.getRGB(x, a_y);
			int r_base = (argb_base & 0xff0000) >> 16;
        	int g_base = (argb_base & 0x00ff00) >> 8;
    		int b_base = (argb_base & 0x0000ff);
    		
    		int rd = Math.abs(r_base-r);
    		int gd = Math.abs(g_base-g);
    		int bd = Math.abs(b_base-b);

    		if(checkThreshold(rd, gd, bd)) return x;
		}
		return -1;
	}
	
}