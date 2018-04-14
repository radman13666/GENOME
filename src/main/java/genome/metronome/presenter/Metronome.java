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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public abstract class Metronome extends Observable {
  
  protected int measure;
  protected int subDivision;
  protected SoundRez soundRez;
  protected WriteAudioTask writingTask;
  protected CreateAudioTask creatingTask;
  protected ExecutorService executor;
  protected Future writingFuture;
  protected Future creatingFuture;

  protected Metronome() {
    executor = Executors.newFixedThreadPool(2);
  }

  protected Metronome(int measure, int subDivision) {
    this();
    setMeasure(measure);
    setSubDivision(subDivision);
  }

  public final int getMeasure() {
    return measure;
  }

  public final void setMeasure(int measure) {
    if (this instanceof GapMetronome || this instanceof SpeedMetronome) {
      if (measure >= MetronomeConstants.Metronome.MIN_MEASURE && 
          measure <= MetronomeConstants.Metronome.MAX_MEASURE)
        this.measure = measure;
      else this.measure = MetronomeConstants.Metronome.COMMON_TIME;
    } else {
      if (measure == MetronomeConstants.Metronome.NO_MEASURE || 
          (measure >= MetronomeConstants.Metronome.MIN_MEASURE && 
           measure <= MetronomeConstants.Metronome.MAX_MEASURE))
        this.measure = measure;
      else this.measure = MetronomeConstants.Metronome.COMMON_TIME;
    }
  }

  public final int getSubDivision() {
    return subDivision;
  }

  public final void setSubDivision(int subDivision) {
    if (subDivision == MetronomeConstants.Metronome.NO_SUB_DIVISION ||
        (subDivision >= MetronomeConstants.Metronome.MIN_SUB_DIVISION &&
         subDivision <= MetronomeConstants.Metronome.MAX_SUB_DIVISION))
      this.subDivision = subDivision;
    else this.subDivision = MetronomeConstants.Metronome.NO_SUB_DIVISION;
  }

  protected final SoundRez getSoundRez() {
    return soundRez;
  }

  public final void setSoundRez(SoundRez soundRez) {
    this.soundRez = soundRez;
  }

  protected final WriteAudioTask getWritingTask() {
    return writingTask;
  }

  protected final void setWritingTask(WriteAudioTask writingTask) {
    this.writingTask = writingTask;
  }

  protected final CreateAudioTask getCreatingTask() {
    return creatingTask;
  }

  protected final void setCreatingTask(CreateAudioTask creatingTask) {
    this.creatingTask = creatingTask;
  }
  
  public abstract void play();
  
  public final void stop() {
//    executor.shutdown();
    while (!(creatingFuture.isDone() || writingFuture.isDone())) {}
  }
  
  public abstract void bulkSet(HashMap<String, Number> settings);
  public abstract HashMap<String, Number> getSettings();
  
  protected abstract class WriteAudioTask implements Runnable {
    //this is the server for writing audio data to the audio output devices.
    
    //TODO: find a way of listening to upates when writing audio.
    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    protected BufferedInputStream in;
    protected DataOutputStream out;
    protected byte[] buffer;

    protected WriteAudioTask() {
    }
  }
  
  protected abstract class CreateAudioTask implements Runnable {
    //this is the client that generates and sends audio data to the server.
    private int a = 0, b = 0, c = 0, d = 0;
    protected Socket socket;
    protected BufferedOutputStream out;
    protected DataInputStream in;
    protected byte[] buffer;
    
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
        case MetronomeConstants.Metronome.AudioTasks.CLICK_MARKER:
          result = getSoundRez().getClickSound()[c];
          c++;
          break;
        case MetronomeConstants.Metronome.AudioTasks.BEAT_MARKER:
          result = getSoundRez().getBeatSound()[b];
          b++;
          break;
        case MetronomeConstants.Metronome.AudioTasks.ACCENT_MARKER:
          result = getSoundRez().getAccentSound()[a];
          a++;
          break;
        case MetronomeConstants.Metronome.AudioTasks.TEMPO_CHANGE_MARKER:
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
