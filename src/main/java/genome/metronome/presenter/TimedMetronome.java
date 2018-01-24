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
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class TimedMetronome extends ConstantTempoMetronome {
  
  private int duration;
  private int currentTimeLeft;

  public TimedMetronome() {
  }

  public TimedMetronome(int duration, float tempo, int measure, 
                                                   int subDivision) {
    super(tempo, measure, subDivision);
    setDuration(duration);
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    if (duration >= MetronomeConstants.TimedMetronome.MIN_DURATION && 
        duration <= MetronomeConstants.TimedMetronome.MAX_DURATION)
      this.duration = duration;
    else this.duration = MetronomeConstants.TimedMetronome.DEFAULT_DURATION;
    this.currentTimeLeft = this.duration;
  }

  public int getCurrentTimeLeft() {
    return currentTimeLeft;
  }

  private void decrementCurrentTimeLeft(int timePast) {
    this.currentTimeLeft -= timePast;
    setChanged();
    notifyObservers(MetronomeConstants.Metronome.AudioTasks
      .TM_CURRENT_TIME_LEFT);
  }

  @Override
  public void play() {
//    super.play();
    setWritingTask(new WriteTimedClickTrackTask());
    setCreatingTask(
      new CreateTimedClickTrackTask(getTempo(), getMeasure(), getDuration())
    );
//    executor.execute(getWritingTask());
    writingFuture = executor.submit(getWritingTask());
//    executor.execute(getCreatingTask());
    creatingFuture = executor.submit(getCreatingTask());
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    setTempo((Float) settings.get(MetronomeConstants.MetronomeSettingsKeys
      .TEMPO));
    setMeasure((Integer) settings.get(MetronomeConstants.MetronomeSettingsKeys
      .MEASURE));
    setSubDivision((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.SUB_DIVISION));
    setDuration((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.DURATION));
  }

  @Override
  public HashMap<String, Number> getSettings() {
    HashMap<String, Number> settings = new HashMap<>();
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .TEMPO, getTempo());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .MEASURE, getMeasure());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .SUB_DIVISION, getSubDivision());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .DURATION, getDuration());
    return settings;
  }
  
  private final class WriteTimedClickTrackTask extends WriteAudioTask {
    
    private BigInteger totalBytesRead = BigInteger.ZERO;
    private int timePast = 0;
    private long minuteInBytes;
    
    @Override
    public void run() {
      try {
        //1. open the clientSocket for the server and listen for incoming 
        //   audio data.
        serverSocket = new ServerSocket(
          MetronomeConstants.Metronome.AudioTasks.SERVER_PORT);
        clientSocket = serverSocket.accept();
        in = new BufferedInputStream(clientSocket.getInputStream(), 
          MetronomeConstants.Metronome.AudioTasks.BIS_BUFFER_SIZE);
        out = new DataOutputStream(clientSocket.getOutputStream());
        
        //2. when the data is received, it is written to the audio devices
        //   through a buffered audio output stream.
        buffer 
          = new byte[MetronomeConstants.Metronome.AudioTasks.WAT_BUFFER_SIZE];
        minuteInBytes = (long) Math.round(
          60 * 
          MetronomeConstants.SoundRez.FRAME_SIZE * 
          MetronomeConstants.SoundRez.FRAME_RATE
        );
        int numBytesRead, p, b;
        
        getSoundRez().getLine().start();
        Thread.sleep(1_000); //wait a second for bytes to pile up in stream
        while ((numBytesRead = in.read(buffer, 0, buffer.length)) != -1) {
          b = numBytesRead % MetronomeConstants.SoundRez.FRAME_SIZE;
          p = numBytesRead - b;
          
          totalBytesRead = totalBytesRead.add(BigInteger.valueOf(p));
          if (totalBytesRead.subtract(totalBytesRead.remainder(
              BigInteger.valueOf(minuteInBytes))).divide(
                BigInteger.valueOf(minuteInBytes)).intValue() == 
              (timePast + 1)) {
            timePast++;
            if (timePast == getDuration()) {
              out.writeInt(MetronomeConstants.Metronome
                .AudioTasks.CAT_STOP_SIGNAL); break;
            } else {
              decrementCurrentTimeLeft(1); 
              out.writeInt(MetronomeConstants.Metronome
                .AudioTasks.CAT_CONTINUE_SIGNAL);
            }
          }
          
          getSoundRez().getLine().write(buffer, 0, p);
        }
        decrementCurrentTimeLeft(getCurrentTimeLeft());
        getSoundRez().getLine().stop();
        getSoundRez().getLine().flush();
        clientSocket.shutdownInput();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      } finally {
        try {
          in.close();
          out.close();
          serverSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private final class CreateTimedClickTrackTask extends CreateAudioTask {
    
    private long periodDutyCycleInBytes;
    private final boolean accentOn;
    private final long periodInBytes;
    private final long measureInBytes;
//    private final long minuteInBytes;
    private long beatIterations = 0L, accentIterations = 0L;
    private BigInteger t = BigInteger.ZERO;
    BigInteger durationInBytes;

    private CreateTimedClickTrackTask(float tempo, int measure, int duration) {
      long period = (long) Math.round(
        (60 / tempo) *
        MetronomeConstants.SoundRez.FRAME_RATE * 
        MetronomeConstants.SoundRez.FRAME_SIZE
      );
//      minuteInBytes = (long) Math.round(
//        60 * 
//        MetronomeConstants.SoundRez.FRAME_SIZE * 
//        MetronomeConstants.SoundRez.FRAME_RATE
//      );
      
      periodInBytes = period - 
                      (period % MetronomeConstants.SoundRez.FRAME_SIZE);
      
      long dutyCycle = (long) Math.round(
        periodInBytes * 
        MetronomeConstants.Metronome.AudioTasks.DUTY_CYCLE
      );
      
      periodDutyCycleInBytes 
        = dutyCycle - (dutyCycle % MetronomeConstants.SoundRez.FRAME_SIZE);
      
      if (periodDutyCycleInBytes > getSoundRez().getAccentSound().length || 
          periodDutyCycleInBytes > getSoundRez().getBeatSound().length) 
        periodDutyCycleInBytes = getSoundRez().getAccentSound().length <= 
                         getSoundRez().getBeatSound().length ? 
                         getSoundRez().getAccentSound().length : 
                         getSoundRez().getBeatSound().length;
      
      long totalDuration = (long) Math.ceil(periodInBytes * tempo * duration);
      
      durationInBytes = BigInteger.valueOf(
        totalDuration - 
        (totalDuration % MetronomeConstants.SoundRez.FRAME_SIZE)
      );
      
      measureInBytes = periodInBytes * measure;
      
      if (measure > 1) accentOn = true;
      else if (measure == MetronomeConstants.Metronome.NO_MEASURE) 
        accentOn = false;
      else accentOn = false;
    }

    @Override
    public void run() {
      try {
        //1. Connect to the server and get an output stream.
        socket = new Socket(MetronomeConstants.Metronome.AudioTasks.HOST, 
          MetronomeConstants.Metronome.AudioTasks.SERVER_PORT);
        out = new BufferedOutputStream(socket.getOutputStream(), 
          MetronomeConstants.Metronome.AudioTasks.BOS_BUFFER_SIZE);
        in = new DataInputStream(socket.getInputStream());
        buffer 
          = new byte[MetronomeConstants.Metronome.AudioTasks.CAT_BUFFER_SIZE];
        int numBytesCreated;

        //2. continuously create data and write it to the stream until
        //   the time elapses.
        while (t.compareTo(durationInBytes) == -1 || 
               in.readInt() != MetronomeConstants.Metronome
               .AudioTasks.CAT_STOP_SIGNAL) {
          numBytesCreated = create(buffer);
          out.write(buffer, 0, numBytesCreated);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          out.close();
          in.close();
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }  
    }

    @Override
    protected int create(byte[] buffer) {
      long n = beatIterations, aN = accentIterations, aT = measureInBytes;
      int c = 0;
      
      // c < buffer.length
      while (c < buffer.length) {
        buffer[c] = soundGenerator(functionGenerator(t, aN, n, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
//        if (t.compareTo(durationInBytes) == 0) { // when t == duration
////          decrementCurrentTimeLeft(getCurrentTimeLeft());
//          break;
//        }
//        if (t.remainder(BigInteger.valueOf(minuteInBytes)).intValue() == 0) {
//          // when a minute passes by while playing
//          decrementCurrentTimeLeft(1);
//        }
        if (t.remainder(BigInteger.valueOf(periodInBytes)).intValue() == 0) n++;
        if (accentOn && t.remainder(BigInteger.valueOf(aT)).intValue() == 0) 
          aN++;
      }
      beatIterations = n;
      accentIterations = aN;
      return c;
    }
    
    //sound function generator for this task
    private byte functionGenerator(BigInteger t, long aN, long bN, long aT) {
      if (accentOn)
        return (byte) ((MetronomeConstants.Metronome.AudioTasks.ACCENT_MARKER * 
                 h(t, BigInteger.valueOf(periodDutyCycleInBytes), aN, aT)) +
                (MetronomeConstants.Metronome.AudioTasks.BEAT_MARKER * 
                 h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                      bN, periodInBytes) * 
                 g(t, BigInteger.valueOf(periodInBytes), aN, aT)));
      else
        return (byte) (MetronomeConstants.Metronome.AudioTasks.BEAT_MARKER * 
                       h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                            bN, periodInBytes));
    }
  }
}
