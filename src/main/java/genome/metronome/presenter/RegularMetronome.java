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
import java.util.HashMap;

/**
 *
 * @author William Kibirango <williamkaos.kibirango76@gmail.com>
 */
public final class RegularMetronome extends ConstantTempoMetronome {
  private Thread creatingThread;

  public RegularMetronome() {
  }

  public RegularMetronome(float tempo, int measure, int subDivision) {
    super(tempo, measure, subDivision);
  }

  private Thread getCreatingThread() {
    return creatingThread;
  }

  private void setCreatingThread(Thread creatingThread) {
    this.creatingThread = creatingThread;
  }

  @Override
  protected byte[] createSilence(float tempo) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void play() {
    setWritingThread(new Thread(new WriteAudioTask()));
    setCreatingThread(new Thread(new CreateRegularClickTrackTask()));
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
  
  protected class CreateRegularClickTrackTask extends CreateAudioTask {
    protected CreateRegularClickTrackTask() {
    }

    @Override
    public void create(byte[] buffer) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  }
}
