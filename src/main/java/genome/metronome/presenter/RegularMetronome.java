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
public final class RegularMetronome extends ConstantTempoMetronome {

  public RegularMetronome() {
  }

  public RegularMetronome(float tempo, int measure, int subDivision) {
    super(tempo, measure, subDivision);
  }

  @Override
  public void play() {
    super.play();
    setCreatingTask(
      new CreateRegularClickTrackTask(getTempo(), getMeasure())
    );
    new Thread(getCreatingTask()).start();
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    setTempo((Float) settings.get(MetronomeConstants.MetronomeSettingsKeys
      .TEMPO));
    setMeasure((Integer) settings.get(MetronomeConstants.MetronomeSettingsKeys
      .MEASURE));
    setSubDivision((Integer) settings.get(MetronomeConstants
      .MetronomeSettingsKeys.SUB_DIVISION));
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
    return settings;
  }
  
  private final class CreateRegularClickTrackTask extends CreateAudioTask {
    
    private Socket socket;
    private BufferedOutputStream out;
    private byte[] buffer;
    private long periodDutyCycleInBytes;
    private final boolean accentOn;
    private final long periodInBytes;
    private final long measureInBytes;
    private long beatIterations = 0L, accentIterations = 0L;
    private BigInteger t = BigInteger.ZERO;

    protected CreateRegularClickTrackTask(float tempo, int measure) {
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
        buffer 
          = new byte[MetronomeConstants.Metronome.AudioTasks.CAT_BUFFER_SIZE];
        int numBytesCreated;
        
        //2. continuously create data and write it to the stream until
        //   the thread is stopped.
        while (!isStopped) {
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
      long n = beatIterations, aN = accentIterations, aT = measureInBytes;

      while (c < buffer.length) {
        buffer[c] = soundGenerator(functionGenerator(t, aN, n, aT));
        
        c++;
        t = t.add(BigInteger.ONE); //t++
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
