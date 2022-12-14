package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Main {
    private static String CLIENT_KEY_STORE= "/client_ts";

    public static void main(String[] args) throws Exception{
            JTextField ip = new JTextField();
            JLabel titulo0=new JLabel("Ingrese direccion IP del Servidor de Autenticacion");

            int valor0=JOptionPane.showConfirmDialog(null,new Object[] {titulo0,
                    ip},"Inicio de Sesión",JOptionPane.OK_CANCEL_OPTION);
            if(valor0==2) {
                System.exit(1);
            }
            //System.setProperty("javax.net.ssk.trustStore", CLIENT_KEY_STORE);
            //SocketFactory sf= SSLSocketFactory.getDefault();

            Socket s;
            s= new Socket(ip.getText(),8445);

            PrintWriter writer=new PrintWriter(s.getOutputStream());
            BufferedReader reader= new BufferedReader(new InputStreamReader(s.getInputStream()));


            for(int intentos=0;intentos<3;intentos++) {
                JTextField us= new JTextField();
                JLabel titulo = new JLabel("Ingrese su usuario");
                int valor=JOptionPane.showConfirmDialog(null,new Object[] {titulo,us},"Inicio de sesión"
                        ,JOptionPane.OK_CANCEL_OPTION);
                if(valor==2) {
                    writer.close();
                    reader.close();
                    s.close();
                    System.exit(1);
                }

                JPasswordField jpf= new JPasswordField();
                JLabel titulo1= new JLabel("Ingrese su contraseña");
                int valor1=JOptionPane.showConfirmDialog(null,new Object[] {titulo1,jpf},"Inicio de sesión"
                        ,JOptionPane.OK_CANCEL_OPTION);

                char p[]=jpf.getPassword();
                String passw=new String(p);
                if(valor1==2) {
                    writer.close();
                    reader.close();
                    s.close();
                    System.exit(1);
                }
                writer.println(us.getText());
                writer.println(passw);
                writer.flush();

                String mensaje=reader.readLine();

                if(mensaje==null) {
                    JOptionPane.showMessageDialog(null, "No hay respuesta del servidor","ACCESO NEGADO",JOptionPane.ERROR_MESSAGE);
                    break;
                }else if(mensaje.equals("valido")){
                    JOptionPane.showMessageDialog(null, "Bienvenido a la Red","ACCESO CONCEDIDO",JOptionPane.INFORMATION_MESSAGE);
                    break;
                }else if(mensaje.split(",")[0].equals("invalido") && mensaje.split(",")[1].equals("true")) {
                    JOptionPane.showMessageDialog(null, "Usuario contraseña incorrectos ","ACCESO NEGADO",JOptionPane.ERROR_MESSAGE);
                }else if(mensaje.split(",")[0].equals("invalido" )&& mensaje.split(",")[1].equals("false")) {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.El numero maximo de intentos ha sido superado.","ACCESO NEGADO",JOptionPane.ERROR_MESSAGE);
                    break;
                }


            }// fin for
            reader.close();
            s.close();
        }

}

