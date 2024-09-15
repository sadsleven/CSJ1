/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class CadastroServer {

    public static void main(String[] args) {
        // Instanciar um objeto do tipo EntityManagerFactory a partir da unidade de persistência
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");

        // Instanciar o objeto ctrl, do tipo ProdutoJpaController
        ProdutoJpaController ctrl = new ProdutoJpaController(emf);

        // Instanciar o objeto ctrlUsu do tipo UsuarioJpaController
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        
        ServerSocket serverSocket = null;

        // Instanciar um objeto do tipo ServerSocket, escutando a porta 4321
        try {
            serverSocket = new ServerSocket(4321);
        } catch (IOException e) {
            System.err.println("Error Server: " + e.getMessage());
        }

        System.out.println("Servidor iniciado. Aguardando conexões...");

        while (true) {
            // Obter a requisição de conexão do cliente
            Socket socket = null;
            
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Error Socket: " + e.getMessage());
            }

            System.out.println("Nova conexão estabelecida.");

            // Instanciar uma Thread, com a passagem de ctrl, ctrlUsu e do Socket da conexão
            CadastroThread thread = new CadastroThread(ctrl, ctrlUsu, socket);

            // Iniciar a Thread
            thread.start();
        }
    }
}
