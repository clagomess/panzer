package br.com.gomespro;

/**
 * DOC:
 * Sentido: (0 = Frente, 1 = Ré)
 * Acelerado: (boolean)
 * Direção: (10 = Esqueda, 01 = direita, 00 = Reto)
 */
public class Opcode {
	private int sentido, acelerado, direcao, opcode;
	
	public Opcode(){
		this.setSentido(0);
		this.setAcelerado(false);
		this.setDirecao(0);
	}
	
	public void setSentido(int sentido) {
		if(sentido == 1){
			this.sentido = 0x1F; //Ré - 11111
		}else{
			this.sentido = 0x17; //Frente - 10111
		}
	}
	
	public void setAcelerado(boolean acelerado) {		
		if(acelerado){
			this.acelerado = 0x1F; //Acelerado - 11111
		}else{
			this.acelerado = 0x1B; //Parado - 11011
		}
	}
	
	/**
	 * @param direcao (1 = esquerda, 2 direita, 0 = reto)
	 */
	public void setDirecao(int direcao) {
		if(direcao == 1){
			this.direcao = 0x1E; //Esquerda - 11110
		}else if(direcao == 2){
			this.direcao = 0x1D; //Direita - 11101
		}else{
			this.direcao = 0x1C; //Reto - 11100
		}
	}
	
	public int getOpcode() {		
		this.opcode = 0x1F;
		this.opcode = (this.opcode & this.sentido);
		this.opcode = (this.opcode & this.acelerado);
		this.opcode = (this.opcode & this.direcao);
		
		return opcode;
	}
	
	public int getOpcodeConfig(int confignum){
		this.opcode = 0xFF;
		this.opcode = (this.opcode & confignum);
		return this.opcode;
	}
}
