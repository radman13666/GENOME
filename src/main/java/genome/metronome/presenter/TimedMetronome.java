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
    if (duration >= MetronomeConstants.TimedMetronome.MIN_DURATION 
        & duration >= MetronomeConstants.TimedMetronome.MAX_DURATION)
      this.duration = duration;
    else this.duration = MetronomeConstants.TimedMetronome.DEFAULT_DURATION;
  }

  @Override
  public void play() {
    super.play();
    setCreatingThread(
      new Thread(
        new CreateTimedClickTrackTask(getTempo(), getMeasure(), getDuration())
      )
    );
    getCreatingThread().start();
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
    private long periodDutyCycleInBytes;
    private final boolean accentOn;
    private final long periodInBytes;
    private final long measureInBytes;
    private long beatIterations = 0L, accentIterations = 0L;
    private BigInteger t = BigInteger.ZERO;
    BigInteger durationInBytes;

    public CreateTimedClickTrackTask(float tempo, int measure, int duration) {
      long period = (long) Math.round(
        (60 / tempo) *
        MetronomeConstants.SoundRez.FRAME_RATE * 
        MetronomeConstants.SoundRez.FRAME_SIZE
      );
      
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
      long n = beatIterations, aN = accentIterations, aT = measureInBytes;
      int c = 0;
      
      // c < buffer.length
      while (c < buffer.length) {
        buffer[c] = soundGenerator(functionGenerator(t, aN, n, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
        if (t.compareTo(durationInBytes) == 0) { //t == duration
          return c;
        }
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
      int value;
      if (accentOn)
        value = (MetronomeConstants.Metronome.AudioTasks.ACCENT * 
                 h(t, BigInteger.valueOf(periodDutyCycleInBytes), aN, aT)) +
                (MetronomeConstants.Metronome.AudioTasks.BEAT * 
                 h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                      bN, periodInBytes) * 
                 g(t, BigInteger.valueOf(periodInBytes), aN, aT));
      else
        value = MetronomeConstants.Metronome.AudioTasks.BEAT * 
                h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                     bN, periodInBytes);

      return (byte) value;
    }
    
  }
}
