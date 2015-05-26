package br.com.gomespro;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {
	public static void main(String[] args) {
		try{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		}catch(Exception e){
			System.out.print(e.getMessage());
		}
		
		Interface ui = new Interface();
		
		System.out.println("Controle Panzer Ver.:" + ui.versao());
		
		ui.home();
		ui.setLog("Panzer Controle Ver.: " + ui.versao() + "\n");
	}
}
