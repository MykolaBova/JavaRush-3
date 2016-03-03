package com.javarush.test.level30.lesson15.big01.client;

import com.javarush.test.level30.lesson15.big01.Connection;
import com.javarush.test.level30.lesson15.big01.ConsoleHelper;
import com.javarush.test.level30.lesson15.big01.Message;
import com.javarush.test.level30.lesson15.big01.MessageType;

import java.io.IOException;

/**
 * Created by Dmitry on 03.03.2016.
 */
public class Client
{
    protected Connection connection;
    private volatile boolean clientConnected = false;


    public static void main(String[] args)
    {
        Client client = new Client();
        client.run();
    }

    protected String getServerAddress()
    {
        ConsoleHelper.writeMessage("input address");
        return ConsoleHelper.readString();
    }

    protected int getServerPort()
    {
        ConsoleHelper.writeMessage("input server port");
        return ConsoleHelper.readInt();
    }

    protected String getUserName()
    {
        ConsoleHelper.writeMessage("input name");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSentTextFromConsole()
    {
        return true;
    }

    protected SocketThread getSocketThread()
    {
        return new SocketThread();
    }

    protected void sendTextMessage(String text)
    {
        Message message = new Message(MessageType.TEXT, text);
        try
        {
            connection.send(message);
        }
        catch (IOException e)
        {
            ConsoleHelper.writeMessage("error");
            clientConnected = false;
        }
    }

    public void run()
    {
        SocketThread thread = getSocketThread();
        thread.setDaemon(true);
        thread.start();
        try
        {
            synchronized (this)
            {
                this.wait();
            }
        }
        catch (InterruptedException e)
        {
            ConsoleHelper.writeMessage("error");
            return;
        }

        if (clientConnected) {
            ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");

            while (clientConnected) {
                String message;
                if (!(message = ConsoleHelper.readString()).equals("exit")) {
                    if (shouldSentTextFromConsole()) {
                        sendTextMessage(message);
                    }
                } else {
                    return;
                }
            }
        }
        else {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        }


    }


    public class SocketThread extends Thread
    {
        @Override
        public void run()
        {
        }
    }
}
