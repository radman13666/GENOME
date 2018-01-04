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
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
  protected Thread creatingThread;

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
    if (this instanceof GapMetronome || this instanceof SpeedMetronome) {
      if (measure >= MetronomeConstants.Metronome.MIN_MEASURE 
           && measure <= MetronomeConstants.Metronome.MAX_MEASURE)
        this.measure = measure;
      else this.measure = MetronomeConstants.Metronome.COMMON_TIME;
    } else {
      if (measure == MetronomeConstants.Metronome.NO_MEASURE
          || (measure >= MetronomeConstants.Metronome.MIN_MEASURE 
             && measure <= MetronomeConstants.Metronome.MAX_MEASURE))
        this.measure = measure;
      else this.measure = MetronomeConstants.Metronome.COMMON_TIME;
    }
  }

  public final int getSubDivision() {
    return subDivision;
  }

  public final void setSubDivision(int subDivision) {
    if (subDivision == MetronomeConstants.Metronome.NO_SUB_DIVISION 
        || (subDivision >= MetronomeConstants.Metronome.MIN_SUB_DIVISION 
           && subDivision <= MetronomeConstants.Metronome.MAX_SUB_DIVISION))
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

  protected final Thread getCreatingThread() {
    return creatingThread;
  }

  protected final void setCreatingThread(Thread creatingThread) {
    this.creatingThread = creatingThread;
  }
  
  public void play() {
    setWritingThread(new Thread(new WriteAudioTask()));
    getWritingThread().start();
  }
  
  public final void stop() {
    getCreatingThread().interrupt();
    getWritingThread().interrupt();
    setCreatingThread(null);
    setWritingThread(null);
  }
  
  public abstract void bulkSet(HashMap<String, Number> settings);
  public abstract HashMap<String, Number> getSettings();
  
  protected class WriteAudioTask implements Runnable {
    //this is the server for writing audio data to the audio output devices.
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream in;
    private byte[] buffer;

    protected WriteAudioTask() {
    }

    @Override
    public void run() {
      try {
        //1. open the clientSocket for the server and listen for incoming 
        //   audio data.
        serverSocket = new ServerSocket(
          MetronomeConstants.Metronome.AudioTasks.SERVER_PORT);
        clientSocket = serverSocket.accept();
        in = clientSocket.getInputStream();
        
        //2. when the data is received, it is written to the audio devices
        //   throught a buffered audio output stream.
        buffer = new byte[MetronomeConstants.Metronome.AudioTasks.BUFFER_SIZE];
        int numBytesRead;
        
        getSoundRez().getLine().start();
        while (!Thread.interrupted()) {
          numBytesRead = in.read(buffer);
          if (numBytesRead == -1) break;
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
    private int a = 0, b = 0, c = 0, d = 0;
    
    protected CreateAudioTask() {
    }
    
    //the unit-step function
    protected int u(BigInteger t, BigInteger a) {
      if (t.compareTo(a) == -1) // t < a 
        return 0;
      else return 1;
    }

    //the unit-impulse function
    protected int h(BigInteger t, BigInteger a, BigInteger b) {
      if (a.compareTo(b) == -1) // a < b 
        return u(t, a) - u(t, b);
      else return 0;
    }

    //periodic unit-step function
    protected int g(BigInteger t, BigInteger a, long n, long T) {
      BigInteger tEff = t.subtract(BigInteger.valueOf(n * T)); // t - (n * T)
      return u(tEff, a);
    }

    //periodic unit-impulse function
    protected int h(BigInteger t, BigInteger a, BigInteger b, long n, long T) {
      BigInteger tEff = t.subtract(BigInteger.valueOf(n * T)); // t - (n * T)
      return h(tEff, a, b);
    }

    //periodic unit-impulse active from 0 to b
    protected int h(BigInteger t, BigInteger b, long n, long T) {
      return h(t, BigInteger.ZERO, b, n, T);
    }
    
    //periodic unit-impulse function --- Overloaded
    protected int h(BigInteger t, BigInteger a, BigInteger b, long n, 
                                                              BigInteger T) {
      // t - (n * T)
      BigInteger tEff = t.subtract(T.multiply(BigInteger.valueOf(n))); 
      return h(tEff, a, b);
    }

    //periodic unit-impulse active from 0 to b --- Overloaded
    protected int h(BigInteger t, BigInteger b, long n, BigInteger T) {
      return h(t, BigInteger.ZERO, b, n, T);
    }
    
    //function to generate actual sound bytes
    protected byte soundGenerator(byte value) {
      byte result;
      switch (value) {
        case 0:
          a = 0; b = 0; c = 0; d = 0;
          result = 0;
          break;
        case MetronomeConstants.Metronome.AudioTasks.CLICK:
          result = getSoundRez().getClickSound()[a];
          a++;
          break;
        case MetronomeConstants.Metronome.AudioTasks.BEAT:
          result = getSoundRez().getBeatSound()[c];
          c++;
          break;
        case MetronomeConstants.Metronome.AudioTasks.ACCENT:
          result = getSoundRez().getAccentSound()[b];
          b++;
          break;
        case MetronomeConstants.Metronome.AudioTasks.TEMPO_CHANGE:
          result = getSoundRez().getTempoChangeSound()[d];
          d++;
          break;
        default:
          result = 0;
          break;
      }
      return result;
    }

    protected abstract int create(byte[] buffer);
  }
}
