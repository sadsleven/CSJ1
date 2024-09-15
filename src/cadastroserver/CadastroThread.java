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
import java.util.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import model.Produto;

public class CadastroThread extends Thread {
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;
    private Socket s1;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public CadastroThread(ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
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
            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            
            Usuario usuario = ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                s1.close();
                return;
            }

            while (true) {
                String comando = (String) in.readObject();
                if (comando.equals("L")) {
                    List<Produto> produtos = ctrl.findAll();
                    Produto[] produtoArray = produtos.toArray(new Produto[produtos.size()]);
                    String[] produtoNames = new String[produtoArray.length];
                    for (int i = 0; i < produtoArray.length; i++) {
                        produtoNames[i] = produtoArray[i].getNome();
                    }
                    out.writeObject(produtoNames);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fim da conexão");
        } finally {
            try {
                s1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}