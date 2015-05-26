package br.com.gomespro;

import java.io.IOException;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

public class Serial {
	private OutputStream serialOut;
	private SerialPort port;
	private boolean iniciado = false;
	
	public boolean iniciado(){
		return iniciado;
	}

	public void iniciar(String porta, int taxa) {
		try {
			CommPortIdentifier portId = null;
			
			System.setProperty("gnu.io.rxtx.SerialPorts", porta);
			
			try {
				portId = CommPortIdentifier.getPortIdentifier(porta);
			} catch (NoSuchPortException e) {
				Interface.alert("Porta não encontrada");
			}
			
			port = (SerialPort) portId.open("Panzer", 2000);
			port.setSerialPortParams(taxa, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
			serialOut = port.getOutputStream();
			
			iniciado = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if(iniciado){
			try {
				serialOut.close();
				port.close();
				iniciado = false;
			} catch (IOException e) {
				Interface.alert("Não foi possivel fechar conexão");
			}
		}
	}
	
	public void enviar(int valor) {
		if(iniciado){
			try {
				serialOut.write(valor);
			} catch (IOException e) {
				Interface.alert("Não foi possivel enviar dados");
			}
		}
	}
}
