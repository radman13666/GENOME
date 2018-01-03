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
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class TimedMetronome extends ConstantTempoMetronome {
  
  private int duration;
  private Thread creatingThread;

  public TimedMetronome() {
  }

  public TimedMetronome(int duration, float tempo, int measure, int subDivision) {
    super(tempo, measure, subDivision);
    setDuration(duration);
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    if (duration >= MetronomeConstants.TimedMetronome.MIN_DURATION 
        & duration >= MetronomeConstants.TimedMetronome.MAX_DURATION)
      this.duration = duration;
    else this.duration = MetronomeConstants.TimedMetronome.DEFAULT_DURATION;
  }

  public Thread getCreatingThread() {
    return creatingThread;
  }

  public void setCreatingThread(Thread creatingThread) {
    this.creatingThread = creatingThread;
  }

  @Override
  protected byte[] createSilence(float tempo) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void play() {
    setWritingThread(new Thread(new WriteAudioTask()));
    setCreatingThread(
      new Thread(
        new CreateTimedClickTrackTask(getTempo(), getMeasure(), getDuration())
      )
    );
    getWritingThread().start();
    getCreatingThread().start();
  }

  @Override
  public void stop() {
    getCreatingThread().interrupt();
    getWritingThread().interrupt();
    setCreatingThread(null);
    setWritingThread(null);
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
  
  protected final class CreateTimedClickTrackTask extends CreateAudioTask {
    
    private Socket socket;
    private BufferedOutputStream out;
    private byte[] buffer;
    private BigInteger t = BigInteger.ZERO;
    private long dutyCycle;
    private final boolean accentOn;
    private final long period;
    private final long accentPeriod;
    private long beatIterations = 0L, accentIterations = 0L;
    BigInteger durationInBytes;

    public CreateTimedClickTrackTask(float tempo, int measure, int duration) {
      float tPeriod = 60 / tempo;
      long periodInBytes 
        = (long) Math.round(MetronomeConstants.SoundRez.FRAME_RATE * 
                            tPeriod * MetronomeConstants.SoundRez.FRAME_SIZE);
      //integral number of sample frames
      period = periodInBytes - 
                    (periodInBytes % MetronomeConstants.SoundRez.FRAME_SIZE);
      
      long dutyCycleInBytes 
        = (long) Math.round(period * 
                            MetronomeConstants.Metronome.AudioTasks.DUTY_CYCLE);
      //integral number of sample frames
      dutyCycle = dutyCycleInBytes - 
                       (dutyCycleInBytes % 
                        MetronomeConstants.SoundRez.FRAME_SIZE);
      if (dutyCycle > getSoundRez().getAccentSound().length || 
          dutyCycle > getSoundRez().getBeatSound().length) 
        dutyCycle = getSoundRez().getAccentSound().length <= 
                         getSoundRez().getBeatSound().length ? 
                         getSoundRez().getAccentSound().length : 
                         getSoundRez().getBeatSound().length;
      
      long totalDuration = (long) Math.ceil(period * tempo * duration);
      //integral number of sample frames
      durationInBytes = BigInteger.valueOf(
        totalDuration - 
        (totalDuration % MetronomeConstants.SoundRez.FRAME_SIZE)
      );
      accentPeriod = period * measure;
      
      if (measure > 1) accentOn = true;
      else if (measure == MetronomeConstants.Metronome.NO_MEASURE) 
        accentOn = false;
      else accentOn = false;
    }

    @Override
    public void run() {
      try {
        //1. Connect to the server and get an output stream.
        socket = new Socket(MetronomeConstants.Metronome.HOST, 
          MetronomeConstants.Metronome.SERVER_PORT);
        out = new BufferedOutputStream(socket.getOutputStream());
        buffer = new byte[MetronomeConstants.Metronome.BUFFER_SIZE];
        int numBytesCreated;

        //2. continuously create data and write it to the stream until
        //   the thread is interrupted or when the time elapses.
        while (!Thread.interrupted() && t.compareTo(durationInBytes) == -1) {
          numBytesCreated = create(buffer);
          out.write(buffer, 0, numBytesCreated);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    protected int create(byte[] buffer) {
      long n = beatIterations, aN = accentIterations, aT = accentPeriod;
      int c = 0;
      
      // c < buffer.length
      while (c < buffer.length) {
        buffer[c] = soundGenerator(functionGenerator(t, aN, n, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
        if (t.compareTo(durationInBytes) == 0) { //t == duration
          return c;
        }
        if (t.remainder(BigInteger.valueOf(period)).intValue() == 0) n++;
        if (accentOn && t.remainder(BigInteger.valueOf(aT)).intValue() == 0) 
          aN++;
      }
      beatIterations = n;
      accentIterations = aN;
      return c;
    }
    
    @Override
    protected byte functionGenerator(BigInteger t, long aN, long bN, long aT) {
      int value;
      if (accentOn)
        value = (MetronomeConstants.Metronome.AudioTasks.ACCENT * 
                 h(t, BigInteger.valueOf(dutyCycle), aN, aT)) +
                (MetronomeConstants.Metronome.AudioTasks.BEAT * 
                 h(t, BigInteger.valueOf(dutyCycle), bN, period) * 
                 g(t, BigInteger.valueOf(period), aN, aT));
      else
        value = MetronomeConstants.Metronome.AudioTasks.BEAT * 
                h(t, BigInteger.valueOf(dutyCycle), bN, period);

      return (byte) value;
    }
    
  }
}
