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
public final class TimedMetronome extends ConstantTempoMetronome {
  
  private int duration;

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

  @Override
  protected byte[] createSilence(float tempo) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void play() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void bulkSet(HashMap<String, Number> settings) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public HashMap<String, Number> getSettings() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
