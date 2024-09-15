/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

/**
 *
 * @author abrah
 */

import model.Usuario;
import model.Movimento;
import model.Produto;
import java.util.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;

public class CadastroThreadV2 extends Thread {
    private ProdutoJpaController ctrlProd;
    private UsuarioJpaController ctrlUsu;
    private MovimentoJpaController ctrlMov;
    private PessoaJpaController ctrlPessoa;
    private Socket s1;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private SaidaFrame saidaFrame;

    public CadastroThreadV2(ProdutoJpaController ctrlProd, UsuarioJpaController ctrlUsu, 
                          MovimentoJpaController ctrlMov, PessoaJpaController ctrlPessoa, 
                          Socket s1) {
        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
        this.s1 = s1;
        this.saidaFrame = new SaidaFrame();
        
        try {
            out = new ObjectOutputStream(s1.getOutputStream());
            in = new ObjectInputStream(s1.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            saidaFrame.setVisible(true);
            
            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            
            Usuario usuario = ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                s1.close();
                return;
            }
            
            saidaFrame.texto.append((String) "Nova conexão estabelecida" + "\n");
            
            while (true) {
                String comando = (String) in.readObject();
                if (comando.equals("L")) {
                    List<Produto> produtos = ctrlProd.findAll();
                    Produto[] produtoArray = produtos.toArray(new Produto[produtos.size()]);
                    String[] produtoNames = new String[produtoArray.length];
                    for (int i = 0; i < produtoArray.length; i++) {
                        produtoNames[i] = produtoArray[i].getNome();
                        saidaFrame.texto.append((String) produtoArray[i].getNome() + "::" + produtoArray[i].getPrecoVenda() + "\n");
                    }
                    out.writeObject(produtoNames);
                } else if (comando.equals("E") || comando.equals("S")) {
                    Movimento movimento = new Movimento();
                    movimento.setIdUsuario(usuario);
                    char caracterComando = comando.charAt(0);
                    movimento.setTipo(caracterComando);
                    
                    String idPessoa = (String) in.readObject();
                    int idIntPessoa = 0;
                    try {
                        idIntPessoa = Integer.parseInt(idPessoa);
                    } catch (NumberFormatException e) {
                        System.err.println("Number" + idPessoa);
                    }
                    movimento.setIdPessoa(ctrlPessoa.findPessoa(idIntPessoa));
                    
                    String idProduto = (String) in.readObject();
                    int idIntProduto = 0;
                    try {
                        idIntProduto = Integer.parseInt(idProduto);
                    } catch (NumberFormatException e) {
                        System.err.println("Number" + idProduto);
                    }
                    movimento.setIdProduto(ctrlProd.findProduto(idIntProduto));
                    
                    String quantidade = (String) in.readObject();
                    int intQuantidade = 0;
                    try {
                        intQuantidade = Integer.parseInt(quantidade);
                    } catch (NumberFormatException e) {
                        System.err.println("Number" + quantidade);
                    }
                    movimento.setQuantidade(intQuantidade);
                    
                    String valorUnitario = (String) in.readObject();
                    double valorUnitarioDouble = 0.0;
                    try {
                        valorUnitarioDouble = Double.parseDouble(valorUnitario);
                    } catch (NumberFormatException e) {
                        System.err.println("Number" + valorUnitario);
                    }
                    movimento.setValorUnitario(valorUnitarioDouble);
                    
                    ctrlMov.create(movimento);
                    
                    if (comando.equals("E")) {
                        ctrlProd.incrementQuantidade(idIntProduto, intQuantidade);
                    } else {
                        ctrlProd.decrementQuantidade(idIntProduto, intQuantidade);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fim da conexão" + e);
        } finally {
            try {
                s1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}