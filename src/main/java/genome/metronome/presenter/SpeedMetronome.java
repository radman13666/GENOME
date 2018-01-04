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
public final class SpeedMetronome extends VariableTempoMetronome {
  
  private int tempoLength;
  private float tempoIncrement;

  public SpeedMetronome() {
  }

  public SpeedMetronome(int tempoLength, float tempoIncrement, float startTempo,
                        float endTempo, int measure, int subDivision) {
    super(startTempo, endTempo, measure, subDivision);
    setTempoLength(tempoLength);
    setTempoIncrement(tempoIncrement);
  }

  public int getTempoLength() {
    return tempoLength;
  }

  public void setTempoLength(int tempoLength) {
    if (tempoLength >= MetronomeConstants.SpeedMetronome.MIN_TEMPO_LENGTH 
        & tempoLength >= MetronomeConstants.SpeedMetronome.MAX_TEMPO_LENGTH)
      this.tempoLength = tempoLength;
    else this.tempoLength = MetronomeConstants.SpeedMetronome
            .DEFAULT_TEMPO_LENGTH;
  }

  public float getTempoIncrement() {
    return tempoIncrement;
  }

  public void setTempoIncrement(float tempoIncrement) {
    if (tempoIncrement >= MetronomeConstants.SpeedMetronome.MIN_TEMPO_INCREMENT 
        & tempoIncrement <= MetronomeConstants.SpeedMetronome
          .MAX_TEMPO_INCREMENT)
      this.tempoIncrement = tempoIncrement;
    else this.tempoIncrement = MetronomeConstants.SpeedMetronome
            .DEFAULT_TEMPO_INCREMENT;
  }

  @Override
  public void play() {
    super.play();
    setCreatingThread(new Thread(new CreateSpeedClickTrackTask()));
    getCreatingThread().start();
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    setStartTempo((Float) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.START_TEMPO));
    setEndTempo((Float) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.END_TEMPO));
    setMeasure((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.MEASURE));
    setSubDivision((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.SUB_DIVISION));
    setTempoLength((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.TEMPO_LENGTH));
    setTempoIncrement((Float) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.TEMPO_INCREMENT));
  }

  @Override
  public HashMap<String, Number> getSettings() {
    HashMap<String, Number> settings = new HashMap<>();
    settings.put(MetronomeConstants.MetronomeSettingsKeys.START_TEMPO, 
                 getStartTempo());
    settings.put(MetronomeConstants.MetronomeSettingsKeys.END_TEMPO, 
                 getEndTempo());
    settings.put(MetronomeConstants.MetronomeSettingsKeys.MEASURE, 
                 getMeasure());
    settings.put(MetronomeConstants.MetronomeSettingsKeys.SUB_DIVISION, 
                 getSubDivision());
    settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_LENGTH, 
                 getTempoLength());
    settings.put(MetronomeConstants.MetronomeSettingsKeys.TEMPO_INCREMENT, 
                 getTempoIncrement());
    return settings;
  }
  
  protected final class CreateSpeedClickTrackTask extends CreateAudioTask {
    
    private Socket socket;
    private BufferedOutputStream out;
    private byte[] buffer;
    private long periodInBytes;
    private long measureInBytes;
    private long tempoChangePeriodInBytes;
    private long periodDutyCycleInBytes;
    private final int numMeasures;
    private float currentTempo;
    private long beatIterations = 0L, accentIterations = 0L,
      tempoChangeIterations = 0L;
    private BigInteger t = BigInteger.ZERO;

    public CreateSpeedClickTrackTask() {
      currentTempo = getStartTempo();
      
      numMeasures = ((int) Math.ceil(
        (getEndTempo() - getStartTempo()) / 
        getTempoIncrement()
      )) * getTempoLength();
      
      doTempoChange();
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
        //   the thread is interrupted or when the actual end tempo is reached.
        while (!Thread.interrupted() && accentIterations < numMeasures) {
          numBytesCreated = create(buffer);
          out.write(buffer, 0, numBytesCreated);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    protected int create(byte[] buffer) {
      int c = 0;
      long n = beatIterations, aN = accentIterations, aT = measureInBytes,
        cN = tempoChangeIterations;

      while (c < buffer.length) {
        buffer[c] = soundGenerator(functionGenerator(t, aN, n, cN, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
        if (t.remainder(BigInteger.valueOf(periodInBytes)).intValue() == 0) n++;
        if (t.remainder(BigInteger.valueOf(aT)).intValue() == 0) aN++;
        if (aN == numMeasures) return c;
        if (t.remainder(BigInteger.valueOf(tempoChangeIterations))
          .intValue() == 0) {
          cN++;
          currentTempo += getTempoIncrement();
          doTempoChange();
        }
      }
      beatIterations = n;
      accentIterations = aN;
      tempoChangeIterations = cN;
      return c;
    }
    
    //sound function generator for this task
    private byte functionGenerator(BigInteger t, long aN, long bN, long cN, 
                                                                   long aT) {
      int value;
      value = ((MetronomeConstants.Metronome.AudioTasks.ACCENT * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), aN, aT)) +
              (MetronomeConstants.Metronome.AudioTasks.BEAT * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                    bN, periodInBytes) * 
               g(t, BigInteger.valueOf(periodInBytes), aN, aT))) * 
              g(t, BigInteger.valueOf(periodInBytes), 
                   cN, tempoChangePeriodInBytes) + 
              h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                   cN, tempoChangePeriodInBytes);
      return (byte) value;
    }
    
    private void doTempoChange() {
      long period = (long) Math.round(
        (60 / currentTempo) *
        MetronomeConstants.SoundRez.FRAME_RATE *
        MetronomeConstants.SoundRez.FRAME_SIZE
      );
      
      periodInBytes = period - 
                      (period % MetronomeConstants.SoundRez.FRAME_SIZE);
      
      measureInBytes = periodInBytes * getMeasure();
      
      tempoChangePeriodInBytes = measureInBytes * getTempoLength();
      
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
    }
  }
}
