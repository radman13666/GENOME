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
public abstract class Metronome {
  
  protected int measure;
  protected int subDivision;
  protected SoundRez soundRez;

  protected Metronome() {
  }

  protected Metronome(int measure, int subDivision) {
    setMeasure(measure);
    setSubDivision(subDivision);
  }

  public final int getMeasure() {
    return measure;
  }

  public final void setMeasure(int measure) {
    if (measure == MetronomeConstants.Metronome.NO_MEASURE
        | (measure >= MetronomeConstants.Metronome.MIN_MEASURE 
           & measure <= MetronomeConstants.Metronome.MAX_MEASURE))
      this.measure = measure;
    else this.measure = MetronomeConstants.Metronome.COMMON_TIME;
  }

  public final int getSubDivision() {
    return subDivision;
  }

  public final void setSubDivision(int subDivision) {
    if (subDivision == MetronomeConstants.Metronome.NO_SUB_DIVISION 
        | (subDivision >= MetronomeConstants.Metronome.MIN_SUB_DIVISION 
           & subDivision <= MetronomeConstants.Metronome.MAX_SUB_DIVISION))
      this.subDivision = subDivision;
    else this.subDivision = MetronomeConstants.Metronome.NO_SUB_DIVISION;
  }

  protected final SoundRez getSoundRez() {
    return soundRez;
  }

  public final void setSoundRez(SoundRez soundRez) {
    this.soundRez = soundRez;
  }
  
  public abstract void play();
  public abstract void stop();
  public abstract void bulkSet(HashMap<String, Number> settings);
  public abstract HashMap<String, Number> getSettings();
}
