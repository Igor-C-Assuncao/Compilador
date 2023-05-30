/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

/**
 *
 * @author tarci
 */

public class Sintatico2 {
    private Lexico lexico;
    private Token token;
    
    public Sintatico2(Lexico lexico){
        this.lexico = lexico;
    }
    
    public void S(){//S determina estado inicial
       this.token = this.lexico.nextToken();

        if(!token.getLexema().equals("main")){
            throw new RuntimeException("Oxe, cadê main?" + "Começasse errado com  -->  [" +  this.token.getLexema() + "]");
        }
        
        this.token = this.lexico.nextToken();

        if(!token.getLexema().equals("(")){
            throw new RuntimeException("Abre o parêntese do main cabra!");
        }
        
        this.token = this.lexico.nextToken();


        if(!token.getLexema().equals(")")){
            throw new RuntimeException("Fechar o parêntese do main cabra!");
        }        
        this.token = this.lexico.nextToken();
        
        this.B();
        if(this.token.getTipo() == Token.TIPO_FIM_CODIGO){
            System.out.println("O Código tá massa! Arretado! Tu botou pra torar!");        
        }else{
            throw new RuntimeException("Oxe, eu deu bronca preto do fim do programa.");
        }
    }
    
    private void B(){
        if(!this.token.getLexema().equals("{")){
            throw new RuntimeException("Oxe, tave esperando um \"{\" pertinho de " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();

        this.DECLARACAO();


        while (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getLexema().equals("{") || this.token.getLexema().equals("while") || this.token.getLexema().equals("if")) {
            this.comando();
            
        }
        if(!this.token.getLexema().equals("}")){
            throw new RuntimeException("Oxe, tava esperando um \"}\" pertinho de " + this.token.getLexema());
        }        
        this.token = this.lexico.nextToken();       

    }
    
    private void decl_ar() {

        if (this.token.getLexema().equals("int") || this.token.getLexema().equals("float")
        || this.token.getLexema().equals("char")) {
            this.token = this.lexico.nextToken();

        }else {
            throw new RuntimeException("tai ddido, tu tem que informat o tipo da id (int, float, char) pertim de  "
                    + this.token.getLexema().toString());
        }
    }

    


    private void comando(){
        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getLexema().equals("{")) {
            this.comandoBasico();

        } else if (this.token.getLexema().equals("while")) {
            this.interacao();

        } else if (this.token.getLexema().equals("if")) {
            this.token = this.lexico.nextToken();

            if (!this.token.getLexema().equals("(")) {
                throw new RuntimeException( "meu irmao cade o parentese --> (  <---- pertinho de   " + this.token.getLexema().toString());
            }

            this.token = this.lexico.nextToken();
            this.expr_relacional();

            if (!this.token.getLexema().equals(")")) {
                throw new RuntimeException( "meu irmao cade o parentese --> )  <---- pra terminar, pertinho de  "   + this.token.getLexema().toString());
            }

            this.token = this.lexico.nextToken();
            this.comando();

            if (token.getLexema().equals("else")) {
                this.token = this.lexico.nextToken();
                this.comando();
            } else {

            }
        } else {
            
        }

    }

    private void interacao() {
        if (!this.token.getLexema().equals("while")) {
            throw new RuntimeException("while esperado, foi encontrado => " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();

        this.token = this.lexico.nextToken();
        if(!token.getLexema().equals("(")){
            throw new RuntimeException("Abre o parêntese do main cabra!");
        }

        this.token = this.lexico.nextToken();

        this.expr_relacional();


        this.token = this.lexico.nextToken();
        if(!token.getLexema().equals(")")){
            throw new RuntimeException("Fechar o parêntese do main cabra!");
        }        

        this.comando();
    }
    
    private void expr_relacional() {

        this.expr_arit();

        if (this.token.getTipo() != Token.TIPO_OPERADOR_RELACIONAL) {
            throw new RuntimeException("Cade teu operador relacional  " + this.token.getLexema().toString());
        }

        this.token = this.lexico.nextToken();

        this.expr_arit();
    
        
    }

   
   

    private void comandoBasico() {

        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.ATRIBUICAO();
        }

        if (this.token.getLexema().equals("{")) {
            this.B();
        }

    }

 
        
    private void DECLARACAO(){
        if(!(this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float"))){
            throw new RuntimeException("Tu vacilou na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Tu vacilou na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(!this.token.getLexema().equalsIgnoreCase(";")){
            throw new RuntimeException("Tu vacilou  na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();        
    }
    
    private void ATRIBUICAO(){
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("onde ta o id para atribuição. Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO){
            throw new RuntimeException("Tem que ter o  = na atribuiçao cabra. Pertinho de: " + this.token.getLexema());
        }


        this.token = this.lexico.nextToken();        

        this.expr_arit();

        if(!this.token.getLexema().equals(";")){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexema() + " Cade o ;  ");
        }
        this.token = this.lexico.nextToken();                
    }
    
    private void expr_arit(){

        this.OP();
        
        this.Termo();

        this.El();
    }
    
    private void El(){
      if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            if (token.getLexema().equals("+")) {
                this.token = this.lexico.nextToken();

            } else if (this.token.getLexema().equals("-")) {
                this.token = this.lexico.nextToken();

            } else {
                throw new RuntimeException("cade o fator aritmetico Rapaz , pertinho de =>  " + this.token.getLexema().toString());
        
            }
            this.Termo();
            this.fator();
              
        }
    }
    
    private void fator() {


    }

    private void Termo(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR || 
                this.token.getTipo() == Token.TIPO_INTEIRO ||
                this.token.getTipo() == Token.TIPO_REAL){
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Oxe, era para ser um identificador "
                    + "ou número pertinho de " + this.token.getLexema());
        }
    }


    
    private void OP(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Oxe, era para ser um operador "
                    + "aritmético (+,-,/,*) pertinho de "  + 
                    this.token.getLexema());
        }
    }
    
    
    
}
