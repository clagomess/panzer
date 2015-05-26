package br.com.gomespro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class Interface {
	private final JTextArea tlog = new JTextArea();
	private final Serial serial = new Serial();
	private final Opcode opcode = new Opcode();
	private final String sysname = "Panzer";
	private final JLabel lpanzer = new JLabel("");
	
	// KEYS
	private static int vbtup = 38;
	private static int vbtright = 39;
	private static int vbtdown = 40;
	private static int vbtleft = 37;
	private static int vbtvup = 87;
	private static int vbtvdown = 83;
	

	public void setLog(String log){
		tlog.append(log);
	}
	
	public void resetLog(){
		tlog.setText("");
	}
	
	public static void alert(String msg){
		JOptionPane.showMessageDialog(null, msg);
		System.out.println(msg);
	}
	
	private JFrame getJanela(int w, int h){
		JFrame janela = new JFrame();
		janela.setTitle(sysname);
		janela.setSize(w,h);
		janela.setResizable(false);
		janela.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		janela.setLocationRelativeTo(null);
		janela.setIconImage(new ImageIcon(Resource.get("img_00.png",sysname)).getImage());
		
		return janela;
	}
	
	public String versao(){
		String slg_versao = "[huebr]";
		
		try{
			String path = Resource.get("VERSAO",sysname);
			BufferedReader fversao = new BufferedReader(new FileReader(path));
			slg_versao = fversao.readLine();
			fversao.close();
		}catch(Exception e){
			System.out.println("Não encontrou RESOURCE");
		}
		
		return slg_versao;
	}
	
	private void setPanzer(int opcode){
		String image = "img_01.png";
		
		switch(opcode){
			case 0x14: image = "img_02.png"; break;
			case 0x1C: image = "img_03.png"; break;
			case 0x1A: image = "img_04.png"; break;
			case 0x12: image = "img_04.png"; break;
			case 0x19: image = "img_05.png"; break;
			case 0x11: image = "img_05.png"; break;
			case 0x16: image = "img_06.png"; break;
			case 0x15: image = "img_07.png"; break;
			case 0x1D: image = "img_08.png"; break;
			case 0x1E: image = "img_09.png"; break;
		}
		
		lpanzer.setIcon(new ImageIcon(Resource.get(image,sysname)));
	}
	
	public void home(){
		JPanel panel;
		
		JFrame janela = this.getJanela(450, 480);
		janela.addWindowListener( new WindowAdapter( ){
			public void windowClosing(WindowEvent w){	
				System.exit(0);
			}
		});
		janela.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		
		//Joystick STATUS		
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setPreferredSize(new Dimension(200, 200));
		final JLabel lopcode = new JLabel("");
		lopcode.setPreferredSize(new Dimension(200, 30));
		lpanzer.setPreferredSize(new Dimension(100, 100));
		lopcode.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.add(lpanzer);
		panel.add(lopcode);
		
		janela.add(panel);
		this.setPanzer(0);
		
		//Comunicação
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Comunicação"));
		panel.setPreferredSize(new Dimension(200, 200));
		panel.add(new JLabel("Porta:"));
		final JTextField porta = new JTextField();
		porta.setText("/dev/ttyACM0");
		porta.setPreferredSize(new Dimension(178, 30));
		panel.add(porta);
		
		JButton bIniciar = new JButton("Iniciar");
		bIniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(porta.getText().equals("")){
					Interface.alert("Digite a porta");
				}else{
					serial.iniciar(porta.getText(), 9600);
					tlog.setText("");
					tlog.append("Iniciando porta: " + porta.getText() + "@9600\n");
					tlog.append((serial.iniciado() ? "Iniciado\n" : "Não Iniciado\n"));
				}
			}
		});
		
		JButton bLogReset = new JButton("Reset Log");
		bLogReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tlog.setText("");
			}
		});	
		
		JButton bFechar = new JButton("Parar");
		bFechar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				serial.close();
				tlog.append("Fim Conexão\n");
			}
		});
		
		final JButton bFocus = new JButton("Focus");
		bFocus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tlog.append("Focus on\n");
				
				serial.enviar(opcode.getOpcode());
				lopcode.setText(Integer.toBinaryString(opcode.getOpcode()));
				setPanzer(opcode.getOpcode());
			}
		});
		
		bFocus.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				int vlopcode = 0;
				
				if(e.getKeyCode() == vbtleft){
					opcode.setDirecao(1);
				}
				
				if(e.getKeyCode() == vbtup){
					opcode.setAcelerado(true);
					opcode.setSentido(0);
				}
				
				if(e.getKeyCode() == vbtright){
					opcode.setDirecao(2);
				}
				
				if(e.getKeyCode() == vbtdown){
					opcode.setAcelerado(true);
					opcode.setSentido(1);
				}
				
				if(e.getKeyCode() == vbtvup){
					vlopcode = opcode.getOpcodeConfig(0x80);
				}
				
				if(e.getKeyCode() == vbtvdown){
					vlopcode = opcode.getOpcodeConfig(0x81);
				}
				
				if(vlopcode == 0){
					vlopcode = opcode.getOpcode();
				}
				
				serial.enviar(vlopcode);
				lopcode.setText(Integer.toBinaryString(vlopcode));
				setPanzer(vlopcode);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == vbtleft || e.getKeyCode() == vbtright){
					opcode.setDirecao(0);
				}
				
				if(e.getKeyCode() == vbtup || e.getKeyCode() == vbtdown){
					opcode.setAcelerado(false);
				}
				
				serial.enviar(opcode.getOpcode());
				lopcode.setText(Integer.toBinaryString(opcode.getOpcode()));
				setPanzer(opcode.getOpcode());
			}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		
		final JButton bTeclas = new JButton("Teclas");
		bTeclas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				remapkey();
			}
		});
		
		panel.add(bIniciar);
		panel.add(bLogReset);
		panel.add(bFechar);
		panel.add(bFocus);
		panel.add(bTeclas);
		janela.add(panel);
		
		// Log
		janela.add(new JLabel("Log:"));
		tlog.setPreferredSize(new Dimension(420, 160));
		tlog.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		janela.add(tlog);		
		
		janela.add(new JLabel("Projeto Panzer Cláudio Gomes Versão.:" + this.versao()));
		janela.setVisible(true);
	}
	
	private void remapkey(){
		JFrame janela = this.getJanela(200, 350);
		janela.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		final JTextField bUp = new JTextField(); 
		bUp.setPreferredSize(new Dimension(174, 30));
		bUp.setText(new Integer(vbtup).toString());
		final JTextField bRight = new JTextField(); 
		bRight.setPreferredSize(new Dimension(174, 30));
		bRight.setText(new Integer(vbtright).toString());
		final JTextField bDown = new JTextField(); 
		bDown.setPreferredSize(new Dimension(174, 30));
		bDown.setText(new Integer(vbtdown).toString());
		final JTextField bLeft = new JTextField(); 
		bLeft.setPreferredSize(new Dimension(174, 30));
		bLeft.setText(new Integer(vbtleft).toString());
		
		JButton bSalvar = new JButton("Salvar");
		bSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vbtup = new Integer(bUp.getText());
				vbtright = new Integer(bRight.getText());
				vbtdown = new Integer(bDown.getText());
				vbtleft = new Integer(bLeft.getText());
				
				JOptionPane.showMessageDialog(null, "Ok!");
			}
		});
		
		JButton bCapturar = new JButton("Capturar");
		bCapturar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame jcap = getJanela(100, 100);
				jcap.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
				jcap.setFocusable(true);
				
				final JLabel label = new JLabel("--");
				
				jcap.add(label);
				
				jcap.addKeyListener(new KeyListener(){
					@Override
					public void keyPressed(KeyEvent e) {
						label.setText(new Integer(e.getKeyCode()).toString());
					}
					@Override
					public void keyReleased(KeyEvent e) {}
					@Override
					public void keyTyped(KeyEvent e) {}
				});
				
				jcap.setVisible(true);
			}
		});
		
		panel.add(new JLabel("^"));
		panel.add(bUp);
		panel.add(new JLabel(">"));
		panel.add(bRight);
		panel.add(new JLabel("v"));
		panel.add(bDown);
		panel.add(new JLabel("<"));
		panel.add(bLeft);
		panel.add(bSalvar);
		panel.add(bCapturar);
		janela.add(panel);
		janela.setVisible(true);
	}
}
