package compiladorl3;

import java.util.ArrayList;

public class Sintatico2 {
    private Lexico lexico;
    private Token token;
    private int valueStored;
    private ArrayList<String> listaDeIds;

    public Sintatico2(Lexico lexico) {
        this.lexico = lexico;
        this.listaDeIds = new ArrayList<>();
    }

    //S
    public void S() {
        this.token = this.lexico.nextToken();

        if (!token.getLexema().equals("main")) {
            throw new RuntimeException("Leviana, esqueceu o Main --> [" + this.token.getLexema() + "]");
        }

        this.token = this.lexico.nextToken();

        if (!token.getLexema().equals("(")) {
            throw new RuntimeException("Se minha vida é errada,ninguem tem nada com isso, abre esse parentese");
        }

        this.token = this.lexico.nextToken();

        if (!token.getLexema().equals(")")) {
            throw new RuntimeException("Se minha vida é errada,ninguem tem nada com isso, fecha esse parentese");
        }
        this.token = this.lexico.nextToken();

        this.B();
        if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
            System.out.println("Encontrei um novo 'compilador' que é fiel a mim esse sim me da valor....!");
        } else {
            throw new RuntimeException("Pq meu bem, Ninguem é perfeito e a vida é assim ");
        }
    }



    // B 
    private void B() {
        if (!this.token.getLexema().equals("{")) {
            throw new RuntimeException("Oxe, tava esperando um \"{\" pertinho de " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();

       
        while (this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA) {
            this.DECLARACAO();
            this.comando();
        }
       
        

        if (!this.token.getLexema().equals("}")) {
            throw new RuntimeException("Oxe, tava esperando um \"}\" pertinho de " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
    }

    // D
    private void DECLARACAO() {
        if (this.token.getLexema().equals("int") || this.token.getLexema().equals("float") ||
                this.token.getLexema().equals("char")) {
            this.token = this.lexico.nextToken();

            if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
                this.listaDeVariaveis(this.token.getLexema());
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Identificador esperado aqui, Encontramos: " + this.token.getLexema());
            }

            if (!this.token.getLexema().equals(";")) {
                throw new RuntimeException("; esperado para finalizar a declaração, ao lado de: " +
                        this.token.getLexema());
            }
            this.token = this.lexico.nextToken();
            this.DECLARACAO();
        }
    }

    //C
    private void comando() {
        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getLexema().equals("{")) {
          
            this.comandoBasico();

        } else if (this.token.getLexema().equals("while")) {

            this.interacao();
        } else if (this.token.getLexema().equals("if")) {
            
            this.token = this.lexico.nextToken();

            if (!this.token.getLexema().equals("(")) {
                throw new RuntimeException("Volta amor, eu nao mereço ficar sem o   (    ,   pertinho de   " + this.token.getLexema());
            }

            this.token = this.lexico.nextToken();
            this.expr_relacional();

            if (!this.token.getLexema().equals(")")) {
                throw new RuntimeException("Já são 5 da manhã e Cadê o meu parêntese --> ) <---- pra terminar, pertinho de " +
                        this.token.getLexema());
            }

            this.token = this.lexico.nextToken();
            this.comando();

            if (token.getLexema().equals("else")) {
                this.token = this.lexico.nextToken();
                this.comando();
            }else{

            }
        }else{

        }
    }

    // I
    private void interacao() {
        if (!this.token.getLexema().equals("while")) {
            throw new RuntimeException("while esperado, foi encontrado: " + this.token.getLexema());
        }

        this.token = this.lexico.nextToken();

        if (!token.getLexema().equals("(")) {
            throw new RuntimeException("Se minha vida é errada,ninguem tem nada com isso, mas  voce esqueceu o  (  , pertinho de " + this.token.getLexema());
        }

        this.token = this.lexico.nextToken();
        this.expr_relacional();

        if (!token.getLexema().equals(")")) {
            throw new RuntimeException("Se minha vida é errada,ninguem tem nada com isso, mas  voce esqueceu o  ), pertinho de  " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        this.comando();

    }

    // E
    private void expr_relacional() {
        this.expr_arit();

        if (this.token.getTipo() != Token.TIPO_OPERADOR_RELACIONAL) {
            throw new RuntimeException("Cadê o operador relacional? " + this.token.getLexema());
        }

        this.token = this.lexico.nextToken();

        this.expr_arit();
    }
    // c
    private void comandoBasico() {
        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.ATRIBUICAO();
        }

        if (this.token.getLexema().equals("{")) {
            this.B();
        }
    }

    //  A
    private void ATRIBUICAO() {
        if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
            throw new RuntimeException("Se minha vida é errada,ninguem tem nada com isso, cade o ID  pertinho de " + this.token.getLexema());
        }

        this.findId(this.token.getLexema());

        this.token = this.lexico.nextToken();

        if (this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO) {
            throw new RuntimeException("Tem que ter o = na atribuição. Pertinho de: " + this.token.getLexema());
        }

        this.token = this.lexico.nextToken();

        this.expr_arit();

        if (!this.token.getLexema().equals(";")) {
            throw new RuntimeException("Passei a noite em claro eu não consegui dormir perambulando pelos bares esperando que esse 'código' funcione e voce nao esqueca o ;.  Pertinho de: " + this.token.getLexema() +
                    " Cadê o ; ?");
        }
        this.token = this.lexico.nextToken();
    }

    // R
    private void expr_arit() {
        this.Termo();
        this.OP();
    }

    // O
    private void OP() {
        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            if (token.getLexema().equals("+") || token.getLexema().equals("-")) {
                this.token = this.lexico.nextToken();
                this.Termo();
                this.OP();
            } else {
                throw new RuntimeException("Cadê o fator aritmético? Pertinho de: " + this.token.getLexema());
            }
        }
    }

    // T
    private void Termo() {
        this.fator();
        this.OPmult();
        this.OP();

    }

    //M
    private void OPmult() {
        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            if (this.token.getLexema().equals("*") || this.token.getLexema().equals("/")) {
                this.token = this.lexico.nextToken();
                this.fator();
                this.OPmult();
            } else {
                throw new RuntimeException("Operador * ou / esperado, foi encontrado: " + this.token.getLexema());
            }
        }
    }

    // F
    private void fator() {
        if (token.getLexema().equals("(")) {
            this.token = this.lexico.nextToken();
            this.expr_arit();
            if (!token.getLexema().equals(")")) {
                throw new RuntimeException("Cadê o parentese pra fechar meu irmão? ) Pertinho de " +
                        this.token.getLexema());
            }
            this.token = this.lexico.nextToken();
        }

        if (this.token.getTipo() == Token.TIPO_INTEIRO || this.token.getTipo() == Token.TIPO_REAL ||
                this.token.getTipo() == Token.TIPO_CHAR || this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Cadê o identificador? Coloque um dos seguintes: float, int ou char. " +
                    "Pertinho de: " + this.token.getLexema());
        }
    }

    private void listaDeVariaveis(String id) {
        for (int i = 0; i < this.listaDeIds.size(); i++) {
            if (id.equals(listaDeIds.get(i))) {
                throw new RuntimeException("Variável duplicada: " + this.token.getLexema());
            }
        }
        this.listaDeIds.add(id);
    }

    private void findId(String id) {
        if (this.listaDeIds.isEmpty()) {
            throw new RuntimeException("Necessário declarar uma variável " + this.token.getLexema().toString());
        }

        for (int i = 0; i < this.listaDeIds.size(); i++) {
            if (id.equals(listaDeIds.get(i))) {
                valueStored = 1;
            }
        }

        if (valueStored != 1) {
            throw new RuntimeException("Necessário declarar uma variável " + this.token.getLexema().toString());
        }
        
    }
}
