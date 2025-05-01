package com.alcanl.app.chat.connection;

import com.alcanl.app.global.ImageDisplayPanel;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.karandev.io.util.console.Console;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("ALL")
public final class ConnectionHandler {

    private void openWebcam(Webcam webcam)
    {
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();
    }

    private void takeImage(ImageDisplayPanel imageDisplayPanel, DataInputStream dataInputStream) throws IOException
    {
        var frameWidth = dataInputStream.readInt();
        var frameHeight = dataInputStream.readInt();
        var pixelData = new int[frameWidth * frameHeight];

        for (var i = 0; i < pixelData.length; i++)
            pixelData[i] = dataInputStream.readInt();

        var frame = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB);
        frame.setRGB(0, 0, frameWidth, frameHeight, pixelData, 0, frameWidth);

        imageDisplayPanel.setBackground(frame);
    }
    private void sendImage(Webcam webcam, DataOutputStream dataOutputStream) throws IOException
    {
        var frame = webcam.getImage();

        var frameWidth = frame.getWidth();
        var frameHeight = frame.getHeight();

        dataOutputStream.writeInt(frameWidth);
        dataOutputStream.writeInt(frameHeight);

        var pixelData = new int[frameWidth * frameHeight];
        frame.getRGB(0, 0, frameWidth, frameHeight, pixelData, 0, frameWidth);
        for (var pixelDatum : pixelData)
            dataOutputStream.writeInt(pixelDatum);

    }
    private void sendMessage(PrintWriter printWriter, String connector)
    {
        printWriter.println(connector + ": " + Console.readLine());
    }
    private void takeMessage(BufferedReader bufferedReader) throws IOException
    {
        Console.writeLine(bufferedReader.readLine());
    }
    public void clientMessageReceiver(Socket clientSocket, BufferedReader bufferedReader)
    {
        try(clientSocket; bufferedReader) {
            while (clientSocket.isConnected()) {
                takeMessage(bufferedReader);
            }
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }
    public void clientMessageSender(Socket clientSocket, PrintWriter printWriter, String connector)
    {
        try(clientSocket; printWriter) {
            while (clientSocket.isConnected()) {
                sendMessage(printWriter, connector);
            }
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }
    public void serverMessageReceiver(ServerSocket serverSocket, Socket clientSocket,
                                              BufferedReader bufferedReader)
    {
        try(serverSocket) {
            clientMessageReceiver(clientSocket, bufferedReader);
        }
        catch(IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public void serverMessageSender(ServerSocket serverSocket, Socket clientSocket, PrintWriter printWriter, String connector)
    {
        try (serverSocket) {
            clientMessageSender(clientSocket, printWriter, connector);
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public void serverImageReceiver(ServerSocket serverSocket, Socket clientSocket,
                                           ImageDisplayPanel imageDisplayPanel, DataInputStream dataInputStream)
    {
        try(serverSocket) {
            clientImageReceiver(clientSocket, imageDisplayPanel, dataInputStream);
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }
    public void serverImageSender(ServerSocket serverSocket, Socket clientSocket,
                                         Webcam webcam, DataOutputStream dataOutputStream)
    {
        try(serverSocket)
        {
            clientImageSender(clientSocket, webcam, dataOutputStream);
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }
    public void clientImageReceiver(Socket socket, ImageDisplayPanel imageDisplayPanel, DataInputStream dataInputStream)
    {
        try(socket; dataInputStream) {
        while (socket.isConnected()) {
                takeImage(imageDisplayPanel, dataInputStream);
            }
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }
    public void clientImageSender(Socket socket, Webcam webcam, DataOutputStream dataOutputStream)
    {
        openWebcam(webcam);
        try(socket; dataOutputStream) {
            while (socket.isConnected() && webcam.isOpen()) {
                sendImage(webcam, dataOutputStream);
            }
        }
        catch (IOException ex)
        {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public void serverAudioReceiver() {throw new UnsupportedOperationException("Not Implemented Yet");}
    public void serverAudioSender()
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    public void clientAudioReceiver()
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    public void clientAudioSender()
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
