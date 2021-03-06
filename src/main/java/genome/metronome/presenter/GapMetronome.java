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
public final class GapMetronome extends ConstantTempoMetronome {
  
  private int loudMeasures;
  private int silentMeasures;
  private int gapLengthIncrement;
  private int gapRepetitions;
  private int currentSilentMeasures;
  private int duration;

  public GapMetronome() {
    super();
  }

  public GapMetronome(int loudMeasures, int silentMeasures,
                      int gapLengthIncrement, int gapRepetitions, float tempo,
                      int measure, int subDivision, int duration) {
    super(tempo, measure, subDivision);
    setLoudMeasures(loudMeasures);
    setSilentMeasures(silentMeasures);
    setGapLengthIncrement(gapLengthIncrement);
    setGapRepetitions(gapRepetitions);
    setDuration(duration);
  }

  public int getLoudMeasures() {
    return loudMeasures;
  }

  public void setLoudMeasures(int loudMeasures) {
    if (loudMeasures >= MetronomeConstants.GapMetronome.MIN_LOUD_MEASURES && 
        loudMeasures <= MetronomeConstants.GapMetronome.MAX_LOUD_MEASURES)
      this.loudMeasures = loudMeasures;
    else this.loudMeasures 
      = MetronomeConstants.GapMetronome.DEFAULT_LOUD_MEASURES;
  }

  public int getSilentMeasures() {
    return silentMeasures;
  }

  public void setSilentMeasures(int silentMeasures) {
    if (silentMeasures >= MetronomeConstants.GapMetronome.MIN_SILENT_MEASURES &&
        silentMeasures <= MetronomeConstants.GapMetronome.MAX_SILENT_MEASURES)
      this.silentMeasures = silentMeasures;
    else this.silentMeasures 
      = MetronomeConstants.GapMetronome.DEFAULT_SILENT_MEASURES;
    this.currentSilentMeasures = this.silentMeasures;
  }

  public int getGapLengthIncrement() {
    return gapLengthIncrement;
  }

  public void setGapLengthIncrement(int gapLengthIncrement) {
    if (gapLengthIncrement >= 
         MetronomeConstants.GapMetronome.MIN_GAP_LENGTH_INCREMENT && 
         gapLengthIncrement <= 
         MetronomeConstants.GapMetronome.MAX_GAP_LENGTH_INCREMENT)
      this.gapLengthIncrement = gapLengthIncrement;
    else this.gapLengthIncrement 
      = MetronomeConstants.GapMetronome.DEFAULT_GAP_LENGTH_INCREMENT;
  }

  public int getGapRepetitions() {
    return gapRepetitions;
  }

  public void setGapRepetitions(int gapRepetitions) {
    if (gapRepetitions == 
        MetronomeConstants.GapMetronome.INFINITE_GAP_REPETITIONS || 
        (gapRepetitions >= 
         MetronomeConstants.GapMetronome.MIN_GAP_REPETITIONS &&
         gapRepetitions <= MetronomeConstants.GapMetronome.MAX_GAP_REPETITIONS))
      this.gapRepetitions = gapRepetitions;
    else this.gapRepetitions 
      = MetronomeConstants.GapMetronome.DEFAULT_GAP_REPETITIONS;
  }
  
  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    if (duration >= MetronomeConstants.TimedMetronome.MIN_DURATION && 
        duration <= MetronomeConstants.TimedMetronome.MAX_DURATION)
      this.duration = duration;
    else this.duration = MetronomeConstants.TimedMetronome.DEFAULT_DURATION;
  }
  
  public int getCurrentSilentMeasures() {
    return currentSilentMeasures;
  }

  private void incrementCurrentSilentMeasures(int gapLengthIncrement) {
    this.currentSilentMeasures += gapLengthIncrement;
    setChanged();
    notifyObservers(MetronomeConstants.Metronome.AudioTasks
      .GM_CURRENT_SILENT_MEASURES);
  }

  @Override
  public void play() {
//    super.play();
    setWritingTask(
      new WriteGapClickTrackTask(
        getTempo(),
        getMeasure(),
        getLoudMeasures(),
        getSilentMeasures(),
        getGapLengthIncrement()
      )
    );
    setCreatingTask(
      new CreateGapClickTrackTask(
        getTempo(),
        getMeasure(),
        getLoudMeasures(),
        getSilentMeasures(),
        getGapLengthIncrement(),
        getDuration()
      )
    );
//    executor.execute(getWritingTask());
    writingFuture = executor.submit(getWritingTask());
//    executor.execute(getCreatingTask());
    creatingFuture = executor.submit(getCreatingTask());
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
      .LOUD_MEASURES, getLoudMeasures());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .SILENT_MEASURES, getSilentMeasures());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .GAP_LENGTH_INCREMENT, getGapLengthIncrement());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .GAP_REPETITIONS, getGapRepetitions());
    settings.put(MetronomeConstants.MetronomeSettingsKeys
      .DURATION, getDuration());
    return settings;
  }
  
  private final class WriteGapClickTrackTask extends WriteAudioTask {
    
    private BigInteger totalBytesRead = BigInteger.ZERO, 
      tMark = BigInteger.ZERO, m = BigInteger.ZERO, gapGraphPeriodInBytes;
    private final long gapLengthIncrementInBytes;
    private final long periodInBytes;
    private final long measureInBytes;
    private final long loudMeasuresInBytes;
    private final long silentMeasuresInBytes;
    private final long minuteInBytes;
    private int timePast = 0;

    private WriteGapClickTrackTask(float tempo, 
                                   int measure, 
                                   int loudMeasures, 
                                   int silentMeasures, 
                                   int gapLengthIncrement) {
      long period = (long) Math.round(
        (60 / tempo) *
        MetronomeConstants.SoundRez.FRAME_RATE *
        MetronomeConstants.SoundRez.FRAME_SIZE
      );
      minuteInBytes = (long) Math.round(
          60 * 
          MetronomeConstants.SoundRez.FRAME_SIZE * 
          MetronomeConstants.SoundRez.FRAME_RATE
      );
      periodInBytes 
        = period - (period % MetronomeConstants.SoundRez.FRAME_SIZE);
      
      measureInBytes = periodInBytes * measure;      
      loudMeasuresInBytes = measureInBytes * loudMeasures;
      silentMeasuresInBytes = measureInBytes * silentMeasures;
      gapLengthIncrementInBytes = measureInBytes * gapLengthIncrement;
      
      gapGraphPeriodInBytes = BigInteger.valueOf(
        loudMeasuresInBytes + silentMeasuresInBytes
      );
    }
    
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
        int numBytesRead, p, b;
        
        getSoundRez().getLine().start();
        Thread.sleep(1_000); //wait a second for bytes to pile up in stream
        while ((numBytesRead = in.read(buffer, 0, buffer.length)) != -1) {
          b = numBytesRead % MetronomeConstants.SoundRez.FRAME_SIZE;
          p = numBytesRead - b;
          
          totalBytesRead = totalBytesRead.add(BigInteger.valueOf(p));
          m = totalBytesRead.subtract(tMark);
          if (totalBytesRead.subtract(totalBytesRead.remainder(
              BigInteger.valueOf(minuteInBytes))).divide(
                BigInteger.valueOf(minuteInBytes)).intValue() == 
              (timePast + 1)) {
            timePast++;
            if (timePast == getDuration()) {
              out.writeInt(MetronomeConstants.Metronome
                .AudioTasks.CAT_STOP_SIGNAL); break;
            } else {
              out.writeInt(MetronomeConstants.Metronome
                .AudioTasks.CAT_CONTINUE_SIGNAL);
            }
          }
          if (getGapRepetitions() != 
                MetronomeConstants.GapMetronome.INFINITE_GAP_REPETITIONS &&
              getGapLengthIncrement() != 
              MetronomeConstants.GapMetronome.NO_GAP_LENGTH_INCREMENT &&
              m.subtract(m.remainder(gapGraphPeriodInBytes))
                .divide(gapGraphPeriodInBytes).intValue() == 
              getGapRepetitions()) {
            tMark = totalBytesRead;
            gapGraphPeriodInBytes 
              = gapGraphPeriodInBytes.add(
                BigInteger.valueOf(gapLengthIncrementInBytes));
            incrementCurrentSilentMeasures(getGapLengthIncrement());
          }

          getSoundRez().getLine().write(buffer, 0, p);
        }
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
  
  private final class CreateGapClickTrackTask extends CreateAudioTask {
    
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
    private BigInteger tMark = BigInteger.ZERO;
    private long nMark = 0L, aNMark = 0L, gNMark = 0L;
    BigInteger durationInBytes;

    private CreateGapClickTrackTask(float tempo, 
                                   int measure, 
                                   int loudMeasures, 
                                   int silentMeasures, 
                                   int gapLengthIncrement,
                                   int duration) {
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
      
      gapGraphPeriodInBytes = BigInteger.valueOf(
        loudMeasuresInBytes + silentMeasuresInBytes
      );
      
      gapGraphDutyCycleInBytes = loudMeasuresInBytes;
      
      long totalDuration = (long) Math.ceil(periodInBytes * tempo * duration);
      
      durationInBytes = BigInteger.valueOf(
        totalDuration - 
        (totalDuration % MetronomeConstants.SoundRez.FRAME_SIZE)
      );
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
      int c = 0;
      long n = beatIterations, aN = accentIterations, aT = measureInBytes,
        gN = gapGraphIterations;

      while (c < buffer.length) {
        buffer[c] 
          = soundGenerator(functionGenerator(t.subtract(tMark), 
                                             aN - aNMark, 
                                             n - nMark, 
                                             gN - gNMark, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
        if (t.compareTo(durationInBytes) == 0) break; // when t == duration
        if (t.remainder(BigInteger.valueOf(periodInBytes)).intValue() == 0) n++;
        if (t.remainder(BigInteger.valueOf(aT)).intValue() == 0) aN++;
        if (t.subtract(tMark)
             .remainder(gapGraphPeriodInBytes).intValue() == 0) {
          gN++;
          if (getGapRepetitions() != 
            MetronomeConstants.GapMetronome.INFINITE_GAP_REPETITIONS && 
            (gN % getGapRepetitions()) == 0) {
            tMark = t; nMark = n; aNMark = aN; gNMark = gN;
            gapGraphPeriodInBytes = gapGraphPeriodInBytes.add(
              BigInteger.valueOf(gapLengthIncrementInBytes)
            );
//            incrementCurrentSilentMeasures(getGapLengthIncrement());
          }
        }
      }
      beatIterations = n;
      accentIterations = aN;
      gapGraphIterations = gN;
      return c;
    }

    //sound function generator for this task
    private byte functionGenerator(BigInteger t, long aN, long bN, long gN, 
                                                                   long aT) {
      return (byte) (((MetronomeConstants.Metronome.AudioTasks.ACCENT_MARKER * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), aN, aT)) +
              (MetronomeConstants.Metronome.AudioTasks.BEAT_MARKER * 
               h(t, BigInteger.valueOf(periodDutyCycleInBytes), 
                    bN, periodInBytes) * 
               g(t, BigInteger.valueOf(periodInBytes), aN, aT))) * 
              h(t, BigInteger.valueOf(gapGraphDutyCycleInBytes), 
                   gN, gapGraphPeriodInBytes));
    }
  }
}
