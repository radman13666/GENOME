/*
 * Copyright (C) 2017 William Kibirango <williamkaos.kibirango76@gmail.com>
 *
 * This file is part of GENOME.
 *
 * GENOME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GENOME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GENOME.  If not, see <http://www.gnu.org/licenses/>.
 */
package genome.metronome.presenter;

import genome.metronome.utils.MetronomeConstants;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public abstract class Metronome {
  
  protected int measure;
  protected int subDivision;
  protected SoundRez soundRez;
  protected Thread writingThread;

  protected Metronome() {
  }

  protected Metronome(int measure, int subDivision) {
    setMeasure(measure);
    setSubDivision(subDivision);
  }

  public final int getMeasure() {
    return measure;
  }

  public final void setMeasure(int measure) {
    if (measure == MetronomeConstants.Metronome.NO_MEASURE
        | (measure >= MetronomeConstants.Metronome.MIN_MEASURE 
           & measure <= MetronomeConstants.Metronome.MAX_MEASURE))
      this.measure = measure;
    else this.measure = MetronomeConstants.Metronome.COMMON_TIME;
  }

  public final int getSubDivision() {
    return subDivision;
  }

  public final void setSubDivision(int subDivision) {
    if (subDivision == MetronomeConstants.Metronome.NO_SUB_DIVISION 
        | (subDivision >= MetronomeConstants.Metronome.MIN_SUB_DIVISION 
           & subDivision <= MetronomeConstants.Metronome.MAX_SUB_DIVISION))
      this.subDivision = subDivision;
    else this.subDivision = MetronomeConstants.Metronome.NO_SUB_DIVISION;
  }

  protected final SoundRez getSoundRez() {
    return soundRez;
  }

  public final void setSoundRez(SoundRez soundRez) {
    this.soundRez = soundRez;
  }

  protected final Thread getWritingThread() {
    return writingThread;
  }

  protected final void setWritingThread(Thread writingThread) {
    this.writingThread = writingThread;
  }
  
  public abstract void play();
  public abstract void stop();
  public abstract void bulkSet(HashMap<String, Number> settings);
  public abstract HashMap<String, Number> getSettings();
  
  protected class WriteAudioTask implements Runnable {
    //this is the server for writing audio data to the audio output devices.
    
    ServerSocket serverSocket;
    Socket clientSocket;
    InputStream in;
    byte[] buffer;

    protected WriteAudioTask() {
    }

    @Override
    public void run() {
      try {
        //1. open the clientSocket for the server and listen for incoming 
        //   audio data.
        serverSocket 
          = new ServerSocket(MetronomeConstants.Metronome.SERVER_PORT);
        clientSocket = serverSocket.accept();
        in = clientSocket.getInputStream();
        
        //2. when the data is received, it is written to the audio devices
        //   throught a buffered audio output stream.
        buffer = new byte[MetronomeConstants.Metronome.BUFFER_SIZE];
        int numBytesRead;
        
        getSoundRez().getLine().start();
        while (!Thread.interrupted()) {
          numBytesRead = in.read(buffer);
          getSoundRez().getLine().write(buffer, 0, numBytesRead);
        }
        getSoundRez().getLine().stop();
        getSoundRez().getLine().flush();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          serverSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  protected abstract class CreateAudioTask implements Runnable {
    //this is the client that generates and sends audio data to the server.
    
    private Socket socket;
    private BufferedOutputStream out;
    protected int counter;
    byte[] buffer;

    protected CreateAudioTask() {
    }
    
    @Override
    public void run() {
      try {
        //1. Connect to the server and get an output stream.
        socket = new Socket(MetronomeConstants.Metronome.HOST, 
          MetronomeConstants.Metronome.SERVER_PORT);
        out = new BufferedOutputStream(socket.getOutputStream());
        buffer = new byte[MetronomeConstants.Metronome.BUFFER_SIZE];
        counter = 0;
        
        //2. continuously create data and write it to the stream until
        //   the thread is interrupted.
        while (!Thread.interrupted()) {
          create(buffer);
          out.write(buffer, 0, buffer.length);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public abstract void create(byte[] buffer);
  }
}
