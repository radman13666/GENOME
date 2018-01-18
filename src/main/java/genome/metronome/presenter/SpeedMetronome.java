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
  private float currentTempo = 0F;

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
    if (tempoLength >= MetronomeConstants.SpeedMetronome.MIN_TEMPO_LENGTH && 
        tempoLength <= MetronomeConstants.SpeedMetronome.MAX_TEMPO_LENGTH)
      this.tempoLength = tempoLength;
    else this.tempoLength 
      = MetronomeConstants.SpeedMetronome.DEFAULT_TEMPO_LENGTH;
  }

  public float getTempoIncrement() {
    return tempoIncrement;
  }

  public void setTempoIncrement(float tempoIncrement) {
    if (tempoIncrement >= 
        MetronomeConstants.SpeedMetronome.MIN_TEMPO_INCREMENT && 
        tempoIncrement <= MetronomeConstants.SpeedMetronome.MAX_TEMPO_INCREMENT)
      this.tempoIncrement = tempoIncrement;
    else this.tempoIncrement 
      = MetronomeConstants.SpeedMetronome.DEFAULT_TEMPO_INCREMENT;
  }

  public float getCurrentTempo() {
    return currentTempo;
  }

  private void incrementCurrentTempo(float tempoIncrement) {
    this.currentTempo += tempoIncrement;
    setChanged(); 
    notifyObservers(MetronomeConstants.Metronome.AudioTasks.SM_CURRENT_TEMPO);
  }

  @Override
  public void play() {
    super.play();
    if (getStartTempo() >= getEndTempo()) {
      setStartTempo(MetronomeConstants
        .VariableTempoMetronome.DEFAULT_START_TEMPO);
      setEndTempo(MetronomeConstants
        .VariableTempoMetronome.DEFAULT_END_TEMPO);
    }
    setCreatingTask(new CreateSpeedClickTrackTask());
    new Thread(getCreatingTask()).start();
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
  
  private final class CreateSpeedClickTrackTask extends CreateAudioTask {
    
    private Socket socket;
    private BufferedOutputStream out;
    private byte[] buffer;
    private long periodInBytes;
    private long measureInBytes;
    private long tempoChangePeriodInBytes;
    private long periodDutyCycleInBytes;
    private final int numMeasures;
    private long beatIterations = 0L, accentIterations = 0L,
      tempoChangeIterations = 0L;
    private BigInteger t = BigInteger.ZERO;
    private BigInteger tMark = BigInteger.ZERO;
    private long nMark = 0L, aNMark = 0L, cNMark = 0L;

    public CreateSpeedClickTrackTask() {
      numMeasures = (((int) Math.ceil(
        (getEndTempo() - getStartTempo()) / 
        getTempoIncrement()
      )) + 1) * getTempoLength();
      
      doTempoChange(getStartTempo());
    }

    @Override
    public void run() {
      try {
        //1. Connect to the server and get an output stream.
        socket = new Socket(MetronomeConstants.Metronome.AudioTasks.HOST, 
          MetronomeConstants.Metronome.AudioTasks.SERVER_PORT);
        out = new BufferedOutputStream(socket.getOutputStream(), 
          MetronomeConstants.Metronome.AudioTasks.BOS_BUFFER_SIZE);
        buffer 
          = new byte[MetronomeConstants.Metronome.AudioTasks.CAT_BUFFER_SIZE];
        int numBytesCreated;

        //2. continuously create data and write it to the stream until
        //   the thread is stopped or when the actual end tempo is reached.
        while (!isStopped && accentIterations < numMeasures) {
          numBytesCreated = create(buffer);
          out.write(buffer, 0, numBytesCreated);
        }
        currentTempo = 0F;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    protected int create(byte[] buffer) {
      int c = 0;
      long n = beatIterations, aN = accentIterations,
        cN = tempoChangeIterations;

      while (c < buffer.length) {
        buffer[c] 
          = soundGenerator(functionGenerator(t.subtract(tMark), 
                                             aN - aNMark, 
                                             n - nMark, 
                                             cN - cNMark, measureInBytes));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
        if (t.subtract(tMark)
             .remainder(BigInteger.valueOf(periodInBytes)).intValue() == 0) 
          n++;
        if (t.subtract(tMark)
             .remainder(BigInteger.valueOf(measureInBytes)).intValue() == 0) 
          aN++;
        if (aN == numMeasures) break;
        if (t.subtract(tMark)
             .remainder(BigInteger.valueOf(tempoChangePeriodInBytes))
             .intValue() == 0) {
          cN++; tMark = t; nMark = n; aNMark = aN; cNMark = cN;
          doTempoChange(getTempoIncrement());
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
      return (byte) (((MetronomeConstants.Metronome.AudioTasks.ACCENT_MARKER * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), aN, aT)) +
              (MetronomeConstants.Metronome.AudioTasks.BEAT_MARKER * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                    bN, periodInBytes) * 
               g(t, BigInteger.valueOf(periodInBytes), aN, aT))) * 
              g(t, BigInteger.valueOf(periodInBytes), 
                   cN, tempoChangePeriodInBytes) + 
              MetronomeConstants.Metronome.AudioTasks.TEMPO_CHANGE_MARKER * 
              h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                   cN, tempoChangePeriodInBytes));
    }
    
    private void doTempoChange(float tempoIncrement) {
      incrementCurrentTempo(tempoIncrement);
      
      long period = (long) Math.round(
        (60 / getCurrentTempo()) *
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
