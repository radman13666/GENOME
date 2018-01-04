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
public final class GapMetronome extends ConstantTempoMetronome {
  
  private int loudMeasures;
  private int silentMeasures;
  private int gapLengthIncrement;
  private int gapRepetitions;

  public GapMetronome() {
  }

  public GapMetronome(int loudMeasures, int silentMeasures,
                      int gapLengthIncrement, int gapRepetitions, float tempo,
                      int measure, int subDivision) {
    super(tempo, measure, subDivision);
    setLoudMeasures(loudMeasures);
    setSilentMeasures(silentMeasures);
    setGapLengthIncrement(gapLengthIncrement);
    setGapRepetitions(gapRepetitions);
  }

  public int getLoudMeasures() {
    return loudMeasures;
  }

  public void setLoudMeasures(int loudMeasures) {
    if (loudMeasures >= MetronomeConstants.GapMetronome.MIN_LOUD_MEASURES 
        && loudMeasures <= MetronomeConstants.GapMetronome.MAX_LOUD_MEASURES)
      this.loudMeasures = loudMeasures;
    else this.loudMeasures = MetronomeConstants.GapMetronome
            .DEFAULT_LOUD_MEASURES;
  }

  public int getSilentMeasures() {
    return silentMeasures;
  }

  public void setSilentMeasures(int silentMeasures) {
    if (silentMeasures >= MetronomeConstants.GapMetronome.MIN_SILENT_MEASURES 
        && silentMeasures <= MetronomeConstants.GapMetronome.MAX_SILENT_MEASURES)
      this.silentMeasures = silentMeasures;
    else this.silentMeasures = MetronomeConstants.GapMetronome
            .DEFAULT_SILENT_MEASURES;
  }

  public int getGapLengthIncrement() {
    return gapLengthIncrement;
  }

  public void setGapLengthIncrement(int gapLengthIncrement) {
    if (gapLengthIncrement >= MetronomeConstants.GapMetronome
        .MIN_GAP_LENGTH_INCREMENT 
        && gapLengthIncrement <= MetronomeConstants.GapMetronome
          .MAX_GAP_LENGTH_INCREMENT)
      this.gapLengthIncrement = gapLengthIncrement;
    else this.gapLengthIncrement = MetronomeConstants.GapMetronome
            .DEFAULT_GAP_LENGTH_INCREMENT;
  }

  public int getGapRepetitions() {
    return gapRepetitions;
  }

  public void setGapRepetitions(int gapRepetitions) {
    if (gapRepetitions == MetronomeConstants.GapMetronome
        .INFINITE_GAP_REPETITIONS || 
        (gapRepetitions >= MetronomeConstants.GapMetronome.MIN_GAP_REPETITIONS 
        && gapRepetitions <= MetronomeConstants.GapMetronome
          .MAX_GAP_REPETITIONS))
      this.gapRepetitions = gapRepetitions;
    else this.gapRepetitions = MetronomeConstants.GapMetronome
            .DEFAULT_GAP_REPETITIONS;
  }

  @Override
  public void play() {
    super.play();
    setCreatingThread(
      new Thread(new CreateGapClickTrackTask(
        getTempo(),
        getMeasure(),
        getLoudMeasures(),
        getSilentMeasures(),
        getGapLengthIncrement()
      ))
    );
    getCreatingThread().start();
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    setTempo((Float) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.TEMPO));
    setMeasure((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.MEASURE));
    setSubDivision((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.SUB_DIVISION));
    setLoudMeasures((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.LOUD_MEASURES));
    setSilentMeasures((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.SILENT_MEASURES));
    setGapLengthIncrement((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.GAP_LENGTH_INCREMENT));
    setGapRepetitions((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.GAP_REPETITIONS));
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
      .LOUD_MEASURES, getLoudMeasures());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .SILENT_MEASURES, getSilentMeasures());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .GAP_LENGTH_INCREMENT, getGapLengthIncrement());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .GAP_REPETITIONS, getGapRepetitions());
    return settings;
  }
  
  protected final class CreateGapClickTrackTask extends CreateAudioTask {
    
    private Socket socket;
    private BufferedOutputStream out;
    private byte[] buffer;
    private final long periodInBytes;
    private long periodDutyCycleInBytes;
    private final long measureInBytes;
    private final long loudMeasuresInBytes;
    private final long silentMeasuresInBytes;
    private final long gapLengthIncrementInBytes;
    private final long gapGraphDutyCycleInBytes;
    private long beatIterations = 0L, accentIterations = 0L,
      gapGraphIterations = 0L;
    private BigInteger gapGraphPeriodInBytes;
    private BigInteger t = BigInteger.ZERO;
    
    public CreateGapClickTrackTask(float tempo, 
                                   int measure, 
                                   int loudMeasures, 
                                   int silentMeasures, 
                                   int gapLengthIncrement) {      
      long period = (long) Math.round(
        (60 / tempo) *
        MetronomeConstants.SoundRez.FRAME_RATE *
        MetronomeConstants.SoundRez.FRAME_SIZE
      );
      
      periodInBytes 
        = period - (period % MetronomeConstants.SoundRez.FRAME_SIZE);
      
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
      
      measureInBytes = periodInBytes * measure;      
      loudMeasuresInBytes = measureInBytes * loudMeasures;
      silentMeasuresInBytes = measureInBytes * silentMeasures;
      gapLengthIncrementInBytes = measureInBytes * gapLengthIncrement;
      
      BigInteger gapGraphPeriod = BigInteger.valueOf(
        loudMeasuresInBytes + silentMeasuresInBytes
      );
      
      gapGraphPeriodInBytes // p - (p % FRAME_SIZE)
        = gapGraphPeriod.subtract(gapGraphPeriod.remainder(BigInteger.valueOf(
          MetronomeConstants.SoundRez.FRAME_SIZE)));
      
      long gapGraphDutyCycle
        = BigInteger.valueOf(loudMeasuresInBytes)
          .divide(gapGraphPeriodInBytes).longValue();
      
      gapGraphDutyCycleInBytes 
        = gapGraphDutyCycle - 
          (gapGraphDutyCycle % MetronomeConstants.SoundRez.FRAME_SIZE);
    }

    @Override
    public void run() {
      try {
        //1. Connect to the server and get an output stream.
        socket = new Socket(MetronomeConstants.Metronome.AudioTasks.HOST, 
          MetronomeConstants.Metronome.AudioTasks.SERVER_PORT);
        out = new BufferedOutputStream(socket.getOutputStream());
        buffer = new byte[MetronomeConstants.Metronome.AudioTasks.BUFFER_SIZE];
        int numBytesCreated;
        
        //2. continuously create data and write it to the stream until
        //   the thread is interrupted.
        while (!Thread.interrupted()) {
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
        gN = gapGraphIterations;

      while (c < buffer.length) {
        buffer[c] = soundGenerator(functionGenerator(t, aN, n, gN, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
        if (t.remainder(BigInteger.valueOf(periodInBytes)).intValue() == 0) n++;
        if (t.remainder(BigInteger.valueOf(aT)).intValue() == 0) aN++;
        if (t.remainder(gapGraphPeriodInBytes).intValue() == 0) gN++;
        if ((gN % getGapRepetitions()) == 0) 
          gapGraphPeriodInBytes = gapGraphPeriodInBytes.add(
            BigInteger.valueOf(gapLengthIncrementInBytes)
          );
      }
      beatIterations = n;
      accentIterations = aN;
      gapGraphIterations = gN;
      return c;
    }

    //sound function generator for this task
    private byte functionGenerator(BigInteger t, long aN, long bN, long gN, 
                                                                   long aT) {
      return (byte) (((MetronomeConstants.Metronome.AudioTasks.ACCENT * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), aN, aT)) +
              (MetronomeConstants.Metronome.AudioTasks.BEAT * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                    bN, periodInBytes) * 
               g(t, BigInteger.valueOf(periodInBytes), aN, aT))) * 
              h(t, BigInteger.valueOf(gapGraphDutyCycleInBytes), 
                   gN, gapGraphPeriodInBytes));
    }
  }
}
